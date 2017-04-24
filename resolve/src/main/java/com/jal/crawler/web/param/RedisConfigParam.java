package com.jal.crawler.web.param;

/**
 * Created by jianganlan on 2017/4/15.
 */
public class RedisConfigParam {
    private String host;

    private int port;

    private String password;


    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getPassword() {
        return password;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "RedisConfigParam{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", password='" + password + '\'' +
                '}';
    }
}
