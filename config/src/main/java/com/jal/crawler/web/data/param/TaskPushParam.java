package com.jal.crawler.web.data.param;

import java.util.List;
import java.util.Set;

/**
 * Created by jal on 2017/3/19.
 */
public class TaskPushParam {

    private download download;

    private resolve resolve;

    public TaskPushParam.download getDownload() {
        return download;
    }

    public void setDownload(TaskPushParam.download download) {
        this.download = download;
    }

    public TaskPushParam.resolve getResolve() {
        return resolve;
    }

    public void setResolve(TaskPushParam.resolve resolve) {
        this.resolve = resolve;
    }

    public static class download {

        private boolean test;

        private boolean dynamic;

        private Set<String> urls;

        private List<process> preProcess;

        private List<process> postProcess;

        public boolean isDynamic() {
            return dynamic;
        }

        public void setDynamic(boolean dynamic) {
            this.dynamic = dynamic;
        }

        public Set<String> getUrls() {
            return urls;
        }

        public void setUrls(Set<String> urls) {
            this.urls = urls;
        }

        public List<process> getPostProcess() {
            return postProcess;
        }

        public void setPostProcess(List<process> postProcess) {
            this.postProcess = postProcess;
        }

        public List<process> getPreProcess() {

            return preProcess;
        }

        public boolean isTest() {
            return test;
        }

        public void setTest(boolean test) {
            this.test = test;
        }

        public void setPreProcess(List<process> preProcess) {
            this.preProcess = preProcess;
        }

        public static class process {
            private int order;

            private int type;

            private String query;

            private String value;

            public int getOrder() {
                return order;
            }

            public void setOrder(int order) {
                this.order = order;
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

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }

    public static class resolve {
        private boolean test;

        private int type;

        private List<var> vars;

        private List<item> items;

        public List<var> getVars() {
            return vars;
        }

        public void setVars(List<var> vars) {
            this.vars = vars;
        }

        public List<item> getItems() {
            return items;
        }

        public void setItems(List<item> items) {
            this.items = items;
        }


        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public boolean isTest() {
            return test;
        }

        public void setTest(boolean test) {
            this.test = test;
        }

        public static class item {
            private String itemName;
            private List<var> vars;


            public String getItemName() {
                return itemName;
            }

            public void setItemName(String itemName) {
                this.itemName = itemName;
            }

            public List<var> getVars() {
                return vars;
            }

            public void setVars(List<var> vars) {
                this.vars = vars;
            }
        }

        public static class var {
            private String name;
            private String query;
            private String value;
            private String option;
            private String optionValue;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getOption() {
                return option;
            }

            public void setOption(String option) {
                this.option = option;
            }

            public String getOptionValue() {
                return optionValue;
            }

            public void setOptionValue(String optionValue) {
                this.optionValue = optionValue;
            }

            public String getQuery() {
                return query;
            }

            public void setQuery(String query) {
                this.query = query;
            }
        }
    }
}
