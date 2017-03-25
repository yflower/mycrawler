package com.jal.crawler.task;

import com.jal.crawler.enums.StatusEnum;
import com.jal.crawler.page.Page;
import com.jal.crawler.parse.tag.HtmlTag;
import com.jal.crawler.parse.tag.Tag;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by home on 2017/1/20.
 */
public class Task {
    private StatusEnum status;

    private String taskTag;

    private List<var> vars;

    private List<item> items;

    public Task(String taskTag) {
        this.taskTag = taskTag;
        vars = new ArrayList<>();
        items = new ArrayList<>();
    }


    public void var(var var) {
        vars.add(var);
    }

    public void item(String itemName, List<var> vars) {
        item item = new item();
        item.itemName = itemName;
        item.itemVar = vars;
        items.add(item);
    }


    public String getTaskTag() {
        return taskTag;
    }


    public Map<String, Object> result(Page page) {
        Tag htmlTag = tag(page);
        Map<String, Object> stringMap = vars.stream().collect(Collectors.toMap(query -> query.name, query -> evaluate(query, htmlTag)));
        Map<String, Object> itemResult = items.stream().map(item -> itemResult(item, htmlTag)).collect(Collectors.reducing(new HashMap<String, Object>(), (m, n) -> {
            m.putAll(n);
            return m;
        }));
        stringMap.putAll(itemResult);
        stringMap.put("url", page.getUrl());
        helpGC(page, htmlTag);
        return stringMap;
    }

    private void helpGC(Page page, Tag tag) {
        page = null;
        tag = null;
    }

    private Map<String, Object> itemResult(item item, Tag tag) {
        Map<String, Object> result = new HashMap<>();
        int row = 1;
        List<Map<String, Object>> itemList = new ArrayList<>();
        while (true) {
            Map<String, Object> map = new HashMap<>();
            for (var var : item.itemVar) {
                String name = var.name;
                String query = itemQueryInfer(var.query, row);
                String option = var.option;
                String optionValue = var.optionValue;
                var newVar = new var(name, query, option, optionValue);
                String string = evaluate(newVar, tag);
                if (string.equals("")) {
                    result.put(item.itemName, itemList);
                    return result;
                }
                map.put(name, evaluate(newVar, tag));
            }
            itemList.add(map);
            row++;

        }
    }

    //目前的推测方法
    private String itemQueryInfer(String query, int row) {
        return query.replaceFirst("tr:nth-child\\(.\\)", "tr:nth-child\\(" + row + "\\)");
    }

    private String evaluate(var query, Tag tag) {
        Tag innerTag = tag.css(query.query);
        String result = "";
        if (innerTag != null) {
            if (query.option.equals("attr")) {
                result = innerTag.attr(query.optionValue);
            } else {
                result = innerTag.text();
            }
        }
        innerTag = null;
        return result;

    }


    private Tag tag(Page page) {
        Document document = Jsoup.parse(page.getRawContent(), page.getUrl());
        return new HtmlTag(document);
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public static class var {
        String name;
        String query;
        String option;
        String optionValue;

        public var(String name, String query, String option, String optionValue) {
            this.name = name;
            this.query = query;
            this.option = option;
            this.optionValue = optionValue;
        }


    }

    public static class item {
        String itemName;
        List<var> itemVar;
    }

}
