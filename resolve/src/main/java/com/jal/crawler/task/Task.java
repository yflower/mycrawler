package com.jal.crawler.task;

import com.cufe.taskProcessor.task.AbstractTask;
import com.jal.crawler.page.Page;
import com.jal.crawler.parse.tag.HtmlTag;
import com.jal.crawler.parse.tag.Tag;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by home on 2017/1/20.
 */
public class Task extends AbstractTask{
    private final static Logger LOGGER= LoggerFactory.getLogger(Task.class);


    private List<var> vars;

    private List<item> items;


    public Optional<Map<String, Object>> result(Page page) {
        Tag htmlTag = tag(page);
        Map<String, Object> stringMap = vars.stream().collect(Collectors.toMap(query -> query.name, query -> evaluate(query, htmlTag)));
        Map<String, Object> itemResult = items.stream().map(item -> itemResult(item, htmlTag)).collect(Collectors.reducing(new HashMap<String, Object>(), (m, n) -> {
            m.putAll(n);
            return m;
        }));
        stringMap.putAll(itemResult);
        stringMap.put("url", page.getUrl());
        List<Tag> tags = htmlTag.cssList("a");
        List<String> links = tags.stream().filter(t -> t != null).map(t -> t.attr("href")).filter(t -> t != null && !t.isEmpty()).collect(Collectors.toList());

        List<String> validLinks = links.parallelStream()
                .filter(t -> !t.startsWith("javascript"))
                .filter(t -> !t.equals("#none"))
                .filter(t -> !t.equals("#comment"))
                .filter(t->t.startsWith("//"))
                .collect(Collectors.toList());
        String[] split = page.getUrl().split("/");
        String baseUrl =split[0];

        List<String> finalLinks = validLinks.parallelStream().map(t -> baseUrl+t).collect(Collectors.toList());
        stringMap.put("links",finalLinks);
        helpGC(page, htmlTag);
        LOGGER.info("成功解析页面 {}",page.getUrl());
        return Optional.of(stringMap);
    }

    private void helpGC(Page page, Tag tag) {
        page = null;
        tag = null;
    }

    private Map<String, Object> itemResult(item item, Tag tag) {
        Map<String, Object> result = new HashMap<>();
        int row = 1;
        List<Map<String, Object>> itemList = new ArrayList<>();
        int notFound=0;
        while (true) {
            Map<String, Object> map = new HashMap<>();
            for (var var : item.vars) {
                String name = var.name;
                String query = itemQueryInfer(var.query, row);
                String option = var.option;
                String optionValue = var.optionValue;
                var newVar = new var(name, query, option, optionValue);
                String string = evaluate(newVar, tag);
                if (string.equals("")) {
                    notFound++;
                    if(notFound>=5){
                        return result;
                    }
                }
                map.put(name, evaluate(newVar, tag));
            }
            itemList.add(map);
            row++;

        }
    }

    //目前的推测方法
    private String itemQueryInfer(String query, int row) {
        if(query.contains("tr:nth-child")){
            return query.replaceFirst("tr:nth-child\\(.\\)", "tr:nth-child\\(" + row + "\\)");
        }
        if(query.contains("li:nth-child")){
            return query.replaceFirst("li:nth-child\\(.\\)", "li:nth-child\\(" + row + "\\)");
        }
        throw new IllegalStateException("自动解析列表数据css失败");
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


    public void setVars(List<var> vars) {
        this.vars = vars;
    }

    public void setItems(List<item> items) {
        this.items = items;
    }

    public List<var> getVars() {
        return vars;
    }

    public List<item> getItems() {
        return items;
    }

    @Override
    public void init() {

    }

    public static class var {
        private String name;
        private String query;
        private String option;
        private String optionValue;

        public var(String name, String query, String option, String optionValue) {
            this.name = name;
            this.query = query;
            this.option = option;
            this.optionValue = optionValue;
        }

        public var() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
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


    }

    public static class item {
        private String itemName;
        private List<var> vars;

        public item(String itemName, List<var> itemVar) {
            this.itemName = itemName;
            this.vars = itemVar;
        }

        public item() {
        }

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



}
