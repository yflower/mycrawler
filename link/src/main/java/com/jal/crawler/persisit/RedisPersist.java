package com.jal.crawler.persisit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

import java.util.*;

/**
 * Created by home on 2017/1/20.
 */
public class RedisPersist implements Persist {
    private static final Logger LOGGER= LoggerFactory.getLogger(RedisPersist.class);


    private RedisTemplate<String, String> redisTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    private SetOperations<String, String> stringStringSetOperations;

    public RedisPersist(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.stringStringSetOperations=redisTemplate.opsForSet();
    }


    public void persist(String taskTag, List<String> links) {
        stringStringSetOperations.add(taskTag+"_url",links.toArray(new String[0]));
    }
}
