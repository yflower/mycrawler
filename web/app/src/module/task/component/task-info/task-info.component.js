/**
 * Created by jal on 2017/3/12.
 */
let name = 'taskInfo';

let config = {
    templateUrl: 'src/module/task/component/task-info/task-info.component.template.html'
//     controller: ['echartService', function(echartService){
//     var self = this;
//     var myChart = echartService.echarts.init(document.getElementById('main'))
//     self.myChart = myChart;
//     function randomData() {
//         now = new Date(+now + oneDay);
//         value = value + Math.random() * 21 - 10;
//         return {
//             name: now.toString(),
//             value: [
//                 [now.getFullYear(), now.getMonth() + 1, now.getDate()].join('/'),
//                 Math.round(value)
//             ]
//         }
//     }
//
//     var data = [];
//     var now = +new Date(1997, 9, 3);
//     var oneDay = 24 * 3600 * 1000;
//     var value = Math.random() * 1000;
//     for (var i = 0; i < 1000; i++) {
//         data.push(randomData());
//     }
//
//
//     var option = {
//         tooltip: {
//             trigger: 'axis',
//             formatter: function (params) {
//                 params = params[0];
//                 var date = new Date(params.name);
//                 return date.getDate() + '/' + (date.getMonth() + 1) + '/' + date.getFullYear() + ' : ' + params.value[1];
//             },
//             axisPointer: {
//                 animation: false
//             }
//         },
//         xAxis: {
//             type: 'time',
//             splitLine: {
//                 show: false
//             }
//         },
//         yAxis: {
//             type: 'value',
//             boundaryGap: [0, '100%'],
//             splitLine: {
//                 show: false
//             }
//         },
//         series: [{
//             name: '模拟数据',
//             type: 'line',
//             showymbol: false,
//             hoverAnimation: false,
//             data: data
//         }]
//     };
//
//     setInterval(function () {
//
//         for (var i = 0; i < 5; i++) {
//             data.shift();
//             data.push(randomData());
//         }
//
//         self.myChart.setOption(option);
//     }, 1000);
// }
// ]

}

export default {
    name: name,
    config: config
}