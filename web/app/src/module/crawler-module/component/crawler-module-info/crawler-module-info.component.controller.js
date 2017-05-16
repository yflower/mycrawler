/**
 * Created by jianganlan on 2017/5/11.
 */
let name = 'moduleInfoController'

let controller = ['moduleService', '$location', function (moduleService, $location) {
    var self = this;
    var arr = $location.path().split("/");
    self.param = arr[arr.length-1].split(":");
    if (self.param.length == 3) {
        configReq();
    }

    self.component={};


    function configReq(){
        moduleService.componentStatus(null,{
            components:[
                {
                    host:self.param[0],
                    port:self.param[2],
                    serverPort:self.param[1]
                }
            ]
        })
            .then(function (result) {
                self.component=result.data.data[0];
                console.log(self.component)

            })
    }
    self.componentType=function () {
        if(self.component.componentType==undefined){
            return ""
        }
        if(self.component.componentType==0){
            return "下载器"
        }else if(self.component.componentType==1){
            return '解析器'
        }else if(self.component.componentType==2){
            return "数据处理"
        }else  if(self.component.componentType==3){
            return '链接处理'
        }
    }

    self.status=function (s) {
        if(s==0){
            return '未设置'
        }
        if(s==1){
            return '已设置'
        }
        if(s==2){
            return '正在运行'
        }

    }

}]

export default {
    name: name,
    controller: controller
}