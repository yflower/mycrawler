package com.jal.crawler.resource;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by jianganlan on 2017/5/2.
 */
public interface DataFetch {
    Optional<List<Map<String, Object>>> fetch(String taskTag);

}
