package com.jal.crawler;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.JedisShardInfo;

/**
 * Created by jal on 2017/1/29.
 */
public class Test {
    public static void main(String[] args) {
        JedisShardInfo shardInfo=new JedisShardInfo("192.168.1.73",6379);
        shardInfo.setPassword("zbbJAL86!");
        RedisConnectionFactory redisConnectionFactory= new JedisConnectionFactory(shardInfo);
        RedisTemplate redisTemplate=new StringRedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.afterPropertiesSet();
        redisTemplate.opsForSet().add("page_url","www.baidu.com");
    }
}
