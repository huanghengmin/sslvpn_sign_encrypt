Ext.onReady(function () {

    Ext.BLANK_IMAGE_URL = '../../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var start = 0;
    var pageSize = 5;
    var toolbar = new Ext.Toolbar({
        plain: true,
        width: 350,
        height: 30,
        items: [
            {
                id: 'update_crl.info',
                xtype: 'button',
                text: '上传CRL',
                iconCls: 'upload',
                handler: function () {
                    updateCRL(store);
                }
            },
            {
                xtype: 'button',
                text: '黑名单校验配置',
                //iconCls: 'set',
                iconCls: 'setting',
                handler: function () {
                    check_crl(grid_panel, store);
                }
            }
        ]
    });
    var record = new Ext.data.Record.create([
        {name: 'name', mapping: 'name'},
        {name: 'filesize', mapping: 'filesize'},
        {name: 'path', mapping: 'path'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url: "../../RevokeFileAction_find.action"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty: "total",
        root: "rows"
    }, record);
    var store = new Ext.data.GroupingStore({
        id: "store.info",
        proxy: proxy,
        reader: reader
    });

    var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var colM = new Ext.grid.ColumnModel([
        rowNumber,
        {header: "证书文件", dataIndex: "name", align: 'center', sortable: true, menuDisabled: true},
        {header: "文件长度", dataIndex: "filesize", align: 'center', sortable: true, width: 150, menuDisabled: true},
        {header: "操作标记", dataIndex: "flag", align: 'center', sortable: true, menuDisabled: true,renderer: show_flag}
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
        id: 'trust.grid.info',
        //title: '证书链管理',
        plain: true,
        viewConfig: {
            forceFit: true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle: 'width:100%',
        loadMask: {msg: '正在加载数据，请稍后...'},
        cm: colM,
        //sm: boxM,
        store: store,
        tbar: toolbar,
        bbar: page_toolbar,
        //title: '资源配置',
        columnLines: true,
        autoScroll: true,
        border: false,
        collapsible: false,
        stripeRows: true,
        enableHdMenu: true,
        enableColumnHide: true,
        height: 300,
        frame: true
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

function show_flag(){
    return String.format(
        '<a href="javascript:void(0);" onclick="del();return false;" >删除</a>&nbsp;&nbsp;'+
        '<a href="javascript:void(0);" onclick="down();return false;" >下载</a>&nbsp;&nbsp;'
    );
}

function show_flag_crl(){
    return String.format(
        '<a href="javascript:void(0);" onclick="del_crl();return false;" >删除</a>&nbsp;&nbsp;'+
        '<a href="javascript:void(0);" onclick="down_crl();return false;" >下载</a>&nbsp;&nbsp;'
    );
}

function check_crl(grid, store) {
    var start = 0;
    var pageSize = 5;
    var record = new Ext.data.Record.create([
        {name: 'name', mapping: 'name'},
        {name: 'filesize', mapping: 'filesize'},
        {name: 'status', mapping: 'status'},
        {name: 'path', mapping: 'path'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url: "../../RevokeFileAction_findStatus.action"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty: "total",
        root: "rows"
    }, record);
    var store = new Ext.data.GroupingStore({
        id: "store.info",
        proxy: proxy,
        reader: reader,
        listeners: {
            load: function () {
                var records = [];//存放选中记录
                for (var i = 0; i < store.getCount(); i++) {
                    var record = store.getAt(i);
                    if (record.data.status == 1) {//根据后台数据判断那些记录默认选中
                        records.push(record);
                    }
                }
                boxM.selectRecords(records);//执行选中记录
            }
        }
    });
    var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
    var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var colM = new Ext.grid.ColumnModel([
        boxM,
        rowNumber,
        {header: "文件名称", dataIndex: "name", align: 'center', sortable: true, menuDisabled: true},
        {
            header: "文件长度",
            dataIndex: "filesize",
            align: 'center',
            sortable: true,
            menuDisabled: true
        },
        {header: "操作标记", dataIndex: "flag", align: 'center', sortable: true, menuDisabled: true,renderer: show_flag_crl}
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
    var auth_panel = new Ext.grid.GridPanel({
        id: 'crl.grid.info',
        plain: true,
        viewConfig: {
            forceFit: true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle: 'width:100%',
        loadMask: {msg: '正在加载数据，请稍后...'},
        cm: colM,
        sm: boxM,
        store: store,
        bbar: page_toolbar,
        columnLines: true,
        autoScroll: true,
        border: false,
        collapsible: false,
        stripeRows: true,
        //autoExpandColumn: 'Position',
        enableHdMenu: true,
        enableColumnHide: true,
        //selModel: new Ext.grid.RowSelectionModel({singleSelect: true}),
        height: 300,
        frame: true/*,
         iconCls: 'icon-grid'*/
    });

    var win = new Ext.Window({
        width: 600,
        layout: 'fit',
        title: '黑名单校验配置',
        height: 300,
        modal: true,
        items: auth_panel,
        bbar: [
            '->',
            {
                id: 'insert_win.info',
                text: '保存配置',
                handler: function () {
                    var selected = new Array();
                    var recode = Ext.getCmp("crl.grid.info").getSelectionModel().getSelections();
                    for (var i = 0; i < recode.length; i++) {
                        selected[i] = recode[i].get("name");
                    }
                    Ext.Ajax.request({
                        url: '../../RevokeFileAction_config.action',
                        params: {names: selected},
                        timeout: 20 * 60 * 1000,
                        method: "POST",
                        success: function (form, action) {
                            Ext.MessageBox.show({
                                title: '信息',
                                width: 250,
                                msg: "更新配置成功",
                                buttons: Ext.MessageBox.OK,
                                buttons: {'ok': '确定'},
                                icon: Ext.MessageBox.INFO,
                                closable: false,
                                fn: function (e) {
                                    store.reload();
                                    win.close();
                                }
                            });
                        },
                        failure: function (result) {
                            Ext.Msg.alert("提示", "更新配置失败!");
                        }
                    });
                }
            }
        ]
    }).show();
    store.load();
}

function updateCRL(store) {
    var uploadForm = new Ext.form.FormPanel({
        frame: true,
        labelWidth: 150,
        labelAlign: 'right',
        fileUpload: true,
        border: false,
        defaults: {
            width: 250,
            allowBlank: false,
            blankText: '该项不能为空！'
        },
        items: [{
            xtype: 'displayfield',
            fieldLabel: '注意',
            value: '上传文件必须为CRL列表'
        }, {
            id: 'crlFile',
            name: 'crlFile',
            xtype: 'textfield',
            inputType: 'file',
            fieldLabel: "上传CRL",
            listeners: {
                render: function () {
                    Ext.get('crlFile').on("change", function () {
                        var file = Ext.get('crlFile').getValue();
                        var fs = file.split('.');
                        if (fs[fs.length - 1].toLowerCase() == 'crl' | fs[fs.length - 1].toLowerCase() == 'pem') {
                            Ext.MessageBox.show({
                                title: '信息',
                                msg: '<font color="green">确定要上传文件:' + file + '？</font>',
                                width: 300,
                                buttons: {'ok': '确定', 'no': '取消'},
                                icon: Ext.MessageBox.WARNING,
                                closable: false,
                                fn: function (e) {
                                    if (e == 'ok') {
                                        if (uploadForm.form.isValid()) {
                                            uploadForm.getForm().submit({
                                                url: '../../RevokeAction_update_crl.action',
                                                method: 'POST',
                                                waitTitle: '系统提示',
                                                waitMsg: '正在上传,请稍后...',
                                                success: function (form, action) {
                                                    var msg = action.result.msg;
                                                    Ext.MessageBox.show({
                                                        title: '信息',
                                                        width: 250,
                                                        msg: msg,
//                                                    animEl:'insert.win.info',
                                                        buttons: {'ok': '确定', 'no': '取消'},
                                                        icon: Ext.MessageBox.INFO,
                                                        closable: false,
                                                        fn: function (e) {
                                                            if (e == 'ok') {
                                                                store.reload();
                                                                win.close();
                                                            } else {
                                                                Ext.getCmp('crlFile').setValue('');
                                                            }
                                                        }
                                                    });
                                                },
                                                failure: function (form, action) {
                                                    var msg = action.result.msg;
                                                    Ext.MessageBox.show({
                                                        title: '信息',
                                                        width: 200,
                                                        msg: msg,
                                                        buttons: {'ok': '确定'},
                                                        icon: Ext.MessageBox.ERROR,
                                                        closable: false
                                                    });
                                                }
                                            });
                                        } else {
                                            Ext.MessageBox.show({
                                                title: '信息',
                                                width: 200,
                                                msg: '请填写完成再提交!',
//                                            animEl:'insert.win.info',
                                                buttons: {'ok': '确定'},
                                                icon: Ext.MessageBox.ERROR,
                                                closable: false
                                            });
                                        }
                                    }
                                }
                            });
                        } else {
                            Ext.MessageBox.show({
                                title: '信息',
                                width: 200,
                                msg: '上传文件格式不对,请重新选择!',
                                buttons: {'ok': '确定'},
                                icon: Ext.MessageBox.ERROR,
                                closable: false,
                                fn: function (e) {
                                    if (e == 'ok') {
                                        Ext.getCmp('crlFile').setValue('');
                                    }
                                }
                            });
                        }
                    });
                }
            }
        }]
    });
    var win = new Ext.Window({
        title: '上传CRL',
        width: 500,
        height: 150,
        layout: 'fit',
        modal: true,
        items: [uploadForm],
        bbar: [
            '->',
            {
                text: '关闭',
                handler: function () {
                    win.close();
                }
            }
        ]
    }).show();
}

function down(){
    if (!Ext.fly('MergeFiles')) {
        var frm = document.createElement('form');
        frm.id = 'MergeFiles';
        frm.name = id;
        frm.style.display = 'none';
        document.body.appendChild(frm);
    }
    ;
    var grid = Ext.getCmp('trust.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    var name = recode.get("name");
    Ext.Ajax.request({
        url: '../../RevokeFileAction_down.action',
        timeout: 20 * 60 * 1000,
        params: {name: name},
        form: Ext.fly('MergeFiles'),
        method: 'POST',
        isUpload: true
    });
}

function down_crl(){
    if (!Ext.fly('MergeFiles')) {
        var frm = document.createElement('form');
        frm.id = 'MergeFiles';
        frm.name = id;
        frm.style.display = 'none';
        document.body.appendChild(frm);
    }
    ;
    var grid = Ext.getCmp('crl.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    var name = recode.get("name");
    Ext.Ajax.request({
        url: '../../RevokeFileAction_down.action',
        timeout: 20 * 60 * 1000,
        params: {name: name},
        form: Ext.fly('MergeFiles'),
        method: 'POST',
        isUpload: true
    });
}

function del(){
    var grid = Ext.getCmp('trust.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    if(!recode){
        Ext.Msg.alert("提示", "请选择一条记录!");
    }else{
        Ext.Msg.confirm("提示", "确认删除文件？", function(sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url : "../../RevokeFileAction_del.action",
                    timeout: 20*60*1000,
                    method : "POST",
                    params:{name:recode.get("name")},
                    success : function(r,o){
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                        grid.getStore().reload();
                    },
                    failure : function(r,o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                    }
                });
            }
        });
    }
}

function del_crl(){
    var grid = Ext.getCmp('crl.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    if(!recode){
        Ext.Msg.alert("提示", "请选择一条记录!");
    }else{
        Ext.Msg.confirm("提示", "确认删除文件？", function(sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url : "../../RevokeFileAction_del.action",
                    timeout: 20*60*1000,
                    method : "POST",
                    params:{name:recode.get("name")},
                    success : function(r,o){
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                        grid.getStore().reload();
                    },
                    failure : function(r,o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                    }
                });
            }
        });
    }
}


function setHeight() {
    var h = document.body.clientHeight - 8;
    return h;
}

function setWidth() {
    return document.body.clientWidth - 8;
}





