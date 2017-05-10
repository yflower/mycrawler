package com.jal.crawler.processor;

import com.jal.crawler.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Created by jianganlan on 2017/5/5.
 */
public class PatternLinkProcessor implements LinkProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PatternLinkProcessor.class);

    private RedisTemplate redisTemplate;

    private DuplicateProcessor duplicateProcessor;

    public PatternLinkProcessor(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        duplicateProcessor = new RedisDuplicateProcessor(redisTemplate);
    }

    @Override
    public Optional<List<String>> processor(List<String> links, Task task) {
        List<String> result = new ArrayList<>();
        for (String s : links) {
            if (task.getLinkPattern().stream().filter(t -> Pattern.matches(t, s)).findAny().isPresent()) {
                result.add(s);
            }
        }
        //去重操作
        List<String> afterDuplicateCheck = duplicateProcessor.duplicateCheck(result, task);

        LOGGER.info("连接处理成功，{}->{}", links.size(), afterDuplicateCheck.size());

        if (afterDuplicateCheck.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(afterDuplicateCheck);
        }
    }
}
