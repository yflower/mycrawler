package com.jal.crawler.persist;

import java.util.Map;

/**
 * Created by home on 2017/1/12.
 */
@FunctionalInterface
public interface Persist {
    void persist(String taskTag, Map<String, Object> map);
}
