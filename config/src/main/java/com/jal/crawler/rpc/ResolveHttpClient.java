package com.jal.crawler.rpc;

import com.jal.crawler.web.data.model.component.ResolveConfigRelation;
import com.jal.crawler.web.data.model.task.ResolveOperationModel;

import java.util.concurrent.ExecutionException;

/**
 * Created by jianganlan on 2017/4/26.
 */
public class ResolveHttpClient extends AbstractHttpClient<ResolveConfigRelation, ResolveOperationModel> {


    @Override
    protected OPStatus internalTask(ResolveOperationModel taskOperation) throws InterruptedException, ExecutionException {
        return OPStatus.FAILD;
    }

    @Override
    protected boolean internalConfigSet(ResolveConfigRelation config) {
        return false;
    }

    @Override
    protected boolean validConfig(ResolveConfigRelation config) {
        return true;
    }
}
