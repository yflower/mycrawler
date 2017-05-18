package com.jal.crawler.download;

import java.util.concurrent.TimeUnit;

/**
 * Created by home on 2017/1/16.
 */
public abstract class DynamicDownload extends AbstractDownLoad {

    public abstract DynamicDownload input(String inputTextElementQuery, String value);

    public abstract DynamicDownload inputSubmit(String submitElementQuery);

    public abstract DynamicDownload click(String enableClickElementQuery);

    public abstract DynamicDownload waitUtilShow(String elementQuery, long time, TimeUnit timeUnit);

    public abstract DynamicDownload linkTo(String url);

    public abstract DynamicDownload download();

}
