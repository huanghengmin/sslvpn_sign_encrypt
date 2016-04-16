Ext.onReady(function () {
    Ext.BLANK_IMAGE_URL = '../../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';
    var record = new Ext.data.Record.create([
        {name:'ip', mapping:'ip'},
        {name:'port', mapping:'port'},
        {name:'rpc_port', mapping:'rpc_port'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url:"../../../MonitorConfigAction_getMonitorInfo.action"
    });

    var reader = new Ext.data.JsonReader({
        totalProperty:"totalCount",
        root:"root"
    }, record);

    var store = new Ext.data.GroupingStore({
        id:"store.info",
        proxy:proxy,
        reader:reader
    });

    store.load();
    store.on('load',function(){
        var ip = store.getAt(0).get('ip');
        var port = store.getAt(0).get('port');
        var rpc_port = store.getAt(0).get('rpc_port');
        Ext.getCmp('com.hzih.sslvpn.monitor.ip').setValue(ip);
        Ext.getCmp('com.hzih.sslvpn.monitor.port').setValue(port);
        Ext.getCmp('com.hzih.sslvpn.jsonrpc.port').setValue(rpc_port);
    });

    var formPanel = new Ext.form.FormPanel({
        plain:true,
        width:500,
        labelAlign:'right',
        labelWidth:150,
        defaultType:'textfield',
        defaults:{
//            width:250,
            anchor:'100%',
            allowBlank:false,
            blankText:'该项不能为空!'
        },
        items:[
            {
                xtype:'fieldset',
                title:'监测服务器',
                //border:false,
                //collapsible:true,
                items:[
                    new Ext.form.TextField({
                    fieldLabel:'地址',
                    name:'ip',
                    regex:/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/,
                    regexText:'地址',
                    id:"com.hzih.sslvpn.monitor.ip",
                    allowBlank:false,
                    blankText:"地址"
                    }),
                    new Ext.form.TextField({
                        fieldLabel:'端口',
                        name:'port',
                        id:"com.hzih.sslvpn.monitor.port",
                        regex:/^(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$/,
                        regexText:'请输入正确的端口',
                        allowBlank:false,
                        blankText:"端口"
                    })
                ]
            },   {
                xtype:'fieldset',
                title:'远程调用接口',
                //collapsible:true,
                //border:false,
                items:[
                    new Ext.form.DisplayField({
                        fieldLabel:'协议',
                        value:'TCP'
                    }),
                    new Ext.form.TextField({
                        fieldLabel:'端口',
                        name:'rpc_port',
                        id:"com.hzih.sslvpn.jsonrpc.port",
                        regex:/^(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$/,
                        regexText:'端口',
                        allowBlank:false,
                        blankText:"端口"
                    })
                ]
            }
        ],
        buttons:[
            // '->',
            {
                id:'insert_win.info',
                text:'保存配置',
                handler:function () {
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url:"../../../MonitorConfigAction_saveMonitorInfo.action",
                            method:'POST',
                            waitTitle:'系统提示',
                            waitMsg:'正在连接...',
                            success:function () {
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:250,
                                    msg:'保存成功,点击返回页面!',
                                    buttons:Ext.MessageBox.OK,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.INFO,
                                    closable:false
                                });
                            },
                            failure:function () {
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:250,
                                    msg:'保存失败，请与管理员联系!',
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
            }
        ]
    });

    var panel = new Ext.Panel({
        plain:true,
        width:600,
        border:false,
        items:[{
            id:'panel.info',
            xtype:'fieldset',
            title:'配置',
            //border:false,
            width:530,
            items:[formPanel]
        }]
    });
    new Ext.Viewport({
        layout :'fit',
        renderTo:Ext.getBody(),
        autoScroll:true,
        items:[{
            frame:true,
            autoScroll:true,
            items:[panel]
        }]
    });

});


