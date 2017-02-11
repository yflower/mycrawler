package com.jal.crawler.page;

/**
 * Created by jal on 2017/1/10.
 */
@FunctionalInterface
public interface PageFetch {
    Page fetch(String taskTag);
}
