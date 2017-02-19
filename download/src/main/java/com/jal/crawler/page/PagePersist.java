package com.jal.crawler.page;

/**
 * Created by jal on 2017/1/8.
 */
@FunctionalInterface
public interface PagePersist {
    void persist(String taskTag, Page page);
}
