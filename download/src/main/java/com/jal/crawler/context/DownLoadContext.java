package com.jal.crawler.context;

import com.cufe.taskProcessor.context.ComponentContext;
import com.jal.crawler.download.AbstractDownLoad;
import com.jal.crawler.download.OkHttpDownLoad;
import com.jal.crawler.download.SeleniumDownload;
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

/**
 * Created by home on 2017/1/9.
 */
@Component
public class DownLoadContext extends ComponentContext<String, Page> {

    private static final Logger logger = LoggerFactory.getLogger(DownLoadContext.class);

    AbstractPageUrlFactory abstractPageUrlFactory;
    PagePersist pagePersist;
    private boolean isProxy = false;
    private String proxyHost;
    private int proxyPort;
    private int sleepTime;
    private AbstractDownLoad.AbstractBuilder staticBuilder;
    private AbstractDownLoad.AbstractBuilder dynamicBuilder;
    private AbstractDownLoad staticDownload;
    private AbstractDownLoad dynamicDownload;
    private RedisTemplate redisTemplate;
    private ThreadLocal<AbstractDownLoad> downLoad = new ThreadLocal<>();

    {
        sink = task -> abstractPageUrlFactory.getUrl(task.getTaskTag());

        processor = (task, url) -> downLoad.get().downLoad(new PageRequest(url));

        repository = (task, page) -> pagePersist.persist(task.getTaskTag(), page);

        cycleBefore = () -> {
            staticDownload = staticBuilder.build();
            dynamicDownload = dynamicBuilder.build();
        };

        taskBeforeHook = t -> {
            Task task = (Task) t;
            downLoad.set(task.isDynamic() ? dynamicDownload : staticDownload);
            if (!task.isUrlInit() && !task.getStartUrls().isEmpty()) {
                task.urlsInit(abstractPageUrlFactory);
            }
            downLoad.get().setPreProcessor(task.getPreProcessor());
            downLoad.get().setPostProcessor(task.getPostProcessor());
            downLoad.get().init();
        };

        taskAfterHook = t -> {
            dynamicDownload.reset();
            staticDownload.reset();
        };

        cycleError = () -> {
            dynamicDownload.close();
            staticDownload.close();
        };

        cycleFinally = () -> {
            dynamicDownload.close();
            staticDownload.close();
        };
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

    public DownLoadContext redisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        return this;
    }

    @Override
    protected void internalInit() {
        abstractPageUrlFactory = new RedisPageUrlFactory(redisTemplate);
        pagePersist = new RedisPagePersist(redisTemplate);
        staticBuilder = new OkHttpDownLoad.Builder();
        dynamicBuilder = new SeleniumDownload.Builder();
    }
}
