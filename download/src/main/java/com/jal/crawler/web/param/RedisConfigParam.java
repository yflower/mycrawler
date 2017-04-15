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

    @Override
    public String toString() {
        return "RedisConfigParam{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", password='" + password + '\'' +
                '}';
    }
}
