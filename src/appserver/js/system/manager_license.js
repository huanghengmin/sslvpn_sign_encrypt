/**
 * Created by IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 11-10-3
 * Time: 下午2:35
 * 证书更新
 */
Ext.onReady(function(){
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget ='side';

    var internal_record = new Ext.data.Record.create([
        {name:'uKeyId',mapping:'uKeyId'},      
        {name:'licenseId',mapping:'licenseId'},
        {name:'startDate',mapping:'startDate'},
        {name:'endDate',mapping:'endDate'},
        {name:'licenseDays',mapping:'licenseDays'}
    ]);
    var internal_proxy = new Ext.data.HttpProxy({
        url:"../../LicenseAction_readLocal.action"
    });
    var internal_reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows"
    },internal_record);
    var internal_store = new Ext.data.Store({
        proxy : internal_proxy,
        reader : internal_reader
    });
    internal_store.load();
	internal_store.on('load',function(){
		var uKeyId = internal_store.getAt(0).get('uKeyId');
		var licenseId = internal_store.getAt(0).get('licenseId');
		var startDate = internal_store.getAt(0).get('startDate');
        var endDate = internal_store.getAt(0).get('endDate');
        var licenseDays = internal_store.getAt(0).get('licenseDays');
        Ext.getCmp('internal.uKeyId.info').setValue(uKeyId);
        Ext.getCmp('internal.licenseId.info').setValue(licenseId);
        Ext.getCmp('internal.startDate.info').setValue(startDate);
        Ext.getCmp('internal.endDate.info').setValue(endDate);
        Ext.getCmp('internal.licenseDays.info').setValue(licenseDays);
        if(uKeyId!=licenseId){
        	Ext.MessageBox.show({
                title:'信息',
                msg:'uKey编号和许可证编号不同!',
                width:250,
                buttons:{'ok':'确定'},
                icon:Ext.MessageBox.INFO,
                closable:false
            });
        }
	});
    var internal_refresh = new Ext.Button({
        text:'更新',
        handler:function(){
            internal_refresh_win();
        }
    });
    var internal_formPanel = new Ext.form.FormPanel({
        plain:true,
        buttonAlign :'left',
        labelAlign:'right',
        labelWidth:100,
        items:[{
            id:'internal.uKeyId.info',
            xtype:'displayfield',
            fieldLabel:'UKey编号'
        },{
            id:'internal.licenseId.info',
            xtype:'displayfield',
            fieldLabel:'许可证编号'
        },{
            id:'internal.startDate.info',
            xtype:'displayfield',
            fieldLabel:'开始时间'
        },{
            id:'internal.endDate.info',
            xtype:'displayfield',
            fieldLabel:'结束时间'
        },{
            id:'internal.licenseDays.info',
            xtype:'displayfield',
            fieldLabel:'许可天数'
        }],
        buttons:[
            new Ext.Toolbar.Spacer({width:100}),
            internal_refresh
        ]
    });

    /*var external_record = new Ext.data.Record.create([
        {name:'uKeyId',mapping:'uKeyId'},
        {name:'licenseId',mapping:'licenseId'},
        {name:'startDate',mapping:'startDate'},
        {name:'endDate',mapping:'endDate'},
        {name:'licenseDays',mapping:'licenseDays'}
    ]);
    var external_proxy = new Ext.data.HttpProxy({
        url:"../../LicenseAction_readRemote.action"
    });
    var external_reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows"
    },external_record);
    var external_store = new Ext.data.Store({
        proxy : external_proxy,
        reader : external_reader
    });
    external_store.load();
	external_store.on('load',function(){
		var uKeyId = external_store.getAt(0).get('uKeyId');
		var licenseId = external_store.getAt(0).get('licenseId');
		var startDate = external_store.getAt(0).get('startDate');
        var endDate = external_store.getAt(0).get('endDate');
        var licenseDays = external_store.getAt(0).get('licenseDays');
        Ext.getCmp('external.uKeyId.info').setValue(uKeyId);
        Ext.getCmp('external.licenseId.info').setValue(licenseId);
        Ext.getCmp('external.startDate.info').setValue(startDate);
        Ext.getCmp('external.endDate.info').setValue(endDate);
        Ext.getCmp('external.licenseDays.info').setValue(licenseDays);
        if(uKeyId=='外网服务器不可访问远程服务器!'){
        	Ext.getCmp('external.refresh.info').disable();
        }
        if(uKeyId!=licenseId){
        	Ext.MessageBox.show({
                title:'信息',
                msg:'uKey编号和许可证编号不同!',
                width:250,
                buttons:Ext.Msg.OK,
                buttons:{'ok':'确定'},
                icon:Ext.MessageBox.INFO,
                closable:false
            });
        }
	});
    var external_refresh = new Ext.Button({
    	id:'external.refresh.info',
        text:'更新',
        handler:function(){
            external_refresh_win();
        }
    });
    var external_formPanel = new Ext.form.FormPanel({
        plain:true,
        buttonAlign :'left',
        labelAlign:'right',
        labelWidth:100,
        items:[{
            id:'external.uKeyId.info',
            xtype:'displayfield',
            fieldLabel:'UKey编号'
        },{
            id:'external.licenseId.info',
            xtype:'displayfield',
            fieldLabel:'许可证编号'
        },{
            id:'external.startDate.info',
            xtype:'displayfield',
            fieldLabel:'开始时间'
        },{
            id:'external.endDate.info',
            xtype:'displayfield',
            fieldLabel:'结束时间'
        },{
            id:'external.licenseDays.info',
            xtype:'displayfield',
            fieldLabel:'许可天数'
        }],
        buttons:[
            new Ext.Toolbar.Spacer({width:100}),
            external_refresh
        ]
    });*/

    var panel = new Ext.Panel({
        frame:true,
        border:false,
        width:500,
        autoScroll:true,
        items:[{
            xtype:'fieldset',
            title:'本地许可证',
            items:[internal_formPanel]
//        },{
//            xtype:'fieldset',
//            title:'远程许可证',
//            items:[external_formPanel]
        }]
    });
    new Ext.Viewport({
    	layout :'fit',
    	renderTo:Ext.getBody(),
    	items:[panel]
    });
});

function internal_refresh_win(){
    var internalForm = new Ext.form.FormPanel({
        plain:true,
        labelWidth:100,
        labelAlign:'left',
        fileUpload:true,
        border:false,
        defaults : {
            width : 200,
            allowBlank : false,
            blankText : '该项不能为空！'
        },
        items:[{
            id:'internalLicense.info',
            fieldLabel:"许可证上传",
            name:'uploadFile',
            xtype:'textfield',
            inputType: 'file',
            listeners:{
                render:function(){
                    Ext.get('internalLicense.info').on("change",function(){
                        var file = this.getValue();
                        Ext.MessageBox.show({
                            title:'信息',
                            msg:'<font color="green">确定要上传文件:'+file+'？</font>',
                            width:300,
                            buttons:{'ok':'确定','no':'取消'},
                            icon:Ext.MessageBox.WARNING,
                            closable:false,
                            fn:function(e){
                                if(e == 'ok'){
                                    if (internalForm.form.isValid()) {
                                        internalForm.getForm().submit({
                                            url :'../../LicenseAction_uploadLocal.action',
                                            method :'POST',
                                            waitTitle :'系统提示',
                                            waitMsg :'正在上传,请稍后...',
                                            success : function(form,action) {
                                                var msg = action.result.msg;
                                                Ext.MessageBox.show({
                                                    title:'信息',
                                                    width:250,
                                                    msg:msg,
//                                                    animEl:'insert.win.info',
                                                    buttons:{'ok':'确定','no':'取消'},
                                                    icon:Ext.MessageBox.INFO,
                                                    closable:false,
                                                    fn:function(e){
                                                        if(e=='ok'){
                                                            win.close();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    } else {
                                        Ext.MessageBox.show({
                                            title:'信息',
                                            width:200,
                                            msg:'请选择需要上传的许可证!',
//                                            animEl:'insert.win.info',
                                            buttons:{'ok':'确定'},
                                            icon:Ext.MessageBox.ERROR,
                                            closable:false
                                        });
                                    }
                                }
                            }
                        });
                    });
                }
            }
        }]
    });
    var win = new Ext.Window({
        title:'本地许可证更新',
        modal:true,
        width:400,
        height:300,
        layout:'fit',
        items:[{
        	frame:true,
        	items:[{height:20},{
	        	height:100,
	        	items:[{
	        		xtype:'fieldset',
	        		title:'说明',
	        		html:"<font color='green'>上传的文件名必须是 “license”!</font>"
	        	}]
	        },internalForm
	        ]
        }],
        bbar:['->',{
/*            text:'上传',
            id:'uploading.internal.info',
            handler: function() {
                if(internalForm.form.isValid()){
                    internalForm.getForm().submit({
                        url:'../../LicenseAction_uploadLocal.action',
                        method:'POST',
                        success: function(form,action) {
                            var flag = action.result.msg;
//                            Ext.MessageBox.hide();
//                            alert(flag);
                            Ext.MessageBox.show({
                                title:'信息',
                                msg:flag,
                                width:210,
                                animEl:'uploading.internal.info',
                                buttons:Ext.Msg.OK,
                                buttons:{'ok':'确定'},
                                icon:Ext.MessageBox.INFO,
                                closable:false,
                                fn:function(e){
                                	if(e=='ok'){
                                		win.close();
                                	}
                                }
                            });
//                            Ext.getCmp('internalLicense.info').setValue('');
                        }
                    });
                }else{
                    Ext.Msg.show({
                        title:'信息',
                        msg:'请选择需要上传的许可证！',
                        width:230,
                        animEl:'uploading.internal.info',
                        buttons:Ext.Msg.OK,
                        buttons:{'ok':'确定'},
                        icon:Ext.MessageBox.ERROR,
                        closable:false
                    });
                }
            }
        },{*/
            text:'关闭',
            handler:function(){
                win.close();
            }
        }]
    }).show();
}

/*
function external_refresh_win(){
    var externalForm = new Ext.form.FormPanel({
        plain:true,
        labelWidth:100,
        labelAlign:'left',
        fileUpload:true,
        border:false,
        defaults : {
            width : 200,
            allowBlank : false,
            blankText : '该项不能为空！'
        },
        items:[{
            id:'externalLicense.info',
            fieldLabel:"许可证上传",
            name:'uploadFile',
            xtype:'textfield',
            inputType: 'file'
        }]
    });
    var win = new Ext.Window({
        title:'远程许可证更新',
        modal:true,
        width:400,
        height:300,
        layout:'fit',
        items:[{
        	frame:true,
        	items:[{height:20},{
	        	height:100,
	        	items:[{
	        		xtype:'fieldset',
	        		title:'说明',
	        		html:"<font color='green'>上传的文件名必须是 “license”!</font>"
	        	}]
	        },externalForm
	        ]
        }],
        bbar:['->',{
            text:'上传',
            id:'uploading.external.info',
            handler: function() {
                if(externalForm.form.isValid()){
//                	Ext.MessageBox.show({
//                        title: '请等待',
//                        msg: '上传中...',
//                        progressText: '',
//                        width:300,
//                        progress:true,
//                        closable:true,
//                        animEl:'uploading.external.info'
//                    });
                    externalForm.getForm().submit({
                        url:'../../LicenseAction_uploadRemote.action',
                        method:'POST',
                        success: function(form,action) {
                            var flag = action.result.msg;
                            Ext.Msg.show({
                                title:'信息',
                                msg:flag,
                                width:230,
                                animEl:'uploading.external.info',
                                buttons:Ext.Msg.OK,
                                buttons:{'ok':'确定'},
                                icon:Ext.MessageBox.INFO,
                                closable:false,
                                fn:function(e){
                                	if(e=='ok'){
                                		win.close();
                                	}
                                }
                            });
//                            Ext.getCmp('externalLicense.info').setValue('');
                        },
                        failure: function(){
                            Ext.Msg.show({
                                title:'信息',
                                msg:'许可证上传失败！',
                                width:230,
                                animEl:'uploading.external.info',
                                buttons:Ext.Msg.OK,
                                buttons:{'ok':'确定'},
                                icon:Ext.MessageBox.ERROR,
                                closable:false
                            });
                            Ext.getCmp('externalLicense.info').setValue('');
                        }
                    });
                }else{
                    Ext.Msg.show({
                        title:'信息',
                        msg:'请选择需要上传的许可证！',
                        width:230,
                        animEl:'uploading.external.info',
                        buttons:Ext.Msg.OK,
                        buttons:{'ok':'确定'},
                        icon:Ext.MessageBox.ERROR,
                        closable:false
                    });
                }
            }
        },{
            text:'关闭',
            handler:function(){
                win.close();
            }
        }]
    }).show();
}*/
