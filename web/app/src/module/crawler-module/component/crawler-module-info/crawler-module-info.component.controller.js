/**
 * Created by jianganlan on 2017/5/11.
 */
let name = 'moduleInfoController'

let controller = ['moduleService', '$location', function (moduleService, $location) {
    var self = this;
    var arr = $location.path().split("/");
    self.param = arr[arr.length - 1].split(":");
    if (self.param.length == 3) {
        configReq();
    }

    self.component = {};


    function configReq() {
        moduleService.componentStatus(null, {
            components: [
                {
                    host: self.param[0],
                    port: self.param[2],
                    serverPort: self.param[1]
                }
            ]
        })
            .then(function (result) {
                self.component = result.data.data[0];
                console.log(self.component)

            })
    }


}]

export default {
    name: name,
    controller: controller
}