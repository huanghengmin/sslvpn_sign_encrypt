/**
 * Created by IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 11-12-8
 * Time: 上午10:15
 * To change this template use File | Settings | File Templates.
 */
Ext.onReady(function(){
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';
    var ping_formPanel = new Ext.form.FormPanel({
        plain:true,
        labelWidth:80,
        border:false,
        loadMask : { msg : '正在加载数据，请稍后.....' },
        labelAlign:'right',
        buttonAlign:'left',
        defaults : {
            //width : 200,
            anchor:'85%',
            allowBlank : false,
            blankText : '该项不能为空！'
        },
        items:[{
            id:'ping.ip.info',
            fieldLabel:'IP地址',
            xtype:'textfield',
            name:'ip',
            regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
            regexText:'这个不是Ip',
            emptyText:'请输入Ip'
        },{
            id:'ping.count.info',
            fieldLabel:'连通次数',
            xtype:'textfield',
            name:'count',
            value:2,
            regex:/^([2-9]|[1-9][0-9]{1})$/,
            regexText:'这个数不是2~99',
            emptyText:'请输入2~99之间的数字'
        }],
        buttons:[
            new Ext.Toolbar.Spacer({width:100}),{
            id:'ping.MergeFiles.info',
            text:'测试',
            handler:function(){
                Ext.getCmp('ping.result.info').setValue('');
                var ip = Ext.getCmp('ping.ip.info').getValue();
                var count = Ext.getCmp('ping.count.info').getValue();
                if(ping_formPanel.form.isValid()) {
                	ping_formPanel.getForm().submit({
                		url:'../../InterfaceManagerAction_ping.action',
                		method:'POST',
                        waitTitle:'信息',
                        waitMsg:'正在PING '+ip+' '+count+'次,请稍后...',
                		success:function(form,action) {
                            var flag = action.result.msg;
                			Ext.getCmp('ping.result.info').setValue(flag);
                		}
                	});
                }
            }
        }]
    });
    var panel_ping = new Ext.Panel({
        plain:true,
        border:false,
        autoScroll:true,
        items:[{
            xtype:'fieldset',
            title:'Ping 连通测试',
            items:[ping_formPanel]
        }]
    });

    var telnet_formPanel = new Ext.form.FormPanel({
        plain:true,
        labelWidth:80,
        border:false,
        loadMask : { msg : '正在加载数据，请稍后.....' },
        labelAlign:'right',
        buttonAlign:'left',
        defaults : {
            //width : 200,
            anchor:'85%',
            allowBlank : false,
            blankText : '该项不能为空！'
        },
        items:[{
            id:'telnet.ip.info',
            fieldLabel:'IP地址',
            xtype:'textfield',
            name:'ip',
            regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
            regexText:'这个不是Ip',
            emptyText:'请输入Ip'
        },{
            id:'telnet.port.info',
            fieldLabel:'端口',
            xtype:'textfield',
            name:'port',
            regex:/^(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$/,
            regexText:'这个不是端口类型1~65536',
            emptyText:'请输入端口1~65536'
        }],
        buttons:[
            new Ext.Toolbar.Spacer({width:100}),{
            id:'telnet.MergeFiles.info',
            text:'测试',
            handler:function(){
                var ip = Ext.getCmp('telnet.ip.info').getValue();
                var port = Ext.getCmp('telnet.port.info').getValue();
                if(telnet_formPanel.form.isValid()){
                	telnet_formPanel.getForm().submit({
                		url:'../../InterfaceManagerAction_telnet.action',
                		method :'POST',
                        waitTitle:'信息',
                        waitMsg:'正在测试,请稍后...',
                		success:function(form,action){
                			var flag = action.result.msg;
                			Ext.MessageBox.show({
                				title:'信息',
                				msg:flag,
                				animEl:'telnet.MergeFiles.info',
                				width:200,
                				buttons:{'ok':'确定'},
                				icon:Ext.MessageBox.INFO,
                				closable:false
                			});
                		}
                	});
                }
            }
        }]
    });

    var panel_telnet = new Ext.Panel({
        plain:true,
        border:false,
        autoScroll:true,
        items:[{
            xtype:'fieldset',
            title:'Telnet 端口测试',
            items:[telnet_formPanel]
        }]
    });
    var port = new Ext.Viewport({
        layout:'fit',
        border:false,
        renderTo:Ext.getBody(),
        items:[{
            frame:true,
            layout:'column',
            anchor:'80%',
            items:[{
                columnWidth:.3,
                items:[{
                    xtype:'fieldset',
                    border:false,
                    title:'Ping 连通测试',
                    items:[ping_formPanel]
                },{
                    xtype:'fieldset',
                    border:false,
                    title:'Telnet 端口测试',
                    items:[telnet_formPanel]
                }]
            },{
                columnWidth:.1,
                items:[{
                    xtype:'displayfield',
                    width:20
                }]
            },{
                columnWidth:.6,
                items:[{
                    xtype:'displayfield',
                    id:'ping.result.info'
                }]
            }]
        }]
    });
});

