/**
 * Created by jal on 2017/3/11.
 */
let name = 'taskService';

let service = ['$q', '$http', function ($q, $http) {
    var taskPush = function (params,data) {
        return $http({
            method: 'post',
            url: 'http://127.0.0.1:8081/tasks/push',
            params: params,
            data:data
        })
    }

    var taskPause = function (params,data) {
        return $http({
            method: 'put',
            url: 'http://127.0.0.1:8081/tasks/pause',
            params: params,
            data:data
        })
    }
    var taskStop = function (params,data) {
        return $http({
            method: 'put',
            url: 'http://127.0.0.1:8081/tasks/stop',
            params: params,
            data:data
        })
    }

    var taskDestroy = function (params,data) {
        return $http({
            method: 'put',
            url: 'http://127.0.0.1:8081/tasks/destroy',
            params: params,
            data:data
        })
    }

    var taskRestart= function (params,data) {
        return $http({
            method: 'put',
            url: 'http://127.0.0.1:8081/tasks/restart',
            params: params,
            data:data
        })
    }


    var taskResult=function(params,data){
        return $http({
            method:'get',
            url:'http://127.0.0.1:8081/tasks/result',
            params:params,
            data:data
        })
    }

    var taskStatus=function (params,data) {
        return $http({
            method:'get',
            url:'http://127.0.0.1:8081/tasks/status',
            params:params,
            data:data
        })
    }

    var processorType = [
        {
            value: 1,
            message: '点击'
        },
        {
            value: 2,
            message: '输入'
        },
        {
            value: 3,
            message: '提交'
        }, {
            value: 4,
            message: '跳转'
        }, {
            value: 5,
            message: '等待出现'
        }]
    var resolveOptionType = [
        {
            value: 0,
            message: '属性'
        }
    ]


    return {
        processorType: processorType,
        resolveOptionType:resolveOptionType,
        taskPush:taskPush,
        taskPause:taskPause,
        taskStop:taskStop,
        taskDestroy:taskDestroy,
        taskRestart:taskRestart,
        taskResult:taskResult,
        taskStatus:taskStatus
    }
}]

export default {
    name: name,
    service: service
}
