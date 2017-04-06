package com.jal.crawler.page;

import java.util.Optional;

/**
 * Created by jal on 2017/1/10.
 */
@FunctionalInterface
public interface PageFetch {
    Optional<Page> fetch(String taskTag);
}
