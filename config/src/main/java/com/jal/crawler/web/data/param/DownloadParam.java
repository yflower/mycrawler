package com.jal.crawler.web.data.param;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * download config param 类
 * Created by jal on 2017/2/19.
 */
public class DownloadParam {
    private List<downloadSocket> downloads;

    public List<downloadSocket> getDownloads() {
        return downloads;
    }

    public void setDownloads(List<downloadSocket> downloads) {
        this.downloads = downloads;
    }

    public static class downloadSocket {
        @NotNull
        private String host;

        @Min(1)
        private int port;

        @Min(1)
        private int serverPort;

        @Min(1)
        private int thread;

        @Min(0)
        private int sleepTime;

        private boolean proxy;

        private List<String> proxyAddress;

        public int getThread() {
            return thread;
        }

        public void setThread(int thread) {
            this.thread = thread;
        }

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

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getHost() {

            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getServerPort() {
            return serverPort;
        }

        public void setServerPort(int serverPort) {
            this.serverPort = serverPort;
        }
    }


}
