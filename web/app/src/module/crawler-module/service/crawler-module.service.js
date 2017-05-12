/**
 * Created by jianganlan on 2017/5/12.
 */
let name='moduleService';

let service=['$q', '$http',function ($q,$http) {

    var address = "http://127.0.0.1:8081";
    var componentStatus = function (params, data) {
        return $http({
            method: 'post',
            url: address + '/components/status',
            params: params,
            data: data
        })
    }
    return {
        componentStatus:componentStatus
    }


}]

export default {
    name:name,
    service:service
}