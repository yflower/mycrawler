package com.jal.crawler.task;

import com.cufe.taskProcessor.task.AbstractTask;
import com.jal.crawler.download.DownloadProcessor;
import com.jal.crawler.url.AbstractPageUrlFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jal on 2017/1/23.
 */
public class Task extends AbstractTask {
    private boolean urlInit;
    private boolean dynamic;

    private DownloadProcessor preProcessor = downLoad -> {
    };
    private DownloadProcessor postProcessor = downLoad -> {
    };
    private Set<String> startUrls = new HashSet<>();


    public synchronized boolean isUrlInit() {
        if (urlInit) {
            return true;
        } else {
            urlInit = true;
            return false;
        }
    }

    public boolean isDynamic() {
        return dynamic;
    }

    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    public DownloadProcessor getPreProcessor() {
        return preProcessor;
    }

    public void setPreProcessor(DownloadProcessor preProcessor) {
        this.preProcessor = preProcessor;
    }

    public DownloadProcessor getPostProcessor() {
        return postProcessor;
    }

    public void setPostProcessor(DownloadProcessor postProcessor) {
        this.postProcessor = postProcessor;
    }

    public Set<String> getStartUrls() {
        return startUrls;
    }

    public void setStartUrls(Set<String> startUrls) {
        this.startUrls = startUrls;
    }

    public void urlsInit(AbstractPageUrlFactory abstractPageUrlFactory) {
        abstractPageUrlFactory.addUrl(super.getTaskTag(), startUrls);
    }

    @Override
    public void init() {

    }
}
