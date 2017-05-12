/**
 * Created by jal on 2017/3/5.
 */

let service = ['$q', function ($q) {
    var leftNavList = [
        {
            name: "task",
            avatar: 'task',
            router:"taskView"
        },
        {
            name: "module",
            avatar: 'module',
            router:"moduleView"
        },

    ];

    return {
        loadAllList:function () {
            return $q.when(leftNavList);
        }
    }
}]

export default {
    name: "navService",
    service: service
}