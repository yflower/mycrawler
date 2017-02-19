package com.jal.crawler.web.data.param;

import javax.validation.constraints.NotNull;

/**
 * redis config 的参数类
 * Created by jal on 2017/2/18.
 */
public class RedisConfigParam {
    @NotNull
    private String host;

    @NotNull
    private int port;

    @NotNull
    private String password;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "RedisConfigModel{" +
                "host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
