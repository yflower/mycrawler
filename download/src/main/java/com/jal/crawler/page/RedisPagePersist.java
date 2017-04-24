package com.jal.crawler.page;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

/**
 * Created by jal on 2017/1/9.
 */
@Component
public class RedisPagePersist implements PagePersist {


    SetOperations<String, String> stringStringSetOperations;
    private RedisTemplate<String, String> redisTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();

    public RedisPagePersist(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        stringStringSetOperations = redisTemplate.opsForSet();
    }


    @Override
    public void persist(String taskTag, Page page) {
        try {
            stringStringSetOperations.add(taskTag + "_page", objectMapper.writeValueAsString(page));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }


}
