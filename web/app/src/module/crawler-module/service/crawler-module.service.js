/**
 * Created by jianganlan on 2017/5/12.
 */
let name='moduleService';

let service=['$q', '$http','appService',function ($q,$http,appService) {

    var address = appService.serverAddress;
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