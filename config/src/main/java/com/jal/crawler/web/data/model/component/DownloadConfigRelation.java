package com.jal.crawler.web.data.model.component;

import java.util.List;

/**
 * Created by jianganlan on 2017/5/3.
 */
public class DownloadConfigRelation extends ComponentConfigRelation {

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
}
