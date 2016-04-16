/**
 * 安全配置
 */
Ext.onReady(function(){
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var form_1 = new Ext.form.FormPanel({
        plain : true,
        labelWidth:180,
        labelAlign:'left',
        border:false,
        defaults : {
            width : 200,
            allowBlank : false,
            blankText : '该项不能为空！'
        },
        items : [{
            id : 'ip1.info',
            xtype:'textfield',
            fieldLabel :'管理服务接口设定IP地址或主机名',
            name : 'ip'/*,
            regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
            regexText:'这个不是IP(例:1.1.1.1)'*/
        }]
    });
    var form_2 = new Ext.form.FormPanel({
        plain : true,
        labelWidth:180,
        labelAlign:'left',
        border:false,
        defaults : {
            width : 200,
            allowBlank : false,
            blankText : '该项不能为空！'
        },
        items : [{
            id : 'ip2.info',
            xtype:'textfield',
            fieldLabel :'集控采集数据接口设定IP地址',
            name : 'ip',
            regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
            regexText:'这个不是IP(例:1.1.1.1)'
        }]
    });
    var record_ips = new Ext.data.Record.create([
        {name:'ip1',mapping:'ip1'},
        {name:'ip2',mapping:'ip2'}
    ]);
    var proxy_ips = new Ext.data.HttpProxy({
        url:"../../ConfigManagerAction_readIps.action"
    });
    var reader_ips = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows"
    },record_ips);
    var store_ips = new Ext.data.GroupingStore({
        proxy : proxy_ips,
        reader : reader_ips
    });
    store_ips.load();
    store_ips.addListener('load',function(){
        var ip1 = store_ips.getAt(0).get('ip1');
        var ip2 = store_ips.getAt(0).get('ip2');
        Ext.getCmp('ip1.info').setValue(ip1);
        Ext.getCmp('ip2.info').setValue(ip2);
    });

    var record = new Ext.data.Record.create([
        {name:'ip',mapping:'ip'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url:"../../ConfigManagerAction_select.action"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows"
    },record);
    var store = new Ext.data.GroupingStore({
        proxy : proxy,
        reader : reader
    });
    var start = 0;
    var pageSize = 10;
    store.load({
        params:{
            start:start,limit:pageSize
        }
    });
    var ip_edit = new Ext.form.TextField({
        id:'ip_edit.info',
        regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
        regexText:'这个不是IP(例:1.1.1.1)'
    });
    var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
    var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var colM = new Ext.grid.ColumnModel([
        rowNumber,
		boxM,
        {header:"IP地址",dataIndex:"ip",align:'center',editor:ip_edit}
    ]);
    colM.defaultSortable = true;
    var page_toolbar = new Ext.PagingToolbar({
        pageSize : pageSize,
        store:store,
        displayInfo:true,
        displayMsg:"显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg:"没有记录",
        beforePageText:"当前页",
        afterPageText:"共{0}页"
    });
    var grid = new Ext.grid.EditorGridPanel({
        id:'grid.info',
        plain:true,
        renderTo:Ext.getBody(),
        animCollapse:true,
        height:300,
        autoWidth:true,
       // width:500,
        loadMask:{msg:'正在加载数据,请稍后...'},
        border:false,
        collapsible:false,
        heightIncrement:true,
        cm:colM,
        sm:boxM,
        store:store,
        clicksToEdit:1,
        autoExpandColumn:2,
        disableSelection:true,
        selModel:new Ext.grid.RowSelectionModel({singleSelect:true}),
        viewConfig:{
            forceFit:true,
            enableRowBody:true,
            getRowClass:function(record,rowIndex,p,store){
            return 'x-grid3-row-collapsed';
            }
        },
        tbar:[
            new Ext.Button({
                id:'btnAdd.info',
                text:'新增',
                iconCls:'add',
                handler:function(){
                    grid.stopEditing();
                    grid.getStore().insert(
                        0,
                        new record({
                            ip:'',
                            flag:2
                        })
                    );
                    grid.startEditing(0,0);
                }
            }),
            {xtype:"tbseparator"},
            new Ext.Button ({
                id : 'btnRemove.info',
                text : '删除',
                iconCls : 'delete',
                handler : function() {
                    deleteGridRow(grid,store);
                }
            }),
            {xtype:"tbseparator"},
            new Ext.Button({
                id:'btnSave.info',
                text:'保存',
                iconCls:'add',
                handler:function(){
                    insertGridFormWin(grid,store);
                }
            })
        ],
        view:new Ext.grid.GroupingView({
            forceFit:true,
            groupingTextTpl:'{text}({[values.rs.length]}条记录)'
        }),
        bbar:page_toolbar
    });
    var button_1 = new Ext.Button({
        text:'修改',
        id:'form_1.info',
        handler: function() {
            if(form_1.form.isValid()){
                form_1.getForm().submit({
                    url:'../../ConfigManagerAction_update8443.action',
                    method:'POST',
                    success: function(form,action) {
                        var flag = action.result.msg;
                        Ext.MessageBox.show({
                            title:'信息',
                            msg:flag,
                            width:230,
                            animEl:'form_1.info',
                            buttons:Ext.Msg.OK,
                            buttons:{'ok':'确定'},
                            icon:Ext.MessageBox.INFO,
                            closable:false
                        });
                    },
                    failure: function(){
                        Ext.MessageBox.show({
                            title:'信息',
                            msg:'修改失败!',
                            width:230,
                            animEl:'form_1.info',
                            buttons:Ext.Msg.OK,
                            buttons:{'ok':'确定'},
                            icon:Ext.MessageBox.ERROR,
                            closable:false
                        });
                    }
                });
            }else{
                Ext.Msg.show({
                    title:'信息',
                    msg:'请输入IP地址!',
                    width:260,
                    animEl:'form_1.info',
                    buttons:Ext.Msg.OK,
                    buttons:{'ok':'确定'},
                    icon:Ext.MessageBox.ERROR,
                    closable:false
                });
            }
        }
    });
    var button_2 = new Ext.Button({
        text:'修改',
        id:'form_2.info',
        handler: function() {
            if(form_2.form.isValid()){
                form_2.getForm().submit({
                    url:'../../ConfigManagerAction_update8000.action',
                    method:'POST',
                    success: function(form,action) {
                        var flag = action.result.msg;
                        Ext.MessageBox.show({
                            title:'信息',
                            msg:flag,
                            width:230,
                            animEl:'form_2.info',
                            buttons:Ext.Msg.OK,
                            buttons:{'ok':'确定'},
                            icon:Ext.MessageBox.INFO,
                            closable:false
                        });
                    },
                    failure: function(){
                        Ext.MessageBox.show({
                            title:'信息',
                            msg:'修改失败!',
                            width:230,
                            animEl:'form_2.info',
                            buttons:Ext.Msg.OK,
                            buttons:{'ok':'确定'},
                            icon:Ext.MessageBox.ERROR,
                            closable:false
                        });
                    }
                });
            }else{
                Ext.Msg.show({
                    title:'信息',
                    msg:'请输入IP地址!',
                    width:260,
                    animEl:'form_2.info',
                    buttons:Ext.Msg.OK,
                    buttons:{'ok':'确定'},
                    icon:Ext.MessageBox.ERROR,
                    closable:false
                });
            }
        }
    });
    var panel = new Ext.Panel({
        frame:true,
        border:false,
        width:520,
        autoScroll:true,
        layout:'form',
        items:[{
            xtype:'fieldset',
            title:'管理服务、集控采集数据接口设定IP地址（注：地址必须为CMS服务器网口地址）',
            items:[{
                layout:'column',
                width:500,
                items:[{items:[form_1],columnWidth:.8},{items:[button_1],columnWidth:.2}]
            },{
                layout:'column',
                width:500,
                items:[{items:[form_2],columnWidth:.8},{items:[button_2],columnWidth:.2}]
            }]
        },{
            xtype:'fieldset',
            title:'管理客户机地址（注：可以多个为客户机地址）',
            items:[grid]
        }]
    });
    var port = new Ext.Viewport({
        layout:'fit',
        renderTo:Ext.getBody(),
        items:[panel]
    });
});

function deleteGridRow(grid,store){
    var selModel = grid.getSelectionModel();
    var count = selModel.getCount();
    if(count==0){
    	Ext.MessageBox.show({
        	title:'信息',
        	width:200,
        	msg:'您没有勾选任何记录!',
        	animEl:'btnRemove.info',
        	buttons:Ext.MessageBox.OK,
        	buttons:{'ok':'确定'},
        	icon:Ext.MessageBox.QUESTION,
        	closable:false
		});

    }else if(count > 0){
        var ipArray = new Array();
        var record = grid.getSelectionModel().getSelections();
        for(var i = 0; i < record.length; i++){
            ipArray[i] = record[i].get('ip');
        }
        Ext.MessageBox.show({
            title:'信息',
            width:200,
            msg:'确定要删除所选的所有记录?',
            animEl:'btnRemove.info',
            buttons:Ext.MessageBox.YESNO,
            buttons:{'ok':'确定','no':'取消'},
            icon:Ext.MessageBox.WARNING,
            closable:false,
            fn:function(e){
         	   if(e=='ok'){
         		   Ext.Ajax.request({
         			   url : '../../ConfigManagerAction_delete.action',
         			   params :{ipArray : ipArray },
         			   success : function(){
         				   Ext.MessageBox.show({
         					   title:'信息',
         					   width:200,
         					   msg:'删除成功!',
         					   animEl:'btnRemove.info',
         					   buttons:Ext.MessageBox.OK,
         					   buttons:{'ok':'确定'},
         					   icon:Ext.MessageBox.INFO,
         					   closable:false,
         					   fn:function(e){
         						   if(e=='ok'){
         							   grid.render();
         							   store.reload();
         						   }
         					   }
         				   });
         			   },
         			   failure : function(){
         				   Ext.MessageBox.show({
         					   title:'信息',
         					   width:230,
         					   msg:'请与后台服务人员联系!',
         					   animEl:'btnRemove.info',
         					   buttons:Ext.MessageBox.OK,
         					   buttons:{'ok':'确定'},
         					   icon:Ext.MessageBox.ERROR,
         					   closable:false
         				   });
         			   }
         		   });
         	   }
            }
        });
    }
}

function insertGridFormWin(grid,store){
    var selModel = grid.getSelectionModel();
    var count = selModel.getCount();
    if(count==0){
        Ext.MessageBox.show({
        	title:'信息',
        	width:200,
        	msg:'您没有勾选任何记录!',
        	animEl:'btnSave.info',
        	buttons:Ext.MessageBox.OK,
        	buttons:{'ok':'确定'},
        	icon:Ext.MessageBox.QUESTION,
        	closable:false
		});

    }else if(count > 0){
        var ipArray = new Array();
        var record = grid.getSelectionModel().getSelections();
        for(var i = 0; i < record.length; i++){
            ipArray[i] = record[i].get('ip');
        }
        Ext.MessageBox.show({
           title:'信息',
           width:230,
           msg:'确定要保存所选的所有记录?',
           animEl:'btnSave.info',
           buttons:Ext.MessageBox.YESNO,
           buttons:{'ok':'确定','no':'取消'},
           icon:Ext.MessageBox.WARNING,
           closable:false,
           fn:function(e){
        	   if(e=='ok'){
        		   Ext.Ajax.request({
        			   url : '../../ConfigManagerAction_insert.action',
        			   params :{ipArray : ipArray },
        			   success : function(){
        				   Ext.MessageBox.show({
        					   title:'信息',
        					   width:200,
        					   msg:'保存成功!',
        					   animEl:'btnSave.info',
        					   buttons:Ext.MessageBox.OK,
        					   buttons:{'ok':'确定'},
        					   icon:Ext.MessageBox.INFO,
        					   closable:false,
        					   fn:function(e){
        						   if(e=='ok'){
        							   grid.render();
        							   store.reload();
        						   }
        					   }
        				   });
	                    },
	                   failure : function(){
	                       Ext.MessageBox.show({
	                        	title:'信息',
	                        	width:230,
	                        	msg:'请与后台服务人员联系!',
	                        	animEl:'btnSave.info',
	                        	buttons:Ext.MessageBox.OK,
	                        	buttons:{'ok':'确定'},
	                        	icon:Ext.MessageBox.ERROR,
	                        	closable:false
	                       });
	                   }
        		   });
        	   }
           }
        });
    }
}