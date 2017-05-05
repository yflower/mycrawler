package com.jal.crawler.web.param;


import java.util.List;

/**
 * Created by jianganlan on 2017/4/24.
 */
public class LinkTaskOpParam {
    private String taskTag;

    private int taskType;

    private boolean test;

    private List<String> linkPattern;

    public String getTaskTag() {
        return taskTag;
    }

    public void setTaskTag(String taskTag) {
        this.taskTag = taskTag;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public List<String> getLinkPattern() {
        return linkPattern;
    }

    public void setLinkPattern(List<String> linkPattern) {
        this.linkPattern = linkPattern;
    }
}
