package com.jal.crawler.parse.tag;

import java.util.List;

/**
 * Created by home on 2017/1/12.
 */
public interface Tag {
    Tag xpath(String query);

    Tag css(String query);

    List<Tag> cssList(String query);

    List<Tag> xpathList(String query);

    String attr(String attr);

    String text();

    String htmlText();


}
