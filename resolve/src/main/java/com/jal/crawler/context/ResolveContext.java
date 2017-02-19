package com.jal.crawler.context;

import com.jal.crawler.page.Page;
import com.jal.crawler.page.PageFetch;
import com.jal.crawler.page.RedisPageFetch;
import com.jal.crawler.persist.ConsolePersist;
import com.jal.crawler.persist.Persist;
import com.jal.crawler.task.Task;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by home on 2017/1/12.
 */
@Component
public class ResolveContext {

    private PageFetch pageFetch;

    private Persist persist;

    private int thread;

    private List<Task> tasks;

    private ExecutorService executorService;

    private Lock taskLock = new ReentrantLock();

    private Condition taskCondition = taskLock.newCondition();

    private RedisTemplate redisTemplate;

    /*
         NO_INIT=0;
        INIT=1;
        STARTED=2;
        STOPPED=3;
        DESTORYED=4;
     */
    private int status;


    //context configure public method
    public void addTask(Task task) {
        this.tasks.add(task);
        task.setStatus(2);
        signalTask();
    }

    public boolean stopTask(String taskTag) {
        Task innerTask = getTaskByTag(taskTag);
        if (innerTask != null && innerTask.getStatus() == 2) {
            innerTask.setStatus(3);
            return true;
        } else {
            return false;
        }
    }

    public boolean finishTask(String taskTag) {
        Task innerTask = getTaskByTag(taskTag);
        if (innerTask != null && innerTask.getStatus() >= 2) {
            innerTask.setStatus(4);
            return true;
        } else {
            return false;
        }
    }

    public boolean destroyTask(String taskTag) {
        Task innerTask = getTaskByTag(taskTag);
        if (innerTask != null) {
            innerTask.setStatus(4);
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
        setStatus(2);
        for (int i = 0; i < thread; ++i) {
            executorService.submit(() -> {
                Page page = null;
                Task task = null;
                Map<String, Object> result = null;
                while (true) {
                    task = randomRunnableTask();
                    if ((page = pageFetch.fetch(task.getTaskTag())) != null) {
                        result = task.result(page);
                        persist.persist(task.getTaskTag(), result);
                    }
                }
            });
        }
        executorService.shutdown();

    }

    //public signal method about task

    public ResolveContext signalTask() {
        releaseTasks();
        return this;
    }

    //internal private method

    private Task randomRunnableTask() {
        Optional<Task> task = tasks.stream().filter(t -> t.getStatus() == 2).findAny();
        if (task.isPresent()) {
            return task.get();
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
        setStatus(1);
    }

    private synchronized void setStatus(int status) {
        this.status = status;
    }

    public synchronized int status() {
        return status;
    }

    public List<Task> tasks() {
        return tasks;
    }


}
