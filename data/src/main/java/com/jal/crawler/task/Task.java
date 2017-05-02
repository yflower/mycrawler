package com.jal.crawler.task;

import com.cufe.taskProcessor.task.AbstractTask;
import com.jal.crawler.data.DataTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by home on 2017/1/20.
 */
public class Task extends AbstractTask{
    private final static Logger LOGGER= LoggerFactory.getLogger(Task.class);


    private DataTypeEnum dataType;

    public DataTypeEnum getDataType() {
        return dataType;
    }

    public void setDataType(DataTypeEnum dataType) {
        this.dataType = dataType;
    }

    @Override
    public void init() {

    }
}
