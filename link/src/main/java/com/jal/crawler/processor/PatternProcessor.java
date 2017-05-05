package com.jal.crawler.processor;

import com.jal.crawler.task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Created by jianganlan on 2017/5/5.
 */
public class PatternProcessor implements LinkProcessor {
    @Override
    public Optional<List<String>> processor(List<String> links, Task task) {
        List<String> result = new ArrayList<>();
        for (String s : links) {
            if (task.getLinkPattern().stream().filter(t -> Pattern.matches(t, s)).findAny().isPresent()) {
                result.add(s);
            }
        }
        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(result);
        }
    }
}
