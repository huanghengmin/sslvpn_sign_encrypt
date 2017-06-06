/*
 *双机热备配置
 */

Ext.onReady(function() {
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';

    /////////////////////////////////////////////////////////////////////////////////////

    var record = new Ext.data.Record.create([
        {name:'device_type',       mapping:'device_type'},
        {name:'listen_inet',       mapping:'listen_inet'},
        {name:'back_inet',       mapping:'back_inet'},
        {name:'virt_ip',              mapping:'virt_ip'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url:"../../HotBackupKeepAlivedAction_find.action"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:'rows'
    },record);
    var store = new Ext.data.Store({
        proxy : proxy,
        reader : reader
    });
    var mainData = [[1,'主设备'],[0,'副设备']];
    var mainStore = new Ext.data.SimpleStore({fields:['value','key'],data:mainData});
    var button = new Ext.Panel({
        plain:true,
        buttonAlign :'left',
        autoScroll:true,
        buttons:[
        	new Ext.Toolbar.Spacer({width:190}),
           {
                    fieldLabel: '服务状态',
                    id: 'serverstatus',
                    name: 'serverstatus',
                    value: "[<font color='red'>未启动</font>]  [<a href='javascript:;' style='color:blue;' onclick='openServer()'>启动</a>]",
                    xtype: 'displayfield'
            },
            {
                text:"保存配置",
                id:'save.info',
                handler:function(){
                    if (initForm.form.isValid()) {
                        initForm.getForm().submit({
                            url :'../../HotBackupKeepAlivedAction_update.action',
                            method :'POST',
                            waitTitle :'系统提示',
                            waitMsg :'正在保存,请稍后...',
                            success : function(form,action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    msg:msg,
                                    animEl:'save.info',
                                    width:300,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.INFO,
                                    closable:false,
                                    fn:function(e){
                                        if(e=='ok'){
//                                            store.reload();
//                                            location.reload();
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        Ext.MessageBox.show({
                            title:'信息',
                            msg:'保存失败，请填写完成再保存!',
                            animEl:'save.info',
                            width:300,
                            buttons:{'ok':'确定'},
                            icon:Ext.MessageBox.ERROR,
                            closable:false
                        });
                    }
                }
            }
        ]
    });

    var initForm = new Ext.form.FormPanel({
        plain:true,
        border:false,
        loadMask : { msg : '正在加载数据，请稍后.....' },
        labelAlign:'right',
        labelWidth:150,
        defaultType:'textfield',
        defaults:{
            width:200,
            allowBlank:false,
            blankText:'该项不能为空!'
        },
        items:[{
            id:'backupDeviceType.info',
            fieldLabel:'设备类型',
            hiddenName:'device_type',
            xtype:'combo',
            mode:'local',
            emptyText :'--请选择--',
            editable : false,
            typeAhead:true,
            forceSelection: true,
            triggerAction:'all',
            displayField:"key",
            valueField:"value",
            store:mainStore
        },{
            id:'backupVip.info',
            fieldLabel:'虚拟对外IP',
            regex:/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/,
            regexText:'请输入正确的IP地址',
            name:'virt_ip'
        },{
            id:'backInet.info',
            fieldLabel:'热备网口',
            name:'back_inet'
        },{
            id:'backupInet.info',
            fieldLabel:'监控网口',
            name:'listen_inet'
        },{
        	fieldLabel:"监控服务",
        	html:"<a href='javascript:;' onclick='telnet_win();' >管理</a>",
        	xtype:'displayfield'
        }],
    buttons:[
        button
    ]
    });

    var panel = new Ext.Panel({
        plain:true,
        width:setWidth(),
        border:false,
        autoScroll:true,
        items:[{
            xtype:'fieldset',
            title:'双机热备',
            width:500,
            items:[initForm]
        }]
    });

    new Ext.Viewport({
    	layout:'fit',
    	renderTo:Ext.getBody(),
    	items:[{
            frame:true,
            autoScroll:true,
            items:[panel]
        }]
    });
    store.load();
    store.on('load',function(){
        Ext.getCmp('backupDeviceType.info').setValue(store.getAt(0).get('device_type'));
        Ext.getCmp('backupVip.info').setValue(store.getAt(0).get('virt_ip'));
        Ext.getCmp('backupInet.info').setValue(store.getAt(0).get('listen_inet'));
        Ext.getCmp('backInet.info').setValue(store.getAt(0).get('back_inet'));
    });
    checkServerStatus();
});


function setWidth(){
    return document.body.clientWidth-15;
}


function checkServerStatus() {
    Ext.Ajax.request({
        url: '../../KeepAlivedServerStatusAction_checkServerStatus.action',
        success: function (response, result) {
            var reText = Ext.util.JSON.decode(response.responseText);
            if ("0" == reText.msg) {
                Ext.getCmp("serverstatus").setValue("[<font color='red'>未启动</font>] [<a href='javascript:;' style='color:blue;' onclick='openServer()'>启动</a>]")
                Ext.getCmp("runtime").setValue("[<font color='orange'>服务未启动</font>]");
            } else {
                Ext.getCmp("serverstatus").setValue("[<font color='green'>已启动</font>] [<font color='#ff4500'><a href='javascript:;' style='color:blue;' onclick='closeServer()'>停止</a></font>]<!--[<a href='javascript:;' style='color:blue;' onclick='reloadServer()'>重启</a>]-->");
                Ext.getCmp("runtime").setValue("[<font color='orange'>服务正在运行</font>]");
            }
        }
    });
}

function closeServer() {
    var myMask = new Ext.LoadMask(Ext.getBody(), {
        msg: '正在关闭,请稍后...',
        removeMask: true
    });
    myMask.show();
    Ext.Ajax.request({
        url: '../../KeepAlivedServerStatusAction_closeServer.action',
        success: function (response, result) {
            myMask.hide();
            var reText = Ext.util.JSON.decode(response.responseText);
            if ("0" == reText.msg) {
                Ext.getCmp("serverstatus").setValue("[<font color='red'>未启动</font>] [<a href='javascript;' style='color:blue;' onclick='openServer()'>启动</a>]");
                Ext.Msg.alert('提示', "停止热备服务成功");
            } else {
                Ext.Msg.alert('提示', "停止热备服务失败");
            }
            checkServerStatus();
        },
        failure: function (form, action) {
            myMask.hide();
            Ext.Msg.alert('提示', "停止热备服务失败");
        }
    });
}

function openServer() {
    Ext.Ajax.request({
        url: '../../KeepAlivedServerStatusAction_openServer.action',
        success: function (response, result) {
            Ext.MessageBox.hide();
            var reText = Ext.util.JSON.decode(response.responseText);
            if ("1" == reText.msg) {
                Ext.getCmp("serverstatus").setValue("[<font color='green'>已启动</font>] [<font color='#ff4500'><a href='javascript:;' style='color:blue;' onclick='closeServer()'>停止</a></font>]");
                Ext.Msg.alert('提示', "开启热备服务成功");
            } else {
                Ext.Msg.alert('提示', "开启热备服务失败");
            }
            checkServerStatus();
        },
        failure: function (form, action) {
            Ext.MessageBox.hide();
            Ext.Msg.alert('提示', "开启热备服务失败");
        }
    });
}

function telnet_win() {
    var start = 0;
    var pageSize = 15;
    var toolbar = new Ext.Toolbar({
        plain: true,
        items: [
            {
                id: 'add_key.info',
                xtype: 'button',
                text: '添加服务',
                iconCls: 'add',
                handler: function () {
                    addsourceNet(store);
                }
            }
        ]
    });
    var record = new Ext.data.Record.create([
        {name: 'id', mapping: 'id'},
        {name: 'ip', mapping: 'ip'},
        {name: 'port', mapping: 'port'},
        {name: 'v_ip', mapping: 'v_ip'},
        {name: 'v_port', mapping: 'v_port'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url: "../../HotBackupKeepAlivedAction_find_virtserver.action"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty: "total",
        root: "rows",
        id: 'id'
    }, record);



    var store = new Ext.data.GroupingStore({
        id: "private.store.info",
        proxy: proxy,
        reader: reader
    });

    store.load({
        params:{
            start:start, limit:pageSize
        }
    });
    //var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
    var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var colM = new Ext.grid.ColumnModel([
        //boxM,
        rowNumber,
        {header: "虚拟IP", dataIndex: "v_ip", align: 'center', sortable: true, menuDisabled: true},
        {header: "虚拟端口", dataIndex: "v_port", align: 'center', sortable: true, menuDisabled: true},
        {header: "服务IP", dataIndex: "ip", align: 'center', sortable: true, menuDisabled: true},
        {header: "服务端口", dataIndex: "port", align: 'center', sortable: true, menuDisabled: true},
        {header: '操作标记', dataIndex: "flag", align: 'center', sortable: true, menuDisabled: true, renderer: show_flag, width: 100}
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
        autoExpandColumn: 'Position',
        enableHdMenu: true,
        enableColumnHide: true,
        selModel: new Ext.grid.RowSelectionModel({singleSelect: true}),
        height: 350,
        frame: true,
        iconCls: 'icon-grid'
    });

    var win = new Ext.Window({
        title:'监控服务',
        width:810,height:383,
        frame:true,modal:true,
        items:[grid_panel]
    }).show();
}

function show_flag(){
    return String.format(
        '<a id="update_private.info" href="javascript:void(0);" onclick="update_private();return false;" style="color: green;">修改</a>&nbsp;&nbsp;&nbsp;'+
        '<a id="delete_private.info" href="javascript:void(0);" onclick="delete_private();return false;" style="color: green;">删除</a>&nbsp;&nbsp;&nbsp;'
    );
}


function addsourceNet(store){
    var formPanel = new Ext.form.FormPanel({
        frame:true,
        autoScroll:true,
        labelWidth:150,
        labelAlign:'right',
        defaultWidth:300,
        autoWidth:true,
        layout:'form',
        border:false,
        defaults : {
            width : 250,
            allowBlank : false,
            blankText : '该项不能为空！'
        },
        items:[
            new Ext.form.TextField({
                fieldLabel : 'IP地址',
                name : 'ip',
                regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                regexText:'请输入正确的网络地址',
                id:  'add.ip',
                allowBlank : false,
                emptyText:"IP地址",
                blankText : "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel : '端口',
                //regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                //regexText:'请输入子网掩码',
                name : 'port',
                id:  'add.port',
                allowBlank : false,
                emptyText:"子网掩码",
                blankText : "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel : '虚拟IP地址',
                name : 'v_ip',
                regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                regexText:'请输入正确的网络地址',
                id:  'add.v_ip',
                allowBlank : false,
                emptyText:"虚拟IP地址",
                blankText : "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel : '虚拟端口',
                //regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                //regexText:'请输入子网掩码',
                name : 'v_port',
                id:  'add.v_port',
                allowBlank : false,
                emptyText:"子网掩码",
                blankText : "不能为空，请正确填写"
            })
        ]
    });
    var win = new Ext.Window({
        title:"添加资源",
        width:500,
        layout:'fit',
        height:250,
        modal:true,
        items:formPanel,
        bbar:[
            '->',
            {
                id:'add_win.info',
                text:'确定',
                width:50,
                handler:function(){
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url :'../../HotBackupKeepAlivedAction_add_virtserver.action',
                            timeout: 20*60*1000,
                            method :'POST',
                            waitTitle :'系统提示',
                            waitMsg :'正在连接...',
                            success : function(form, action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:250,
                                    msg:msg,
                                    buttons:Ext.MessageBox.OK,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.INFO,
                                    closable:false,
                                    fn:function(e){
                                        store.reload();
                                        win.close();
                                    }
                                });
                            },
                            failure : function(form, action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:250,
                                    msg:msg,
                                    buttons:Ext.MessageBox.OK,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.ERROR,
                                    closable:false
                                });
                            }
                        });
                    } else {
                        Ext.MessageBox.show({
                            title:'信息',
                            width:200,
                            msg:'请填写完成再提交!',
                            buttons:Ext.MessageBox.OK,
                            buttons:{'ok':'确定'},
                            icon:Ext.MessageBox.ERROR,
                            closable:false
                        });
                    }
                }
            },{
                text:'重置',
                width:50,
                handler:function(){
                    formPanel.getForm().reset();
                }
            }
        ]
    }).show();
}

function update_private(){
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    var formPanel = new Ext.form.FormPanel({
        frame:true,
        autoScroll:true,
        labelWidth:150,
        labelAlign:'right',
        defaultWidth:300,
        autoWidth:true,
        layout:'form',
        border:false,
        defaults : {
            width : 250,
            allowBlank : false,
            blankText : '该项不能为空！'
        },
        items:[
            new Ext.form.DisplayField({
                fieldLabel : 'IP地址',
                name : 'ip',
                regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                regexText:'请输入正确的网络地址',
                id:  'update.ip',
                value:recode.get('ip'),
                readOnly:true,
                allowBlank : false,
                emptyText:"IP地址",
                blankText : "不能为空，请正确填写"
            }),
            new Ext.form.DisplayField({
                fieldLabel : '端口',
                //regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                //regexText:'请输入子网掩码',
                value:recode.get('port'),
                name : 'port',
                readOnly:true,
                id:  'update.port',
                allowBlank : false,
                emptyText:"子网掩码",
                blankText : "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel : '虚拟IP地址',
                name : 'v_ip',
                regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                regexText:'请输入正确的网络地址',
                id:  'update.v_ip',
                value:recode.get('v_ip'),
                allowBlank : false,
                emptyText:"虚拟IP地址",
                blankText : "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel : '虚拟端口',
                //regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                //regexText:'请输入子网掩码',
                value:recode.get('v_port'),
                name : 'v_port',
                id:  'update.v_port',
                allowBlank : false,
                emptyText:"子网掩码",
                blankText : "不能为空，请正确填写"
            })
        ]
    });
    var win = new Ext.Window({
        title:"修改资源",
        width:500,
        layout:'fit',
        height:250,
        modal:true,
        items:formPanel,
        bbar:[
            '->',
            {
                id:'add_win.info',
                text:'确定',
                width:50,
                handler:function(){
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url :'../../HotBackupKeepAlivedAction_update_virtserver.action',
                            timeout: 20*60*1000,
                            params:{
                                ip:recode.get("ip"),
                                port:recode.get("port")
                            },
                            method :'POST',
                            waitTitle :'系统提示',
                            waitMsg :'正在连接...',
                            success : function(form, action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:250,
                                    msg:msg,
                                    buttons:Ext.MessageBox.OK,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.INFO,
                                    closable:false,
                                    fn:function(e){
                                        grid_panel.getStore().reload();
                                        win.close();
                                    }
                                });
                            },
                            failure : function(form, action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:250,
                                    msg:msg,
                                    buttons:Ext.MessageBox.OK,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.ERROR,
                                    closable:false
                                });
                            }
                        });
                    } else {
                        Ext.MessageBox.show({
                            title:'信息',
                            width:200,
                            msg:'请填写完成再提交!',
                            buttons:Ext.MessageBox.OK,
                            buttons:{'ok':'确定'},
                            icon:Ext.MessageBox.ERROR,
                            closable:false
                        });
                    }
                }
            },{
                text:'重置',
                width:50,
                handler:function(){
                    formPanel.getForm().reset();
                }
            }
        ]
    }).show();
}

function delete_private(){
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    if(!recode){
        Ext.Msg.alert("提示", "请选择一条记录!");
    }else{
        Ext.Msg.confirm("提示", "确定删除这条记录？", function(sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url : "../../HotBackupKeepAlivedAction_delete_virtserver.action",
                    timeout: 20*60*1000,
                    method : "POST",
                    params:{
                        ip:recode.get("ip"),
                        port:recode.get("port")
                    },
                    success : function(r,o){
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                        grid_panel.getStore().reload();
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

