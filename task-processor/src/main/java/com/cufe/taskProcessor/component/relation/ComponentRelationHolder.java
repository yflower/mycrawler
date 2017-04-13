package com.cufe.taskProcessor.component.relation;

import com.cufe.taskProcessor.component.relation.ComponentRelation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianganlan on 2017/4/13.
 */
public class ComponentRelationHolder {
    private List<ComponentRelation> clusters=new ArrayList<>();


    public boolean addRelation(ComponentRelation componentRelation){
        return clusters.add(componentRelation);
    }
}
