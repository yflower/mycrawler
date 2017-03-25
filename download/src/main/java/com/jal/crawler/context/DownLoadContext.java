package com.jal.crawler.context;

import com.jal.crawler.download.AbstractDownLoad;
import com.jal.crawler.download.OkHttpDownLoad;
import com.jal.crawler.download.SeleniumDownload;
import com.jal.crawler.enums.StatusEnum;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Created by home on 2017/1/9.
 */
@Component
public class DownLoadContext {

    private static final Logger logger = LoggerFactory.getLogger(DownLoadContext.class);
    AbstractPageUrlFactory abstractPageUrlFactory;
    ExecutorService executorService;
    PagePersist pagePersist;
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
    private ThreadLocalRandom random;

    private StatusEnum status = StatusEnum.NO_INIT;

    public boolean addTask(Task task) {
        tasks.add(task);
        task.setStatus(StatusEnum.STARTED);
        signalTask();
        return true;
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
        List<Task> list = tasks.stream().filter(t -> t.getStatus() == StatusEnum.STARTED).collect(Collectors.toList());
        if (!list.isEmpty()) {
            return list.get(Math.abs(random.nextInt() % list.size()));
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
        setStatus(StatusEnum.STARTED);
        for (int i = 0; i < thread; ++i) {
            execute();
        }

    }

    private void execute() {
        executorService.submit(() -> {

            AbstractDownLoad staticDownLoad = staticBuilder.build();
            AbstractDownLoad dynamicDownLoad = dynamicBuilder.build();
            random = ThreadLocalRandom.current();
            Task task;
            String url = null;
            try {
                while ((task = randomRunnableTask()) != null) {
                    if (!task.isUrlInit() && !task.getStartUrls().isEmpty()) task.urlsInit(abstractPageUrlFactory);
                    if ((url = abstractPageUrlFactory.fetchUrl(task.getTaskTag())) == null) {
                        continue;
                    }
                    AbstractDownLoad downLoad = task.isDynamic() ? dynamicDownLoad : staticDownLoad;
                    downLoad.setPreProcessor(task.getPreProcessor());
                    downLoad.setPostProcessor(task.getPostProcessor());
                    downLoad.init();
                    while (task.getStatus() == StatusEnum.STARTED && url != null) {
                        String finalUrl = url;
                        Page page = downLoad.downLoad(new PageRequest(finalUrl));
                        if (!downLoad.isSkip()) {
                            pagePersist.persist(task.getTaskTag(), page);
                            logger.info("persist page");
                        }
                        downLoad.setSkip(false);
                        sleep();
                        url = abstractPageUrlFactory.fetchUrl(task.getTaskTag());
                    }
                    dynamicDownLoad.reset();
                    staticDownLoad.reset();
                }
            } catch (Exception e) {
                logger.error(" download error", e);
                execute();
            } finally {
                staticDownLoad.close();
                dynamicDownLoad.close();
            }

        });
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
        setStatus(StatusEnum.INIT);
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
