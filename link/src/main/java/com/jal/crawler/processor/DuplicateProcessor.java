package com.jal.crawler.processor;

import com.jal.crawler.task.Task;

import java.util.List;

/**
 * Created by jianganlan on 2017/5/10.
 */
public interface DuplicateProcessor {
    List<String> duplicateCheck(List<String> links, Task task);
}
