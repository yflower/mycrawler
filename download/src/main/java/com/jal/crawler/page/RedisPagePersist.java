package com.jal.crawler.page;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jal on 2017/1/9.
 */
@Component
public class RedisPagePersist implements PagePersist {


    private RedisTemplate<String,String> redisTemplate;

    private ObjectMapper objectMapper=new ObjectMapper();

    public RedisPagePersist(RedisTemplate redisTemplate){
        this.redisTemplate=redisTemplate;
    }


    @Override
    public void persist(String taskTag,Page page) {
        SetOperations<String, String> stringStringSetOperations = redisTemplate.opsForSet();
        try {
            stringStringSetOperations.add(taskTag+"_page",objectMapper.writeValueAsString(page));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }


}
