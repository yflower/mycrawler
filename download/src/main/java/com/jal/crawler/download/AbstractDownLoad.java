package com.jal.crawler.download;

import com.jal.crawler.page.Page;
import com.jal.crawler.request.PageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by home on 2017/1/8.
 */
public abstract class AbstractDownLoad implements DownLoad {

    private static final Logger logger = LoggerFactory.getLogger(AbstractDownLoad.class);
    public DownloadProcessor preProcessor;
    public DownloadProcessor postProcessor;
    protected String proxyHost;
    protected int proxyPort;
    protected boolean isProxy;
    protected boolean isSkip;

    public AbstractDownLoad init() {
        preProcessor.process(this);
        return this;
    }

    public AbstractDownLoad reset() {
        internalReset();
        return this;
    }

    public AbstractDownLoad close() {
        internalClose();
        return this;
    }


    @Override
    public Optional<Page> downLoad(PageRequest pageRequest) {
        try {
            internalDown(pageRequest);
            postProcessor.process(this);
        } catch (IOException ex) {
            logger.info("fail down html  {}", pageRequest.getUrl());
            return Optional.empty();
        }
        logger.info("success down html {}", pageRequest.getUrl());
        Page page = new Page();
        page.setUrl(pageRequest.getUrl());
        page.setRawContent(rawContent());
        page.setHeaders(responseHeaders());
        page.setCode(responseCode());
        return Optional.of(page);
    }


    public boolean isSkip() {
        return isSkip;
    }

    public void setSkip(boolean skip) {
        this.isSkip = skip;
    }

    public boolean isProxy() {
        return isProxy;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public void setPreProcessor(DownloadProcessor preProcessor) {
        this.preProcessor = preProcessor;
    }

    public void setPostProcessor(DownloadProcessor postProcessor) {
        this.postProcessor = postProcessor;
    }


    protected abstract void internalDown(PageRequest pageRequest) throws IOException;

    protected abstract void internalClose();

    protected abstract String rawContent();

    protected abstract int responseCode();

    protected abstract Map<String, List<String>> responseHeaders();

    protected abstract void internalReset();

    public static abstract class AbstractBuilder {

        protected String proxyHost;

        protected int proxyPort;

        protected boolean isProxy;

        public AbstractBuilder proxy(String proxyHost, int proxyPort) {
            this.isProxy = true;
            this.proxyHost = proxyHost;
            this.proxyPort = proxyPort;
            logger.debug("configure proxy host:{} port:{}", proxyHost, proxyPort);
            return this;
        }

        public AbstractDownLoad build() {
            logger.info("success create download ");
            if (isProxy) {
                internalProxy(proxyHost, proxyPort);
            }
            AbstractDownLoad downLoad = internalBuild();
            return downLoad;
        }

        protected abstract AbstractDownLoad internalBuild();

        protected abstract AbstractBuilder internalProxy(String proxyHost, int proxyPort);
    }

}
