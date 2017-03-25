package com.jal.crawler.web.convert;

import com.jal.crawler.proto.status.ComponentType;
import com.jal.crawler.proto.task.TaskType;
import com.jal.crawler.web.data.enums.ComponentEnum;
import com.jal.crawler.web.data.enums.TaskOperationEnum;

/**
 * Created by jianganlan on 2017/3/25.
 */
public class RpcEnumConvert {
    public static ComponentEnum componentType(ComponentType componentType) {
        return ComponentEnum.numberOf(componentType.getNumber());
    }

    public static TaskType taskOperationType(TaskOperationEnum taskOperationEnum) {
        switch (taskOperationEnum) {
            case ADD:
                return TaskType.ADD;
            case DESTROY:
                return TaskType.DESTROY;
            case FINISH:
                return TaskType.FINISH;
            case STOP:
                return TaskType.STOP;
            case UPDATE:
                return TaskType.UPDATE;
            default:
                return null;
        }
    }
}
