Ext.onReady(function(){
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';
    var start = 0;			//分页--开始数
    var pageSize = 15;		//分页--每页数
    var record = new Ext.data.Record.create([
        {name:'interfaceName',	mapping:'interfaceName'},
        {name:'encap',	        mapping:'encap'},
        {name:'ip', 			mapping:'ip'},
        {name:'mac', 			mapping:'mac'},
        {name:'subnetMask', 	mapping:'subnetMask'},
        {name:'broadCast',      mapping:'broadCast'},
        {name:'gateway', 		mapping:'gateway'},
        {name:'dns', 			mapping:'dns'},
        {name:'isUp', 			mapping:'isUp'},
        {name:'flag',			mapping:'flag'}
    ]);
    var proxy = new Ext.data.HttpProxy({
       url:"../../InterfaceManagerAction_readInterface.action"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows"
    },record);
    var store = new Ext.data.GroupingStore({
        proxy : proxy,
        reader : reader
    });

    //var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
//    var boxM = new Ext.grid.RadioboxSelectionModel();   //单选框
    var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var colM = new Ext.grid.ColumnModel([
        //boxM,
        rowNumber,
        {header:"网卡显示名",	    dataIndex:"interfaceName",	align:'center',sortable:true,menuDisabled:true},
        {header:"启动时是否启用",	dataIndex:"isUp",			    align:'center',sortable:true,menuDisabled:true,      renderer:showURL_isUp},
        {header:"网卡类型",		dataIndex:"encap",	        align:'center',sortable:true,menuDisabled:true},
        {header:"IP",			    dataIndex:"ip",			    align:'center',sortable:true,menuDisabled:true},
        {header:"MAC",			    dataIndex:"mac",			    align:'center',sortable:true,menuDisabled:true},
        {header:"子网掩码",		dataIndex:"subnetMask",		align:'center',sortable:true,menuDisabled:false},
        {header:"广播地址",		dataIndex:"broadCast",		align:'center',sortable:true,menuDisabled:false},
        {header:"默认网关",		dataIndex:"gateway",		    align:'center',sortable:true,menuDisabled:false},
        {header:"DNS服务器",		dataIndex:"dns",			    align:'center',sortable:true,menuDisabled:false,     renderer:showURL_dns},
        {header:'操作标记',		dataIndex:'flag',			    align:'center',sortable:true,menuDisabled:true,		renderer:showURL_flag,        width:250}

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
        id:'grid.interface.info',
        animCollapse:true,
        height:setHeight(),
        width:setWidth(),
        loadMask:{msg:'正在加载数据，请稍后...'},
        border:false,
        collapsible:false,
        cm:colM,
        //sm:boxM,
        store:store,
        stripeRows:true,
        autoExpandColumn:2,
        disableSelection:true,
        bodyStyle:'width:100%',
        enableDragDrop: true,
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
                insert(grid_panel,store);     //新增接口
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
		if(value == 'Ethernet_isUp'){
			return "<a href='javascript:;' onclick='detail_win();' style='color: green;'>详细</a>&nbsp;&nbsp;&nbsp;&nbsp;" +
                "<a href='javascript:;' onclick='insert_win();' style='color: green;'>新增虚拟接口</a>&nbsp;&nbsp;&nbsp;&nbsp;" +
                "<a href='javascript:;' onclick='update_win();' style='color: green;'>修改</a>&nbsp;&nbsp;&nbsp;&nbsp;注销";
		}else if(value == 'Ethernet(虚拟)_isUp'){
			return "<a href='javascript:;' onclick='detail_win();' style='color: green;'>详细</a>&nbsp;&nbsp;&nbsp;&nbsp;" +
                "<a id='btnRemove.interface.info' href='javascript:;' onclick='delete_row();' style='color: green;'>删除虚拟接口</a>&nbsp;&nbsp;&nbsp;&nbsp;" +
                "<a href='javascript:;' onclick='update_xn_win();' style='color: green;'>修改</a>&nbsp;&nbsp;&nbsp;&nbsp;" +
                "<a href='javascript:;' onclick='ifDown();' style='color: green;'>注销</a>";
		}else if(value == 'Ethernet_isDown'){
			return "<a href='javascript:;' onclick='detail_win();' style='color: green;'>详细</a>&nbsp;&nbsp;&nbsp;&nbsp;" +
                "<a href='javascript:;' onclick='insert_win();' style='color: green;'>新增虚拟接口</a>&nbsp;&nbsp;&nbsp;&nbsp;" +
                "<a href='javascript:;' onclick='update_win();'style='color: green;'>修改</a>&nbsp;&nbsp;&nbsp;&nbsp;" +
                "<a href='javascript:;' onclick='ifUp();' style='color: green;'>激活</a>";
		}else if(value == 'Ethernet(虚拟)_isDown'){
			return "<a href='javascript:;' onclick='detail_win();' style='color: green;'>详细</a>&nbsp;&nbsp;&nbsp;&nbsp;" +
                "<a id='btnRemove.interface.info' href='javascript:;' onclick='delete_row();' style='color: green;'>删除虚拟接口</a>&nbsp;&nbsp;&nbsp;&nbsp;" +
                "<a href='javascript:;' onclick='update_xn_win();' style='color: green;'>修改</a>&nbsp;&nbsp;&nbsp;&nbsp;" +
                "<a href='javascript:;' onclick='ifUp();' style='color: green;'>激活</a>";
		}
	}
    function showURL_dns(value){
        return "<a href='javascript:;' onclick='dns_win()' style='color: green;'>DNS管理</a>";
    }
    function showURL_isUp(value){
        return Boolean(value)?"是":"否";
    }
});

function setHeight(){
	var h = document.body.clientHeight-8;
	return h;
}

function setWidth(){
    return document.body.clientWidth-8;
}

function ifUp(){
	var grid = Ext.getCmp('grid.interface.info');
    var store = Ext.getCmp('grid.interface.info').getStore();
    var selModel = grid.getSelectionModel();
    if(selModel.hasSelection()){
        var selections = selModel.getSelections();
        Ext.each(selections,function(item){
        	var interfaceName = item.data.interfaceName;
        	Ext.MessageBox.show({
            	title:'信息',
            	msg:'<font color="green">确定要激活所选网络接口？</font>',
//            	animEl:'ifUp.interface.info',
            	width:200,
            	buttons:{'ok':'确定','no':'取消'},
            	icon:Ext.MessageBox.INFO,
            	closable:false,
            	fn:function(e){
            		if(e == 'ok'){
            			Ext.Ajax.request({
            				url : '../../InterfaceManagerAction_ifInterfaceUp.action',
            				params :{interfaceName : interfaceName},
            				success : function(action){
            					var json = Ext.decode(action.responseText);
            					Ext.MessageBox.show({
            						title:'信息',
            						msg:json.msg,
//            						animEl:'ifUp.interface.info',
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
//            						animEl:'ifUp.interface.info',
            						buttons:{'ok':'确定'},
            						icon:Ext.MessageBox.ERROR,
            						closable:false
            					});
            				}
            			});
            		}
            	}
            });
        });
    }
}

function ifDown(){
	var grid = Ext.getCmp('grid.interface.info');
    var store = Ext.getCmp('grid.interface.info').getStore();
    var selModel = grid.getSelectionModel();
    if(selModel.hasSelection()){
        var selections = selModel.getSelections();
        Ext.each(selections,function(item){
        	var interfaceName = item.data.interfaceName;
        	Ext.MessageBox.show({
            	title:'信息',
            	msg:'<font color="green">确定要注销所选网络接口？</font>',
//            	animEl:'ifUp.interface.info',
            	width:200,
            	buttons:Ext.Msg.YESNO,
            	buttons:{'ok':'确定','no':'取消'},
            	icon:Ext.MessageBox.INFO,
            	closable:false,
            	fn:function(e){
            		if(e == 'ok'){
            			Ext.Ajax.request({
            				url : '../../InterfaceManagerAction_ifInterfaceDown.action',
            				params :{interfaceName : interfaceName},
            				success : function(action){
            					var json = Ext.decode(action.responseText);
            					Ext.MessageBox.show({
            						title:'信息',
            						msg:json.msg,
//            						animEl:'ifUp.interface.info',
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
            						msg:'请与后台服务人员联系!',
//            						animEl:'ifUp.interface.info',
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
        });
    }
}

//新增接口
function insert(){
    var record = new Ext.data.Record.create([{name:'value',mapping:'value'}, {name:'key',mapping:'key'}]);
    var reader = new Ext.data.JsonReader({ totalProperty:'total',root:"rows"},record);
    var storeName = new Ext.data.Store({
        url:"../../InterfaceManagerAction_readAddInterfaceName.action",
        reader:reader
    });
    storeName.load();
    var grid = Ext.getCmp('grid.interface.info');
    var store = Ext.getCmp('grid.interface.info').getStore();
    var formPanel;
    formPanel = new Ext.form.FormPanel({
        labelWidth:100,
        frame:true,
        border:false,
        labelAlign:'right',autoScroll:true,
        defaults : {
            //width : 150,
            anchor:"90%",
            allowBlank:false,
            blankText:'该项不能为空！'
        },
        items:[{
            id:'interfaceName.insert.info',
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
            store:storeName
        },
            /*{
             id:'interfaceName.insert.hidden.info',
             name:'netInfo.interfaceName',
             xtype:'hidden'
             },*/
            {
                fieldLabel:'网卡类型',
                xtype:'displayfield',
                value:'Ethernet'
            },{
                fieldLabel:"启动时是否启用",
                layout:'column',
                defaultType: 'radio',
                items: [
                    { /*width:50,*/ boxLabel: '是', name: 'isUp', inputValue: true,  checked: true },
                    { /*width:50,*/ boxLabel: '否', name: 'isUp', inputValue: false }
                ]
            },{
                fieldLabel:'IP',
                xtype:'textfield',
                name:'ip',
                regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                regexText:'这个不是IP',
                emptyText:'请输入IP'
            },
            /*{
             fieldLabel:'MAC',
             xtype:'textfield',
             name:'netInfo.mac',
             regex:/([0-9a-fA-F]{2})(([/\s:-][0-9a-fA-F]{2}){5})/,
             regexText:'这个不是mac地址',
             emptyText:'请输入网卡mac'
             },*/
            {
                fieldLabel:'子网掩码',
                xtype:'textfield',
                name:'subnetMask',
//            value:item.data.subnetMask,
                regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                regexText:'这个不是IP',
                emptyText:'请输入IP'
            },{
                fieldLabel:'广播地址',
                xtype:'textfield',
                name:'broadCast',
//            value:item.data.broadCast,
                regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                regexText:'这个不是IP',
                emptyText:'请输入IP'
            }]
    });
    var win = new Ext.Window({
        title:'新增接口信息',
        width:400,
        height:250,
        modal:true,
        layout:'fit',
        items:[formPanel],
        listeners :{
            show:function(){
//                setName(interfaceName);
            }
        },
        bbar:[
            new Ext.Toolbar.Fill(),
            new Ext.Button ({
                id:'interface.save.info',
                text : '保存',
                allowDepress : false,
                handler : function() {
                    if (formPanel.form.isValid()) {
                        Ext.MessageBox.show({
                            title:'信息',
                            width:200,
                            msg:'是否确定要保存?',
                            animEl:'interface.save.info',
                            buttons:Ext.MessageBox.YESNO,
                            buttons:{'ok':'确定','no':'取消'},
                            icon:Ext.MessageBox.QUESTION,
                            closable:false,
                            fn:function(e){
                                if(e=='ok'){
                                    formPanel.getForm().submit({
                                        url :'../../InterfaceManagerAction_saveInterface.action',
                                        method :'POST',
                                        waitTitle :'系统提示',
                                        waitMsg :'正在保存...',
                                        success : function(form,action) {
                                            var flag = action.result.msg;
                                            Ext.MessageBox.show({
                                                title:'信息',
                                                width:200,
                                                msg:flag,
                                                animEl:'interface.save.win.info',
                                                buttons:Ext.MessageBox.OK,
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
                                                width:200,
                                                msg:'新增失败，请与管理员联系!',
                                                animEl:'interface.save.info',
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

                    } else {
                        Ext.MessageBox.show({
                            title:'信息',
                            width:200,
                            msg:'请填写完成再提交!',
                            animEl:'interface.save.info',
                            buttons:Ext.MessageBox.OK,
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

function insert_win(){
	var grid = Ext.getCmp('grid.interface.info');
    var store = Ext.getCmp('grid.interface.info').getStore();
    var selModel = grid.getSelectionModel();
    var formPanel;
    var interfaceName;
    if(selModel.hasSelection()){
        var selections = selModel.getSelections();
        Ext.each(selections,function(item){
            interfaceName = item.data.interfaceName;
        	formPanel = new Ext.form.FormPanel({
                labelWidth:100,
                frame:true,
                border:false,
                labelAlign:'right',autoScroll:true,
                defaults : {
                    //width : 150,
                    anchor:'90%',
                    allowBlank:false,
                    blankText:'该项不能为空！'
                },
                items:[{
                	id:'interfaceName.insert.info',
                    fieldLabel:'网卡显示名',
                    xtype:'displayfield'
                },{
                	id:'interfaceName.insert.hidden.info',
                	name:'interfaceName',
                	xtype:'hidden'
                },{
                    fieldLabel:'网卡类型',
                    xtype:'displayfield',
                    value:'Ethernet(虚拟)'
                },{
                    fieldLabel:"启动时是否启用",
                    layout:'column',
                    defaultType: 'radio',
                    items: [
                        { width:50, boxLabel: '是', name: 'isUp', inputValue: true,  checked: true },
                        { width:50, boxLabel: '否', name: 'isUp', inputValue: false }
                    ]
                },{
                    fieldLabel:'IP',
                    xtype:'textfield',
                    name:'ip',
                    regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                    regexText:'这个不是IP',
                    emptyText:'请输入IP'
                },{
                    fieldLabel:'MAC',
                    xtype:'displayfield',
                    value:item.data.mac
                },{
                    fieldLabel:'子网掩码',
                    xtype:'textfield',
                    name:'subnetMask',
                    value:item.data.subnetMask,
                    regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                    regexText:'这个不是IP',
                    emptyText:'请输入IP'
                },{
                    fieldLabel:'广播地址',
                    xtype:'textfield',
                    name:'broadCast',
                    value:item.data.broadCast,
                    regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                    regexText:'这个不是IP',
                    emptyText:'请输入IP'
                }]
            });
        });
    }
    var win = new Ext.Window({
        title:'新增信息',
        width:400,
        height:280,
        modal:true,
        layout:'fit',
        items:[formPanel],
        listeners :{
        	show:function(){
        		setName(interfaceName);
        	}
        },
        bbar:[
            new Ext.Toolbar.Fill(),
            new Ext.Button ({
                id:'interface.save.win.info',
                text : '保存',
                allowDepress : false,
                handler : function() {
                    if (formPanel.form.isValid()) {
                        Ext.MessageBox.show({
                    		title:'信息',
                            width:200,
                            msg:'是否确定要保存?',
                            animEl:'interface.save.win.info',
                            buttons:Ext.MessageBox.YESNO,
                            buttons:{'ok':'确定','no':'取消'},
                            icon:Ext.MessageBox.QUESTION,
                            closable:false,
                            fn:function(e){
                            	if(e=='ok'){
                            		formPanel.getForm().submit({
			                            url :'../../InterfaceManagerAction_saveInterface.action',
			                            method :'POST',
			                            waitTitle :'系统提示',
			                            waitMsg :'正在保存...',
			                            success : function(form,action) {
                                            var flag = action.result.msg;
			                                Ext.MessageBox.show({
			                                    title:'信息',
			                                    width:200,
			                                    msg:flag,
			                                    animEl:'interface.save.win.info',
			                                    buttons:Ext.MessageBox.OK,
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
			                                    width:200,
			                                    msg:'修改失败，请与管理员联系!',
			                                    animEl:'interface.save.win.info',
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

                    } else {
                        Ext.MessageBox.show({
                            title:'信息',
                            width:200,
                            msg:'请填写完成再提交!',
                            animEl:'interface.save.win.info',
                            buttons:Ext.MessageBox.OK,
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

function setName(interfaceName){
	var name_record = new Ext.data.Record.create([{name:'key',mapping:'key'},{name:'value',mapping:'value'}]);
	var name_reader = new Ext.data.JsonReader({ totalProperty:'total',root:"rows"},name_record);
	var name_store = new Ext.data.Store({
		id:'name_store.info',
	    url:"../../InterfaceManagerAction_readInterfaceName.action?t=interface",
	    reader:name_reader
	});
	name_store.load();
	var idx;
	name_store.addListener('load',function(){
		var names = new Array();
		var i = 0;
		name_store.each(function(record){
			names[i++] = record.get('key');
		});
		var index = new Array();
		var k = 0;
		for(var j = 0; j<names.length; j++){
			if(names[j].split(':').length>1 && names[j].split(':')[0]==interfaceName){
				index[k++] = names[j].split(':')[1];
			}
		}
		idx = getIdx(index);
		interfaceName +=':'+idx;
		Ext.getCmp('interfaceName.insert.info').setValue(interfaceName);
		Ext.getCmp('interfaceName.insert.hidden.info').setValue(interfaceName);
	});
}

function getIdx(index){
	var idx;
	if(index.length>1){
		for(var i=0;i<index.length;i++){
			if(index[i]-index[i+1]!=-1){
				idx = Number(index[i])+1;
				break;
			}
		}
	}else if(index.length==1){
		if(index[0]!=1){
			idx = 1;
		}else{
			idx = 2;
		}
	}else{
		idx = 1;
	}
	return idx;
}

function delete_row(){
	var grid = Ext.getCmp('grid.interface.info');
    var store = Ext.getCmp('grid.interface.info').getStore();
    var selModel = grid.getSelectionModel();
    if(selModel.hasSelection()){
        var selections = selModel.getSelections();
        Ext.each(selections,function(item){
        	var interfaceName = item.data.interfaceName;
        	Ext.MessageBox.show({
            	title:'信息',
            	msg:'<font color="green">确定要删除所选记录？</font>',
            	animEl:'btnRemove.interface.info',
            	width:200,
            	buttons:Ext.Msg.YESNO,
            	buttons:{'ok':'确定','no':'取消'},
            	icon:Ext.MessageBox.INFO,
            	closable:false,
            	fn:function(e){
            		if(e == 'ok'){
                        var myMask = new Ext.LoadMask(Ext.getBody(), {
                            msg: '正在处理,请稍后...',
                            removeMask: true
                        });
                        myMask.show();
            			Ext.Ajax.request({
            				url : '../../InterfaceManagerAction_deleteInterface.action',             // 删除 连接 到后台
            				params :{interfaceName : interfaceName},
            				success : function(action){
                                myMask.hide();
            					var json = Ext.decode(action.responseText);
            					Ext.MessageBox.show({
            						title:'信息',
            						msg:json.msg,
            						animEl:'btnRemove.interface.info',
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
            						msg:'请与后台服务人员联系!',
            						animEl:'btnRemove.interface.info',
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
        });
    }
}

function update_win(){
    var grid = Ext.getCmp('grid.interface.info');
    var store = Ext.getCmp('grid.interface.info').getStore();
    var selModel = grid.getSelectionModel();
    var formPanel;
    if(selModel.hasSelection()){
        var selections = selModel.getSelections();
        Ext.each(selections,function(item){
            formPanel = new Ext.form.FormPanel({
                labelWidth:100,
                frame:true,
                labelAlign:'right',autoScroll:true,
                defaults : {
					//width : 150
                    anchor:'90%'
				},
                items:[{
                    fieldLabel:'网卡显示名',
                    xtype:'displayfield',
                    value:item.data.interfaceName
                },{
                    xtype:'hidden',
                    name:'interfaceName',
                    value:item.data.interfaceName
                },{
                    fieldLabel:'网卡类型',
                    xtype:'displayfield',
                    value:item.data.encap
                },{
                    xtype:'hidden',
                    name:'encap',
                    value:item.data.encap
                },{
                    fieldLabel:"启动时是否启用",
                    layout:'column',
                    defaultType: 'radio',
                    items: [
                        { width:50, boxLabel: '是', name: 'isUp', inputValue: true,  checked: Boolean(item.data.isUp) },
                        { width:50, boxLabel: '否', name: 'isUp', inputValue: false, checked: !Boolean(item.data.isUp) }
                    ]
                },{
                	xtype:'hidden',
                	name:'isUpOlder',
                	value:item.data.isUp
                },{
                    fieldLabel:'IP',
                    xtype:'textfield',
                    name:'ip',
                    value:item.data.ip,
                    allowBlank:false,
		            blankText:'该项不能为空！',
                    regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                    regexText:'这个不是IP',
                    emptyText:'请输入IP'
                },{
                    fieldLabel:'MAC',
                    xtype:'displayfield',
                    value:item.data.mac
                },{
                    fieldLabel:'默认网关',
                    xtype:'textfield',
                    name:'gateway',
                    value:item.data.gateway,
                    regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                    regexText:'这个不是IP',
                    emptyText:'请输入IP'
                },{
                    xtype:'hidden',
                    name:'gatewayOlder',
                    value:item.data.gateway
                },{
                    fieldLabel:'子网掩码',
                    xtype:'textfield',
                    name:'subnetMask',
                    value:item.data.subnetMask,
                    allowBlank:false,
		            blankText:'该项不能为空！',
                    regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                    regexText:'这个不是IP',
                    emptyText:'请输入IP'
                },{
                    fieldLabel:'广播地址',
                    xtype:'textfield',
                    name:'broadCast',
                    value:item.data.broadCast,
                    allowBlank:false,
		            blankText:'该项不能为空！',
                    regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                    regexText:'这个不是IP',
                    emptyText:'请输入IP'
                }]
            });
        });
    }
    var win = new Ext.Window({
        title:'修改信息',
        width:400,
        height:300,
        modal:true,
        layout:'fit',
        items:[formPanel],
        bbar:[
            new Ext.Toolbar.Fill(),
            new Ext.Button ({
                id:'interface.update.win.info',
                text : '保存',
                allowDepress : false,
                handler : function() {
                    if (formPanel.form.isValid()) {
                        Ext.MessageBox.show({
                    		title:'信息',
                            width:200,
                            msg:'是否确定要修改?',
                            animEl:'interface.update.win.info',
                            buttons:Ext.MessageBox.YESNO,
                            buttons:{'ok':'确定','no':'取消'},
                            icon:Ext.MessageBox.QUESTION,
                            closable:false,
                            fn:function(e){
                            	if(e=='ok'){
                            		formPanel.getForm().submit({
			                            url :'../../InterfaceManagerAction_updateInterface.action',
			                            method :'POST',
			                            waitTitle :'系统提示',
			                            waitMsg :'正在保存...',
			                            success : function(form,action) {
                                            var flag = action.result.msg;
			                                Ext.MessageBox.show({
			                                    title:'信息',
			                                    width:200,
			                                    msg:flag,
			                                    animEl:'interface.update.win.info',
			                                    buttons:Ext.MessageBox.OK,
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
			                                    width:200,
			                                    msg:'修改失败，请与管理员联系!',
			                                    animEl:'interface.update.win.info',
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

                    } else {
                        Ext.MessageBox.show({
                            title:'信息',
                            width:200,
                            msg:'请填写完成再提交!',
                            animEl:'interface.update.win.info',
                            buttons:Ext.MessageBox.OK,
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

function update_xn_win(){
	var grid = Ext.getCmp('grid.interface.info');
    var store = Ext.getCmp('grid.interface.info').getStore();
    var selModel = grid.getSelectionModel();
    var formPanel;
    if(selModel.hasSelection()){
        var selections = selModel.getSelections();
        Ext.each(selections,function(item){
            formPanel = new Ext.form.FormPanel({
                labelWidth:100,
                frame:true,
                labelAlign:'right',autoScroll:true,
                defaults : {
					//width : 150
                    anchor:'90%'
				},
                items:[{
                    fieldLabel:'网卡显示名',
                    xtype:'displayfield',
                    value:item.data.interfaceName
                },{
                    xtype:'hidden',
                    name:'interfaceName',
                    value:item.data.interfaceName
                },{
                    fieldLabel:'网卡类型',
                    xtype:'displayfield',
                    value:item.data.encap
                },{
                    xtype:'hidden',
                    name:'encap',
                    value:item.data.encap
                },{
                    fieldLabel:"启动时是否启用",
                    layout:'column',
                    defaultType: 'radio',
                    items: [
                        { width:50, boxLabel: '是', name: 'isUp', inputValue: true,  checked: Boolean(item.data.isUp) },
                        { width:50, boxLabel: '否', name: 'isUp', inputValue: false, checked: !Boolean(item.data.isUp) }
                    ]
                },{
                	xtype:'hidden',
                	name:'isUpOlder',
                	value:item.data.isUp
                },{
                    fieldLabel:'IP',
                    xtype:'textfield',
                    name:'ip',
                    value:item.data.ip,
                    allowBlank:false,
		            blankText:'该项不能为空！',
                    regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                    regexText:'这个不是IP',
                    emptyText:'请输入IP'
                },{
                    fieldLabel:'MAC',
                    xtype:'displayfield',
                    value:item.data.mac
                },{
                    fieldLabel:'子网掩码',
                    xtype:'textfield',
                    name:'subnetMask',
                    value:item.data.subnetMask,
                    allowBlank:false,
		            blankText:'该项不能为空！',
                    regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                    regexText:'这个不是IP',
                    emptyText:'请输入IP'
                },{
                    fieldLabel:'广播地址',
                    xtype:'textfield',
                    name:'broadCast',
                    value:item.data.broadCast,
                    allowBlank:false,
		            blankText:'该项不能为空！',
                    regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                    regexText:'这个不是IP',
                    emptyText:'请输入IP'
                }]
            });
        });
    }
    var win = new Ext.Window({
        title:'修改信息',
        width:400,
        height:300,
        modal:true,
        layout:'fit',
        items:[formPanel],
        bbar:[
            new Ext.Toolbar.Fill(),
            new Ext.Button ({
                id:'interface.update.win.info',
                text : '保存',
                allowDepress : false,
                handler : function() {
                    if (formPanel.form.isValid()) {
                        Ext.MessageBox.show({
                    		title:'信息',
                            width:200,
                            msg:'是否确定要修改?',
                            animEl:'interface.update.win.info',
                            buttons:Ext.MessageBox.YESNO,
                            buttons:{'ok':'确定','no':'取消'},
                            icon:Ext.MessageBox.QUESTION,
                            closable:false,
                            fn:function(e){
                            	if(e=='ok'){
                            		formPanel.getForm().submit({
			                            url :'../../InterfaceManagerAction_updateXNInterface.action',
			                            method :'POST',
			                            waitTitle :'系统提示',
			                            waitMsg :'正在保存...',
			                            success : function(form,action) {
                                            var flag = action.result.msg;
			                                Ext.MessageBox.show({
			                                    title:'信息',
			                                    width:200,
			                                    msg:flag,
			                                    animEl:'interface.update.win.info',
			                                    buttons:Ext.MessageBox.OK,
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
			                                    width:200,
			                                    msg:'修改失败，请与管理员联系!',
			                                    animEl:'interface.update.win.info',
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

                    } else {
                        Ext.MessageBox.show({
                            title:'信息',
                            width:200,
                            msg:'请填写完成再提交!',
                            animEl:'interface.update.win.info',
                            buttons:Ext.MessageBox.OK,
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

function detail_win(){
    var grid = Ext.getCmp('grid.interface.info');
    var store = Ext.getCmp('grid.interface.info').getStore();
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
					width : 150
				},
                items:[{
                    fieldLabel:'网卡名',
                    value:item.data.interfaceName
                },{
                    fieldLabel:'网卡类型',
                    value:item.data.encap
                },{
                    fieldLabel:'启动时是否启用',
                    value:Boolean(item.data.isUp)?"是":"否"
                },{
                    fieldLabel:'IP',
                    value:item.data.ip
                },{
                    fieldLabel:'MAC',
                    value:item.data.mac
                },{
                    fieldLabel:'默认网关',
                    value:item.data.gateway
                },{
                    fieldLabel:'子网掩码',
                    value:item.data.subnetMask
                },{
                    fieldLabel:'首选DNS',
                    value:item.data.dns.split(',')[0]
                },{
                	fieldLabel:'备用DNS',
                    value:item.data.dns.split(',')[1]=='null'?"":item.data.dns.split(',')[1]
                }]
            });
        });
    }
    var win = new Ext.Window({
        title:'详细信息',
        width:400,
        height:300,
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

function dns_win(){
    var grid = Ext.getCmp('grid.interface.info');
    var store = Ext.getCmp('grid.interface.info').getStore();
    var selModel = grid.getSelectionModel();
    var formPanel;
    if(selModel.hasSelection()){
        var selections = selModel.getSelections();
        Ext.each(selections,function(item){
            formPanel = new Ext.form.FormPanel({
                labelWidth:80,
                frame:true,
                labelAlign:'right',autoScroll:true,
                defaultType:'textfield',
                defaults : {
					//width : 150
                    anchor:'90%'
				},
                items:[{
                    xtype:'hidden',
                    name:'interfaceName',
                    value:item.data.interfaceName
                },{
                    fieldLabel:'首选DNS',
                    name:'dns_1',
                    value:item.data.dns.split(',')[0],
                    allowBlank:false,
                    blankText:'该项不能为空！',
                    regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                    regexText:'这个不是IP',
                    emptyText:'请输入IP'
                },{
                    fieldLabel:'备用DNS',
                    name:'dns_2',
                    value:item.data.dns.split(',')[1]=='null'?"":item.data.dns.split(',')[1],
                    regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                    regexText:'这个不是IP',
                    emptyText:'请输入IP'
                }]
            });
        });
    }
   var win = new Ext.Window({
       title:'DNS信息',
       width:300,
       height:130,
       modal:true,
       layout:'fit',
       items:[formPanel],
       bbar:[
            new Ext.Toolbar.Fill(),
            new Ext.Button ({
                id:'interface.dns.update.win.info',
                text : '修改',
                allowDepress : false,
                handler : function() {
                    if (formPanel.form.isValid()) {
                        Ext.MessageBox.show({
                    		title:'信息',
                            msg:'是否确定要修改?',
                            animEl:'interface.dns.update.win.info',
                            buttons:Ext.MessageBox.YESNO,
                            buttons:{'ok':'确定','no':'取消'},
                            icon:Ext.MessageBox.QUESTION,
                            closable:false,
                            fn:function(e){
                            	if(e=='ok'){
                            		formPanel.getForm().submit({
			                            url :'../../InterfaceManagerAction_updateDNS.action',
			                            method :'POST',
			                            waitTitle :'系统提示',
			                            waitMsg :'正在保存...',
			                            success : function(form,action) {
                                            var flag = action.result.msg;
			                                Ext.MessageBox.show({
			                                    title:'信息',
			                                    width:200,
			                                    msg:flag,
			                                    animEl:'interface.dns.update.win.info',
			                                    buttons:Ext.MessageBox.OK,
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
			                                    width:200,
			                                    msg:'修改失败，请与管理员联系!',
			                                    animEl:'interface.dns.update.win.info',
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

                    } else {
                        Ext.MessageBox.show({
                            title:'信息',
                            width:200,
                            msg:'请填写完成再提交!',
                            animEl:'interface.dns.update.win.info',
                            buttons:Ext.MessageBox.OK,
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
