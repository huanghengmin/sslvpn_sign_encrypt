Ext.onReady(function () {
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';
    var record = new Ext.data.Record.create([
        {name:'cpu', mapping:'cpu'} ,
        {name:'mem', mapping:'mem'} ,
        {name:'storage', mapping:'storage'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url:"../../SystemAlertAction_loadConfig.action"
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
        var cpu = store.getAt(0).get('cpu');
        var mem = store.getAt(0).get('mem');
        var storage = store.getAt(0).get('storage');
        Ext.getCmp('device.cpu').setValue(cpu);
        Ext.getCmp('device.mem').setValue(mem);
        Ext.getCmp('device.storage').setValue(storage);
    });


    var formPanel = new Ext.form.FormPanel({
        plain:true,
        width:500,
        labelAlign:'right',
        labelWidth:180,
        defaultType:'textfield',
        defaults:{
            width:250,
            allowBlank:false,
            blankText:'该项不能为空!'
        },
        items:[
            new Ext.form.NumberField({
                fieldLabel : 'CPU报警阀值(%)',
                name : 'cpu',
                id:"device.cpu",
                maxValue:100,  //最大值
                minValue:0,    //最小值
                allowBlank : false,
                blankText : "CPU报警阀值(%)"
            }),
            new Ext.form.NumberField({
                fieldLabel:'内存使用阀值(%)',
                name:'mem',
                id:"device.mem",
                maxValue:100,  //最大值
                minValue:0,    //最小值
                allowBlank:false,
                blankText:"内存使用阀值(%)"
            }),
            new Ext.form.NumberField({
                fieldLabel:'硬盘使用阀值(%)',
                name:'storage',
                id:"device.storage",
                maxValue:100,  //最大值
                minValue:0,    //最小值
                allowBlank:false,
                blankText:"硬盘使用阀值(%)"
            })
        ],
        buttons:[
            '->',
            {
                id:'insert_win.info',
                text:'保存配置',
                handler:function () {
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url:"../../SystemAlertAction_saveConfig.action",
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
            title:'设备报警配置',
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


