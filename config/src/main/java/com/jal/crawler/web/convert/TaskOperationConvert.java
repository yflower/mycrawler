package com.jal.crawler.web.convert;

import com.jal.crawler.web.data.enums.ComponentEnum;
import com.jal.crawler.web.data.model.task.DownloadOperationModel;
import com.jal.crawler.web.data.model.task.LinkOperationModel;
import com.jal.crawler.web.data.model.task.ResolveOperationModel;
import com.jal.crawler.web.data.param.TaskPushParam;

import java.util.stream.Collectors;

/**
 * Created by jal on 2017/2/25.
 */
public class TaskOperationConvert {
    public static ResolveOperationModel paramToModel(TaskPushParam.resolve param, String taskTag) {
        ResolveOperationModel model = new ResolveOperationModel(taskTag);
        model.setTaskTag(taskTag);
        model.setTest(param.isTest());
        model.setComponentType(ComponentEnum.RESOLVE);
        model.setVars(
                param.getVars().stream()
                        .map(t -> TaskOperationConvert.toVar(t))
                        .collect(Collectors.toList())
        );
        model.setItems(
                param.getItems().stream().map(tx -> {
                            ResolveOperationModel.item item = new ResolveOperationModel.item();
                            item.setItemName(tx.getItemName());
                            item.setVars(
                                    tx.getVars().stream().map(TaskOperationConvert::toVar)
                                            .collect(Collectors.toList())
                            );
                            return item;
                        }
                ).collect(Collectors.toList())
        );
        return model;
    }

    public static DownloadOperationModel paramToModel(TaskPushParam.download param, String taskTag) {
        DownloadOperationModel model = new DownloadOperationModel(taskTag);
        model.setTaskTag(taskTag);
        model.setComponentType(ComponentEnum.DOWNLOAD);
        model.setDynamic(param.isDynamic());
        model.setUrls(param.getUrls());
        model.setTest(param.isTest());
        model.setPreProcess(param.getPreProcess().stream().map(TaskOperationConvert::toProcess).collect(Collectors.toList()));
        model.setPostProcess(param.getPostProcess().stream().map(TaskOperationConvert::toProcess).collect(Collectors.toList()));
        return model;
    }

    public static LinkOperationModel paramToModel(TaskPushParam.link param, String taskTag) {
        LinkOperationModel model = new LinkOperationModel(taskTag);
        model.setTaskTag(taskTag);
        model.setComponentType(ComponentEnum.LINK);
        model.setTest(param.isTest());
        model.setLinkPattern(param.getLinkPattern());
        return model;
    }

    private static ResolveOperationModel.var toVar(TaskPushParam.resolve.var t) {
        ResolveOperationModel.var var = new ResolveOperationModel.var();
        var.setName(t.getName());
        var.setQuery(t.getQuery());
        var.setValue(t.getValue());
        var.setOption(t.getOption());
        var.setOptionValue(t.getOptionValue());
        return var;
    }

    private static DownloadOperationModel.process toProcess(TaskPushParam.download.process process) {
        DownloadOperationModel.process pr = new DownloadOperationModel.process();
        pr.setType(DownloadOperationModel.process.type.numberOf(process.getType()));
        pr.setOrder(process.getOrder());
        pr.setQuery(process.getQuery());
        pr.setValue(process.getValue());
        return pr;
    }
}
