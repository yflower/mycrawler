package com.jal.crawler.download;

import com.jal.crawler.page.Page;
import com.jal.crawler.request.PageRequest;

import java.util.List;
import java.util.Optional;

/**
 * Created by home on 2017/1/8.
 */
public interface DownLoad {
    Optional<List<Page>> downLoad(PageRequest pageRequest);
}
