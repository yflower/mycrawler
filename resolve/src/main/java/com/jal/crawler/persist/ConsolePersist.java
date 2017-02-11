package com.jal.crawler.persist;

import java.util.Map;

/**
 * Created by home on 2017/1/12.
 */
public class ConsolePersist implements Persist {
    @Override
    public void persist(String taskTag, Map<String, Object> map) {
        System.out.println(map);
        ;
    }
}
