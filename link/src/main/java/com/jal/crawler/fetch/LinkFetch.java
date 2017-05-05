package com.jal.crawler.fetch;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by jianganlan on 2017/5/5.
 */
public interface LinkFetch {
    Optional<List<String>> fetch(String taskTag);

}
