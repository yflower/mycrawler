package com.jal.crawler.web.data.param;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by jal on 2017/2/19.
 */
public class ResolveParam {
    private List<resolveSocket> resolves;

    public List<resolveSocket> getResolves() {
        return resolves;
    }

    public void setResolves(List<resolveSocket> resolves) {
        this.resolves = resolves;
    }

    public static class resolveSocket {
        @NotNull
        private String host;

        @Min(1)
        private int port;

        @Min(1)
        private int thread;

        public int getThread() {
            return thread;
        }

        public void setThread(int thread) {
            this.thread = thread;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }


}
