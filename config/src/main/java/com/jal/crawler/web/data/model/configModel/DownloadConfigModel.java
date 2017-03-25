package com.jal.crawler.web.data.model.configModel;

import com.jal.crawler.web.data.model.ComponentConfigModel;

import java.util.List;

/**
 * Created by jal on 2017/2/19.
 */
public class DownloadConfigModel extends ComponentConfigModel {

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
