/**
 * Created by jianganlan on 2017/5/12.
 */
let name='moduleService';

let service=['$q', '$http',function ($q,$http) {

    var address = "http://192.168.113.207:8081";
    var componentStatus = function (params, data) {
        return $http({
            method: 'post',
            url: address + '/components/status',
            params: params,
            data: data
        })
    }
    var componentAdd=function (params,data) {
        return $http({
            method: 'post',
            url: address + '/components',
            params: params,
            data: data
        })
    }
    return {
        componentStatus:componentStatus,
        componentAdd:componentAdd
    }


}]

export default {
    name:name,
    service:service
}