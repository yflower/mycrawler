package com.jal.crawler.processor;

import com.jal.crawler.task.Task;

import java.util.List;
import java.util.Optional;

/**
 * Created by jianganlan on 2017/5/5.
 */
public interface LinkProcessor {
    Optional<List<String>> processor(List<String> link, Task task);
}
