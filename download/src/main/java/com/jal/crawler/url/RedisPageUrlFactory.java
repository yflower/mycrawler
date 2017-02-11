package com.jal.crawler.url;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by home on 2017/1/8.
 */
public class RedisPageUrlFactory extends AbstractPageUrlFactory {

    private RedisTemplate<String, String> redisTemplate;

    public RedisPageUrlFactory(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        setOperations=redisTemplate.opsForSet();
    }

    private SetOperations<String,String> setOperations;


    @Override
    public String fetchUrl(String taskTag) {
        return setOperations.pop(taskTag + "_url");
    }

    @Override
    public void urlRegister(String taskTag, String url) {
        setOperations.add(taskTag + "_url", url);

    }

    @Override
    public void urlRegister(String taksTag, List<String> urls) {
        setOperations.add(taksTag+"_url", urls.toArray(new String[0]));
    }

    @Override
    public void urlRegister(String taksTag, Set<String> urls) {
        setOperations.add(taksTag+"_url", urls.toArray(new String[0]));

    }

}
