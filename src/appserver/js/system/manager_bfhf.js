Ext.onReady(function () {
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';
    var start = 0;
    var pageSize = 15;
    var record = new Ext.data.Record.create([
        {name: 'id', mapping: 'id'},
        {name: 'backup_all', mapping: 'backup_all'},
        {name: 'backup_server', mapping: 'backup_server'},
        {name: 'backup_pki', mapping: 'backup_pki'},
        {name: 'backup_net', mapping: 'backup_net'},
        {name: 'backup_desc', mapping: 'backup_desc'},
        {name: 'backup_time', mapping: 'backup_time'},
        {name: 'backup_account_id', mapping: 'backup_account_id'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url: "../../BackUpAction_findBackUp.action",
        timeout: 10 * 1000
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

    store.load({
        params: {
            start: start, limit: pageSize
        }
    });

//    var boxM = new Ext.grid.CheckboxSelectionModel({singleSelect:true});   //复选框单选
    var rowNumber = new Ext.grid.RowNumberer();         //自动编号
    var colM = new Ext.grid.ColumnModel([
//        boxM,
        rowNumber,
        {
            header: "备份人员",
            dataIndex: "backup_account_id",
            align: 'center',
            sortable: true,
            menuDisabled: true,
            sort: true
        },
        {header: "备份说明", dataIndex: "backup_desc", align: 'center', sortable: true, menuDisabled: true},
        {header: "所有配置", dataIndex: "backup_all", align: 'center', sortable: true,renderer:show_back, menuDisabled: true},
        {header: "服务配置", dataIndex: "backup_server", align: 'center', sortable: true,renderer:show_back, menuDisabled: true},
        {header: "证书及黑名单配置", dataIndex: "backup_pki", align: 'center', sortable: true, renderer:show_back,menuDisabled: true},
        {header: "网络配置", dataIndex: "backup_net", align: 'center', sortable: true,renderer:show_back, menuDisabled: true},
        {header: "备份时间", dataIndex: "backup_time", align: 'center',width:150, sortable: true, menuDisabled: true},
        {
            header: '操作标记',
            dataIndex: 'flag',
            align: 'center',
            sortable: true,
            menuDisabled: true,
            renderer: show_flag
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

    var tb = new Ext.Toolbar({
        autoWidth: true,
        autoHeight: true,
        items: [
            {
                id: 'backup.info',
                xtype: 'button',
                text: '备份',
                iconCls: 'upload',
                handler: function () {
                    backup(grid_panel);
                }
            }]
    });

    var grid_panel = new Ext.grid.GridPanel({
        id: 'grid.info',
        plain: true,
        //autoHeight: true,
        height:300,
        viewConfig: {
            forceFit: true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle: 'width:100%',
        loadMask: {msg: '正在加载数据，请稍后...'},
        border: true,
        cm: colM,
//        sm:boxM,
        store: store,
        tbar: tb, /*
         listeners:{
         render:function(){
         tbar.render(this.tbar);
         }
         },*/
        bbar: page_toolbar,
        columnLines: true,
        //autoScroll: true,
        //border: false,
        //collapsible: false,
        stripeRows: true,
        //autoExpandColumn: 'Position',
        enableHdMenu: true,
        enableColumnHide: true,
        //selModel: new Ext.grid.RowSelect.ionModel({singleSelect: true}),
        //height: 300,
        frame: true/*,
         iconCls: 'icon-grid'*/
    });

    var port = new Ext.Viewport({
        layout: 'fit',
        renderTo: Ext.getBody(),
        items: [grid_panel]
    });
});

function show_flag(value, p, r) {
    return String.format(
        '<a id="recover.info" href="javascript:void(0);" onclick="recover();return false;" style="color: green;">恢复</a>&nbsp;&nbsp;&nbsp;'+
        '<a id="remove.info" href="javascript:void(0);" onclick="del();return false;" style="color: green;">删除</a>&nbsp;&nbsp;&nbsp;'
    );
};


function show_back(value) {
    if(value == "1"){
        return '<span style="color:green;">已备份</span>';
    }else{
        return '<span style="color:red;">未备份</span>';
    }
};

function backup(grid_panel) {
    var formPanel = new Ext.form.FormPanel({
        frame: true,
        autoScroll: true,
        labelWidth: 150,
        labelAlign: 'right',
        //defaultWidth: 300,
        autoWidth: true,
        layout: 'form',
        border: false,
        defaults: {
            //width: 250,
            anchor:"90%",
            allowBlank: false,
            blankText: '该项不能为空！'
        },
        items: [
            new Ext.form.TextField({
                fieldLabel: '备份描述',
                name: 'backup_desc',
                allowBlank: false,
                emptyText: "请输入备份描述",
                blankText: "不能为空，请正确填写"
            }),
           /* new Ext.form.TextField({
                fieldLabel: '备份内容',
                name: 'backup_content',
                allowBlank: false,
                emptyText: "请输入备份内容",
                blankText: "不能为空，请正确填写"
            }),*/
            {
                xtype: 'checkboxgroup',
                fieldLabel: '备份内容',
                columns: 2,
                allowBlank: true,
                items: [
                    {boxLabel: '所有配置',  name: 'backup_all', inputValue: 1},
                    {boxLabel: '服务及用户配置', name: 'backup_server', inputValue: 1},
                    {boxLabel: '黑名单及证书配置', name: 'backup_pki', inputValue: 1},
                    {boxLabel: '网络配置', name: 'backup_net', inputValue: 1}
                ]
            }
        ]
    });
    var win = new Ext.Window({
        title: "备份",
        width: 600,
        layout: 'fit',
        height: 250,
        modal: true,
        items: formPanel,
        bbar: [
            '->',
            {
                id: 'insert_win.info',
                text: '备份',
                width: 50,
                handler: function () {
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url: '../../BackUpAction_backup.action',
                            timeout: 20 * 60 * 1000,
                            method: 'POST',
                            waitTitle: '系统提示',
                            waitMsg: '正在连接...',
                            success: function (form, action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title: '信息',
                                    width: 250,
                                    msg: msg,
                                    buttons: Ext.MessageBox.OK,
                                    buttons: {'ok': '确定'},
                                    icon: Ext.MessageBox.INFO,
                                    closable: false,
                                    fn: function (e) {
                                        grid_panel.getStore().reload();
                                        win.close();
                                    }
                                });
                            },
                            failure: function (form, action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title: '信息',
                                    width: 250,
                                    msg: msg,
                                    buttons: Ext.MessageBox.OK,
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
                            buttons: Ext.MessageBox.OK,
                            buttons: {'ok': '确定'},
                            icon: Ext.MessageBox.ERROR,
                            closable: false
                        });
                    }
                }
            }, {
                text: '重置',
                width: 50,
                handler: function () {
                    formPanel.getForm().reset();
                }
            }
        ]
    }).show();
};

function recover() {
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    if (!recode) {
        Ext.Msg.alert("提示", "请选择一条记录!");
    } else {
        Ext.Msg.confirm("提示", "确定恢复备份？", function (sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url: "../../BackUpAction_recover.action",
                    timeout: 20 * 60 * 1000,
                    method: "POST",
                    params: {id: recode.get("id")},
                    success: function (r, o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                        grid_panel.getStore().reload();
                    },
                    failure: function (r, o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                    }
                });
            }
        });
    }
}

function del() {
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    if (!recode) {
        Ext.Msg.alert("提示", "请选择一条记录!");
    } else {
        Ext.Msg.confirm("提示", "确定删除备份？", function (sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url: "../../BackUpAction_delete.action",
                    timeout: 20 * 60 * 1000,
                    method: "POST",
                    params: {id: recode.get("id")},
                    success: function (r, o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                        grid_panel.getStore().reload();
                    },
                    failure: function (r, o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                    }
                });
            }
        });
    }
}





