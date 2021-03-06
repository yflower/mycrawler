package com.jal.crawler.context;

import com.cufe.taskProcessor.Processor;
import com.cufe.taskProcessor.component.client.AbstractComponentClientFactory;
import com.cufe.taskProcessor.context.ComponentContext;
import com.cufe.taskProcessor.task.AbstractTask;
import com.jal.crawler.component.DownloadClientFactory;
import com.jal.crawler.download.AbstractDownLoad;
import com.jal.crawler.download.OkHttpDownLoad;
import com.jal.crawler.download.SeleniumDownload;
import com.jal.crawler.enums.ComponentTypeEnum;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by home on 2017/1/9.
 */
@Component
public class DownLoadContext extends ComponentContext<String, List<Page>, Task> {

    private static final Logger logger = LoggerFactory.getLogger(DownLoadContext.class);

    AbstractPageUrlFactory abstractPageUrlFactory;
    PagePersist pagePersist;
    private boolean isProxy = false;
    private String proxyHost;
    private int proxyPort;
    private int sleepTime;
    private AbstractDownLoad.AbstractBuilder staticBuilder;
    private AbstractDownLoad.AbstractBuilder dynamicBuilder;
    private ThreadLocal<AbstractDownLoad> staticDownload = new ThreadLocal<>();
    private ThreadLocal<AbstractDownLoad> dynamicDownload = new ThreadLocal<>();
    private RedisTemplate redisTemplate;
    private ThreadLocal<AbstractDownLoad> downLoad = new ThreadLocal<>();
    private DownloadClientFactory downloadClientFactory = new DownloadClientFactory(this);

    {

        sink = task -> abstractPageUrlFactory.getUrl(task.getTaskTag());


        repository = (task, page) -> pagePersist.persist(task.getTaskTag(), page);


        processor = new Processor<String, List<Page>, Task>() {
            @Override
            public Optional<List<Page>> processor(AbstractTask task, String resource) {
                long start = System.currentTimeMillis();
                Optional<List<Page>> result = downLoad.get().downLoad(new PageRequest(resource));
                logger.info("下载消耗时间 {}", System.currentTimeMillis() - start);
                return result;
            }


            public void taskBeforeHook(Task task) {
                downLoad.set(task.isDynamic() ? dynamicDownload.get() : staticDownload.get());
                if (!task.isUrlInit() && !task.getStartUrls().isEmpty()) {
                    task.urlsInit(abstractPageUrlFactory);
                }
                downLoad.get().setPreProcessor(task.getPreProcessor());
                downLoad.get().setPostProcessor(task.getPostProcessor());
                downLoad.get().init();
            }

            public void taskAfterHook(Task task) {
                dynamicDownload.get().reset();
                staticDownload.get().reset();
            }


            public void cycleBeforeHook() {
                staticDownload.set(staticBuilder.build());
                dynamicDownload.set(dynamicBuilder.build());
            }


            public void cycleErrorHook() {
                dynamicDownload.get().close();
                staticDownload.get().close();
            }


            public void cycleFinalHook() {
                dynamicDownload.get().close();
                staticDownload.get().close();
            }
        };
    }

    public void proxy(String address) {
        String[] args = address.split(":");
        isProxy = true;
        proxyHost = args[0];
        proxyPort = Integer.parseInt(args[1]);
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

    @Override
    public int componentType() {
        return ComponentTypeEnum.DOWNLOAD.getCode();
    }

    @Override
    public AbstractComponentClientFactory getComponentClientFactory() {
        return downloadClientFactory;
    }


    public int getSleepTime() {
        return sleepTime;
    }

    public boolean isProxy() {
        return isProxy;
    }

    public List<String> proxyList() {
        if (isProxy) {
            return Arrays.asList(proxyHost + ":" + proxyPort);
        }
        return new ArrayList<>();
    }
}
