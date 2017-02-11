package com.jal.crawler.parse.selector;

import com.jal.crawler.parse.tag.Tag;
import org.jsoup.nodes.Element;

import java.util.List;

/**
 * Created by home on 2017/1/12.
 */
public interface Selectable {
    Tag select(Element element, String query);

    List<Tag> selectList(Element element, String query);

}
