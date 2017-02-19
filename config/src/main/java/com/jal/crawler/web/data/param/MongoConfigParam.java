package com.jal.crawler.web.data.param;

import javax.validation.constraints.NotNull;

/**
 * monogo 设置的请求
 * Created by jal on 2017/2/18.
 */
public class MongoConfigParam {
    @NotNull
    private String host;

    @NotNull
    private int port;

    @NotNull
    private String user;

    @NotNull
    private String password;

    @NotNull
    private String database;


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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    @Override
    public String toString() {
        return "MongoConfigModel{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", database='" + database + '\'' +
                '}';
    }
}
