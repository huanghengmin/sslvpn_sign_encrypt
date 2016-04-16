Ext.onReady(function () {
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';

    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';




        var detailForm = new Ext.FormPanel({
            layout:"form",
            labelWidth: 100,
            labelAlign:"right",
            border:false,
            //frame:true,
            autoWidth:true,
            autoHeight:true,
            //defaultType: 'textfield',//column布局时不能设置该属性，否则不能正常显示
            items: [{//第一行
                layout:"column",
                items:[{
                    columnWidth:.7,//第一列
                    layout:"form",
                    items:[ {
                        fieldLabel:'设置时间',
                        id: 'time',
                        xtype: 'datetimefield',
                        format: 'Y-m-d H:i:s',
                        blankText:"请选择时间",
                        emptyText:'请选择时间'
                    }]
                },{
                    columnWidth:.3,//第二列
                    layout:"form",
                    items:[{
                        xtype:"button",
                        text:'设置',
                        handler: function () {
                            var v = Ext.getCmp('time').getValue();
                            if(!v){
                                Ext.MessageBox.alert("提示", "请先在设置时间控件中选择时间！");
                            }else{
                                var myMask = new Ext.LoadMask(Ext.getBody(), {
                                    msg: '正在设置时间,请稍后...',
                                    removeMask: true
                                });
                                myMask.show();
                                Ext.Ajax.request({
                                    url: "../../DateTimeAction_setDateTime.action",
                                    timeout: 20 * 60 * 1000,
                                    method: "POST",
                                    params: {time: v},
                                    success: function (r, o) {
                                        myMask.hide();
                                        var respText = Ext.util.JSON.decode(r.responseText);
                                        var msg = respText.msg;
                                        Ext.Msg.alert("提示", msg);
                                    },
                                    failure: function (r, o) {
                                        myMask.hide();
                                        var respText = Ext.util.JSON.decode(r.responseText);
                                        var msg = respText.msg;
                                        Ext.Msg.alert("提示", msg);
                                    }
                                });
                            }
                        }
                    }]
                }]},//第一行结束
                {//第二行
                    layout:"column",
                    items:[{
                        columnWidth:.7,//第一列
                        layout:"form",
                        items:[
                            new Ext.form.TextField({
                            fieldLabel:'同步时间',
                            id:"ntp.host",
                            value:"cn.pool.ntp.org",
                            allowBlank:true,
                            blankText:"同步时间服务器",
                            emptyText:'同步时间服务器'
                        })]
                    },{
                        columnWidth:.3,//第二列
                        layout:"form",
                        items:[{
                            xtype:"button",
                            text:'同步',
                            handler: function () {
                                var v = Ext.getCmp('ntp.host').getValue();
                                if(!v){
                                    Ext.MessageBox.alert("提示", "请先在同步时间控件中填写服务器地址！");
                                }else{
                                    var myMask = new Ext.LoadMask(Ext.getBody(), {
                                        msg: '正在同步时间,请稍后...',
                                        removeMask: true
                                    });
                                    myMask.show();
                                    Ext.Ajax.request({
                                        url: "../../DateTimeAction_setNtpServer.action",
                                        timeout: 20 * 60 * 1000,
                                        method: "POST",
                                        params: {host: v},
                                        success: function (r, o) {
                                            myMask.hide();
                                            var respText = Ext.util.JSON.decode(r.responseText);
                                            var msg = respText.msg;
                                            Ext.Msg.alert("提示", msg);
                                        },
                                        failure: function (r, o) {
                                            myMask.hide();
                                            var respText = Ext.util.JSON.decode(r.responseText);
                                            var msg = respText.msg;
                                            Ext.Msg.alert("提示", msg);
                                        }
                                    });
                                }
                            }
                        }]
                    }]}//第二行结束
            ]
        });

    var panel = new Ext.Panel({
        plain: true,
        width: 600,
        border: false,
        items: [{
            id: 'panel.info',
            xtype: 'fieldset',
            title: '时间配置',
            width: 530,
            items: [detailForm]
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






