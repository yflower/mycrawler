package com.jal.crawler.url;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by home on 2017/1/8.
 */
public abstract class AbstractPageUrlFactory implements PageUrlFactory {
    private static final Logger logger = LoggerFactory.getLogger(AbstractPageUrlFactory.class);

    private AtomicInteger count = new AtomicInteger(0);

    public AbstractPageUrlFactory() {
    }


    public void addUrl(String taskTag, String url) {
        count.incrementAndGet();
        urlRegister(taskTag, url);
    }

    public Optional<String> getUrl(String taskTag) {
        count.decrementAndGet();
        String result = fetchUrl(taskTag);
        if (result == null) {
            return Optional.empty();
        }
        return Optional.of(result);
    }

    public int count() {
        return count.get();
    }

    public void addUrl(String taskTag, Set<String> urls) {
        count.addAndGet(urls.size());
        urlRegister(taskTag, urls);
    }
}
