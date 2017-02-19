package com.jal.crawler.download;

/**
 * Created by home on 2017/1/16.
 */
@FunctionalInterface
public interface DownloadProcessor {
    void process(AbstractDownLoad downLoad);
}
