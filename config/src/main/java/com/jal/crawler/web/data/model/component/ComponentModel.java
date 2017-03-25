package com.jal.crawler.web.data.model.component;

import com.jal.crawler.web.data.enums.ComponentEnum;

/**
 * Created by jal on 2017/2/19.
 */
public class ComponentModel {
    private ComponentEnum componentEnum;

    private String host;

    private int port;

    public ComponentModel() {
    }

    public ComponentModel(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public ComponentEnum getComponentEnum() {
        return componentEnum;
    }

    public void setComponentEnum(ComponentEnum componentEnum) {
        this.componentEnum = componentEnum;
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
