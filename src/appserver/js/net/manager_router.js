Ext.onReady(function(){
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

/********************************************* -- internal_grid_panel start -- *******************************************************************************************************/
    var start = 0;			//分页--开始数
    var pageSize = 15;		//分页--每页数
    var record = new Ext.data.Record.create([
        {name:'interfaceName', 	mapping:'interfaceName'},
        {name:'destination', 	mapping:'destination'},
        {name:'subnetMask', 	mapping:'subnetMask'},
        {name:'gateway', 		mapping:'gateway'},
        {name:'flag',			mapping:'flag'}
    ]);
    var proxy = new Ext.data.HttpProxy({
       url:"../../InterfaceManagerAction_readRouter.action"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows"
    },record);
    var store = new Ext.data.GroupingStore({
        proxy : proxy,
        reader : reader
    });

    var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
//    var boxM = new Ext.grid.RadioboxSelectionModel();   //单选框
    var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var colM = new Ext.grid.ColumnModel([
        boxM,
        rowNumber,
        {header:"接口",		dataIndex:"interfaceName", align:'center',sortable:true,menuDisabled:true},
        {header:"目标地址",	dataIndex:"destination",   align:'center',sortable:true,menuDisabled:true},
        {header:"网关",		dataIndex:"gateway",	       align:'center',sortable:true,menuDisabled:true},
        {header:"子网掩码",	dataIndex:"subnetMask",	   align:'center',sortable:true,menuDisabled:true},
        {header:'操作标记',	dataIndex:'flag',		       align:'center',sortable:true,menuDisabled:true,		renderer:showURL_flag}
    ]);

    colM.defaultSortable = true;
    var page_toolbar = new Ext.PagingToolbar({
        pageSize : pageSize,
        store: store,
        displayInfo:true,
        displayMsg:"显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg:"没有记录",
        beforePageText:"当前页",
        afterPageText:"共{0}页"
    });
    var grid_panel = new Ext.grid.GridPanel({
        id:'grid.router.info',
        plain:true,
        animCollapse:true,
        height:300,
        loadMask:{msg:'正在加载数据，请稍后...'},
        border:false,
        collapsible:false,
        heightIncrement:true,
        cm:colM,
        sm:boxM,
        store:store,
        stripeRows:true,
        autoExpandColumn:2,
        disableSelection:true,
        bodyStyle:'width:100%',
        selModel:new Ext.grid.RowSelectionModel({singleSelect:true}),
        viewConfig:{
            forceFit:true,
            enableRowBody:true,
            getRowClass:function(record,rowIndex,p,store){
                return 'x-grid3-row-collapsed';
            }
        },
        view:new Ext.grid.GroupingView({
            forceFit:true,
            groupingTextTpl:'{text}({[values.rs.length]}条记录)'
        }),
        bbar:page_toolbar,
        tbar:[{
            id:'btnAdd.db.internal.info',
            text:'新增',
            iconCls:'add',
            handler:function(){
                insert_win(grid_panel,store);     //连接到 新增 面板
            }
        },{xtype:"tbseparator"},{
            id : 'btnRemove.db.internal.info',
            text : '删除',
			iconCls : 'remove',
			handler : function() {
				delete_row(grid_panel,store);         //删除 表格 的 一 行 或多行
			}
        }]
    });
    
/********************************************* -- grid_panel end   -- *******************************************************************************************************/
    var port = new Ext.Viewport({
        layout:'fit',
        renderTo:Ext.getBody(),
        items:[grid_panel]
    });
    store.load({
        params:{
            start:start,limit:pageSize
        }
    });

	function showURL_flag(value){
		return "<a href='javascript:;' onclick='detail_win();' style='color: green;'>详细</a>";
	}
});
var name_record = new Ext.data.Record.create([{name:'value',mapping:'value'},{name:'key',mapping:'key'}]);
var name_reader = new Ext.data.JsonReader({ totalProperty:'total',root:"rows"},name_record);


function insert_win(grid,store){
    var name_store = new Ext.data.Store({
        url:"../../InterfaceManagerAction_readInterfaceName.action?t=router",
        reader:name_reader
    });
    name_store.load();
    var gateway;
    var formPanel = new Ext.form.FormPanel({
        labelWidth:80,
        frame:true,
        border:false,
        labelAlign:'right',autoScroll:true,
        defaults : {
            width : 200,
            allowBlank:false,
            blankText:'该项不能为空！'
        },
        items:[{
            fieldLabel:'网卡显示名',
            hiddenName:'interfaceName',
            xtype:'combo',
            mode:'local',
            emptyText :'--请选择--',
            editable : false,
            typeAhead:true,
            forceSelection: true,
            triggerAction:'all',
            displayField:"key",valueField:"value",
            store:name_store,
            allowBlank:false,
            blankText:'该项不能为空！',
            listeners:{
                "select" : function(){
                    var name = Ext.getCmp("router.interfaceName").getValue();
                    Ext.Ajax.request({
                        url : '../../InterfaceManagerAction_checkInterface.action',             // 删除 连接 到后台
                        params :{name:name},
                        success : function(action){
                            var ip = Ext.decode(action.responseText);
                            ip = ip.msg;
                            if(ip.length > 0 && ip.indexOf("null") <= -1){
                                gateway = ip;

                                Ext.getCmp("router.gateway").setDisabled(false);
                                Ext.getCmp("router.gateway").setValue(ip);
                            }else{
                                Ext.getCmp("router.gateway").setDisabled(true);
                            }
                        },
                        failure : function(){
                            Ext.MessageBox.show({
                                title:'信息',
                                msg:'请与后台服务人员联系!',
                                animEl:'btnRemove.router.info',
                                buttons:{'ok':'确定'},
                                icon:Ext.MessageBox.ERROR,
                                closable:false
                            });
                        }
                    });
                }
            }
        },{
            fieldLabel:'目标地址',
            xtype:'textfield',
            name:'destination',
            regex:/^((((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))|default)$/,
            regexText:'这个不是IP(例:1.1.1.1)或者不是default',
            emptyText:'请输入IP(例:1.1.1.1)或者default'
        },{
            id:'router.gateway',
            fieldLabel:'网关',
            xtype:'textfield',
            name:'gateway',
            regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
            regexText:'这个不是IP(例:1.1.1.1)',
            emptyText:'请输入IP(例:1.1.1.1)' ,
            disabled:true,
            listeners:{
                "blur":function(){
                    if(!(gateway != null && Ext.getCmp("router.gateway").getValue().indexOf(gateway ) > -1)){
                        Ext.MessageBox.show({
                            title:'信息',
                            width:260,
                            msg:'该网关应与接口管理中的默认网关在同一段中',
                            buttons:{'ok':'确定'},
                            icon:Ext.MessageBox.ERROR,
                            closable:false
                        });
                    }
                }
            }
        },{
            fieldLabel:'子网掩码',
            xtype:'textfield',
            name:'subnetMask',
            regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
            regexText:'这个不是IP',
            emptyText:'请输入IP'
        }]
    });
    var win = new Ext.Window({
        title:'新增信息',
        width:350,
        height:200,
        modal:true,
        layout:'fit',
        items:[formPanel],
        bbar:[
            new Ext.Toolbar.Fill(),
            new Ext.Button ({
                id:'router.save.win.info',
                text : '保存',
                allowDepress : false,
                handler : function() {
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url :'../../InterfaceManagerAction_saveRouter.action',
                            method :'POST',
                            waitTitle :'系统提示',
			                waitMsg :'正在保存...',
			                success : function(form,action) {
                                var flag = action.result.msg;
			                    Ext.MessageBox.show({
			                        title:'信息',
                                    width:260,
                                    msg:flag,
                                    animEl:'router.save.win.info',
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.INFO,
                                    closable:false,
                                    fn:function(e){
                                        if(e=='ok'){
                                            grid.render();
                                            store.reload();
                                            win.close();
                                        }
                                    }
			                    });
			                },
			                failure : function() {
			                    Ext.MessageBox.show({
                                    title:'信息',
                                    width:260,
                                    msg:'保存失败,请与管理员联系!',
                                    animEl:'router.save.win.info',
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
                            animEl:'router.save.win.info',
                            buttons:{'ok':'确定'},
                            icon:Ext.MessageBox.ERROR,
                            closable:false
                        });
                    }
                }
            }),
            new Ext.Button ({
                allowDepress : false,
                text : '关闭',
                handler : function() {win.close();}
            })
        ]
    }).show();
}

function delete_row(grid,store){
    var selModel = grid.getSelectionModel();
    var count = selModel.getCount();
    if(count==0){
        Ext.MessageBox.show({
            title:'信息',
            msg:'<font color="green">您没有勾选任何记录!</font>',
            animEl:'btnRemove.proxy.external.info',
            buttons:{'ok':'确定'},
            icon:Ext.MessageBox.INFO,
            closable:false
        });
    }else if(count > 0){
        var interfaceNameArray = new Array();
        var destinationArray = new Array();
        var gatewayArray = new Array();
        var subnetMaskArray = new Array();
        var record = grid.getSelectionModel().getSelections();
        for(var i = 0; i < record.length; i++){
            interfaceNameArray[i] = record[i].get('interfaceName');
            destinationArray[i] = record[i].get('destination');
            gatewayArray[i] = record[i].get('gateway');
            subnetMaskArray[i] = record[i].get('subnetMask');
        }
        Ext.MessageBox.show({
            title:'信息',
            msg:'<font color="green">确定要删除所选记录？</font>',
            animEl:'btnRemove.router.info',
            width:200,
            buttons:{'ok':'确定','no':'取消'},
            icon:Ext.MessageBox.INFO,
            closable:false,
            fn:function(e){
                if(e == 'ok'){
                    Ext.Ajax.request({
                        url : '../../InterfaceManagerAction_deleteRouter.action',             // 删除 连接 到后台
                        params :{interfaceNameArray : interfaceNameArray,destinationArray:destinationArray,gatewayArray:gatewayArray,subnetMaskArray:subnetMaskArray},
                        success : function(action){
                            var json = Ext.decode(action.responseText);
                            Ext.MessageBox.show({
                                title:'信息',
                                msg:json.msg,
                                animEl:'btnRemove.router.info',
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
                                msg:'请与后台服务人员联系!',
                                animEl:'btnRemove.router.info',
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

function detail_win(){
    var grid = Ext.getCmp('grid.router.info');
    var selModel = grid.getSelectionModel();
    var formPanel;
    if(selModel.hasSelection()){
        var selections = selModel.getSelections();
        Ext.each(selections,function(item){
            formPanel = new Ext.form.FormPanel({
                labelWidth:100,
                frame:true,
                labelAlign:'right',autoScroll:true,
                defaultType:'displayfield',
                defaults : {
					//width : 150
                    anchor:'90%'
				},
                items:[{
                    fieldLabel:'网卡名',
                    value:item.data.interfaceName
                },{
                    fieldLabel:'目标IP',
                    value:item.data.destination
                },{
                    fieldLabel:'默认网关',
                    value:item.data.gateway
                },{
                    fieldLabel:'子网掩码',
                    value:item.data.subnetMask
                }]
            });
        });
    }
    var win = new Ext.Window({
        title:'详细信息',
        width:350,
        height:200,
        modal:true,
        layout:'fit',
        items:[formPanel],
        bbar:[
            new Ext.Toolbar.Fill(),
//            new Ext.Button ({
//                text : '保存',
//                allowDepress : false,
//                handler : function() {win.close();}
//            }),
            new Ext.Button ({
                allowDepress : false,
                text : '关闭',
                handler : function() {win.close();}
            })
        ]
    }).show();
}
