package com.jal.crawler.context;


import com.cufe.taskProcessor.component.client.AbstractComponentClientFactory;
import com.cufe.taskProcessor.context.ComponentContext;
import com.jal.crawler.component.DataClientFactory;
import com.jal.crawler.data.Data;
import com.jal.crawler.data.dataProcessor.DataProcessors;
import com.jal.crawler.enums.ComponentTypeEnum;
import com.jal.crawler.resource.DataFetch;
import com.jal.crawler.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jal on 2017/2/13.
 */
@Component
public class DataContext extends ComponentContext<List<Map<String, Object>>,Data,Task> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataContext.class);


    private AbstractComponentClientFactory componentClientFactory = new DataClientFactory(this);


    private DataFetch dataFetch;

    private Map<String,Data> finishResult;

    {
        sink = task -> dataFetch.fetch(task.getTaskTag());

        processor = (t, data) -> DataProcessors.of(((Task)t).getDataType()).processor(data);

        repository = (t, dataModel) -> finishResult.put(t.getTaskTag(),dataModel);
    }
    @Override
    protected void internalInit() {
        if(finishResult==null){
            finishResult=new HashMap<>();
        }

    }

    @Override
    public int componentType() {
        return ComponentTypeEnum.DATA.getCode();
    }

    @Override
    public AbstractComponentClientFactory getComponentClientFactory() {
        return componentClientFactory;
    }

    public void setDataFetch(DataFetch dataFetch) {
        this.dataFetch = dataFetch;
    }

    public Map<String, Data> getFinishResult() {
        return finishResult;
    }
}
