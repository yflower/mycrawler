package com.jal.crawler.context;

import com.jal.crawler.download.*;
import com.jal.crawler.page.Page;
import com.jal.crawler.page.PagePersist;
import com.jal.crawler.page.RedisPagePersist;
import com.jal.crawler.request.PageRequest;
import com.jal.crawler.task.Task;
import com.jal.crawler.url.AbstractPageUrlFactory;
import com.jal.crawler.url.RedisPageUrlFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by home on 2017/1/9.
 */
@Component
public class DownLoadContext {

    private static final Logger logger = LoggerFactory.getLogger(DownLoadContext.class);

    private int thread;

    private boolean isProxy = false;

    private String proxyHost;

    private int proxyPort;

    private int sleepTime;

    private List<Task> tasks;

    private Lock taskLock = new ReentrantLock();

    private Condition taskCondition = taskLock.newCondition();


    private AbstractDownLoad.AbstractBuilder staticBuilder;

    private AbstractDownLoad.AbstractBuilder dynamicBuilder;

    private RedisTemplate redisTemplate;

    AbstractPageUrlFactory abstractPageUrlFactory;

    ExecutorService executorService;

    PagePersist pagePersist;

    /*
         NO_INIT=0;
        INIT=1;
        STARTED=2;
        STOPPED=3;
        DESTORYED=4;
     */
    private int status;

    public boolean addTask(Task task) {
        tasks.add(task);
        task.setStatus(2);
        signalTask();
        return true;
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


    public DownLoadContext proxy(String host, int port) {
        isProxy = true;
        proxyHost = host;
        proxyPort = port;
        return this;
    }

    public DownLoadContext proxy(String address) {
        String[] args = address.split(":");
        return proxy(args[0], Integer.parseInt(args[1]));
    }

    public DownLoadContext sleep(int sleepTime) {
        this.sleepTime = sleepTime;
        return this;
    }

    public DownLoadContext thread(int thread) {
        this.thread = thread;
        return this;
    }

    public DownLoadContext redisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        return this;
    }

    //tasktag
    public DownLoadContext signalTask() {
        releaseTasks();
        return this;
    }

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
        return tasks.stream().filter(task -> task.getTaskTag().equals(taskTag)).findAny().get();
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
            taskCondition.signal();
        } finally {
            taskLock.unlock();
        }
    }


    public void run() {
        init();
        setStatus(2);
        for (int i = 0; i < thread; ++i) {
            executorService.submit(() -> {
                AbstractDownLoad staticDownLoad = staticBuilder.build();
                AbstractDownLoad dynamicDownLoad = dynamicBuilder.build();
                Task task;
                while ((task = randomRunnableTask()) != null) {
                    if (!task.isUrlInit()&&!task.getStartUrls().isEmpty()) task.urlsInit(abstractPageUrlFactory);
                    AbstractDownLoad downLoad = task.isDynamic() ? dynamicDownLoad : staticDownLoad;
                    downLoad.setPreProcessor(task.getPreProcessor());
                    downLoad.setPostProcessor(task.getPostProcessor());
                    downLoad.init();
                    String url;
                    while (task.getStatus() == 2 && (url = abstractPageUrlFactory.fetchUrl(task.getTaskTag())) != null) {
                        String finalUrl = url;
                        Page page = downLoad.downLoad(new PageRequest(finalUrl));
                        if(!downLoad.isSkip())pagePersist.persist(task.getTaskTag(), page);
                        downLoad.setSkip(false);
                        sleep();
                    }
                }
            });
        }
        close();

    }

    private void init() {
        dynamicBuilder = new SeleniumDownload.Builder();
        staticBuilder = new OkHttpDownLoad.Builder();
        if (isProxy) {
            dynamicBuilder.proxy(proxyHost, proxyPort);
            staticBuilder.proxy(proxyHost, proxyPort);
        }
        executorService = Executors.newFixedThreadPool(thread);
        if (redisTemplate != null) {
            abstractPageUrlFactory = new RedisPageUrlFactory(redisTemplate);
            pagePersist = new RedisPagePersist(redisTemplate);
        } else {
            throw new NullPointerException("redisTemplate must init");
        }
        tasks = new ArrayList<>();
        setStatus(1);
    }

    private void sleep() {
        try {
            Thread.currentThread().sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void close() {
        executorService.shutdown();
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
