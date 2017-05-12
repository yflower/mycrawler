/**
 * Created by jianganlan on 2017/5/11.
 */

let name='moduleListController'

let controller=['moduleService',function (moduleService) {
    var self=this;

    self.components={};

    self.download=[];

    self.resolve=[];

    self.data=[];

    self.link=[];

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
}]

export default {
    name:name,
    controller:controller
}