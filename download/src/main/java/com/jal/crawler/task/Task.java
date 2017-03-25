package com.jal.crawler.task;

import com.jal.crawler.download.DownloadProcessor;
import com.jal.crawler.enums.StatusEnum;
import com.jal.crawler.url.AbstractPageUrlFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jal on 2017/1/23.
 */
public class Task {
    public boolean urlInit;
    private StatusEnum status;
    private String taskTag;
    private boolean dynamic;
    private DownloadProcessor preProcessor = downLoad -> {
    };
    private DownloadProcessor postProcessor = downLoad -> {
    };
    private Set<String> startUrls = new HashSet<>();

    public boolean isUrlInit() {
        return urlInit;
    }

    public synchronized void setUrlInit(boolean urlInit) {
        this.urlInit = urlInit;
    }

    public String getTaskTag() {
        return taskTag;
    }

    public void setTaskTag(String taskTag) {
        this.taskTag = taskTag;
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
        abstractPageUrlFactory.addUrl(taskTag, startUrls);
        setUrlInit(true);
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }
}
