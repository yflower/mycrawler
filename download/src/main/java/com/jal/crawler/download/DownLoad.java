package com.jal.crawler.download;

import com.jal.crawler.page.Page;
import com.jal.crawler.request.PageRequest;

/**
 * Created by home on 2017/1/8.
 */
public interface DownLoad {
    Page downLoad(PageRequest pageRequest);
}
