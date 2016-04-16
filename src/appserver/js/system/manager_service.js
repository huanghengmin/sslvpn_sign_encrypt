Ext.onReady(function () {

    Ext.BLANK_IMAGE_URL = '../../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    //var snmp_start = 0;
    //var snmp_pageSize = 5;
    var snmp_record = new Ext.data.Record.create([
        {name: 'service', mapping: 'service'},
        {name: 'status', mapping: 'status'}
    ]);
    var snmp_proxy = new Ext.data.HttpProxy({
        url: "../../SnmpStatusAction_checkServerStatus.action"
    });
    var snmp_reader = new Ext.data.JsonReader({
        totalProperty: "totalCount",
        root: "root"
    }, snmp_record);
    var snmp_store = new Ext.data.GroupingStore({
        id: "store.info",
        proxy: snmp_proxy,
        reader: snmp_reader
    });
    var snmp_boxM = new Ext.grid.RowSelectionModel({singleSelect: true});   //复选框
    //var snmp_rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var snmp_colM = new Ext.grid.ColumnModel([
        snmp_boxM,
        //snmp_rowNumber,
        {header: "服务", dataIndex: "service", align: 'center', sortable: true, menuDisabled: true},
        {
            header: "状态",
            dataIndex: "status",
            align: 'center',
            sortable: true,
            menuDisabled: true,
            renderer: show_snmp_status
        },
        {
            header: '动作',
            dataIndex: "status",
            align: 'center',
            sortable: true,
            menuDisabled: true,
            renderer: show_snmp_flag
        }
    ]);
    /*var snmp_page_toolbar = new Ext.PagingToolbar({
     pageSize: snmp_pageSize,
     store: snmp_store,
     displayInfo: true,
     displayMsg: "显示第{0}条记录到第{1}条记录，一共{2}条",
     emptyMsg: "没有记录",
     beforePageText: "当前页",
     afterPageText: "共{0}页"
     });*/
    var snmp_grid_panel = new Ext.grid.GridPanel({
        id: 'snmp.grid.info',
        //title: 'SNMP',
        plain: true,
        autoHeight: true,
        viewConfig: {
            forceFit: true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle: 'width:100%',
        loadMask: {msg: '正在加载数据，请稍后...'},
        border: true,
        cm: snmp_colM,
        columnLines: true,
        sm:snmp_boxM,
        store: snmp_store/*,
         bbar: snmp_page_toolbar*/
    });

    <!-- SSH -->
    //var ssh_start = 0;
    //var ssh_pageSize = 5;
    var ssh_record = new Ext.data.Record.create([
        {name: 'service', mapping: 'service'},
        {name: 'status', mapping: 'status'}
    ]);
    var ssh_proxy = new Ext.data.HttpProxy({
        url: "../../SshStatusAction_checkServerStatus.action"
    });
    var ssh_reader = new Ext.data.JsonReader({
        totalProperty: "totalCount",
        root: "root"/*,
         id:'id'*/
    }, ssh_record);
    var ssh_store = new Ext.data.GroupingStore({
        id: "store.info",
        proxy: ssh_proxy,
        reader: ssh_reader
    });
    var ssh_boxM = new Ext.grid.RowSelectionModel({singleSelect: true});   //复选框
    //var ssh_rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var ssh_colM = new Ext.grid.ColumnModel([
        ssh_boxM,
        //ssh_rowNumber,
        {header: "服务", dataIndex: "service", align: 'center', sortable: true, menuDisabled: true},
        {
            header: "状态",
            dataIndex: "status",
            align: 'center',
            sortable: true,
            menuDisabled: true,
            renderer: show_ssh_status
        },
        {
            header: '动作',
            dataIndex: "flag",
            align: 'center',
            sortable: true,
            menuDisabled: true,
            renderer: show_ssh_flag
        }
    ]);
    /*var ssh_page_toolbar = new Ext.PagingToolbar({
     pageSize: ssh_pageSize,
     store: ssh_store,
     displayInfo: true,
     displayMsg: "显示第{0}条记录到第{1}条记录，一共{2}条",
     emptyMsg: "没有记录",
     beforePageText: "当前页",
     afterPageText: "共{0}页"
     });*/
    var ssh_grid_panel = new Ext.grid.GridPanel({
        id: 'ssh.grid.info',
        //title: 'SSH',
        plain: true,
        autoHeight: true,
        viewConfig: {
            forceFit: true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle: 'width:100%',
        loadMask: {msg: '正在加载数据，请稍后...'},
        border: true,
        collapsible:false,
        columnLines: true,
        cm: ssh_colM,
        sm:ssh_boxM,
        store: ssh_store/*,
         bbar: ssh_page_toolbar*/
    });


    <!--webmin-->
    //var webmin_start = 0;
    //var webmin_pageSize = 5;
    var webmin_record = new Ext.data.Record.create([
        {name: 'service', mapping: 'service'},
        {name: 'status', mapping: 'status'}
    ]);
    var webmin_proxy = new Ext.data.HttpProxy({
        url: "../../WebminStatusAction_checkServerStatus.action"
    });
    var webmin_reader = new Ext.data.JsonReader({
        totalProperty: "totalCount",
        root: "root"/*,
         id:'id'*/
    }, webmin_record);
    var webmin_store = new Ext.data.GroupingStore({
        id: "webmin.store.info",
        proxy: webmin_proxy,
        reader: webmin_reader
    });
    var webmin_boxM = new Ext.grid.RowSelectionModel({singleSelect: true});   //复选框
    //var webmin_rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var webmin_colM = new Ext.grid.ColumnModel([
        webmin_boxM,
        //webmin_rowNumber,
        {header: "服务", dataIndex: "service", align: 'center', sortable: true, menuDisabled: true},
        {
            header: "状态",
            dataIndex: "status",
            align: 'center',
            sortable: true,
            menuDisabled: true,
            renderer: show_webmin_status
        },
        {
            header: '动作',
            dataIndex: "status",
            align: 'center',
            sortable: true,
            menuDisabled: true,
            renderer: show_webmin_flag
        }
    ]);
    /* var webmin_page_toolbar = new Ext.PagingToolbar({
     pageSize: webmin_pageSize,
     store: webmin_store,
     displayInfo: true,
     displayMsg: "显示第{0}条记录到第{1}条记录，一共{2}条",
     emptyMsg: "没有记录",
     beforePageText: "当前页",
     afterPageText: "共{0}页"
     });*/
    var webmin_grid_panel = new Ext.grid.GridPanel({
        id: 'webmin.grid.info',
        //title: 'WEBMIN',
        plain: true,
        autoHeight: true,
        viewConfig: {
            forceFit: true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle: 'width:100%',
        loadMask: {msg: '正在加载数据，请稍后...'},
        border: true,
        cm: webmin_colM,
        columnLines: true,
        sm:webmin_boxM,
        store: webmin_store/*,
         bbar: webmin_page_toolbar*/
    });


    var panel = new Ext.Panel({
        plain: true,
        autoWidth: true,
        autoHeight: true,
        border: false,
        items: [
            {
                id: 'snmp.info',
                xtype: 'fieldset',
                title: 'SNMP服务',
                //height: Ext.getBody().getHeight() / 3,
                items: [snmp_grid_panel]
            },
            {
                id: 'ssh.info',
                xtype: 'fieldset',
                title: 'SSH服务',
                //height: Ext.getBody().getHeight() / 3,
                items: [ssh_grid_panel]
            },
            {
                id: 'webmin.info',
                xtype: 'fieldset',
                //height: Ext.getBody().getHeight() / 3,
                title: 'WEBMIN服务',
                items: [webmin_grid_panel]
            }
        ]
    });
    new Ext.Viewport({
        layout: 'fit',
        renderTo: Ext.getBody(),
        autoScroll: true,
        items: [
            {
                frame: true,
                autoScroll: true,
                items: [panel]
            }
        ]
    });

    snmp_store.load();

    webmin_store.load();

    ssh_store.load();
});

function show_ssh_status(value, p, r) {
    if (r.get("status") == "0") {
        return String.format('<img src="../../img/icon/off.gif" alt="停止" title="停止" />');
    } else if (r.get("status") == "1") {
        return String.format('<img src="../../img/icon/ok.png" alt="运行" title="运行" />');
    }
}

function show_snmp_status(value, p, r) {
    if (r.get("status") == "0") {
        return String.format('<img src="../../img/icon/off.gif" alt="停止" title="停止" />');
    } else if (r.get("status") == "1") {
        return String.format('<img src="../../img/icon/ok.png" alt="运行" title="运行" />');
    }
}

function show_webmin_status(value, p, r) {
    if (r.get("status") == "0") {
        return String.format('<img src="../../img/icon/off.gif" alt="停止" title="停止" />');
    } else if (r.get("status") == "1") {
        return String.format('<img src="../../img/icon/ok.png" alt="运行" title="运行" />');
    }
}

function show_snmp_flag(value, p, r) {
    if (r.get("status") == "0") {
        return String.format(
            '<a id="show_snmp_flag.info" href="javascript:void(0);" onclick="start_snmp();return false;" style="color: green;">开启</a>&nbsp;&nbsp;&nbsp;' +
            '<a id="show_snmp_reload_flag.info" href="javascript:void(0);" onclick="restart_snmp();return false;" style="color: green;">重启</a>&nbsp;&nbsp;&nbsp;'
        );
    } else if (r.get("status") == "1") {
        return String.format(
            '<a id="show_snmp_flag.info" href="javascript:void(0);" onclick="stop_snmp();return false;" style="color: green;">停止</a>&nbsp;&nbsp;&nbsp;' +
            '<a id="show_snmp_reload_flag.info" href="javascript:void(0);" onclick="restart_snmp();return false;" style="color: green;">重启</a>&nbsp;&nbsp;&nbsp;'
        );
    }
}

function show_ssh_flag(value, p, r) {
    if (r.get("status") == "0") {
        return String.format(
            '<a id="show_ssh_flag.info" href="javascript:void(0);" onclick="start_ssh();return false;" style="color: green;">开启</a>&nbsp;&nbsp;&nbsp;' +
            '<a id="show_ssh_reload_flag.info" href="javascript:void(0);" onclick="restart_ssh();return false;" style="color: green;">重启</a>&nbsp;&nbsp;&nbsp;'
        );
    } else if (r.get("status") == "1") {
        return String.format(
            '<a id="show_ssh_flag.info" href="javascript:void(0);" onclick="stop_ssh();return false;" style="color: green;">停止</a>&nbsp;&nbsp;&nbsp;' +
            '<a id="show_ssh_reload_flag.info" href="javascript:void(0);" onclick="restart_ssh();return false;" style="color: green;">重启</a>&nbsp;&nbsp;&nbsp;'
        );
    }
}

function show_webmin_flag(value, p, r) {
    if (r.get("status") == "0") {
        return String.format(
            '<a id="show_webmin_flag.info" href="javascript:void(0);" onclick="start_webmin();return false;" style="color: green;">开启</a>&nbsp;&nbsp;&nbsp;' +
            '<a id="show_webmin_reload_flag.info" href="javascript:void(0);" onclick="restart_webmin();return false;" style="color: green;">重启</a>&nbsp;&nbsp;&nbsp;'
        );
    } else if (r.get("status") == "1") {
        return String.format(
            '<a id="show_webmin_flag.info" href="javascript:void(0);" onclick="stop_webmin();return false;" style="color: green;">停止</a>&nbsp;&nbsp;&nbsp;' +
            '<a id="show_webmin_reload_flag.info" href="javascript:void(0);" onclick="restart_webmin();return false;" style="color: green;">重启</a>&nbsp;&nbsp;&nbsp;'
        );
    }
}

function stop_snmp() {
    var grid = Ext.getCmp('snmp.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    if (!recode) {
        Ext.Msg.alert("提示", "请选择一条记录!");
    } else {
        Ext.Msg.confirm("提示", "确认停止SNMP服务吗？", function (sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url: '../../SnmpStatusAction_closeServer.action',
                    timeout: 20 * 60 * 1000,
                    method: "POST",
                    success: function (r, o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                        grid.getStore().reload();
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

function start_snmp() {
    var grid = Ext.getCmp('snmp.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    if (!recode) {
        Ext.Msg.alert("提示", "请选择一条记录!");
    } else {
        Ext.Msg.confirm("提示", "确认开启SNMP服务吗？", function (sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url: '../../SnmpStatusAction_openServer.action',
                    timeout: 20 * 60 * 1000,
                    method: "POST",
                    success: function (r, o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                        grid.getStore().reload();
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

function restart_snmp() {
    var grid = Ext.getCmp('snmp.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    if (!recode) {
        Ext.Msg.alert("提示", "请选择一条记录!");
    } else {
        Ext.Msg.confirm("提示", "确认重启SNMP服务吗？", function (sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url: '../../SnmpStatusAction_reloadServer.action',
                    timeout: 20 * 60 * 1000,
                    method: "POST",
                    success: function (r, o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                        grid.getStore().reload();
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

function stop_ssh() {
    var grid = Ext.getCmp('ssh.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    if (!recode) {
        Ext.Msg.alert("提示", "请选择一条记录!");
    } else {
        Ext.Msg.confirm("提示", "确认停止SSH服务吗？", function (sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url: '../../SshStatusAction_closeServer.action',
                    timeout: 20 * 60 * 1000,
                    method: "POST",
                    success: function (r, o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                        grid.getStore().reload();
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

function start_ssh() {
    var grid = Ext.getCmp('ssh.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    if (!recode) {
        Ext.Msg.alert("提示", "请选择一条记录!");
    } else {
        Ext.Msg.confirm("提示", "确认开启SSH服务吗？", function (sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url: '../../SshStatusAction_openServer.action',
                    timeout: 20 * 60 * 1000,
                    method: "POST",
                    success: function (r, o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                        grid.getStore().reload();
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

function restart_ssh() {
    var grid = Ext.getCmp('ssh.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    if (!recode) {
        Ext.Msg.alert("提示", "请选择一条记录!");
    } else {
        Ext.Msg.confirm("提示", "确认重启SSH服务吗？", function (sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url: '../../SshStatusAction_reloadServer.action',
                    timeout: 20 * 60 * 1000,
                    method: "POST",
                    success: function (r, o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                        grid.getStore().reload();
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

function stop_webmin() {
    var grid = Ext.getCmp('webmin.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    if (!recode) {
        Ext.Msg.alert("提示", "请选择一条记录!");
    } else {
        Ext.Msg.confirm("提示", "确认停止WEBMIN服务吗？", function (sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url: '../../WebminStatusAction_closeServer.action',
                    timeout: 20 * 60 * 1000,
                    method: "POST",
                    success: function (r, o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                        grid.getStore().reload();
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

function start_webmin() {
    var grid = Ext.getCmp('webmin.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    if (!recode) {
        Ext.Msg.alert("提示", "请选择一条记录!");
    } else {
        Ext.Msg.confirm("提示", "确认开启WEBMIN服务吗？", function (sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url: '../../WebminStatusAction_openServer.action',
                    timeout: 20 * 60 * 1000,
                    method: "POST",
                    success: function (r, o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                        grid.getStore().reload();
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

function restart_webmin() {
    var grid = Ext.getCmp('webmin.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    if (!recode) {
        Ext.Msg.alert("提示", "请选择一条记录!");
    } else {
        Ext.Msg.confirm("提示", "确认重启WEBMIN服务吗？", function (sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url: '../../WebminStatusAction_reloadServer.action',
                    timeout: 20 * 60 * 1000,
                    method: "POST",
                    success: function (r, o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                        grid.getStore().reload();
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
