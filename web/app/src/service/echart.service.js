/**
 * Created by jal on 2017/3/12.
 */
import echarts from 'echarts';

let name = 'echartService'

let service = function () {
    return {
        echarts: echarts
}
}
export default {
    name:name,
    service:service
}