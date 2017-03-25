/**
 * Created by jal on 2017/3/5.
 */
let name = 'navController';

let controller = ['navService', function (navService) {
    var self = this;
    self.list = [];
    self.selected = null;
    self.selectList = null;

    navService.loadAllList()
        .then(function (list) {
            self.list = list;
            self.selected = list[0];
        })

    self.selected = function (item) {
        self.selected = item;
    }
}]
export default {
    name: name,
    controller: controller
}