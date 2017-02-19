package com.jal.crawler.web.data.view;

/**
 * 所有db设置相关的显示
 * Created by jal on 2017/2/18.
 */
public class DBConfigVO {
    private String redisConfig;

    private String mongoConfig;

    public String getRedisConfig() {
        return redisConfig;
    }

    public void setRedisConfig(String redisConfig) {
        this.redisConfig = redisConfig;
    }

    public String getMongoConfig() {
        return mongoConfig;
    }

    public void setMongoConfig(String mongoConfig) {
        this.mongoConfig = mongoConfig;
    }

    @Override
    public String toString() {
        return "DBConfigVO{" +
                "redisConfig='" + redisConfig + '\'' +
                ", mongoConfig='" + mongoConfig + '\'' +
                '}';
    }
}
