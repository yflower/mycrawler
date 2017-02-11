package com.jal.crawler.page;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by jal on 2017/1/10.
 */

public class RedisPageFetch implements PageFetch {

    private RedisTemplate<String, String> redisTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    public RedisPageFetch(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Page fetch(String taskTag) {
        SetOperations<String, String> stringStringSetOperations = redisTemplate.opsForSet();
        String page = stringStringSetOperations.pop(taskTag + "_page");
        if (page != null) {
            try {
                return objectMapper.readValue(page, Page.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
