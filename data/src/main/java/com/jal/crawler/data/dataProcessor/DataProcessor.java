package com.jal.crawler.data.dataProcessor;

import com.jal.crawler.data.Data;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by jianganlan on 2017/5/2.
 */
public interface DataProcessor {
    Optional<Data> processor(List<Map<String,Object>> data);
}
