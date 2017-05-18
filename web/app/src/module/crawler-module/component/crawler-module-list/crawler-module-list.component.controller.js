/**
 * Created by jianganlan on 2017/5/11.
 */

let name = 'moduleListController'

let controller = ['moduleService', function (moduleService) {
    var self = this;

    self.components = {};

    self.download = [];

    self.resolve = [];

    self.data = [];

    self.link = [];

    self.addComponent = function ($event) {
        if (componentCheck()) {
            moduleService.componentAdd(null, {
                components: [{
                    host: self.tempHost,
                    port: self.tempRpcPort,
                    serverPort: self.tempHttpPort
                }]
            }).then(function (result) {
                if(result.data.code==200){
                    alert("组件添加成功");
                    fresh();
                }
                console.log(result.data)
            }, function (error) {
                alert("组件添加失败"+error)
            })
        }
    }

    self.itemToString=function (item) {
        return item.host+":"+item.httpPort+":"+item.rpcPort;
    }

    function componentCheck() {
        if (self.tempHost == undefined || self.tempHost.length == 0 || self.tempHttpPort == undefined || self.tempRpcPort == undefined) {
            return false;
        }else {
            return true;
        }

    }


    fresh();
    //加载执行
    function fresh() {
        moduleService.componentStatus()
            .then(function (result) {
                self.components = result.data.data;

                self.download = self.components.filter(function (value) {
                    return value.componentType == 0;
                })
                self.resolve = self.components.filter(function (value) {
                    return value.componentType == 1;
                })
                self.data = self.components.filter(function (value) {
                    return value.componentType == 2;
                })
                self.link = self.components.filter(function (value) {
                    return value.componentType == 3;
                })

                console.log(self)

            })
    }
}]

export default {
    name: name,
    controller: controller
}