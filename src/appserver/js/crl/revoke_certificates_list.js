Ext.onReady(function () {
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';
    /*var start = 0;
     var pageSize = 15;
     var record = new Ext.data.Record.create([
     {name: 'username', mapping: 'username'},
     {name: 'id_card', mapping: 'id_card'},
     {name: 'serial', mapping: 'serial'},
     {name: 'revoke_time', mapping: 'revoke_time'},
     {name: 'iss_name', mapping: 'iss_name'}
     ]);

     var proxy = new Ext.data.HttpProxy({
     url: "../../RevokeAction_get_revoke.action",
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
     });*/


    var start = 0;
    var pageSize = 15;
    var record = new Ext.data.Record.create([
        {name: 'name', mapping: 'name'},
        {name: 'type', mapping: 'type'},
        {name: 'late_time', mapping: 'late_time'},
        {name: 'status', mapping: 'status'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url: "../../RevokeAction_findPoint.action",
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
            header: "配置名称",
            dataIndex: "name",
            align: 'center',
            renderer: show_name,
            sortable: true,
            menuDisabled: true,
            sort: true
        },
        {header: "类型", dataIndex: "type", align: 'center', sortable: true, menuDisabled: true},
        {header: "最后更新时间", dataIndex: "late_time", align: 'center', sortable: true, menuDisabled: true},
        {header: "自动更新状态", dataIndex: "status", align: 'center', sortable: true, menuDisabled: true, renderer: show_status},
        {header: '操作标记', dataIndex: 'flag', align: 'center', sortable: true, menuDisabled: true, renderer: show_flag}
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

    function setHeight() {
        var h = document.body.clientHeight - 8;
        return h;
    };

    var tb = new Ext.Toolbar({
        autoWidth: true,
        autoHeight: true,
        items: [
            {
                id: 'ldap_point.info',
                xtype: 'button',
                text: '添加LDAP下载点',
                iconCls: 'add',
                handler: function () {
                    addLdapPoint(store);
                }
            },
            {
                id: 'http_point.info',
                xtype: 'button',
                text: '添加HTTP下载点',
                iconCls: 'add',
                handler: function () {
                    addHttpPoint(store);
                }
            }/*,
            {
                id: 'setting_crl.info',
                xtype: 'button',
                text: 'CRL自动更新',
                iconCls: 'setting',
                handler: function () {
                    autoCRL(store);
                }
            }*//*, {
                id: 'auto_down_crl.info',
                xtype: 'button',
                text: '自动更新CRL',
                iconCls: 'download',
                handler: function () {
                    autoDownCRL(store);
                }
            }, {
                id: 'down_crl.info',
                xtype: 'button',
                text: '手动更新CRL',
                iconCls: 'download',
                handler: function () {
                    downCRL(store);
                }
            }*/]
    });

    var grid_panel = new Ext.grid.GridPanel({
        id: 'grid.info',
//        title:'吊销列表',
        plain: true,
        height: setHeight(),
        viewConfig: {
            forceFit: true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle: 'width:100%',
        loadMask: {msg: '正在加载数据，请稍后...'},
        border: true,
        cm: colM,
//        sm:boxM,
        store: store,
        tbar: tb,
        /*listeners:{
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

function show_status(value){
    if (value == "1") {
        return '<img src="../../img/icon/ok.png" alt="启用"/><a href="javascript:void(0);" onclick="disable();return false;" >停用</a>&nbsp;&nbsp;'
    } else if (value == "0") {
        return '<img src="../../img/icon/off.gif" alt="未启用"/><a href="javascript:void(0);" onclick="enable();return false;" >启用</a>&nbsp;&nbsp;'
    }
}

function autoCRL(){
    var initForm = new Ext.form.FormPanel({
        plain: true,
        border: false,
        loadMask: {msg: '正在加载数据，请稍后.....'},
        labelAlign: 'right',
        labelWidth: 150,
        reader: new Ext.data.JsonReader({}, [ {
            name: 'auto_flag'
        }, {
            name: 'conf_time'
        }, {
            name: 'conf_day'
        }, {
            name: 'hours'
        }, {
            name: 'minutes'
        }, {
            name: 'seconds'
        },{
            name: 'conf_type'
        }, {
            name: 'conf_time2'
        }, {
            name: 'conf_month_day'
        }, {
            name: 'conf_time3'
        }]),
        defaultType: 'textfield',
        defaults: {
            allowBlank: false,
            blankText: '该项不能为空!'
        },
        items: [
            new Ext.form.FieldSet({
                title: 'CRL自动更新',
                checkboxToggle: true,
                autoHeight: true,
                border: false,
                checkboxName: 'auto_flag',
                defaultType: 'textfield',
                items: [{
                    xtype: 'compositefield',
                    hideLabel: true,
                    items: [{
                        name: 'conf_type',
                        xtype: 'radio',
                        inputValue: 'interval',
                        boxLabel: '周期更新'
                    }, {
                        xtype: 'textfield',
                        name: 'hours',
                        width: 50
                    }, {
                        xtype: 'displayfield',
                        value: '时'
                    }, {
                        xtype: 'textfield',
                        name: 'minutes',
                        width: 50
                    }, {
                        xtype: 'displayfield',
                        value: '分'
                    }, {
                        xtype: 'textfield',
                        name: 'seconds',
                        width: 50
                    }, {
                        xtype: 'displayfield',
                        value: '秒'
                    }]
                },{
                    xtype: 'compositefield',
                    hideLabel: true,
                    items: [{
                        name: 'conf_type',
                        xtype: 'radio',
                        inputValue: 'day',
                        boxLabel: '按日更新'
                    }, {
                        xtype: 'textfield',
                        name: 'conf_time',
                        width: 50,
                        value: '23:00',
                        //regex: /^([0][0-9]|[1][0-9]|[2][0-3]|[0-9]):([0-5][0-9])$/,
                        regexText: '只能输入00:00--23:59'/*,
                         emptyText: '请输入00:00--23:59'*/
                    }]
                }, {
                    xtype: 'compositefield',
                    hideLabel: true,
                    items: [{
                        name: 'conf_type',
                        xtype: 'radio',
                        inputValue: 'week',
                        boxLabel: '按周更新'
                    }, {
                        xtype: 'radiogroup',
                        columns: 4,
                        vertical: false,
                        width: 300,
                        items: [{
                            boxLabel: '周一',
                            inputValue: 1,
                            name: 'conf_day'
                        }, {
                            boxLabel: '周二',
                            inputValue: 2,
                            name: 'conf_day'
                        }, {
                            boxLabel: '周三',
                            inputValue: 3,
                            name: 'conf_day'
                        }, {
                            boxLabel: '周四',
                            inputValue: 4,
                            name: 'conf_day'
                        }, {
                            boxLabel: '周五',
                            inputValue: 5,
                            name: 'conf_day'
                        }, {
                            boxLabel: '周六',
                            inputValue: 6,
                            name: 'conf_day'
                        }, {
                            boxLabel: '周日',
                            inputValue: 7,
                            name: 'conf_day'
                        }],
                        name: 'conf_day'
                    }, {
                        xtype: 'textfield',
                        name: 'conf_time2',
                        width: 50,
                        value: '23:00',
                        //regex: /^([0][0-9]|[1][0-9]|[2][0-3]|[0-9]):([0-5][0-9])$/,
                        regexText: '只能输入00:00--23:59'/*,
                         emptyText: '请输入00:00--23:59'*/
                    }]
                }, {
                    xtype: 'compositefield',
                    hideLabel: true,
                    items: [{
                        name: 'conf_type',
                        xtype: 'radio',
                        inputValue: 'month',
                        boxLabel: '按月更新'
                    }, {
                        xtype: 'textfield',
                        name: 'conf_month_day',
                        width: 40,
                        regex: /^[1-9]|[1-2][0-9]|3[0-1]$/,
                        regexText: '只能输入0-31'/*,
                         emptyText: '请输入0-31'*/
                    }, {
                        xtype: 'displayfield',
                        value: '日'
                    }, {
                        xtype: 'textfield',
                        name: 'conf_time3',
                        width: 50,
                        value: '23:00',
                        //    regex: /^([0][0-9]|[1][0-9]|[2][0-3]|[0-9]):([0-5][0-9])$/,
                        regexText: '只能输入00:00--23:59'/*,
                         emptyText: '请输入00:00--23:59'*/
                    }]
                }]
            })
        ],
        buttons: [  {
            id: 'insert_win.info',
            text: '保存配置',
            autoWidth: true,
            handler: function () {
                if (initForm.form.isValid()) {
                    initForm.getForm().submit({
                        url: "../../RevokeAction_updateAutoCRL.action",
                        method: 'POST',
                        waitTitle: '系统提示',
                        waitMsg: '正在连接...',
                        success: function () {
                            Ext.MessageBox.show({
                                title: '信息',
                                width: 250,
                                msg: '保存成功,点击返回页面!',
                                buttons: Ext.MessageBox.OK,
                                buttons: {'ok': '确定'},
                                icon: Ext.MessageBox.INFO,
                                closable: false
                            });
                        },
                        failure: function () {
                            Ext.MessageBox.show({
                                title: '信息',
                                width: 250,
                                msg: '保存失败，请与管理员联系!',
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
        }]
    });

    // 加载配置数据
    if (initForm) {
        initForm.getForm().load({
            url: '../../RevokeAction_findAutoCRL.action',
            success: function (form, action) {

            },
            failure: function (form, action) {
                Ext.Msg.alert('错误', '加载数据出错！');
            }
        });
    }

    var win = new Ext.Window({
        title: 'CRL自动更新',
        width: 500,
        layout: 'fit',
        height: 250,
        modal: true,
        items: initForm
    });
    win.show();

}

function disable(){
    var grid = Ext.getCmp('grid.info');
    var recode = grid.getSelectionModel().getSelected();
    if(!recode){
        Ext.Msg.alert("提示", "请选择一条记录!");
    }else{
        Ext.Msg.confirm("提示", "禁用自动更新？", function(sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url : "../../RevokeAction_disablePoint.action",
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

function enable(){
    var grid = Ext.getCmp('grid.info');
    var recode = grid.getSelectionModel().getSelected();
    if(!recode){
        Ext.Msg.alert("提示", "请选择一条记录!");
    }else{
        Ext.Msg.confirm("提示", "启用自动更新？", function(sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url : "../../RevokeAction_enablePoint.action",
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

function show_flag(value, p, r) {
    return String.format(
        '<a href="javascript:void(0);" onclick="deletePoint();return false;" >删除</a>&nbsp;&nbsp;'+
        '<a href="javascript:void(0);" onclick="downPoint();return false;" >下载</a>&nbsp;&nbsp;'
    );
}

function show_name(value, p, r) {
    return String.format(
        '<a id="\'' + value + '\'.updatePoint.info" href="javascript:void(0);" onclick="updatePoint(\'' + value + '\');return false;" >' + value + '</a>'
    );
}

function downPoint(){
    var grid = Ext.getCmp('grid.info');
    var recode = grid.getSelectionModel().getSelected();
    if(!recode){
        Ext.Msg.alert("提示", "请选择一条记录!");
    }else{
        Ext.Msg.confirm("提示", "确认下载证书？", function(sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url : "../../RevokeAction_downPoint.action",
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

function updatePoint(v) {
    var grid = Ext.getCmp('grid.info');
    var recode = grid.getSelectionModel().getSelected();
    if (recode.get("type") == "http") {
        var formPanel = new Ext.form.FormPanel({
            frame: true,
            autoScroll: true,
            labelWidth: 100,
            labelAlign: 'right',
            //defaultWidth: 300,
            autoWidth: true,
            layout: 'form',
            border: false,
            reader: new Ext.data.JsonReader({},[{
                name: 'name'
            }, {
                name: 'type'
            }, {
                name: 'status'
            }, {
                name: 'late_time'
            }, {
                name: 'url'
            }]),
            defaultType: 'textfield',
            defaults: {
                //width: 250,
                anchor:"90%",
                //allowBlank: false,
                blankText: '该项不能为空!'
            },
            items: [
                new Ext.form.TextField({
                    readOnly: true,
                    fieldLabel: 'Http配置名称',
                    name: 'name',
                    allowBlank: false,
                    blankText: "不能为空，请正确填写"
                }),
                new Ext.form.TextField({
                    fieldLabel: 'Http URL地址',
                    name: 'url',
                    allowBlank: false,
                    blankText: "不能为空，请正确填写"
                })
            ],
            buttons: [
                '->',
                {
                    id: 'modify_http_win.info',
                    text: '保存配置',
                    handler: function () {
                        if (formPanel.form.isValid()) {
                            formPanel.getForm().submit({
                                url: "../../RevokeAction_updateHttpPoint.action",
                                method: 'POST',
                                params: {name: recode.get("name")},
                                waitTitle: '系统提示',
                                waitMsg: '正在连接...',
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
                                                grid.getStore().reload();
                                                win.close();
                                            }
                                        }
                                    });
                                },
                                failure: function (form, action) {
                                    var msg = action.result.msg;
                                    Ext.MessageBox.show({
                                        title: '信息',
                                        width: 250,
                                        msg: msg,
                                        animEl: 'insert_win.info',
                                        buttons: {'ok': '确定', 'no': '取消'},
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
                }
            ]
        });

        var win = new Ext.Window({
            title: "Http下载点",
            width: 550,
            layout: 'fit',
            height: 200,
            modal: true,
            items: formPanel
        }).show();

        // 加载配置数据
        if (formPanel) {
            formPanel.getForm().load({
                url: "../../RevokeAction_findHttpPoint.action",
                params: {name: recode.get("name")},
                success: function (form, action) {

                },
                failure: function (form, action) {
                    Ext.Msg.alert('错误', '加载数据出错！');
                }
            });
        }
    }
    else {
        var formPanel = new Ext.form.FormPanel({
            frame: true,
            autoScroll: true,
            labelWidth: 150,
            labelAlign: 'right',
            defaultWidth: 300,
            autoWidth: true,
            layout: 'form',
            border: false,
            reader: new Ext.data.JsonReader({},[{
                name: 'name'
            }, {
                name: 'type'
            }, {
                name: 'status'
            }, {
                name: 'late_time'
            }, {
                name: 'ldap_host'
            }, {
                name: 'ldap_port'
            }, {
                name: 'ldap_adm'
            }, {
                name: 'ldap_pwd'
            }, {
                name: 'ldap_search_path'
            }, {
                name: 'ldap_filter_string'
            }, {
                name: 'ldap_attribute_name'
            }]),
            defaultType: 'textfield',
            defaults: {
                width: 250,
                //allowBlank: false,
                blankText: '该项不能为空!'
            },
            items: [
                new Ext.form.TextField({
                    readOnly: true,
                    fieldLabel: 'LDAP配置名称',
                    name: 'name',
                    allowBlank: false,
                    blankText: "不能为空，请正确填写"
                }),
                new Ext.form.TextField({
                    fieldLabel: 'LDAP服务器地址',
                    name: 'ldap_host',
                    regex: /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/,
                    regexText: '请输入正确的IP地址',
                    allowBlank: false,
                    blankText: "不能为空，请正确填写"
                }),
                new Ext.form.TextField({
                    fieldLabel: 'LDAP服务器端口',
                    regex: /^(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$/,
                    regexText: '请输入正确的端口',
                    name: 'ldap_port',
                    allowBlank: false,
                    blankText: "不能为空，请正确填写"
                }),
                new Ext.form.TextField({
                    fieldLabel: 'LDAP登陆用户名',
                    name: 'ldap_adm',
                    allowBlank: true,
                    blankText: "LDAP登陆用户名"
                }),
                new Ext.form.TextField({
                    fieldLabel: 'LDAP登陆密码',
                    name: 'ldap_pwd',
                    inputType: 'password',
                    allowBlank: true,
                    blankText: "LDAP登陆密码"
                }),
                new Ext.form.TextField({
                    fieldLabel: '搜索路径',
                    name: 'ldap_search_path',
                    allowBlank: false,
                    blankText: "搜索路径"
                }),
                new Ext.form.TextField({
                    fieldLabel: '过滤字符串',
                    name: 'ldap_filter_string',
                    allowBlank: false,
                    blankText: "过滤字符串"
                }),
                new Ext.form.TextField({
                    fieldLabel: 'LDAP节点属性名',
                    name: 'ldap_attribute_name',
                    //value: 'certificateRevocationList;binary',
                    allowBlank: false,
                    blankText: "LDAP节点属性名"
                })
            ],
            buttons: [
                '->',
                {
                    id: 'modify_ldap_win.info',
                    text: '保存配置',
                    handler: function () {
                        if (formPanel.form.isValid()) {
                            formPanel.getForm().submit({
                                url: "../../RevokeAction_updateLdapPoint.action",
                                method: 'POST',
                                params: {name: recode.get("name")},
                                waitTitle: '系统提示',
                                waitMsg: '正在连接...',
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
                                                grid.getStore().reload();
                                                win.close();
                                            }
                                        }
                                    });
                                },
                                failure: function (form, action) {
                                    var msg = action.result.msg;
                                    Ext.MessageBox.show({
                                        title: '信息',
                                        width: 250,
                                        msg: msg,
                                        animEl: 'insert_win.info',
                                        buttons: {'ok': '确定', 'no': '取消'},
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
                }
            ]
        });

        var win = new Ext.Window({
            title: "LDAP下载点",
            width: 500,
            layout: 'fit',
            height: 340,
            modal: true,
            items: formPanel
        }).show();

        // 加载配置数据
        if (formPanel) {
            formPanel.getForm().load({
                url: "../../RevokeAction_findLdapPoint.action",
                params: {name: recode.get("name")},
                success: function (form, action) {

                },
                failure: function (form, action) {
                    Ext.Msg.alert('错误', '加载数据出错！');
                }
            });
        }
    }
}

function deletePoint() {
    var grid = Ext.getCmp('grid.info');
    var recode = grid.getSelectionModel().getSelected();
    if(!recode){
        Ext.Msg.alert("提示", "请选择一条记录!");
    }else{
        Ext.Msg.confirm("提示", "删除下载点？", function(sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url : "../../RevokeAction_deletePoint.action",
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

function addLdapPoint(store) {
    var formPanel = new Ext.form.FormPanel({
        frame: true,
        autoScroll: true,
        labelWidth: 150,
        labelAlign: 'right',
        defaultWidth: 300,
        autoWidth: true,
        layout: 'form',
        border: false,
        defaultType: 'textfield',
        defaults: {
            width: 250,
            //allowBlank: false,
            blankText: '该项不能为空!'
        },
        items: [
            new Ext.form.TextField({
                fieldLabel: 'LDAP配置名称',
                name: 'name',
                allowBlank: false,
                blankText: "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel: 'LDAP服务器地址',
                name: 'ldap_host',
                regex: /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/,
                regexText: '请输入正确的IP地址',
                allowBlank: false,
                blankText: "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel: 'LDAP服务器端口',
                regex: /^(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$/,
                regexText: '请输入正确的端口',
                name: 'ldap_port',
                allowBlank: false,
                blankText: "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel: 'LDAP登陆用户名',
                name: 'ldap_adm',
                allowBlank: true,
                blankText: "LDAP登陆用户名"
            }),
            new Ext.form.TextField({
                fieldLabel: 'LDAP登陆密码',
                name: 'ldap_pwd',
                inputType: 'password',
                allowBlank: true,
                blankText: "LDAP登陆密码"
            }),
            new Ext.form.TextField({
                fieldLabel: '搜索路径',
                name: 'ldap_search_path',
                allowBlank: false,
                blankText: "搜索路径"
            }),
            new Ext.form.TextField({
                fieldLabel: '过滤字符串',
                name: 'ldap_filter_string',
                allowBlank: false,
                blankText: "过滤字符串"
            }),
            new Ext.form.TextField({
                fieldLabel: 'LDAP节点属性名',
                name: 'ldap_attribute_name',
                value: 'certificateRevocationList;binary',
                allowBlank: false,
                blankText: "LDAP节点属性名"
            })
        ],
        buttons: [
            '->',
            {
                id: 'insert_win.info',
                text: '保存配置',
                handler: function () {
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url: "../../RevokeAction_addLdapPoint.action",
                            method: 'POST',
                            waitTitle: '系统提示',
                            waitMsg: '正在连接...',
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
                                            store.reload();
                                            win.close();
                                        }
                                    }
                                });
                            },
                            failure: function (form, action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title: '信息',
                                    width: 250,
                                    msg: msg,
                                    animEl: 'insert_win.info',
                                    buttons: {'ok': '确定', 'no': '取消'},
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
            }
        ]
    });

    var win = new Ext.Window({
        title: "LDAP下载点",
        width: 500,
        layout: 'fit',
        height: 340,
        modal: true,
        items: formPanel
    }).show();
}

function addHttpPoint(store) {
    var formPanel = new Ext.form.FormPanel({
        frame: true,
        autoScroll: true,
        labelWidth: 150,
        labelAlign: 'right',
        defaultWidth: 300,
        autoWidth: true,
        layout: 'form',
        border: false,
        defaultType: 'textfield',
        defaults: {
            width: 250,
            allowBlank: false,
            blankText: '该项不能为空!'
        },
        items: [
            new Ext.form.TextField({
                fieldLabel: 'HTTP配置名称',
                name: 'name',
                allowBlank: false,
                blankText: "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel: 'HTTP URL地址',
                name: 'url',
                allowBlank: false,
                blankText: "不能为空，请正确填写"
            })
        ],
        buttons: [
            '->',
            {
                id: 'insert_win.info',
                text: '保存配置',
                handler: function () {
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url: "../../RevokeAction_addHttpPoint.action",
                            method: 'POST',
                            waitTitle: '系统提示',
                            waitMsg: '正在连接...',
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
                                            store.reload();
                                            win.close();
                                        }
                                    }
                                });
                            },
                            failure: function (form, action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title: '信息',
                                    width: 250,
                                    msg: msg,
                                    animEl: 'insert_win.info',
                                    buttons: {'ok': '确定', 'no': '取消'},
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
            }
        ]
    });

    var win = new Ext.Window({
        title: "HTTP下载点",
        width: 500,
        layout: 'fit',
        height: 150,
        modal: true,
        items: formPanel
    }).show();
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
        title: '更新CRL',
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

function downCRL(store) {

    var record = new Ext.data.Record.create([
        {name: 'url', mapping: 'url'} /*,
         {name:'second', mapping:'second'} ,
         {name:'hour', mapping:'hour'},
         {name:'day', mapping:'day'}*/
    ]);

    var proxy = new Ext.data.HttpProxy({
        url: "../../CRLConfigAction_find.action"
    });

    var reader = new Ext.data.JsonReader({
        totalProperty: "totalCount",
        root: "root"
    }, record);

    var store = new Ext.data.GroupingStore({
        id: "store.info",
        proxy: proxy,
        reader: reader
    });

    store.load();
    store.on('load', function () {
        var url = store.getAt(0).get('url');
//        var second = store.getAt(0).get('second');
//        var hour = store.getAt(0).get('hour');
//        var day = store.getAt(0).get('day');
        Ext.getCmp('crl.url').setValue(url);
//        Ext.getCmp('crl.second').setValue(second);
//        Ext.getCmp('crl.hour').setValue(hour);
//        Ext.getCmp('crl.day').setValue(day);
    });

    var downForm = new Ext.form.FormPanel({
        frame: true,
        labelWidth: 150,
        labelAlign: 'right',
        fileUpload: true,
        border: false,
        defaults: {
//            width : 250,
            anchor: '%95',
            allowBlank: false,
            blankText: '该项不能为空！'
        },
        items: [
            {
                xtype: 'displayfield',
                fieldLabel: '<font color="red">注意</font>',
                value: 'HTTP请求CRL下载地址'
            },
            new Ext.form.TextField({
                fieldLabel: 'URL地址',
                name: 'url',
                id: "crl.url",
                allowBlank: false,
                blankText: "URL地址"
            })]
    });
    var win = new Ext.Window({
        title: '下载CRL',
        width: 500,
        height: 150,
        layout: 'fit',
        modal: true,
        items: [downForm],
        bbar: [
            '->',
            {
                text: '下载',
                handler: function () {
                    if (downForm.form.isValid()) {
                        downForm.getForm().submit({
                            url: '../../RevokeAction_down_crl.action',
                            method: 'POST',
                            waitTitle: '系统提示',
                            waitMsg: '正在下载,请稍后...',
                            success: function (form, action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title: '信息',
                                    width: 250,
                                    msg: msg,
                                    buttons: {'ok': '确定', 'no': '取消'},
                                    icon: Ext.MessageBox.INFO,
                                    closable: false,
                                    fn: function (e) {
                                        if (e == 'ok') {
                                            store.reload();
                                            win.close();
                                        }
                                    }
                                });
                            }, failure: function (form, action) {
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
                            buttons: {'ok': '确定'},
                            icon: Ext.MessageBox.ERROR,
                            closable: false
                        });
                    }
                }
            },
            {
                text: '关闭',
                handler: function () {
                    win.close();
                }
            }
        ]
    }).show();
}

function autoDownCRL(store) {

    var record = new Ext.data.Record.create([
        {name: 'url', mapping: 'url'},
        {name: 'second', mapping: 'second'},
        {name: 'hour', mapping: 'hour'},
        {name: 'day', mapping: 'day'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url: "../../CRLConfigAction_find.action"
    });

    var reader = new Ext.data.JsonReader({
        totalProperty: "totalCount",
        root: "root"
    }, record);

    var store = new Ext.data.GroupingStore({
        id: "store.info",
        proxy: proxy,
        reader: reader
    });

    store.load();
    store.on('load', function () {
        var url = store.getAt(0).get('url');
        var second = store.getAt(0).get('second');
        var hour = store.getAt(0).get('hour');
        var day = store.getAt(0).get('day');
        Ext.getCmp('crl.auto.url').setValue(url);
        Ext.getCmp('crl.second').setValue(second);
        Ext.getCmp('crl.hour').setValue(hour);
        Ext.getCmp('crl.day').setValue(day);
    });

    var downForm = new Ext.form.FormPanel({
        frame: true,
        labelWidth: 150,
        labelAlign: 'right',
        fileUpload: true,
        border: false,
        defaults: {
            anchor: '%95',
            allowBlank: false,
            blankText: '该项不能为空！'
        },
        items: [
            {
                xtype: 'displayfield',
                fieldLabel: '<font color="red">注意</font>',
                value: 'HTTP请求CRL下载地址'
            },
            new Ext.form.TextField({
                fieldLabel: 'URL地址',
                name: 'url',
                id: "crl.auto.url",
                allowBlank: false,
                blankText: "URL地址"
            }),
            {
                layout: 'column',
                border: false,
                fieldLabel: '更新周期',
                items: [
                    new Ext.form.NumberField({
                        columnWidth: .2,
                        name: 'second',
                        id: "crl.second",
                        nanText: '只能输入0-59之间的数字',//无效数字提示
                        maxValue: 59,//最大值
                        minValue: 0, //最小值
                        allowBlank: true
                    }),
                    new Ext.form.DisplayField({
                        columnWidth: .1,
                        value: '分'
                    }),
                    new Ext.form.NumberField({
                        fieldLabel: '时',
                        columnWidth: .2,
                        nanText: '只能输入0-23之间的数字',//无效数字提示
                        maxValue: 23,//最大值
                        minValue: 0, //最小值
                        name: 'hour',
                        id: "crl.hour",
                        allowBlank: true
                    }),
                    new Ext.form.DisplayField({
                        columnWidth: .1,
                        value: '时'
                    }),
                    new Ext.form.NumberField({
                        fieldLabel: '天',
                        columnWidth: .2,
                        name: 'day',
                        id: "crl.day",
                        allowBlank: true
                    }),
                    new Ext.form.DisplayField({
                        columnWidth: .1,
                        value: '天'
                    })
                ]
            }
        ]
    });
    var win = new Ext.Window({
        title: '自动更新CRL配置',
        width: 500,
        height: 200,
        layout: 'fit',
        modal: true,
        items: [downForm],
        bbar: [
            '->',
            {
                text: '保存配置',
                handler: function () {
                    if (downForm.form.isValid()) {
                        downForm.getForm().submit({
                            url: '../../CRLConfigAction_save.action',
                            method: 'POST',
                            waitTitle: '系统提示',
                            waitMsg: '正在下载,请稍后...',
                            success: function (form, action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title: '信息',
                                    width: 250,
                                    msg: msg,
                                    buttons: {'ok': '确定', 'no': '取消'},
                                    icon: Ext.MessageBox.INFO,
                                    closable: false,
                                    fn: function (e) {
                                        if (e == 'ok') {
                                            store.reload();
                                            win.close();
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
                            buttons: {'ok': '确定'},
                            icon: Ext.MessageBox.ERROR,
                            closable: false
                        });
                    }
                }
            },
            {
                text: '关闭',
                handler: function () {
                    win.close();
                }
            }
        ]
    }).show();
}



