/**
 * 用户管理
 */
Ext.onReady(function () {

    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';

    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var start = 0;
    var pageSize = 15;
    var toolbar = new Ext.Toolbar({
        plain: true,
        width: 350,
        height: 30,
        items: ['用户名', {
            id: 'userName.tb.info',
            xtype: 'textfield',
            emptyText: '输入用户名',
            width: 100
        }, {
            xtype: 'tbseparator'
        }, '状态', {
            id: 'status.tb.info',
            xtype: 'combo',
            store: new Ext.data.ArrayStore({
                autoDestroy: true,
                fields: ['value', 'key'],
                data: [
                    ['', '全部'],
                    ['有效', '有效'],
                    ['无效', '无效'],
                    ['已删除', '已删除']
                ]
            }),
            valueField: 'value',
            displayField: 'key',
            mode: 'local',
            forceSelection: true,
            triggerAction: 'all',
            emptyText: '--请选择--',
            value: '',
            selectOnFocus: true,
            width: 100
        }, {
            xtype: 'tbseparator'
        }, {
            text: '查询',
            iconCls: 'query',
            listeners: {
                click: function () {
                    var userName = Ext.fly("userName.tb.info").dom.value == '输入用户名'
                        ? null
                        : Ext.getCmp('userName.tb.info').getValue();
                    var status = Ext.fly('status.tb.info').dom.value == '--请选择--'
                        ? null
                        : Ext.getCmp('status.tb.info').getValue();
                    store.setBaseParam('userName', userName);
                    store.setBaseParam('status', status);
                    store.load({
                        params: {
                            start: start,
                            limit: pageSize
                        }
                    });
                }
            }
        }]
    });
    var record = new Ext.data.Record.create([
        {name: 'id', mapping: 'id'},
        {name: 'name', mapping: 'name'},
        {name: 'userName', mapping: 'userName'},
        {name: 'sex', mapping: 'sex'},
        {name: 'password', mapping: 'password'},
        {name: 'phone', mapping: 'phone'},
        {name: 'status', mapping: 'status'},
        {name: 'email', mapping: 'email'},
        {name: 'title', mapping: 'title'},
        {name: 'description', mapping: 'description'},
        {name: 'ipType', mapping: 'ipType'},
        {name: 'remoteIp', mapping: 'remoteIp'},
        {name: 'startIp', mapping: 'startIp'},
        {name: 'endIp', mapping: 'endIp'},
        {name: 'startHour', mapping: 'startHour'},
        {name: 'endHour', mapping: 'endHour'},
        {name: 'roleName', mapping: 'roleName'},
        {name: 'roleId', mapping: 'roleId'},
        {name: 'mac', mapping: 'mac'},
        {name: 'depart', mapping: 'depart'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url: "../../AccountAction_select.action"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty: "total",
        root: "rows",
        id: 'id'
    }, record);
    var store = new Ext.data.GroupingStore({
        id: "store.info",
        proxy: proxy,
        reader: reader
    });

//    var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
    var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var colM = new Ext.grid.ColumnModel([
//        boxM,
        rowNumber,
        {header: "用户名", dataIndex: "userName", align: 'center', sortable: true, menuDisabled: true},
        {header: "真实姓名", dataIndex: "name", align: 'center', sortable: true, menuDisabled: true},
        {header: '电话', dataIndex: 'phone', align: 'center', sortable: true, menuDisabled: true},
        {header: '状态', dataIndex: 'status', align: 'center', sortable: true, menuDisabled: true, renderer: show_status},
        {
            header: '操作标记',
            dataIndex: 'id',
            align: 'center',
            sortable: true,
            menuDisabled: true,
            renderer: show_flag,
            width: 100
        }
    ]);
    var page_toolbar = new Ext.PagingToolbar({
        pageSize: pageSize,
        store: store,
        displayInfo: true,
        displayMsg: "显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg: "没有记录",
        beforePageText: "当前页",
        afterPageText: "共{0}页"
    });
    var grid_panel = new Ext.grid.GridPanel({
        id: 'grid.info',
        plain: true,
        height: setHeight(),
        width: setWidth(),
        animCollapse: true,
        loadMask: {msg: '正在加载数据，请稍后...'},
        border: false,
        collapsible: false,
        cm: colM,
//        sm:boxM,
        store: store,
        stripeRows: true,
        autoExpandColumn: 2,
        disableSelection: true,
        bodyStyle: 'width:100%',
        enableDragDrop: true,
        selModel: new Ext.grid.RowSelectionModel({singleSelect: true}),
        viewConfig: {
            forceFit: true,
            enableRowBody: true,
            getRowClass: function (record, rowIndex, p, store) {
                return 'x-grid3-row-collapsed';
            }
        },
        tbar: [new Ext.Button({
            id: 'btnAdd.info',
            text: '新增',
            iconCls: 'add',
            handler: function () {
                insert_win(grid_panel, store);     //连接到 新增 面板
            }
        }), {
            xtype: 'tbseparator'
        }, toolbar],
        view: new Ext.grid.GroupingView({
            forceFit: true,
            groupingTextTpl: '{text}({[values.rs.length]}条记录)'
        }),
        bbar: page_toolbar
    });
    var port = new Ext.Viewport({
        layout: 'fit',
        renderTo: Ext.getBody(),
        items: [grid_panel]
    });
    store.load({
        params: {
            start: start, limit: pageSize
        }
    });
});
function setHeight() {
    var h = document.body.clientHeight - 8;
    return h;
}

function setWidth() {
    return document.body.clientWidth - 8;
}

function show_status(value) {
    if (value == '无效') {
        return '<span style="color:red;">无效</span>';
    } else {
        return '<span style="color:green;">' + value + '</span>';
    }
}

function show_flag(value, p, r) {
    var userName = r.get('userName');
    var status = r.get('status');
    if (userName == 'admin' ||
        userName == 'auditadmin' ||
        userName == 'authadmin' ||
        userName == 'configadmin') {
        return String
            .format('<a href="javascript:void(0);" onclick="showUpdateUser();return false;" >修改</a>'
            + '&nbsp;&nbsp;'
            + '<font color="gray">删除</font>');
    } else {
        if (status == "已删除") {
            return String .format(
                '<font color="gray">修改</font>'
            + '&nbsp;&nbsp;'
            + '<font color="gray">删除</font>');
        } else {
            return String
                .format('<a href="javascript:void(0);" onclick="showUpdateUser();return false;">修改</a>'
                + '&nbsp;&nbsp;'
                + '<a id="\'' + value + '\'.delete.info" href="javascript:void(0);" onclick="deleteUser(\'' + value + '\');return false;" >删除</a>');

        }
    }
}

var dataSex = [['男', '男'], ['女', '女']];
var storeSex = new Ext.data.SimpleStore({fields: ['value', 'key'], data: dataSex});
var dataStatus = [['有效', '有效'], ['无效', '无效']];
var storeStatus = new Ext.data.SimpleStore({fields: ['value', 'key'], data: dataStatus});
var recordRole = new Ext.data.Record.create([{name: 'value', mapping: 'value'}, {name: 'key', mapping: 'key'}]);
var readerRole = new Ext.data.JsonReader({totalProperty: 'total', root: "rows"}, recordRole);
var storeRole = new Ext.data.Store({
    url: "../../RoleAction_readNameKeyValue.action",
    reader: readerRole
});
storeRole.load();

/**
 * 新增用户
 * @param grid
 * @param store
 */
function insert_win(grid, store) {
    var formPanel = new Ext.form.FormPanel({
        frame: true,
        autoScroll: true,
        layout: 'column',
        border: false,
        items: [{
            plain: true,
            columnWidth: .5,
            border: false,
            layout: 'form',
            items: [{
                plain: true,
                labelAlign: 'right',
                labelWidth: 80,
                defaultType: 'textfield',
                border: false,
                layout: 'form',
                defaults: {
                    anchor: "95%",
                    allowBlank: false,
                    blankText: '该项不能为空！'
                },
                items: [{
                    id: 'userName.insert.info',
                    fieldLabel: "用户名",
                    name: 'account.userName',
                    regex: /^.{2,30}$/,
                    regexText: '请输入任意2--30个字符',
                    emptyText: '请输入任意2--30个字符',
                    listeners: {
                        blur: function () {
                            var userName = this.getValue();
                            if (userName.length > 0) {
                                var myMask = new Ext.LoadMask(Ext.getBody(), {
                                    msg: '正在校验,请稍后...',
                                    removeMask: true
                                });
                                myMask.show();
                                Ext.Ajax.request({
                                    url: '../../AccountAction_checkUserName.action',
                                    params: {userName: userName},
                                    method: 'POST',
                                    success: function (r, o) {
                                        var respText = Ext.util.JSON.decode(r.responseText);
                                        var msg = respText.msg;
                                        myMask.hide();
                                        if (msg != 'true') {
                                            Ext.MessageBox.show({
                                                title: '信息',
                                                width: 250,
                                                msg: msg,
                                                buttons: {'ok': '确定'},
                                                icon: Ext.MessageBox.INFO,
                                                closable: false,
                                                fn: function (e) {
                                                    if (e == 'ok') {
                                                        Ext.getCmp('userName.insert.info').setValue('');
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    }
                }, {
                    fieldLabel: "真实姓名",
                    name: 'account.name',
                    regex: /^.{2,30}$/,
                    regexText: '请输入任意2--30个字符',
                    emptyText: '请输入任意2--30个字符'
                }, {
                    fieldLabel: "状态", hiddenName: 'account.status',
                    xtype: 'combo',
                    mode: 'local',
                    emptyText: '--请选择--',
                    editable: false,
                    typeAhead: true,
                    forceSelection: true,
                    triggerAction: 'all',
                    displayField: "key", valueField: "value",
                    store: storeStatus,
                    value: '有效'
                }, {
                    id: 'password.info',
                    fieldLabel: "密码",
                    name: 'account.password',
                    inputType: 'password',
                    regex: /^.{8,100}$/,
                    regexText: '密码规则:8~100位!',
                    emptyText: '请输入密码!',
                    listeners: {
                        blur: function () {
                            var password = this.getValue();
                            if (password.length > 0) {
                                var myMask = new Ext.LoadMask(Ext.getBody(), {
                                    msg: '正在校验,请稍后...',
                                    removeMask: true
                                });
                                myMask.show();
                                Ext.Ajax.request({
                                    url: '../../AccountAction_checkPassword.action',
                                    params: {password: password},
                                    method: 'POST',
                                    success: function (r, o) {
                                        var respText = Ext.util.JSON.decode(r.responseText);
                                        var msg = respText.msg;
                                        myMask.hide();
                                        if (msg != 'true') {
                                            Ext.MessageBox.show({
                                                title: '信息',
                                                width: 250,
                                                msg: msg,
                                                buttons: {'ok': '确定'},
                                                icon: Ext.MessageBox.INFO,
                                                closable: false,
                                                fn: function (e) {
                                                    if (e == 'ok') {
                                                        Ext.getCmp('password.info').setValue('');
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    }
                }, {
                    id: 'password2.info',
                    fieldLabel: "确认密码",
                    inputType: 'password',
                    regex: /^.{8,100}$/,
                    regexText: '密码规则:8~100位!',
                    emptyText: '请输入密码!',
                    listeners: {
                        blur: function () {
                            var password = Ext.getCmp('password.info').getValue();
                            if (password.length > 0) {
                                var password2 = this.getValue();
                                if (password != password2 && password2.length > 0) {
                                    Ext.MessageBox.show({
                                        title: '信息',
                                        width: 250,
                                        msg: '<font color="red">"确认密码"和"密码"不一致!</font>',
                                        animEl: 'password2.info',
                                        buttons: {'ok': '确定'},
                                        icon: Ext.MessageBox.INFO,
                                        closable: false,
                                        fn: function (e) {
                                            if (e == 'ok') {
                                                Ext.getCmp('password2.info').setValue('');
                                            }
                                        }
                                    });
                                }
                            } else {
                                Ext.MessageBox.show({
                                    title: '信息',
                                    width: 270,
                                    msg: '<font color="red">请先输入"密码",再输入"确认密码"!</font>',
                                    animEl: 'password2.info',
                                    buttons: {'ok': '确定'},
                                    icon: Ext.MessageBox.INFO,
                                    closable: false,
                                    fn: function (e) {
                                        if (e == 'ok') {
                                            Ext.getCmp('password2.info').setValue('');
                                        }
                                    }
                                });
                            }
                        }
                    }
                }, {
                    fieldLabel: "角色", hiddenName: 'role.id',
                    xtype: 'combo',
                    mode: 'local',
                    emptyText: '--请选择--',
                    editable: false,
                    typeAhead: true,
                    forceSelection: true,
                    triggerAction: 'all',
                    displayField: "key", valueField: "value",
                    store: storeRole
                }, {
                    fieldLabel: '登陆选择',
                    xtype: 'radiogroup',
                    defaultType: 'radio',
                    layout: 'column',
                    items: [
                        {columnWidth: .5, boxLabel: 'IP段', name: 'account.ipType', inputValue: 1, checked: true},
                        {columnWidth: .5, boxLabel: '指定IP', name: 'account.ipType', inputValue: 0}
                    ],
                    listeners: {
                        change: function (group, ck) {
                            if (ck.inputValue == 1) {
                                Ext.getCmp('ipMac.info').hide();
                                Ext.getCmp('remoteIp.info').disable();
                                Ext.getCmp('startIp.info').enable();
                                Ext.getCmp('endIp.info').enable();
                                Ext.getCmp('ips.info').show();
                            } else {
                                Ext.getCmp('ips.info').hide();
                                Ext.getCmp('startIp.info').disable();
                                Ext.getCmp('endIp.info').disable();
                                Ext.getCmp('remoteIp.info').enable();
                                Ext.getCmp('ipMac.info').show();
                            }
                        }
                    }
                }]
            }, {
                id: 'ips.info',
                plain: true,
                labelAlign: 'right',
                labelWidth: 80,
                defaultType: 'textfield',
                border: false,
                layout: 'form',
                defaults: {
                    //width:200,
                    anchor: "95%",
                    allowBlank: false,
                    blankText: '该项不能为空！'
                },
                items: [{
                    id: 'startIp.info',
                    fieldLabel: "开始IP",
                    name: 'account.startIp',
                    regex: /^.{2,30}$/,
                    regexText: '请输入任意2--30个字符',
                    emptyText: '请输入任意2--30个字符'
                },
                    {
                        id: 'endIp.info',
                        fieldLabel: "结束IP",
                        name: 'account.endIp',
                        regex: /^.{2,30}$/,
                        regexText: '请输入任意2--30个字符',
                        emptyText: '请输入任意2--30个字符'
                    }]
            }, {
                id: 'ipMac.info',
                hidden: true,
                plain: true,
                labelAlign: 'right',
                labelWidth: 80,
                defaultType: 'textfield',
                border: false,
                layout: 'form',
                defaults: {
                    //width:200,
                    anchor: "95%",
                    allowBlank: false,
                    blankText: '该项不能为空！'
                },
                items: [{
                    id: 'remoteIp.info',
                    fieldLabel: "登录IP",
                    name: 'account.remoteIp',
                    regex: /^.{2,30}$/,
                    regexText: '请输入任意2--30个字符'
                }]
            },
                {
                    plain: true,
                    labelAlign: 'right',
                    labelWidth: 80,
                    defaultType: 'textfield',
                    border: false,
                    layout: 'form',
                    defaults: {
                        //width:200,
                        anchor: "95%",
                        allowBlank: false,
                        blankText: '该项不能为空！'
                    },
                    items: [{
                        fieldLabel: "MAC地址",
                        name: 'account.mac',
                        allowBlank: true,
                        regex: /^((([0-9a-fA-F]{2}\-){5}[0-9a-fA-F]{2})|(([0-9a-fA-F]{2}:){5}[0-9a-fA-F]{2}))$/,
                        regexText: '这个不是mac地址:0a-45-be-e6-00-aa或者0a:45:be:e6:00:aa'
                    }]
                }]
        }, {
            plain: true,
            defaultType: 'textfield',
            columnWidth: .5,
            labelAlign: 'right',
            labelWidth: 80,
            border: false,
            layout: 'form',
            defaults: {
                //width:200,
                anchor: "95%",
                allowBlank: false,
                blankText: '该项不能为空！'
            },
            items: [{
                fieldLabel: "性别", hiddenName: 'account.sex',
                xtype: 'combo',
                mode: 'local',
                emptyText: '--请选择--',
                editable: false,
                typeAhead: true,
                forceSelection: true,
                triggerAction: 'all',
                displayField: "key", valueField: "value",
                store: storeSex,
                value: '男'
            }, {
                fieldLabel: "部门",
                name: 'account.depart',
                regex: /^.{2,30}$/,
                regexText: '请输入任意2--30个字符',
                emptyText: '请输入任意2--30个字符',
                value: '信息部'
            }, {
                fieldLabel: "职务",
                name: 'account.title',
                regex: /^.{2,30}$/,
                regexText: '请输入任意2--30个字符',
                emptyText: '请输入任意2--30个字符',
                value: '主任'
            }, {
                fieldLabel: "电话",
                name: 'account.phone',
                regex: /^.{2,100}$/,
                regexText: '请输入(例:0571-88880571)',
                emptyText: '请输入(例:0571-88880571)'
            }, {
                fieldLabel: "Email",
                name: 'account.email',
                regex: /^\w+[\w.]*@[\w.]+\.\w+$/,
                regexText: '请输入(例:hello@hzih.com)',
                emptyText: '请输入(例:hello@hzih.com)',
                value: 'hello@hzih.net'
            }, {
                fieldLabel: "开始时间",
                name: 'account.startHour',
                regex: /^[0-9]{1,2}$/,
                regexText: '请输入任意2--30个字符',
                emptyText: '请输入任意2--30个字符',
                value: 9
            }, {
                fieldLabel: "结束时间",
                name: 'account.endHour',
                regex: /^[0-9]{1,2}$/,
                regexText: '请输入任意2--30个字符',
                emptyText: '请输入任意2--30个字符',
                value: 18
            }, {
                fieldLabel: "描述",
                name: 'account.description',
                xtype: 'textarea',
                allowBlank: true,
                regex: /^.{0,3000}$/,
                regexText: '请输入任意1--3000个字符',
                value: '这是一个用户信息'
            }]
        }]
    });
    var win = new Ext.Window({
        title: "新增信息",
        width: 650,
        layout: 'fit',
        height: 340,
        modal: true,
        items: formPanel,
        listeners: {
            show: function () {
                Ext.getCmp('ipMac.info').hide();
                Ext.getCmp('remoteIp.info').disable();
                Ext.getCmp('startIp.info').enable();
                Ext.getCmp('endIp.info').enable();
                Ext.getCmp('ips.info').show();
            }
        },
        bbar: [
            '->',
            {
                id: 'insert_win.info',
                text: '保存',
                handler: function () {
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url: '../../AccountAction_insert.action',
                            method: 'POST',
                            waitTitle: '系统提示',
                            waitMsg: '正在保存,请稍后...',
                            success: function (form, action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title: '信息',
                                    width: 250,
                                    msg: msg,
                                    animEl: 'insert_win.info',
                                    buttons: {'ok': '确定', 'no': '取消'},
                                    icon: Ext.MessageBox.INFO,
                                    closable: false,
                                    fn: function (e) {
                                        if (e == 'ok') {
                                            grid.render();
                                            store.reload();
                                            win.close();
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        Ext.MessageBox.show({
                            title: '信息',
                            width: 200,
                            msg: '请填写完成再提交!',
                            animEl: 'insert_win.info',
                            buttons: {'ok': '确定'},
                            icon: Ext.MessageBox.ERROR,
                            closable: false
                        });
                    }
                }
            }, {
                text: '关闭',
                handler: function () {
                    win.close();
                }
            }
        ]
    }).show();
}

/**
 * 修改用户
 */
function showUpdateUser() {
    var recordRoleUpdate = new Ext.data.Record.create([{name: 'value', mapping: 'value'}, {
        name: 'key',
        mapping: 'key'
    }]);
    var readerRoleUpdate = new Ext.data.JsonReader({totalProperty: 'total', root: "rows"}, recordRoleUpdate);
    var storeRoleUpdate = new Ext.data.Store({
        url: "../../RoleAction_readNameKeyValue.action",
        reader: readerRoleUpdate,
        listeners: {
            load: function () {
                var value = Ext.getCmp('role.update.info').getValue();
                Ext.getCmp('role.update.info').setValue(value);
            }
        }
    });
    storeRoleUpdate.load();
    var grid = Ext.getCmp('grid.info');
    var store = grid.getStore();
    var selModel = grid.getSelectionModel();
    var formPanel;
    if (selModel.hasSelection()) {
        var selections = selModel.getSelections();
        Ext.each(selections, function (item) {
            var ipType = item.data.ipType;
            var ipTypeT = ipType == true ? true : false;
            var ipTypeF = ipType == true ? false : true;
            formPanel = new Ext.form.FormPanel({
                frame: true,
                autoScroll: true,
                layout: 'column',
                border: false,
                items: [{
                    plain: true,
                    columnWidth: .5,
                    border: false,
                    layout: 'form',
                    items: [{
                        plain: true,
                        labelAlign: 'right',
                        labelWidth: 80,
                        defaultType: 'textfield',
                        border: false,
                        layout: 'form',
                        defaults: {
                            //width:200,
                            anchor: "95%",
                            allowBlank: false,
                            blankText: '该项不能为空！'
                        },
                        items: [{
                            name: 'account.id',
                            xtype: 'hidden',
                            value: item.data.id
                        }, {
                            name: 'account.userName',
                            xtype: 'hidden',
                            value: item.data.userName
                        }, {
                            id: 'userName.insert.info',
                            fieldLabel: "用户名",
                            xtype: 'displayfield',
                            value: item.data.userName
                        }, {
                            fieldLabel: "真实姓名",
                            name: 'account.name',
                            value: item.data.name,
                            regex: /^.{2,30}$/,
                            regexText: '请输入任意2--30个字符',
                            emptyText: '请输入任意2--30个字符'
                        }, {
                            fieldLabel: "状态", hiddenName: 'account.status', value: item.data.status,
                            xtype: 'combo',
                            mode: 'local',
                            emptyText: '--请选择--',
                            editable: false,
                            typeAhead: true,
                            forceSelection: true,
                            triggerAction: 'all',
                            displayField: "key", valueField: "value",
                            store: storeStatus
                        }, {
                            id: 'password.info',
                            fieldLabel: "密码",
                            name: 'account.password',
                            inputType: 'password',
                            regex: /^.{8,100}$/,
                            regexText: '密码规则:8~100位!',
                            emptyText: '请输入密码!',
                            listeners: {
                                blur: function () {
                                    var password = this.getValue();
                                    if (password.length > 0) {
                                        var myMask = new Ext.LoadMask(Ext.getBody(), {
                                            msg: '正在校验,请稍后...',
                                            removeMask: true
                                        });
                                        myMask.show();
                                        Ext.Ajax.request({
                                            url: '../../AccountAction_checkPassword.action',
                                            params: {password: password},
                                            method: 'POST',
                                            success: function (r, o) {
                                                var respText = Ext.util.JSON.decode(r.responseText);
                                                var msg = respText.msg;
                                                myMask.hide();
                                                if (msg != 'true') {
                                                    Ext.MessageBox.show({
                                                        title: '信息',
                                                        width: 250,
                                                        msg: msg,
                                                        buttons: {'ok': '确定'},
                                                        icon: Ext.MessageBox.INFO,
                                                        closable: false,
                                                        fn: function (e) {
                                                            if (e == 'ok') {
                                                                Ext.getCmp('password.info').setValue('');
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        }, {
                            id: 'password2.info',
                            fieldLabel: "确认密码",
                            inputType: 'password',
                            regex: /^.{8,100}$/,
                            regexText: '密码规则:8~100位!',
                            emptyText: '请输入密码!',
                            listeners: {
                                blur: function () {
                                    var password = Ext.getCmp('password.info').getValue();
                                    if (password.length > 0) {
                                        var password2 = this.getValue();
                                        if (password != password2 && password2.length > 0) {
                                            Ext.MessageBox.show({
                                                title: '信息',
                                                width: 250,
                                                msg: '<font color="red">"确认密码"和"密码"不一致!</font>',
                                                animEl: 'password2.info',
                                                buttons: {'ok': '确定'},
                                                icon: Ext.MessageBox.INFO,
                                                closable: false,
                                                fn: function (e) {
                                                    if (e == 'ok') {
                                                        Ext.getCmp('password2.info').setValue('');
                                                    }
                                                }
                                            });
                                        }
                                    } else {
                                        Ext.MessageBox.show({
                                            title: '信息',
                                            width: 270,
                                            msg: '<font color="red">请先输入"密码",再输入"确认密码"!</font>',
                                            animEl: 'password2.info',
                                            buttons: {'ok': '确定'},
                                            icon: Ext.MessageBox.INFO,
                                            closable: false,
                                            fn: function (e) {
                                                if (e == 'ok') {
                                                    Ext.getCmp('password2.info').setValue('');
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        }, {
                            id: 'role.update.info',
                            fieldLabel: "角色", hiddenName: 'role.id', value: item.data.roleId,
                            xtype: 'combo',
                            mode: 'local',
                            emptyText: '--请选择--',
                            editable: false,
                            typeAhead: true,
                            forceSelection: true,
                            triggerAction: 'all',
                            displayField: "key", valueField: "value",
                            store: storeRoleUpdate
                        }, {
                            id: 'ipType.info',
                            fieldLabel: '登陆选择',
                            xtype: 'radiogroup',
                            defaultType: 'radio',
                            layout: 'column',
                            items: [
                                {
                                    columnWidth: .5,
                                    boxLabel: 'IP段',
                                    name: 'account.ipType',
                                    inputValue: 1,
                                    checked: ipTypeT
                                },
                                {
                                    columnWidth: .5,
                                    boxLabel: '指定IP',
                                    name: 'account.ipType',
                                    inputValue: 0,
                                    checked: ipTypeF
                                }
                            ],
                            listeners: {
                                change: function (group, ck) {
                                    if (ck.inputValue == 1) {
                                        Ext.getCmp('ipMac.info').hide();
                                        Ext.getCmp('remoteIp.info').disable();
                                        Ext.getCmp('startIp.info').enable();
                                        Ext.getCmp('endIp.info').enable();
                                        Ext.getCmp('ips.info').show();
                                    } else {
                                        Ext.getCmp('ips.info').hide();
                                        Ext.getCmp('startIp.info').disable();
                                        Ext.getCmp('endIp.info').disable();
                                        Ext.getCmp('remoteIp.info').enable();
                                        Ext.getCmp('ipMac.info').show();
                                    }
                                }
                            }
                        }]
                    }, {
                        id: 'ips.info',
                        plain: true,
                        labelAlign: 'right',
                        labelWidth: 80,
                        defaultType: 'textfield',
                        border: false,
                        layout: 'form',
                        defaults: {
                            //width:200,
                            anchor: "95%",
                            allowBlank: false,
                            blankText: '该项不能为空！'
                        },
                        items: [{
                            id: 'startIp.info',
                            fieldLabel: "开始IP",
                            name: 'account.startIp', value: item.data.startIp,
                            regex: /^.{2,30}$/,
                            regexText: '请输入任意2--30个字符',
                            emptyText: '请输入任意2--30个字符'
                        }, {
                            id: 'endIp.info',
                            fieldLabel: "结束IP",
                            name: 'account.endIp', value: item.data.endIp,
                            regex: /^.{2,30}$/,
                            regexText: '请输入任意2--30个字符',
                            emptyText: '请输入任意2--30个字符'
                        }]
                    }, {
                        id: 'ipMac.info',
                        hidden: true,
                        plain: true,
                        labelAlign: 'right',
                        labelWidth: 80,
                        defaultType: 'textfield',
                        border: false,
                        layout: 'form',
                        defaults: {
                            //width:200,
                            anchor: "95%",
                            allowBlank: false,
                            blankText: '该项不能为空！'
                        },
                        items: [{
                            id: 'remoteIp.info',
                            fieldLabel: "登录IP",
                            name: 'account.remoteIp', value: item.data.remoteIp,
                            regex: /^.{2,30}$/,
                            regexText: '请输入任意2--30个字符'
                        }]
                    }, {
                        plain: true,
                        labelAlign: 'right',
                        labelWidth: 80,
                        defaultType: 'textfield',
                        border: false,
                        layout: 'form',
                        defaults: {
                            //width:200,
                            anchor: "95%",
                            allowBlank: false,
                            blankText: '该项不能为空！'
                        },
                        items: [{
                            fieldLabel: "MAC地址",
                            name: 'account.mac', value: item.data.mac,
                            allowBlank: true,
                            regex: /^((([0-9a-fA-F]{2}\-){5}[0-9a-fA-F]{2})|(([0-9a-fA-F]{2}:){5}[0-9a-fA-F]{2}))$/,
                            regexText: '这个不是mac地址:0a-45-be-e6-00-aa或者0a:45:be:e6:00:aa'
                        }]
                    }]
                }, {
                    plain: true,
                    defaultType: 'textfield',
                    columnWidth: .5,
                    labelAlign: 'right',
                    labelWidth: 80,
                    border: false,
                    layout: 'form',
                    defaults: {
                        //width:200,
                        anchor: "95%",
                        allowBlank: false,
                        blankText: '该项不能为空！'
                    },
                    items: [{
                        fieldLabel: "性别", hiddenName: 'account.sex', value: item.data.sex,
                        xtype: 'combo',
                        mode: 'local',
                        emptyText: '--请选择--',
                        editable: false,
                        typeAhead: true,
                        forceSelection: true,
                        triggerAction: 'all',
                        displayField: "key", valueField: "value",
                        store: storeSex
                    }, {
                        fieldLabel: "部门",
                        name: 'account.depart', value: item.data.depart,
                        regex: /^.{2,30}$/,
                        regexText: '请输入任意2--30个字符',
                        emptyText: '请输入任意2--30个字符'
                    }, {
                        fieldLabel: "职务",
                        name: 'account.title', value: item.data.title,
                        regex: /^.{2,30}$/,
                        regexText: '请输入任意2--30个字符',
                        emptyText: '请输入任意2--30个字符'
                    }, {
                        fieldLabel: "电话",
                        name: 'account.phone', value: item.data.phone,
                        regex: /^.{2,100}$/,
                        regexText: '请输入(例:0571-88880571)',
                        emptyText: '请输入(例:0571-88880571)'
                    }, {
                        fieldLabel: "Email",
                        name: 'account.email', value: item.data.email,
                        regex: /^\w+[\w.]*@[\w.]+\.\w+$/,
                        regexText: '请输入(例:hello@hzih.com)',
                        emptyText: '请输入(例:hello@hzih.com)'
                    }, {
                        fieldLabel: "开始时间",
                        name: 'account.startHour', value: item.data.startHour,
                        regex: /^[0-9]{1,2}$/,
                        regexText: '请输入任意2--30个字符',
                        emptyText: '请输入任意2--30个字符'
                    }, {
                        fieldLabel: "结束时间",
                        name: 'account.endHour', value: item.data.endHour,
                        regex: /^[0-9]{1,2}$/,
                        regexText: '请输入任意2--30个字符',
                        emptyText: '请输入任意2--30个字符'
                    }, {
                        fieldLabel: "描述",
                        name: 'account.description', value: item.data.description,
                        xtype: 'textarea',
                        allowBlank: true,
                        regex: /^.{0,3000}$/,
                        regexText: '请输入任意1--3000个字符'
                    }]
                }]
            });
        });
    }

    var win = new Ext.Window({
        title: "修改信息",
        width: 650,
        layout: 'fit',
        height: 340,
        modal: true,
        items: formPanel,
        listeners: {
            show: function () {
                var ipType = Ext.getCmp('ipType.info').getValue();
                if (ipType.inputValue == 1) {
                    Ext.getCmp('ipMac.info').hide();
                    Ext.getCmp('remoteIp.info').disable();
                    Ext.getCmp('startIp.info').enable();
                    Ext.getCmp('endIp.info').enable();
                    Ext.getCmp('ips.info').show();
                } else {
                    Ext.getCmp('ips.info').hide();
                    Ext.getCmp('startIp.info').disable();
                    Ext.getCmp('endIp.info').disable();
                    Ext.getCmp('remoteIp.info').enable();
                    Ext.getCmp('ipMac.info').show();
                }
            }
        },
        bbar: [
            '->',
            {
                id: 'update_win.info',
                text: '修改',
                handler: function () {
                    Ext.MessageBox.show({
                        title: '信息',
                        width: 250,
                        msg: '确定要修改?',
                        animEl: 'update_win.info',
                        buttons: {'ok': '继续', 'no': '取消'},
                        icon: Ext.MessageBox.WARNING,
                        closable: false,
                        fn: function (e) {
                            if (e == 'ok') {
                                if (formPanel.form.isValid()) {
                                    formPanel.getForm().submit({
                                        url: '../../AccountAction_update.action',
                                        method: 'POST',
                                        waitTitle: '系统提示',
                                        waitMsg: '正在修改,请稍后...',
                                        success: function (form, action) {
                                            var msg = action.result.msg;
                                            Ext.MessageBox.show({
                                                title: '信息',
                                                width: 250,
                                                msg: msg,
                                                animEl: 'update_win.info',
                                                buttons: {'ok': '确定', 'no': '取消'},
                                                icon: Ext.MessageBox.INFO,
                                                closable: false,
                                                fn: function (e) {
                                                    if (e == 'ok') {
                                                        grid.render();
                                                        store.reload();
                                                        win.close();
                                                    }
                                                }
                                            });
                                        }
                                    });
                                } else {
                                    Ext.MessageBox.show({
                                        title: '信息',
                                        width: 200,
                                        msg: '请填写完成再提交!',
                                        animEl: 'update_win.info',
                                        buttons: {'ok': '确定'},
                                        icon: Ext.MessageBox.ERROR,
                                        closable: false
                                    });
                                }
                            }
                        }
                    });

                }
            }, {
                text: '关闭',
                handler: function () {
                    win.close();
                }
            }
        ]
    }).show();
}

/**
 * 删除用户
 */
function deleteUser(value) {
    var grid = Ext.getCmp('grid.info');
    var store = grid.getStore();
    var selModel = grid.getSelectionModel();
    var formPanel;
    if (selModel.hasSelection()) {
        var selections = selModel.getSelections();
        Ext.each(selections, function (item) {
            Ext.MessageBox.show({
                title: '信息',
                msg: '<font color="green">确定要删除所选记录？</font>',
                animEl: value + '.delete.info',
                width: 260,
                buttons: {'ok': '确定', 'no': '取消'},
                icon: Ext.MessageBox.INFO,
                closable: false,
                fn: function (e) {
                    if (e == 'ok') {
                        var myMask = new Ext.LoadMask(Ext.getBody(), {
                            msg: '正在删除,请稍后...',
                            removeMask: true
                        });
                        myMask.show();
                        Ext.Ajax.request({
                            url: '../../AccountAction_delete.action',             // 删除 连接 到后台
                            params: {id: value, userName: item.data.userName},
                            method: 'POST',
                            success: function (r, o) {
                                myMask.hide();
                                var respText = Ext.util.JSON.decode(r.responseText);
                                var msg = respText.msg;
                                Ext.MessageBox.show({
                                    title: '信息',
                                    width: 250,
                                    msg: msg,
                                    animEl: value + '.delete.info',
                                    buttons: {'ok': '确定'},
                                    icon: Ext.MessageBox.INFO,
                                    closable: false,
                                    fn: function (e) {
                                        if (e == 'ok') {
                                            grid.render();
                                            store.reload();
                                        }
                                    }
                                });
                            }
                        });
                    }
                }
            });
        });
    }

}