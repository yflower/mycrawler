package com.jal.crawler.web.param;


import com.jal.crawler.task.Task;

import java.util.List;

/**
 * Created by jianganlan on 2017/4/24.
 */
public class ResolveTaskOpParam {
    private String taskTag;

    private int taskType;

    private boolean test;

    private List<Task.var> vars;

    private List<Task.item> items;

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

    public List<Task.var> getVars() {
        return vars;
    }

    public void setVars(List<Task.var> vars) {
        this.vars = vars;
    }

    public List<Task.item> getItems() {
        return items;
    }

    public void setItems(List<Task.item> items) {
        this.items = items;
    }
}
