package com.jal.crawler.processor;

import com.jal.crawler.task.Task;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jianganlan on 2017/5/10.
 */
public class RedisDuplicateProcessor implements DuplicateProcessor {

    private RedisTemplate redisTemplate;

    private SetOperations<String, String> stringStringSetOperations;

    private ValueOperations<String, Integer> lock;

    public RedisDuplicateProcessor(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        stringStringSetOperations = redisTemplate.opsForSet();
        lock = redisTemplate.opsForValue();
    }

    @Override
    public List<String> duplicateCheck(List<String> links, Task task) {
        //锁未创建则添加锁字段，直接获取锁
        Boolean setSuccess = lock.setIfAbsent(task.getTaskTag() + "_lock", 1);
        //若锁已经存在
        if (!setSuccess) {
            //尝试获取锁
            while (lock.getAndSet(task.getTaskTag() + "_lock", 1) == 1) {

            }
        }

        List<String> validLinks = links
                .stream()
                .filter(t -> !stringStringSetOperations.isMember(task.getTaskTag() + "_links", t))
                .collect(Collectors.toList());

        if (!validLinks.isEmpty()) {

            stringStringSetOperations.add(task.getTaskTag() + "_links", validLinks.toArray(new String[0]));
        }
        //释放锁
        lock.set(task.getTaskTag() + "_lock", 0);

        return validLinks;
    }
}
