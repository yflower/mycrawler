package com.jal.crawler.web.data.view.componnet;

/**
 * Created by jal on 2017/2/10.
 */
public class ComponentVO {

    private String status;

    private String address;

    private int thread;


    public int getThread() {
        return thread;
    }

    public void setThread(int thread) {
        this.thread = thread;
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
                '}';
    }
}
