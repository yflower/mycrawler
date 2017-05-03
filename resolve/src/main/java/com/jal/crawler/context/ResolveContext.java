package com.jal.crawler.context;

import com.cufe.taskProcessor.component.client.AbstractComponentClientFactory;
import com.cufe.taskProcessor.context.ComponentContext;
import com.jal.crawler.component.ResolveClientFactory;
import com.jal.crawler.enums.ComponentTypeEnum;
import com.jal.crawler.page.Page;
import com.jal.crawler.page.PageFetch;
import com.jal.crawler.page.RedisPageFetch;
import com.jal.crawler.persist.ConsolePersist;
import com.jal.crawler.persist.Persist;
import com.jal.crawler.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by home on 2017/1/12.
 */
@Component
public class ResolveContext extends ComponentContext<Page, Map<String, Object>, Task> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResolveContext.class);

    private PageFetch pageFetch;

    private Persist persist;

    private RedisTemplate redisTemplate;

    private AbstractComponentClientFactory componentClientFactory = new ResolveClientFactory(this);


    public ResolveContext persist(Persist persist) {
        this.persist = persist;
        return this;
    }

    public ResolveContext redisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        return this;
    }

    {
        sink = task -> pageFetch.fetch(task.getTaskTag());

        processor = (t, page) -> ((Task) t).result(page);

        repository = (t, map) -> persist.persist(t.getTaskTag(), map);
    }

    @Override
    protected void internalInit() {
        if (persist == null) {
            persist = new ConsolePersist();
        }
        if (redisTemplate != null) {
            pageFetch = new RedisPageFetch(redisTemplate);
        } else {
            throw new NullPointerException("redis必须先设置");
        }
    }

    @Override
    public int componentType() {
        return ComponentTypeEnum.RESOLVE.getCode();
    }

    @Override
    public AbstractComponentClientFactory getComponentClientFactory() {
        return componentClientFactory;
    }


}
