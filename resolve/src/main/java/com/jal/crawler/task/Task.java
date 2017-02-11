package com.jal.crawler.task;

import com.jal.crawler.page.Page;
import com.jal.crawler.parse.tag.HtmlTag;
import com.jal.crawler.parse.tag.Tag;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by home on 2017/1/20.
 */
public class Task {
    private int status;

    private String taskTag;

    private List<query> vars;


    public void var(String name, String query, String option, String optionValue) {
        query query1 = new query();
        query1.name = name;
        query1.query = query;
        query1.option = option;
        query1.optionValue = optionValue;
        vars.add(query1);
    }

    public Task(String taskTag) {
        this.taskTag = taskTag;
        vars = new ArrayList<>();
    }


    public String getTaskTag() {
        return taskTag;
    }

    public List<query> getVars() {
        return vars;
    }

    public Map<String, Object> result(Page page) {
        Tag htmlTag=tag(page);
        Map<String, Object> stringMap = getVars().stream().collect(Collectors.toMap(query -> query.name, query -> evaluate(query, htmlTag)));
        stringMap.put("url",page.getUrl());
        return stringMap;
    }

    private String evaluate(query query, Tag tag) {
        Tag innerTag = tag.css(query.query);
        if(innerTag==null)return "";
        if (query.option.equals("attr")) {
            return innerTag.attr(query.optionValue);
        } else {
            return innerTag.text();
        }
    }

    private Tag tag(Page page) {
        Document document = Jsoup.parse(page.getRawContent(), page.getUrl());
        return new HtmlTag(document);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class query {
        String name;
        String query;
        String option;
        String optionValue;
    }

}
