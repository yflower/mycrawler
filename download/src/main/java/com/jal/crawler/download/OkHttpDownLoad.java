package com.jal.crawler.download;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jal.crawler.page.Page;
import com.jal.crawler.request.PageRequest;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by home on 2017/1/8.
 */
public class OkHttpDownLoad extends AbstractDownLoad {

    private static final Logger logger = LoggerFactory.getLogger(OkHttpDownLoad.class);

    private OkHttpClient client;

    private Response response;

    private OkHttpDownLoad(OkHttpClient okHttpClient) {
        this.client = okHttpClient;
    }


    @Override
    protected void internalDown(PageRequest pageRequest) throws IOException {
        RequestBody requestBody = pageRequest.getBodyParam() == null ? null :
                RequestBody.create(MediaType.parse("json/application"), new ObjectMapper().writeValueAsString(pageRequest.getBodyParam()));
        Headers okHeaders = Headers.of(pageRequest.getHeaders());
        Request request = new Request.Builder()
                .url(pageRequest.getUrl())
                .method(pageRequest.getMethod(), requestBody)
                .headers(okHeaders)
                .build();
        response = client.newCall(request).execute();
    }

    @Override
    protected void internalClose() {

    }

    @Override
    protected String rawContent() {
        String raw = "";
        try {
            raw = response.body().string();
        } catch (IOException e) {
            logger.info("reader the html text error {}", response.request().url());
        }
        return raw;
    }

    @Override
    protected int responseCode() {
        return response.code();
    }

    @Override
    protected Map<String, List<String>> responseHeaders() {
        return response.headers().toMultimap();
    }

    @Override
    protected void internalReset() {

    }

    @Override
    protected List<Page> extraPage() {
        return new ArrayList<>();
    }


    public static class Builder extends AbstractBuilder {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        @Override
        protected AbstractDownLoad internalBuild() {
            AbstractDownLoad downLoad = new OkHttpDownLoad(builder.build());
            return downLoad;
        }

        @Override
        protected AbstractBuilder internalProxy(String proxyHost, int proxyPort) {
            builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)));
            return this;
        }
    }
}
