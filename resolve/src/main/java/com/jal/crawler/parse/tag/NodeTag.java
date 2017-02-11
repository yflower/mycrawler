package com.jal.crawler.parse.tag;

import com.jal.crawler.parse.selector.Selectors;
import org.jsoup.nodes.Element;

import java.util.List;

/**
 * Created by home on 2017/1/12.
 */
public class NodeTag implements Tag {

    private Element element;

    public NodeTag(Element element) {
        this.element = element;
    }


    @Override
    public Tag xpath(String query) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Tag> xpathList(String query) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Tag css(String query) {
        return Selectors.css.select(element, query);
    }

    @Override
    public List<Tag> cssList(String query) {
        return Selectors.css.selectList(element, query);
    }

    @Override
    public String attr(String attr) {
        return element.attr(attr);
    }

    @Override
    public String text() {
        return element.text();
    }

    @Override
    public String htmlText() {
        return element.outerHtml();
    }

}
