/**
 * Created by jal on 2017/3/11.
 */
let name = 'taskService';

let service = ['$q', '$http', function ($q, $http) {
    var taskPush = function (param,data) {
        return $http({
            method: 'post',
            url: 'http://127.0.0.1:8081/tasks/push',
            param: param,
            data:data
        })
    }

    var processorType = [
        {
            value: 0,
            message: '点击'
        },
        {
            value: 1,
            message: '输入'
        },
        {
            value: 2,
            message: '提交'
        }, {
            value: 3,
            message: '跳转'
        }, {
            value: 4,
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
        taskPush:taskPush
    }
}]

export default {
    name: name,
    service: service
}