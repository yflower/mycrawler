package com.jal.crawler.web.data.model.taskOperation;

import com.jal.crawler.web.data.model.TaskOperationModel;

import java.util.List;
import java.util.Set;

/**
 * Created by jal on 2017/2/25.
 */
public class DownloadOperationModel extends TaskOperationModel {
    private boolean dynamic;

    private List<process> preProcess;

    private List<process> postProcess;

    private Set<String> urls;


    public boolean isDynamic() {
        return dynamic;
    }

    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    public List<process> getPreProcess() {
        return preProcess;
    }

    public void setPreProcess(List<process> preProcess) {
        this.preProcess = preProcess;
    }

    public List<process> getPostProcess() {
        return postProcess;
    }

    public void setPostProcess(List<process> postProcess) {
        this.postProcess = postProcess;
    }

    public Set<String> getUrls() {
        return urls;
    }

    public void setUrls(Set<String> urls) {
        this.urls = urls;
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
