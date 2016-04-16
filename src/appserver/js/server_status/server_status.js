Ext.onReady(function () {
    /*var tabs = new Ext.TabPanel({
     renderTo:Ext.getBody(),
     height:setHeight()
     });*/

    var record = new Ext.data.Record.create([
        {name: 'protocol', mapping: 'protocol'},
        {name: 'port', mapping: 'port'},
        {name: 'interface', mapping: 'interface'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url: "../../ServerStatusAction_findConfig.action"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty: "totalCount",
        root: "root"
    }, record);
    var store = new Ext.data.Store({
        proxy: proxy,
        reader: reader
    });
    store.load();
    store.on('load', function () {
        var protocol = store.getAt(0).get('protocol');
        var port = store.getAt(0).get('port');
        var interface = store.getAt(0).get('interface');
        Ext.getCmp('server.interface').setValue(interface);
        Ext.getCmp('server.listen').setValue(protocol + '/' + port);
    });

    /*var panel = new Ext.form.FormPanel({
     id:'about.info',
     labelWidth:180,
     labelAlign:'right',
     border:false,
     //        frame:true,
     loadMask:{ msg:'正在加载数据，请稍后.....' },
     autoScroll:true,
     items:[
     {
     id:'server.interface',
     xtype:'displayfield',
     fieldLabel:'服务器IP',
     value:'192.168.1.212'
     },
     */
    /*{
     id:'ser.info',
     xtype:'displayfield',
     fieldLabel : '对用户进行身份验证',
     value:'PAM'
     },{
     id:'os.info',
     xtype:'displayfield',
     fieldLabel : '接受VPN客户端连接的IP地址'  ,
     value:'eth14：192.168.1.212'
     },*/
    /*{
     xtype:'displayfield',
     id:'server.listen',
     fieldLabel:'服务端口',
     value:'tcp/1194'
     },{
     xtype:'displayfield',
     id:'server.sucrity',
     fieldLabel:'策略端口',
     value:'tcp/80'
     }, {
     fieldLabel:'运行状态',
     name:'runtime',
     id:'runtime',
     value:"<font color='orange'>服务未启动</font>",
     xtype:'displayfield'

     }*/
    /*,{
     xtype:'displayfield',
     fieldLabel : 'OSI层',
     value:'3（路由/ NAT）'
     },{
     xtype:'displayfield',
     fieldLabel : '客户访问私有子网使用',
     value:'NAT'
     },{
     xtype:'displayfield',
     fieldLabel : '节点',
     value:'VPN'
     }*/
    /*
     ]
     });*/

//--------------------------------------------------------服务状态---------------------------------------------------//
    /* var fwztform = new Ext.form.FormPanel({
     //        height:100,
     //        height:setHeight(),
     border:false,
     labelWidth:180,
     //frame:false,
     plain:true,
     defaultType:'displayfield',

     buttonAlign:'left',
     labelAlign:'right',
     //此处添加url，那么在getForm().sumit方法不需要在添加了url地址了
     //        baseParams:{create:true },
     defaults:{
     blankText:'不能为空!',
     msgTarget:'side'
     },
     items:[
     {
     fieldLabel:'服务状态',
     id:'serverstatus',
     name:'serverstatus',
     value:"[<font color='red'>未启动</font>]  [<a href='javascript:;' style='color:blue;' onclick='openServer()'>启动</a>]",
     xtype:'displayfield'

     }*/
    /*,
     {
     fieldLabel:'运行状态',
     name:'runtime',
     id:'runtime',
     value:"<font color='orange'>服务未启动</font>",
     xtype:'displayfield'

     }*/
    /*

     ]
     });*/

    /* tabs.add({
     title:'服务状态',
     items:[
     */
    /*{
     xtype:'fieldset',
     //title:'运行状态控制',
     //collapsible:true,
     items:[*/
    /*{
     xtype:'fieldset',
     title:'运行状态',
     //collapsible:true,
     items:[fwztform]
     },{
     xtype:'fieldset',
     title:'配置信息',
     //collapsible:true,
     items:[panel]
     }*/
    /*]*/
    /*
     //}
     ]
     });
     tabs.activate(0);*/


    var formPanel = new Ext.form.FormPanel({
        frame: true,
        autoWidth: true,
        //title: '基本参数',
        bodyPadding: 15,
        plain: true,
        labelAlign: 'right',
        buttonAlign: 'center',
        labelWidth: 120,
        defaultType: 'textfield',
        defaults: {
            //anchor: '80%',
            allowBlank: false,
            blankText: '该项不能为空!'
        },
        items: [
            {
                xtype: 'fieldset',
                title: '运行状态',
                border: false,
                //collapsible:true,
                items: [{
                    fieldLabel: '服务状态',
                    id: 'serverstatus',
                    name: 'serverstatus',
                    value: "[<font color='red'>未启动</font>]  [<a href='javascript:;' style='color:blue;' onclick='openServer()'>启动</a>]",
                    xtype: 'displayfield'

                }]
            },
            {
                xtype: 'fieldset',
                title: '监测状态',
                border: false,
                //collapsible:true,
                items: [{
                    fieldLabel: '监测状态',
                    id: 'devicestatus',
                    name: 'devicestatus',
                    //value: "[<font color='greenyellow'>加密卡状态:</font><font color='red'>正常</font>][<font color='greenyellow'> 软件状态:</font><font color='red'>正常</font>] [<font color='greenyellow'>配置文件状态:</font><font color='red'>正常</font>]",
                    value: "[<font color='greenyellow'> 软件状态:</font><font color='red'>正常</font>] [<font color='greenyellow'>配置文件状态:</font><font color='red'>正常</font>]",
                    xtype: 'displayfield'

                }]
            },
            {
                xtype: 'fieldset',
                title: '运行参数',
                border: false,
                //collapsible:true,
                items: [{
                    id: 'server.interface',
                    xtype: 'displayfield',
                    fieldLabel: '服务器IP',
                    value: '0.0.0.0'
                }, {
                    xtype: 'displayfield',
                    id: 'server.listen',
                    fieldLabel: '通迅协议',
                    value: 'udp'
                },
                    {
                        xtype: 'displayfield',
                        id: 'server.port',
                        fieldLabel: '服务端口',
                        value: '1194'
                    }, {
                        xtype: 'displayfield',
                        id: 'server.sucrity',
                        fieldLabel: '管理端口',
                        value: '443'
                    }, {
                        fieldLabel: '运行状态',
                        name: 'runtime',
                        id: 'runtime',
                        value: "<font color='orange'>服务未启动</font>",
                        xtype: 'displayfield'

                    }]
            }
        ]
    });

    new Ext.Viewport({
        layout: 'fit',
        renderTo: Ext.getBody(),
        autoScroll: true,
        items: [{
            frame: true,
            border: true,
            autoScroll: true,
            items: [formPanel]
        }]
    });

    /* var port = new Ext.Viewport({
     layout: 'form',
     renderTo: Ext.getBody(),
     items: [{
     xtype:'fieldset',
     title:'运行状态',
     //collapsible:true,
     items:[fwztform]
     },{
     xtype:'fieldset',
     title:'配置信息',
     //collapsible:true,
     items:[panel]
     }]
     });*/
    checkServerStatus();
    checkDeviceStatus();
});

function setHeight() {
    var h = document.body.clientHeight - 8;
    return h;
}

function setWidth() {
    var h = document.body.clientWidth - 300;
    return h;
}

function checkServerStatus() {
    Ext.Ajax.request({
        url: '../../ServerStatusAction_checkServerStatus.action',
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

function checkDeviceStatus() {
    Ext.Ajax.request({
        url: '../../ServerStatusAction_checkDeviceStatus.action',
        success: function (response, result) {
            var reText = Ext.util.JSON.decode(response.responseText);
                //Ext.getCmp("devicestatus").setValue("[<font color='greenyellow'>加密卡状态:</font><font color='red'>"+reText.pcie_status+"</font>][<font color='greenyellow'> 软件状态:</font><font color='red'>"+reText.soft_status+"</font>] [<font color='greenyellow'>配置文件状态:</font><font color='red'>"+reText.config_status+"</font>]")
                Ext.getCmp("devicestatus").setValue("[<font color='greenyellow'> 软件状态:</font><font color='red'>"+reText.soft_status+"</font>] [<font color='greenyellow'>配置文件状态:</font><font color='red'>"+reText.config_status+"</font>]")
        }
    });
}

function reloadServer() {
    var myMask = new Ext.LoadMask(Ext.getBody(), {
        msg: '正在重启,请稍后...',
        removeMask: true
    });
    myMask.show();
    Ext.Ajax.request({
        url: '../../ServerStatusAction_reloadServer.action',
        success: function (response, result) {
            myMask.hide();
            var reText = Ext.util.JSON.decode(response.responseText);
            if ("1" == reText.msg) {
                Ext.Msg.alert('提示', "重启VPN服务成功");
            } else {
                Ext.Msg.alert('提示', "重启VPN服务失败");
            }
            checkServerStatus();
        },
        failure: function (form, action) {
            myMask.hide();
            Ext.Msg.alert('提示', "重启VPN服务失败");
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
        url: '../../ServerStatusAction_closeServer.action',
        success: function (response, result) {
            myMask.hide();
            var reText = Ext.util.JSON.decode(response.responseText);
            if ("0" == reText.msg) {
                Ext.getCmp("serverstatus").setValue("[<font color='red'>未启动</font>] [<a href='javascript;' style='color:blue;' onclick='openServer()'>启动</a>]");
                Ext.Msg.alert('提示', "停止VPN服务成功");
            } else {
                Ext.Msg.alert('提示', "停止VPN服务失败");
            }
            checkServerStatus();
        },
        failure: function (form, action) {
            myMask.hide();
            Ext.Msg.alert('提示', "停止VPN服务失败");
        }
    });
}

function openServer() {
    Ext.MessageBox.show({
        title: '开启VPN服务....',
        msg: '正在执行系统检测,请稍后...',
        progressText: '检测中...',
        width: 300,
        progress: true,
        closable: false
    });
    Ext.MessageBox.updateProgress(1/4,"系统检测","正在执行系统检测,请稍后...");
    Ext.Ajax.request({
        url: '../../CheckAction_check.action',
        method: "POST",
        success: function (response, result) {
            var reText = Ext.util.JSON.decode(response.responseText);
            if(reText.flag){
                Ext.MessageBox.updateProgress(2/4,"系统检测",reText.msg);
                Ext.MessageBox.updateProgress(3/4,"开启VPN服务","正在开启VPN服务......");
                Ext.Ajax.request({
                    url: '../../ServerStatusAction_openServer.action',
                    success: function (response, result) {
                        Ext.MessageBox.hide();
                        var reText = Ext.util.JSON.decode(response.responseText);
                        if ("1" == reText.msg) {
                            Ext.getCmp("serverstatus").setValue("[<font color='green'>已启动</font>] [<font color='#ff4500'><a href='javascript:;' style='color:blue;' onclick='closeServer()'>停止</a></font>]");
                            Ext.Msg.alert('提示', "开启VPN服务成功");
                        } else {
                            Ext.Msg.alert('提示', "开启VPN服务失败");
                        }
                        checkServerStatus();
                    },
                    failure: function (form, action) {
                        Ext.MessageBox.hide();
                        Ext.Msg.alert('提示', "开启VPN服务失败");
                    }
                });
            }else{
                Ext.MessageBox.updateProgress(2/4,"系统检测",reText.msg);
                Ext.MessageBox.hide();
                Ext.MessageBox.show({
                    title: '信息',
                    width: 250,
                    msg: reText.msg,
                    buttons: {'ok': '确定'},
                    icon: Ext.MessageBox.INFO,
                    closable: false
                });
            }
        },
        failure: function (response, result) {
            var reText = Ext.util.JSON.decode(response.responseText);
            Ext.MessageBox.updateProgress(2/4,"系统检测",reText.msg);
            Ext.MessageBox.hide();
            Ext.MessageBox.show({
                title: '信息',
                width: 250,
                msg: reText.msg,
                buttons: {'ok': '确定'},
                icon: Ext.MessageBox.INFO,
                closable: false
            });
        }
    });
}


