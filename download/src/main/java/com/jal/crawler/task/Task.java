package com.jal.crawler.task;

import com.cufe.taskProcessor.task.AbstractTask;
import com.jal.crawler.download.DownloadProcessor;
import com.jal.crawler.url.AbstractPageUrlFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jal on 2017/1/23.
 */
public class Task extends AbstractTask {
    private boolean urlInit;
    private boolean dynamic;

    private DownloadProcessor preProcessor = downLoad -> {
    };
    private DownloadProcessor postProcessor = downLoad -> {
    };

    private List<process> pres;

    private List<process> posts;

    private Set<String> startUrls = new HashSet<>();


    public synchronized boolean isUrlInit() {
        if (urlInit) {
            return true;
        } else {
            urlInit = true;
            return false;
        }
    }

    public boolean isDynamic() {
        return dynamic;
    }

    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    public DownloadProcessor getPreProcessor() {
        return preProcessor;
    }

    public void setPreProcessor(DownloadProcessor preProcessor) {
        this.preProcessor = preProcessor;
    }

    public DownloadProcessor getPostProcessor() {
        return postProcessor;
    }

    public void setPostProcessor(DownloadProcessor postProcessor) {
        this.postProcessor = postProcessor;
    }

    public Set<String> getStartUrls() {
        return startUrls;
    }

    public void setStartUrls(Set<String> startUrls) {
        this.startUrls = startUrls;
    }

    public List<process> getPres() {
        return pres;
    }

    public void setPres(List<process> pres) {
        this.pres = pres;
    }

    public List<process> getPosts() {
        return posts;
    }

    public void setPosts(List<process> posts) {
        this.posts = posts;
    }

    public void urlsInit(AbstractPageUrlFactory abstractPageUrlFactory) {
        abstractPageUrlFactory.addUrl(super.getTaskTag(), startUrls);
    }

    @Override
    public void init() {

    }

    public static class process {
        private int order;

        private type type;

        private String query;

        private String value;

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public process.type getType() {
            return type;
        }

        public void setType(process.type type) {
            this.type = type;
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public static enum type {
            CLICK(1),
            INPUT(2),
            INPUT_SUBMIT(3),
            LINK_TO(4),
            WAIT_UTIL(5),
            NONE(15);

            private int code;

            type(int code) {
                this.code = code;
            }

            public static type numberOf(int code) {
                switch (code) {
                    case 1:
                        return CLICK;
                    case 2:
                        return INPUT;
                    case 3:
                        return INPUT_SUBMIT;
                    case 4:
                        return LINK_TO;
                    case 5:
                        return WAIT_UTIL;
                    default:
                        return NONE;
                }
            }
        }
    }
}
