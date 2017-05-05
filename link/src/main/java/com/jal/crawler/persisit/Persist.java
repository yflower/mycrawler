package com.jal.crawler.persisit;

import java.util.List;
import java.util.Map;

/**
 * Created by home on 2017/1/12.
 */
@FunctionalInterface
public interface Persist {
    void persist(String taskTag, List<String> links);
}
