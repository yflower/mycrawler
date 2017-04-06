package com.jal.crawler.page;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by jal on 2017/1/10.
 */

public class RedisPageFetch implements PageFetch {

    private RedisTemplate<String, String> redisTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    private SetOperations<String, String> stringStringSetOperations;

    public RedisPageFetch(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.stringStringSetOperations=redisTemplate.opsForSet();
    }

    @Override
    public Optional<Page> fetch(String taskTag) {
        String page = stringStringSetOperations.pop(taskTag + "_page");
        if (page != null) {
            try {
                return Optional.of(objectMapper.readValue(page, Page.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }
}
