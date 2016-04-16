Ext.onReady(function(){
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget="side";
    var b=new Ext.FormPanel({
        plain:true,title:"",border:false,labelAlign:"right",
        labelWidth:280,width:"100%",waitMsgTarget:true,
        buttonAlign: 'left',
        renderTo:document.body,
        reader:
            new Ext.data.JsonReader(
                {},
                [{name:"id"},{name:"timeout"},
                    {name:"passwordRules"},
                    {name:"errorLimit"},
                    {name:"remoteDisabled"},
                    {name:'macDisabled'},
                    {name:'lockTime'}]
            ),
        items:[
            new Ext.form.FieldSet({
                title:"配置安全策略",autoHeight:true,defaultType:"textfield",
                items:[
                    {id:"id",name:"safePolicy.id",hidden:true,width:200},
                    {
                        fieldLabel:"会话超时时间设置（秒）",xtype:"textfield",
                        id:"timeout",name:"safePolicy.timeout",width:200,allowBlank:false,
                        regex:/^(1800|1[0-7]{3}|[1-9][0-9]{0,2}|[6-9][0-9])$/,
                        regexText:'这个不是60~1800之间的数字',
                        emptyText:'请输入60~1800'
                    },
                    {
                        fieldLabel:"密码校验限制（正则表达式）",xtype:"textarea",
                        id:"passwordRules",name:"safePolicy.passwordRules",width:200,allowBlank:false,
                        regex:/^(\^.{5,240}\$)$/,
                        regexText:'这个不是符合的正则表达式以^开始,以$结束,最多240位,密码有效位数8-100',
                        emptyText:'请输入正则表达式(例:^.{8,20}$),密码的最大位数为100'
                    },
                    {
                        fieldLabel:"账户锁定策略（用户登录最多失败次数）",xtype:"textfield",
                        id:"errorLimit",name:"safePolicy.errorLimit",width:200,allowBlank:false,
                        regex:/^(10|[1-9])$/,
                        regexText:'这个不是1~10之间的数字',
                        emptyText:'请输入1~10'
                    },
                    {
                        fieldLabel:"账户锁定策略（锁定时间:小时）",xtype:"textfield",
                        id:"lockTime",name:"safePolicy.lockTime",width:200,allowBlank:false,
                        regex:/^(2[0-4]|1[0-9]|[1-9])$/,
                        regexText:'这个不是1~24之间的数字',
                        emptyText:'请输入1~24'
                    },

//                    {xtype:"checkbox",boxLabel:"启用远程登录限制",id:"remoteDisabled",name:"remoteDisabled"},
                    {
                        plain:true,border:false,
                        xtype:'container',
                        layout:'column',
                        items:[{
                            width:255,
                            items:[{
                                width:180,
                                xtype:'displayfield',
                                value:''
                            }]
                        },{
                            columnWidth:.2,
                            items:[{
                                xtype:"checkbox",boxLabel:"启用MAC登录验证",id:"macDisabled",
                                name:"macDisabled",
                                listeners:{
                                    check:function(){
                                        var value = this.getValue();
                                        if(value){
                                            Ext.getCmp('check.info').show();
                                        } else {
                                            Ext.getCmp('check.info').hide();
                                        }
                                    }
                                }
                            }]
                        },{
                            id:'check.info',
                            columnWidth:.6,
                            hidden:true,
                            items:[{
                                xtype:'displayfield',
                                value:'<font color="green">选中后表示用户登陆时会校验MAC地址!</font>'
                            }]
                        }]

                    }

                ]
            })
        ]
    });
    if(b){
        b.getForm().load({
            url:"../../SafePolicyAction_select.action",
            success:function(c,d){},
            failure:function(c,d){
                Ext.Msg.alert("错误","加载数据出错！")
            }
        })
    }
    var c = new Ext.FormPanel({
        plain:true,
        border:false,
        buttonAlign: 'left',
        renderTo:document.body,
        buttons:[new Ext.Toolbar.Spacer({width:250}),{
            text:"保存",
            listeners:{
                click:function(){
                    b.getForm().submit({
                        clientValidation:true,
                        url:"../../SafePolicyAction_update.action",
                        waitMsg:'正在处理,请稍后...',
                        waitTitle :'系统提示',
                        success:function(form,action){
                            Ext.MessageBox.show({
                                title:'信息',
                                msg:action.result.msg,
                                width:300,
                                buttons:Ext.Msg.OK,
                                buttons:{'ok':'确定'},
                                icon:Ext.MessageBox.INFO,
                                closable:false
                            });
                        }
                    })
                }
            }
        }]
    });
    var a=new Ext.Viewport({
        layout:"fit",
        border:false,
        items:[{frame:true,items:[b,c]}]
    })
});