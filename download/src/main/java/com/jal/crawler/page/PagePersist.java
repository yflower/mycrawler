package com.jal.crawler.page;

import java.util.List;

/**
 * Created by jal on 2017/1/8.
 */
@FunctionalInterface
public interface PagePersist {
    void persist(String taskTag, List<Page> pages);
}
