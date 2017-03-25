package com.jal.crawler.context;

import com.jal.crawler.enums.StatusEnum;
import com.jal.crawler.page.Page;
import com.jal.crawler.page.PageFetch;
import com.jal.crawler.page.RedisPageFetch;
import com.jal.crawler.persist.ConsolePersist;
import com.jal.crawler.persist.Persist;
import com.jal.crawler.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Created by home on 2017/1/12.
 */
@Component
public class ResolveContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResolveContext.class);

    private PageFetch pageFetch;

    private Persist persist;

    private int thread;

    private List<Task> tasks;

    private ExecutorService executorService;

    private Lock taskLock = new ReentrantLock();

    private Condition taskCondition = taskLock.newCondition();

    private RedisTemplate redisTemplate;

    private ThreadLocalRandom random;

    /*
         NO_INIT=0;
        INIT=1;
        STARTED=2;
        STOPPED=3;
        DESTORYED=4;
     */
    private StatusEnum status;


    //context configure public method
    public void addTask(Task task) {
        this.tasks.add(task);
        task.setStatus(StatusEnum.INIT);
        signalTask();
    }

    public boolean stopTask(String taskTag) {
        Task innerTask = getTaskByTag(taskTag);
        if (innerTask != null && innerTask.getStatus() == StatusEnum.STARTED) {
            innerTask.setStatus(StatusEnum.STOPPED);
            return true;
        } else {
            return false;
        }
    }

    public boolean finishTask(String taskTag) {
        Task innerTask = getTaskByTag(taskTag);
        if (innerTask != null && StatusEnum.isStart(innerTask.getStatus())) {
            innerTask.setStatus(StatusEnum.FINISHED);
            return true;
        } else {
            return false;
        }
    }

    public boolean destroyTask(String taskTag) {
        Task innerTask = getTaskByTag(taskTag);
        if (innerTask != null) {
            innerTask.setStatus(StatusEnum.DESTROYED);
            return true;
        } else {
            return false;
        }
    }

    public ResolveContext thread(int thread) {
        this.thread = thread;
        return this;
    }

    public ResolveContext persist(Persist persist) {
        this.persist = persist;
        return this;
    }

    public ResolveContext redisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        return this;
    }


    public void run() {
        init();
        setStatus(StatusEnum.STARTED);
        for (int i = 0; i < thread; ++i) {
            try {
                execute();
            } catch (Exception e) {
                LOGGER.error("ERROR ", e);
                execute();
            }
        }


    }

    private void execute() {
        executorService.submit(() -> {
            Page page = null;
            Task task = null;
            Map<String, Object> result = null;
            random = ThreadLocalRandom.current();
            while (true) {
                task = randomRunnableTask();
                if ((page = pageFetch.fetch(task.getTaskTag())) != null) {
                    result = task.result(page);
                    persist.persist(task.getTaskTag(), result);
                }
            }
        });
    }
    //public signal method about task

    public ResolveContext signalTask() {
        releaseTasks();
        return this;
    }

    //internal private method

    private Task randomRunnableTask() {
        List<Task> list = tasks.stream().filter(t -> t.getStatus() == StatusEnum.STARTED).collect(Collectors.toList());
        if (!list.isEmpty()) {
            return list.get(Math.abs(random.nextInt() % list.size()));
        } else {
            lockTasks();
        }
        return randomRunnableTask();
    }

    private Task getTaskByTag(String taskTag) {
        return tasks.stream().filter(task -> taskTag.equals(task.getTaskTag())).findFirst().orElseGet(null);
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

    private void init() {
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(thread == 0 ? 1 : thread);
        }
        if (persist == null) {
            persist = new ConsolePersist();
        }
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        if (redisTemplate != null) {
            pageFetch = new RedisPageFetch(redisTemplate);
        } else {
            throw new NullPointerException("redisTemplate must init");
        }
        setStatus(StatusEnum.INIT);
    }

    private synchronized void setStatus(StatusEnum status) {
        this.status = status;
    }

    public synchronized StatusEnum status() {
        return status;
    }

    public List<Task> tasks() {
        return tasks;
    }


}
