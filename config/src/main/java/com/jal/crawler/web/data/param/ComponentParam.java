package com.jal.crawler.web.data.param;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by jal on 2017/2/19.
 */
public class ComponentParam {
    @NotNull
    private String host;

    @Min(1)
    private int port;

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
