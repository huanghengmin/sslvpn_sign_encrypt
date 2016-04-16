Ext.onReady(function () {

    Ext.BLANK_IMAGE_URL = '../../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';


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

/*    var win = new Ext.Window({
        title: 'CRL自动更新',
        width: 500,
        layout: 'fit',
        height: 250,
        modal: true,
        items: initForm
    });
    win.show();*/


    var panel = new Ext.Panel({
        plain:true,
        width:600,
        border:false,
        items:[{
            id:'panel.info',
            xtype:'fieldset',
            title:'黑名单自动更新',
            width:530,
            items:[initForm]
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

})





