package com.jal.crawler.web.data.view.componnet;

import java.util.List;

/**
 * Created by jal on 2017/2/10.
 */

public class DownloadVO extends ComponentVO {

    private int sleepTime;

    private boolean proxy;


    private List<String> proxyAddress;

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public boolean isProxy() {
        return proxy;
    }

    public void setProxy(boolean proxy) {
        this.proxy = proxy;
    }

    public List<String> getProxyAddress() {
        return proxyAddress;
    }

    public void setProxyAddress(List<String> proxyAddress) {
        this.proxyAddress = proxyAddress;
    }

    @Override
    public String toString() {
        return "DownloadVO{" +
                "sleepTime=" + sleepTime +
                ", proxy=" + proxy +
                ", proxyAddress=" + proxyAddress +
                "} " + super.toString();
    }
}
