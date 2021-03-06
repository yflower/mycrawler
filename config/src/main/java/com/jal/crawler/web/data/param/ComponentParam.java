package com.jal.crawler.web.data.param;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by jal on 2017/2/19.
 */
public class ComponentParam {
    @Valid
    private List<socket> components;

    public List<socket> getComponents() {
        return components;
    }

    public void setComponents(List<socket> components) {
        this.components = components;
    }

    public static class socket {
        @NotNull
        private String host;

        @Min(1)
        private int port;

        @Min(1)
        private int serverPort;

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

        public int getServerPort() {
            return serverPort;
        }

        public void setServerPort(int serverPort) {
            this.serverPort = serverPort;
        }

        @Override
        public String toString() {
            return "socket{" +
                    "host='" + host + '\'' +
                    ", port=" + port +
                    ", serverPort=" + serverPort +
                    '}';
        }
    }


}
