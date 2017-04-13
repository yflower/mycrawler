package com.cufe.taskProcessor.context;

import com.cufe.taskProcessor.Processor;
import com.cufe.taskProcessor.Repository;
import com.cufe.taskProcessor.Sink;
import com.cufe.taskProcessor.component.relation.ComponentRelationHolder;
import com.cufe.taskProcessor.component.ComponentStatus;
import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskProcessor.task.AbstractTask;
import com.cufe.taskProcessor.task.CycleEnum;
import com.cufe.taskProcessor.task.StatusEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


/**
 * Created by jianganlan on 2017/4/3.
 * 主要的task-processor的使用流程。
 */
public abstract class ComponentContext<S, R, T extends AbstractTask> {

    private static final Logger LOGGER = Logger.getLogger(Processor.class.getSimpleName());

    private ComponentRelation componentRelation;

    private ComponentRelationHolder componentRelationHolder;

    protected Sink<S> sink;

    protected Repository<R> repository;

    protected Processor<S, R, T> processor;

    private int thread;

    private List<T> tasks;

    private Lock taskLock = new ReentrantLock();

    private Condition taskCondition = taskLock.newCondition();

    private ThreadLocalRandom random;

    private ExecutorService executorService;


    private void run() {
        for (int i = 0; i < thread; ++i) {
            execute();
        }

    }

    public boolean componentStart(String host, int port) {
        componentRelation = new ComponentRelation();
        componentRelation.setStatus(StatusEnum.NO_INIT);
        componentRelation.setHost(host);
        componentRelation.setPort(port);
        return true;
    }

    public boolean addTask(T task) {
        //只有任务到来时才启动
        if (componentRelation.getStatus() == StatusEnum.INIT) {
            setStatus(StatusEnum.STARTED);
            run();
        }
        tasks.add(task);
        task.setStatus(StatusEnum.STARTED);
        releaseTasks();
        return true;
    }


    public boolean stopTask(String taskTag) {
        Optional<T> optional = getTaskByTag(taskTag);
        T innerTask;
        if (optional.isPresent() && (innerTask = optional.get()).getStatus() == StatusEnum.STARTED) {
            innerTask.setStatus(StatusEnum.STOPPED);
            return true;
        } else {
            return false;
        }
    }

    public boolean finishTask(String taskTag) {
        Optional<T> optional = getTaskByTag(taskTag);
        T interTask;
        if (optional.isPresent() && (StatusEnum.isStart((interTask = optional.get()).getStatus()))) {
            interTask.setStatus(StatusEnum.FINISHED);
            return true;
        } else {
            return false;
        }
    }

    public boolean destroyTask(String taskTag) {
        Optional<T> optional = getTaskByTag(taskTag);
        T innerTask;
        if (optional.isPresent()) {
            innerTask = optional.get();
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

        executorService = Executors.newFixedThreadPool(thread);
        tasks = new ArrayList<>();
        setStatus(StatusEnum.INIT);
        internalInit();

    }


    private void execute() {
        executorService.submit(() -> {
            processor.cycleBeforeHook();
            random = ThreadLocalRandom.current();
            T task;
            S resource;
            try {
                while ((task = randomRunnableTask()) != null) {
                    processor.taskBeforeHook(task);
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
                            if (data.isPresent()) {
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
                    processor.taskAfterHook(task);
                }
            } catch (Exception e) {
                processor.cycleErrorHook();
                LOGGER.log(Level.SEVERE, "循环过程出错", e);
                execute();
            } finally {
                processor.cycleFinalHook();
            }
        });
    }

    private boolean singletonCycleCheck(int singletonCycle) {
        return singletonCycle >= CycleEnum.SINGLETON_CYCLE_LIMIT.getCycle();
    }

    private T randomRunnableTask() {
        //优先获取test
        List<T> testList = tasks.stream().filter(t -> t.getStatus() == StatusEnum.STARTED)
                .filter(AbstractTask::isTest)
                .collect(Collectors.toList());
        if (!testList.isEmpty()) {
            return testList.get(Math.abs(random.nextInt() % testList.size()));
        }

        List<T> list = tasks.stream().filter(t -> t.getStatus() == StatusEnum.STARTED).collect(Collectors.toList());
        if (!list.isEmpty()) {
            return list.get(Math.abs(random.nextInt() % list.size()));
        } else {
            lockTasks();
        }
        return randomRunnableTask();
    }

    private Optional<T> getTaskByTag(String taskTag) {
        return tasks.stream().filter(task -> task.getTaskTag().equals(taskTag)).findAny();
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


    abstract void internalInit();

    public ComponentStatus componentStatus(int componentType) {
        ComponentStatus self = new ComponentStatus();
        self.setComponentType(componentType);
        self.setComponentStatus(componentRelation.getStatus());
        if (componentRelation.getStatus() == StatusEnum.STARTED) {
            self.setTasks((List<AbstractTask>) tasks);
        }
        return self;
    }


    public synchronized void setStatus(StatusEnum status) {
        componentRelation.setStatus(status);
    }

    public List<T> getTasks() {
        return tasks;
    }

    public StatusEnum getStatus() {
        return componentRelation.getStatus();
    }

    public void setThread(int thread) {
        this.thread = thread;
    }


}
