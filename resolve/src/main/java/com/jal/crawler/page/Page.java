package com.jal.crawler.page;

import java.util.List;
import java.util.Map;

/**
 * Created by home on 2017/1/8.
 */
public class Page {

    private String taskTag;

    private String url;

    private String rawContent;

    private int code;

    private Map<String, List<String>> headers;

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getRawContent() {
        return rawContent;
    }

    public void setRawContent(String rawContent) {
        this.rawContent = rawContent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTaskTag() {
        return taskTag;
    }

    public void setTaskTag(String taskTag) {
        this.taskTag = taskTag;
    }

    @Override
    public String toString() {
        return "Page{" +
                "taskTag='" + taskTag + '\'' +
                ", url='" + url + '\'' +
                ", rawContent='" + rawContent + '\'' +
                ", code=" + code +
                ", headers=" + headers +
                '}';
    }
}
