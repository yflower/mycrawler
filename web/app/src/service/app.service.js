/**
 * Created by jianganlan on 2017/5/24.
 */
let name='appService';

let service=function () {
    var serverAddress="http://192.168.113.207:8081";
    return {
        serverAddress:serverAddress
    }
}
export  default {
    name:name,
    service:service
}