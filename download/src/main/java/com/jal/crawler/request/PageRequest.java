package com.jal.crawler.request;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by home on 2017/1/8.
 */
public class PageRequest {
    private String url;

    private String method;

    private Set<Integer> acceptCode;

    private Map<String, String> bodyParam;

    private Map<String, String> headers;

    public PageRequest(String url) {
        this.url = url;
        method = PageRequestMethod.GET;
        acceptCode = new HashSet<>();
        acceptCode.add(PageResponseCode.OK);
        acceptCode.add(PageResponseCode.CACHE);
        headers = new HashMap<>();
    }

    public String getUrl() {
        return url;
    }


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Set<Integer> getAcceptCode() {
        return acceptCode;
    }

    public void setAcceptCode(Set<Integer> acceptCode) {
        this.acceptCode = acceptCode;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getBodyParam() {
        return bodyParam;
    }

    public void setBodyParam(Map<String, String> bodyParam) {
        this.bodyParam = bodyParam;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
