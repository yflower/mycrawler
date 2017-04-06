package com.cufe.taskProcessor.context;

import com.cufe.taskProcessor.Processor;
import com.cufe.taskProcessor.Repository;
import com.cufe.taskProcessor.Sink;
import com.cufe.taskProcessor.enums.CycleEnum;
import com.cufe.taskProcessor.enums.StatusEnum;
import com.cufe.taskProcessor.help.FunctionHelp;
import com.cufe.taskProcessor.task.AbstractTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


/**
 * Created by jianganlan on 2017/4/3.
 */
public abstract class ComponentContext<S, R> {

    private static final Logger LOGGER = Logger.getLogger(Processor.class.getSimpleName());

    protected Sink<S> sink;

    protected Repository<R> repository;

    protected Processor<S, R> processor;

    protected Consumer<AbstractTask> taskBeforeHook;

    protected Consumer<AbstractTask> taskAfterHook;

    protected Runnable cycleBefore;

    protected Runnable cycleError;

    protected Runnable cycleFinally;

    private int thread;

    private List<AbstractTask> tasks;

    private Lock taskLock = new ReentrantLock();

    private Condition taskCondition = taskLock.newCondition();

    private ThreadLocalRandom random;

    private StatusEnum status = StatusEnum.NO_INIT;

    private ExecutorService executorService;


    public void run() {
        for (int i = 0; i < thread; ++i) {
            execute();
        }

    }

    public boolean addTask(AbstractTask task) {
        //只有任务到来时才启动
        if (status == StatusEnum.INIT) {
            setStatus(StatusEnum.STARTED);
            run();
        }
        tasks.add(task);
        task.setStatus(StatusEnum.STARTED);
        releaseTasks();
        return true;
    }


    public boolean stopTask(String taskTag) {
        AbstractTask innerTask = getTaskByTag(taskTag);
        if (innerTask != null && innerTask.getStatus() == StatusEnum.STARTED) {
            innerTask.setStatus(StatusEnum.STOPPED);
            return true;
        } else {
            return false;
        }
    }

    public boolean finishTask(String taskTag) {
        AbstractTask innerTask = getTaskByTag(taskTag);
        if (innerTask != null && StatusEnum.isStart(innerTask.getStatus())) {
            innerTask.setStatus(StatusEnum.FINISHED);
            return true;
        } else {
            return false;
        }
    }

    public boolean destroyTask(String taskTag) {
        AbstractTask innerTask = getTaskByTag(taskTag);
        if (innerTask != null) {
            innerTask.setStatus(StatusEnum.DESTROYED);
            return true;
        } else {
            return false;
        }
    }

    public void init() {
        if (sink == null || repository == null || processor == null) {
            throw new IllegalStateException("sink,repository,processor必须设置");
        }
        if (taskBeforeHook == null) {
            taskBeforeHook = FunctionHelp.TASK_CYCLE_EMPTY_HOOK;
        }
        if (taskAfterHook == null) {
            taskAfterHook = FunctionHelp.TASK_CYCLE_EMPTY_HOOK;
        }
        if (cycleBefore == null) {
            cycleBefore = FunctionHelp.CYCLE_EMPTY_HOOK;
        }
        if (cycleError == null) {
            cycleError = FunctionHelp.CYCLE_EMPTY_HOOK;
        }
        if (cycleFinally == null) {
            cycleFinally = FunctionHelp.CYCLE_EMPTY_HOOK;
        }

        executorService = Executors.newFixedThreadPool(thread);
        tasks = new ArrayList<>();
        setStatus(StatusEnum.INIT);
        internalInit();

    }

    protected abstract void internalInit();

    private void execute() {
        executorService.submit(() -> {
            cycleBefore.run();
            random = ThreadLocalRandom.current();
            AbstractTask task;
            S resource;
            try {
                while ((task = randomRunnableTask()) != null) {
                    taskBeforeHook.accept(task);
                    taskCycleCheck(task);
                    Optional<S> optional = sink.get(task);
                    if (!optional.isPresent()) {
                        task.resourceNotFoundHook();
                        continue;
                    }
                    task.resourceFoundHook();
                    resource = optional.get();
                    while (task.getStatus() == StatusEnum.STARTED) {
                        int singletonCycle = 0;
                        try {
                            Optional<R> data = this.processor.processor(task, resource);
                            task.processorSuccessHook();
                            if (optional.isPresent()) {
                                try {
                                    repository.persist(task, data.get());
                                    task.persisSuccessHook();
                                } catch (Exception e) {
                                    task.persistErrorHook();
                                    LOGGER.log(Level.WARNING, "数据持久化失败", e);
                                }
                            }
                        } catch (Exception e) {
                            task.processorErrorHook();
                            LOGGER.log(Level.WARNING, "数据处理失败", e);
                        }
                        if (singletonCycleCheck(singletonCycle)) break;
                        optional = sink.get(task);
                        if (!optional.isPresent()) {
                            task.resourceNotFoundHook();
                            break;
                        }
                    }
                    taskAfterHook.accept(task);
                }
            } catch (Exception e) {
                cycleError.run();
                LOGGER.log(Level.SEVERE, "循环过程出错", e);
                execute();
            } finally {
                cycleFinally.run();
            }
        });
    }

    private boolean singletonCycleCheck(int singletonCycle) {
        return singletonCycle >= CycleEnum.SINGLETON_CYCLE_LIMIT.getCycle();
    }

    private AbstractTask randomRunnableTask() {
        //优先获取test
        List<AbstractTask> testList = tasks.stream().filter(t -> t.getStatus() == StatusEnum.STARTED)
                .filter(AbstractTask::isTest)
                .collect(Collectors.toList());
        if (!testList.isEmpty()) {
            return testList.get(Math.abs(random.nextInt() % testList.size()));
        }

        List<AbstractTask> list = tasks.stream().filter(t -> t.getStatus() == StatusEnum.STARTED).collect(Collectors.toList());
        if (!list.isEmpty()) {
            return list.get(Math.abs(random.nextInt() % list.size()));
        } else {
            lockTasks();
        }
        return randomRunnableTask();
    }

    private AbstractTask getTaskByTag(String taskTag) {
        return tasks.stream().filter(task -> task.getTaskTag().equals(taskTag)).findAny().get();
    }

    private void taskCycleCheck(AbstractTask task) {
        if (task.isTest() && task.getTaskStatistics().getResourceTotalCycle() > CycleEnum.RESOURCE_TEST_LIMIT.getCycle()) {
            destroyTask(task.getTaskTag());
        }
        if (task.getTaskStatistics().getResourceNotFoundCycle().get() > CycleEnum.RESOURCE_NOT_FOUND_LIMIT.getCycle()) {
            finishTask(task.getTaskTag());
        }
        if (task.getTaskStatistics().getPersistErrorCycle().get() > CycleEnum.PROCESSOR_ERROR_LIMIT.getCycle()) {
            stopTask(task.getTaskTag());
        }
    }


    // task lock method
    private void lockTasks() {
        taskLock.lock();
        try {
            taskCondition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            taskLock.unlock();
        }
    }

    private void releaseTasks() {
        taskLock.lock();
        try {
            taskCondition.signalAll();
        } finally {
            taskLock.unlock();
        }
    }


    public void setSink(Sink<S> sink) {
        this.sink = sink;
    }

    public synchronized void setStatus(StatusEnum status) {
        this.status = status;
    }

    public void setRepository(Repository<R> repository) {
        this.repository = repository;
    }

    public void setProcessor(Processor<S, R> processor) {
        this.processor = processor;
    }

    public int getThread() {
        return thread;
    }

    public List<AbstractTask> getTasks() {
        return tasks;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setThread(int thread) {
        this.thread = thread;
    }

    public void setTaskBeforeHook(Consumer<AbstractTask> taskBeforeHook) {
        this.taskBeforeHook = taskBeforeHook;
    }

    public void setTaskAfterHook(Consumer<AbstractTask> taskAfterHook) {
        this.taskAfterHook = taskAfterHook;
    }

    public void setCycleBefore(Runnable cycleBefore) {
        this.cycleBefore = cycleBefore;
    }

    public void setCycleError(Runnable cycleError) {
        this.cycleError = cycleError;
    }

    public void setCycleFinally(Runnable cycleFinally) {
        this.cycleFinally = cycleFinally;
    }


}
