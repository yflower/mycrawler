package com.jal.crawler.web.param;

import com.cufe.taskProcessor.task.TaskTypeEnum;
import com.jal.crawler.task.Task;

import java.util.List;
import java.util.Set;

/**
 * Created by jianganlan on 2017/4/15.
 */
public class DownloadTaskOpParam {
    private boolean test;

    private TaskTypeEnum type;

    private String taskTag;

    private boolean dynamic;

    private List<Task.process> preProcess;

    private List<Task.process> postProcess;

    private Set<String> urls;


    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public TaskTypeEnum getType() {
        return type;
    }

    public void setType(TaskTypeEnum type) {
        this.type = type;
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

    public List<Task.process> getPreProcess() {
        return preProcess;
    }

    public void setPreProcess(List<Task.process> preProcess) {
        this.preProcess = preProcess;
    }

    public List<Task.process> getPostProcess() {
        return postProcess;
    }

    public void setPostProcess(List<Task.process> postProcess) {
        this.postProcess = postProcess;
    }

    public Set<String> getUrls() {
        return urls;
    }

    public void setUrls(Set<String> urls) {
        this.urls = urls;
    }
}
