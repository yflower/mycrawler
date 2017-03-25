package com.jal.crawler.web.data.model.taskOperation;

import com.jal.crawler.web.data.enums.ComponentEnum;
import com.jal.crawler.web.data.model.TaskOperationModel;

import java.util.List;

/**
 * Created by jal on 2017/2/25.
 */
public class ResolveOperationModel extends TaskOperationModel {
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

    public ResolveOperationModel(String taskTag) {
        this.setComponentType(ComponentEnum.RESOLVE);
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
