/**
 * Created by jianganlan on 2017/5/18.
 */
let filters = [{
    name: 'status',
    filterFactory: function () {
        return function (value) {
            if (value == undefined || isNaN(value)) {
                return ''
            } else if (value == 0) {
                return '未设置'
            } else if (value == 1) {
                return '已设置'
            } else if (value == 2) {
                return '运行中'
            } else if (value == 3) {
                return '停止'
            } else if (value == 4) {
                return '完成'
            } else if (value == 5) {
                return '销毁'
            }
        }
    }
}, {
    name: 'componentType',
    filterFactory: function () {
        return function (value) {
            if (value == undefined || isNaN(value)) {
                return '未知组件'
            } else if (value == 0) {
                return '下载器'
            } else if (value == 1) {
                return '解析器'
            } else if (value == 2) {
                return '数据处理'
            } else if (value == 3) {
                return '链接处理'
            }
        }
    }
},{
    name: 'componentIcon',
    filterFactory: function () {
        return function (value) {
            if (value == undefined || isNaN(value)) {
                return 'module'
            } else if (value == 0) {
                return 'download'
            } else if (value == 1) {
                return 'processor'
            } else if (value == 2) {
                return 'data'
            } else if (value == 3) {
                return 'link'
            }
        }
    }
}
]

export  default filters;