package com.jal.crawler.web.data.VO;

import com.jal.crawler.proto.ComponentClient;

import java.util.List;

/**
 * Created by jal on 2017/2/10.
 */
public class ComponentVO {

    private String status;

    private String address;

    private int thread;

    private int taskNum;

    private List<ComponentClient.internalTask> tasks;

    public int getThread() {
        return thread;
    }

    public void setThread(int thread) {
        this.thread = thread;
    }

    public int getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(int taskNum) {
        this.taskNum = taskNum;
    }

    public List<ComponentClient.internalTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<ComponentClient.internalTask> tasks) {
        this.tasks = tasks;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ComponentVO{" +
                "status='" + status + '\'' +
                ", address='" + address + '\'' +
                ", thread=" + thread +
                ", taskNum=" + taskNum +
                ", tasks=" + tasks +
                '}';
    }
}
