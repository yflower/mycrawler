package com.jal.crawler.url;

import java.util.List;
import java.util.Set;

/**
 * Created by jal on 2017/2/11.
 */
public interface PageUrlFactory {
    String fetchUrl(String taskTag);

    void urlRegister(String taskTag, String url);

    void urlRegister(String taksTag, List<String> urls);

    void urlRegister(String taksTag, Set<String> urls);
}
