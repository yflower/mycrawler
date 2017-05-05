package com.jal.crawler.context;

import com.cufe.taskProcessor.component.client.AbstractComponentClientFactory;
import com.cufe.taskProcessor.context.ComponentContext;
import com.jal.crawler.component.LinkClientFactory;
import com.jal.crawler.enums.ComponentTypeEnum;
import com.jal.crawler.fetch.LinkFetch;
import com.jal.crawler.persisit.Persist;
import com.jal.crawler.persisit.RedisPersist;
import com.jal.crawler.processor.LinkProcessor;
import com.jal.crawler.processor.PatternProcessor;
import com.jal.crawler.task.Task;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by jianganlan on 2017/5/5.
 */
@Component
public class LinkContext extends ComponentContext<List<String>, List<String>, Task> {

    private LinkClientFactory downloadClientFactory = new LinkClientFactory(this);


    private LinkFetch linkFetch;

    private Persist persist;

    private LinkProcessor linkProcessor;

    private RedisTemplate redisTemplate;

    {

        sink = task -> linkFetch.fetch(task.getTaskTag());

        processor = (task, resource) -> linkProcessor.processor(resource, (Task) task);

        repository = (task, links) -> persist.persist(task.getTaskTag(), links);
    }

    @Override
    protected void internalInit() {
        persist = new RedisPersist(redisTemplate);
        linkProcessor = new PatternProcessor();
    }

    @Override
    public int componentType() {
        return ComponentTypeEnum.LINK.getCode();
    }

    @Override
    public AbstractComponentClientFactory getComponentClientFactory() {
        return downloadClientFactory;
    }


    public void setLinkFetch(LinkFetch linkFetch) {
        this.linkFetch = linkFetch;
    }

    public void setPersist(Persist persist) {
        this.persist = persist;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
