package com.jal.crawler.parse.tag;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Set;

/**
 * Created by home on 2017/1/12.
 */
public class HtmlTag extends NodeTag {
    private Set<String> links;

    public HtmlTag(Document document) {
        super(document);
    }
}
