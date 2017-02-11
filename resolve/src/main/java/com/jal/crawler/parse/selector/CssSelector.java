package com.jal.crawler.parse.selector;

import com.jal.crawler.parse.tag.HtmlTag;
import com.jal.crawler.parse.tag.NodeTag;
import com.jal.crawler.parse.tag.Tag;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by home on 2017/1/12.
 */
public class CssSelector implements Selectable {

    @Override
    public Tag select(Element element, String selectQuery) {
        Element el=element.select(selectQuery).first();
        if(el==null){
            return null;
        }else {
            return new NodeTag(el);
        }
    }

    @Override
    public List<Tag> selectList(Element element, String selectQuery) {
        return element.select(selectQuery).stream().map(NodeTag::new).filter(t->t!=null).collect(Collectors.toList());
    }

}
