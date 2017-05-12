/**
 * Created by jianganlan on 2017/5/12.
 */
let name='moduleService';

let service=['$q', '$http',function ($q,$http) {

    var address = "http://192.168.1.58:8081";
    var componentStatus = function (params, data) {
        return $http({
            method: 'get',
            url: address + '/tasks/push',
            params: params,
            data: data
        })
    }


}]

export default {
    name:name,
    service:service
}