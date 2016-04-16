Ext.onReady(function () {
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var record = new Ext.data.Record.create([
        {name: 'gprs', mapping: 'gprs'},
        {name: 'wifi', mapping: 'wifi'},
        {name: 'bluetooth', mapping: 'bluetooth'},
        {name: 'gps', mapping: 'gps'},
        {name: 'gps_interval', mapping: 'gps_interval'},
        //{name: 'terminal', mapping: 'terminal'},
        //{name: 'terminal_interval', mapping: 'terminal_interval'},
        {name: 'view', mapping: 'view'},
        {name: 'view_interval', mapping: 'view_interval'},
        {name: 'threeyards', mapping: 'threeyards'},
        {name: 'strategy_interval', mapping: 'strategy_interval'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url: "../../ClientStrategyAction_find.action"
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
        var gps = store.getAt(0).get('gps');
        var gps_interval = store.getAt(0).get('gps_interval');
        var view = store.getAt(0).get('view');
        var view_interval = store.getAt(0).get('view_interval');
        //var terminal = store.getAt(0).get('terminal');
        //var terminal_interval = store.getAt(0).get('terminal_interval');
        var threeyards = store.getAt(0).get('threeyards');
        var strategy_interval = store.getAt(0).get('strategy_interval');
        var gprs = store.getAt(0).get('gprs');
        var wifi = store.getAt(0).get('wifi');
        var bluetooth = store.getAt(0).get('bluetooth');
        Ext.getCmp('strategy.gprs').setValue(gprs);
        Ext.getCmp('strategy.wifi').setValue(wifi);
        Ext.getCmp('strategy.bluetooth').setValue(bluetooth);
        Ext.getCmp('strategy.gps').setValue(gps);
        Ext.getCmp('strategy.gps_interval').setValue(gps_interval);
        Ext.getCmp('strategy.view').setValue(view);
        Ext.getCmp('strategy.view_interval').setValue(view_interval);
        //Ext.getCmp('strategy.terminal').setValue(terminal);
        //Ext.getCmp('strategy.terminal_interval').setValue(terminal_interval);
        Ext.getCmp('strategy.threeyards').setValue(threeyards);
        Ext.getCmp('strategy.strategy_interval').setValue(strategy_interval);
    });


    var formPanel = new Ext.form.FormPanel({
        plain: true,
        width: 550,
        labelAlign: 'right',
        labelWidth: 120,
        defaultType: 'textfield',
        defaults: {
            anchor: '95%',
            allowBlank: false,
            blankText: '该项不能为空!'
        },
        items: [
            {
                xtype: 'checkboxgroup',
                fieldLabel: '终端绑定',
                columns: 2,
                allowBlank: true,
                items: [

                    {
                        boxLabel: '启用', id: 'strategy.threeyards',  inputValue: 1,name: 'threeyards'
                    },
                    {
                        xtype:'textfield', fieldLabel: '心跳(秒)', id: 'strategy.strategy_interval', name: 'strategy_interval'
                    }
                ]
            },
            {
                xtype: 'checkboxgroup',
                fieldLabel: 'GPS上报',
                columns: 2,
                allowBlank: true,
                items: [
                    {
                        boxLabel: '启用', id: 'strategy.gps', inputValue: 1, name: 'gps'
                    },
                    {
                       xtype:'textfield', fieldLabel: '间隔(秒)', id: 'strategy.gps_interval', name: 'gps_interval'
                    }
                ]
            },
            {
                xtype: 'checkboxgroup',
                fieldLabel: '截屏上报',
                columns: 2,
                allowBlank: true,
                items: [
                    {
                        boxLabel: '启用', id: 'strategy.view', inputValue: 1, name: 'view'
                    },
                    {
                        xtype:'textfield', fieldLabel: '间隔(秒)', id: 'strategy.view_interval', name: 'view_interval'
                    }
                ]
            },
           /* {
                xtype: 'checkboxgroup',
                fieldLabel: '终端上报',
                columns: 2,
                allowBlank: true,
                items: [
                    {
                        boxLabel: '启用', id: 'strategy.terminal', inputValue: 1, name: 'terminal'
                    },
                    {
                        xtype:'textfield', fieldLabel: '间隔(秒)', id: 'strategy.terminal_interval', name: 'terminal_interval'
                    }
                ]
            },*/
            {
                xtype: 'checkboxgroup',
                fieldLabel: '蓝牙',
                columns:1,
                allowBlank: true,
                items: [
                    {
                        boxLabel: '禁用', id: 'strategy.bluetooth', inputValue: 1, name: 'bluetooth'
                    }
                ]
            },
            {
                xtype: 'checkboxgroup',
                fieldLabel: 'WIFI',
                columns:1,
                allowBlank: true,
                items: [
                    {
                        boxLabel: '禁用', id: 'strategy.wifi', inputValue: 1, name: 'wifi'
                    }
                ]
            },
            {
                xtype: 'checkboxgroup',
                fieldLabel: 'GPRS',
                columns:1,
                allowBlank: true,
                items: [
                    {
                        boxLabel: '禁用', id: 'strategy.gprs', inputValue: 1, name: 'gprs'
                    }
                ]
            }
        ],
        buttons: [
            '->',
            {
                id: 'insert_win.info',
                text: '保存配置',
                handler: function () {
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url: "../../ClientStrategyAction_save.action",
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
            }
        ]
    });

    var panel = new Ext.Panel({
        plain: true,
        width: 800,
        border: false,
        items: [{
            id: 'panel.info',
            xtype: 'fieldset',
            title: '终端策略配置',
            width: 600,
            items: [formPanel]
        }]
    });
    new Ext.Viewport({
        layout: 'fit',
        renderTo: Ext.getBody(),
        autoScroll: true,
        items: [{
            frame: true,
            autoScroll: true,
            items: [panel]
        }]
    });

});


