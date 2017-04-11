package com.jal.crawler.web.data.view.task;

/**
 * Created by jianganlan on 2017/4/10.
 */
public class TaskStatusVO {
    private String taskTag;

    private long curTime;

    private long beginTime;

    private long endTime;

    private int status;

    private int pageTimes;

    private int errorTimes;

    public String getTaskTag() {
        return taskTag;
    }

    public void setTaskTag(String taskTag) {
        this.taskTag = taskTag;
    }

    public long getCurTime() {
        return curTime;
    }

    public void setCurTime(long curTime) {
        this.curTime = curTime;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPageTimes() {
        return pageTimes;
    }

    public void setPageTimes(int pageTimes) {
        this.pageTimes = pageTimes;
    }

    public int getErrorTimes() {
        return errorTimes;
    }

    public void setErrorTimes(int errorTimes) {
        this.errorTimes = errorTimes;
    }
}
