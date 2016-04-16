/*
 *双机热备配置
 */

Ext.onReady(function() {
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';

    /////////////////////////////////////////////////////////////////////////////////////

    var record = new Ext.data.Record.create([
        {name:'isActive',       mapping:'isActive'},
        {name:'isMainSystem',       mapping:'isMainSystem'},
        {name:'mainIp',              mapping:'mainIp'},
        {name:'mainPort',           mapping:'mainPort'},
        {name:'mainStatus',           mapping:'mainStatus'},
        {name:'backupIp',           mapping:'backupIp'},
        {name:'backupPort',         mapping:'backupPort'},
        {name:'backupStatus',         mapping:'backupStatus'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url:"../../HotBackupAction_query.action"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:'rows'
    },record);
    var store = new Ext.data.Store({
        proxy : proxy,
        reader : reader
    });
    var activeData = [[true,'启用'],[false,'停用']];
    var activeStore = new Ext.data.SimpleStore({fields:['value','key'],data:activeData});
    var mainData = [[true,'主设备'],[false,'副设备']];
    var mainStore = new Ext.data.SimpleStore({fields:['value','key'],data:mainData});
    var button = new Ext.Panel({
        plain:true,
        buttonAlign :'left',
        autoScroll:true,
        buttons:[
        	new Ext.Toolbar.Spacer({width:190}),
            {
                text:"保存配置",
                id:'save.info',
                handler:function(){
                    if (initForm.form.isValid()) {
                        initForm.getForm().submit({
                            url :'../../HotBackupAction_update.action',
                            method :'POST',
                            waitTitle :'系统提示',
                            waitMsg :'正在保存,请稍后...',
                            success : function(form,action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                	title:'信息',
                                    msg:msg,
                                    animEl:'save.info',
                                    width:300,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.INFO,
                                    closable:false,
                                    fn:function(e){
                                        if(e=='ok'){
//                                            store.reload();
//                                            location.reload();
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        Ext.MessageBox.show({
                        	title:'信息',
                            msg:'保存失败，请填写完成再保存!',
                            animEl:'save.info',
                            width:300,
                            buttons:{'ok':'确定'},
                            icon:Ext.MessageBox.ERROR,
                            closable:false
                        });
                    }
                }
            }
        ]
    });

    var initForm = new Ext.form.FormPanel({
        plain:true,
        border:false,
        loadMask : { msg : '正在加载数据，请稍后.....' },
        labelAlign:'right',
        labelWidth:150,
        defaultType:'textfield',
        defaults:{
            width:200,
            allowBlank:false,
            blankText:'该项不能为空!'
        },
        items:[{
            xtype:"checkbox",
            fieldLabel:"启用热备",
            id:"isActive.info",
            name:"active"
        },{
            id:'isMainSystem.info',
            fieldLabel:'设备类型',
            hiddenName:'hotBackUp.isMainSystem',
            xtype:'combo',
            mode:'local',
            emptyText :'--请选择--',
            editable : false,
            typeAhead:true,
            forceSelection: true,
            triggerAction:'all',
            displayField:"key",
            valueField:"value",
            store:mainStore
        },{
            id:'mainStatus.info',
            fieldLabel:'主设备服务状态',
//            name:'backUp.mainStatus',
            xtype:'displayfield'
        },{
            id:'mainStatus.hidden.info',
//            fieldLabel:'主设备服务状态',
            name:'hotBackUp.mainStatus',
            xtype:'hidden'
        },{
            id:'mainIp.info',
            fieldLabel:'主设备IP',
            regex:/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/,
            regexText:'请输入正确的IP地址',
            name:'hotBackUp.mainIp'
        },{
            id:'mainPort.info',
            fieldLabel:'主设备端口',
            regex:/^(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$/,
            regexText:'请输入正确的端口',
            name:'hotBackUp.mainPort'
        },{
            id:'backupStatus.info',
            fieldLabel:'副设备服务状态',
//            name:'hotBackUp.mainStatus',
            xtype:'displayfield'
        },{
            id:'backupStatus.hidden.info',
//            fieldLabel:'主设备服务状态',
            name:'hotBackUp.backupStatus',
            xtype:'hidden'
        },{
            id:'backupIp.info',
            fieldLabel:'副设备IP',
            regex:/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/,
            regexText:'请输入正确的IP地址',
            name:'hotBackUp.backupIp'
        },{
            id:'backupPort.info',
            fieldLabel:'副设备端口',
            regex:/^(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$/,
            regexText:'请输入正确的端口',
            name:'hotBackUp.backupPort'
        },/*{
        	fieldLabel:"Ping策略",
        	id:'restarttime.info',
        	html:"<a href='javascript:;' onclick='ping_win();'>管理</a>",
        	xtype:'displayfield'
        },*/{
        	fieldLabel:"Telnet策略",
        	html:"<a href='javascript:;' onclick='telnet_win();' >管理</a>",
        	xtype:'displayfield'
        }/*,{
        	fieldLabel:"文件共享策略",
        	html:"<a href='javascript:;' onclick='other_win();'>管理</a>",
        	xtype:'displayfield'
        }*/],
    buttons:[
        button
    ]
    });

    var panel = new Ext.Panel({
        plain:true,
        width:setWidth(),
        border:false,
        autoScroll:true,
        items:[{
            xtype:'fieldset',
            title:'双机热备',
            width:500,
            items:[initForm]
        }]
    });
   /* var panel2 = new Ext.Panel({
        plain:true,
        width:setWidth(),
        border:false,
        buttonAlign :'left',
        buttons:[
            button
        ]
    });*/
    new Ext.Viewport({
    	layout:'fit',
    	renderTo:Ext.getBody(),
    	items:[{
            frame:true,
            autoScroll:true,
            items:[panel/*,panel2*/]
        }]
    });
    store.load();
    store.on('load',function(){
        Ext.getCmp('isActive.info').setValue(store.getAt(0).get('isActive'));
        Ext.getCmp('isMainSystem.info').setValue(store.getAt(0).get('isMainSystem'));
        Ext.getCmp('mainIp.info').setValue(store.getAt(0).get('mainIp'));
        Ext.getCmp('mainPort.info').setValue(store.getAt(0).get('mainPort'));
        Ext.getCmp('mainStatus.info').setValue(show_status(store.getAt(0).get('mainStatus')));
        Ext.getCmp('mainStatus.hidden.info').setValue(''+store.getAt(0).get('mainStatus'));
        Ext.getCmp('backupIp.info').setValue(store.getAt(0).get('backupIp'));
        Ext.getCmp('backupPort.info').setValue(store.getAt(0).get('backupPort'));
        Ext.getCmp('backupStatus.info').setValue(show_status(store.getAt(0).get('backupStatus')));
        Ext.getCmp('backupStatus.hidden.info').setValue(''+store.getAt(0).get('backupStatus'));
    });
});

function show_status(value){
    if(value == '1' || value == 1) {
//        return "<h1 >热备正常</h1>";
        return '<img src="../../img/icon/ok.png" alt="运行正常" title="运行正常" />';
//            '&nbsp;&nbsp;' +
//            '<a href="javascript:;" onclick="closeLocalHotBack();" style="color: green;">关闭</a>';
    } else if(value == '0' || value == 0) {
//        return "<h1 style='color: red;'>热备失败</h1>";
        return '<img src="../../img/icon/off.gif" alt="停止" title="停止"/>';
//            '&nbsp;&nbsp;' +
//            '<a href="javascript:;" onclick="startLocalHotBack();" style="color: green;">启动</a>';
    } else if(value == '3' || value == 3) {
        return '<img src="../../img/icon/off.gif" alt="停止中..." title="停止中..."/>&nbsp;&nbsp;停止中...';
    } else if(value == '2' || value == 2) {
        return '<img src="../../img/icon/ok.png" alt="启动中..." title="启动中..."/>&nbsp;&nbsp;启动中...';
    } else {
        return '<img src="../../img/icon/off.gif" alt="异常" title="异常"/>';
    }
}

function setWidth(){
    return document.body.clientWidth-15;
}


function ping_win() {
    var record = new Ext.data.Record.create([
        {name:'ip',mapping:'ip'},
        {name:'flag',mapping:'flag'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url:"../../HotBackupAction_queryList.action?type=ping"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows"
    },record);
    var store = new Ext.data.GroupingStore({
        proxy : proxy,
        reader : reader
    });
    var start = 0;			//分页--开始数
	var pageSize = 5;		//分页--每页数
	store.load({
        params:{
            start:start,limit:pageSize
        }
    });
    var client_edit = new Ext.form.TextField({
        regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
        regexText:'这个不是Ip'
    });
	var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
	var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
	var colM = new Ext.grid.ColumnModel([
        rowNumber,
		boxM,
        {header:"服务器IP",dataIndex:"ip",align:'center',editor:client_edit},
        {header:"操作标记",dataIndex:"flag",align:'center',renderer:show_url_ping,width:40}
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
        id:'grid.ping.info',
        plain:true,
        renderTo:Ext.getBody(),
        animCollapse:true,
        height:300,width:500,
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
                id:'btnAdd.ping',
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
                id : 'btnRemove.ping',
                text : '删除',
                iconCls : 'remove',
                handler : function() {
                    deletePingRows(grid,store);
                }
            }),
            {xtype:"tbseparator"},
            new Ext.Button({
                id:'btnSave.ping',
                text:'保存',
                iconCls:'save',
                handler:function(){
                    insertPingRows(grid,store);
                }
            })
        ],
        view:new Ext.grid.GroupingView({
            forceFit:true,
            groupingTextTpl:'{text}({[values.rs.length]}条记录)'
        }),
        bbar:page_toolbar
    });
    var win = new Ext.Window({
        title:'Ping策略',
        width:510,height:333,
        frame:true,modal:true,
        items:[grid]
    }).show();
}

function show_url_ping(value) {
    if(value==2){
        return "<a href='javascript:;' onclick='updatePingWin()' >修改信息</a>";
   }
}

function insertPingRows(grid,store){
    var selModel = grid.getSelectionModel();
    var count = selModel.getCount();
    if(count==0){
        Ext.MessageBox.show({
        	title:'信息',
        	width:200,
        	msg:'您没有勾选任何记录!',
        	animEl:'btnSave.ping',
        	buttons:{'ok':'确定'},
        	icon:Ext.MessageBox.QUESTION,
        	closable:false
		});

    }else if(count > 0){
        var array = new Array();
        var record = grid.getSelectionModel().getSelections();
        for(var i = 0; i < record.length; i++){
            array[i] = record[i].get('ip');
        }
        Ext.MessageBox.show({
            title:'信息',
            width:230,
            msg:'确定要保存所选的所有记录?',
            animEl:'btnSave.ping',
            buttons:{'ok':'确定','no':'取消'},
            icon:Ext.MessageBox.WARNING,
            closable:false,
            fn:function(e){
                if(e=='ok'){
                    var myMask = new Ext.LoadMask(Ext.getBody(),{
                        msg:'正在保存,请稍后...',
                        removeMask:true
                    });
                    myMask.show();
                    Ext.Ajax.request({
                        url : '../../HotBackupAction_insertList.action?type=pings',
                        params :{array : array },
                        success : function(r,o){
                            var respText = Ext.util.JSON.decode(r.responseText);
                            var msg = respText.msg;
                            myMask.hide();
                            Ext.MessageBox.show({
                                title:'信息',
                                width:300,
                                msg:msg,
                                animEl:'btnSave.ping',
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
                        }
                    });
                }
            }
        });
    }
}

function deletePingRows(grid,store){
    var selModel = grid.getSelectionModel();
    var count = selModel.getCount();
    if(count==0){
    	Ext.MessageBox.show({
        	title:'信息',
        	width:200,
        	msg:'您没有勾选任何记录!',
        	animEl:'btnRemove.ping',
        	buttons:{'ok':'确定'},
        	icon:Ext.MessageBox.QUESTION,
        	closable:false
		});

    }else if(count > 0){
        var array = new Array();
        var record = grid.getSelectionModel().getSelections();
        for(var i = 0; i < record.length; i++){
            array[i] = record[i].get('ip');
        }
        Ext.MessageBox.show({
            title:'信息',
            width:200,
            msg:'确定要删除所选的所有记录?',
            animEl:'btnRemove.ping',
            buttons:{'ok':'确定','no':'取消'},
            icon:Ext.MessageBox.WARNING,
            closable:false,
            fn:function(e){
                if(e=='ok'){
                    var myMask = new Ext.LoadMask(Ext.getBody(),{
                        msg:'正在删除,请稍后...',
                        removeMask:true
                    });
                    myMask.show();
                    Ext.Ajax.request({
                        url : '../../HotBackupAction_deleteList.action?type=pings',    // 删除 连接 到后台
                        params :{array : array },
                        success : function(r,o){
                            var respText = Ext.util.JSON.decode(r.responseText);
                            var msg = respText.msg;
                            myMask.hide();
                            Ext.MessageBox.show({
                                title:'信息',
                                width:300,
                                msg:msg,
                                animEl:'btnRemove.ping',
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
                        }
                    });
                }
            }
        });
    }
}

function updatePingWin(){
    var grid = Ext.getCmp("grid.ping.info");//获取对应grid
    var store = grid.getStore();
    var selModel = grid.getSelectionModel();
    var formPanel;
    if(selModel.hasSelection()){
        var selections = selModel.getSelections();
        Ext.each(selections,function(item){
            formPanel = new Ext.form.FormPanel({
                defaultType : 'textfield',
                labelWidth:110,
                frame:true,
                loadMask : { msg : '正在加载数据，请稍后.....' },
                height : 100,
                labelAlign:'right',
                defaults : {
                    width : 150,
                    allowBlank : false,
                    blankText : '该项不能为空！'
                },
                items : [{
                    fieldLabel:"服务器IP",
                    name:'l',
                    value:item.data.ip,
                    emptyText:'--请输入Ip--',
                    regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                    regexText:'这个不是Ip'
                },{
                    xtype:'hidden',
                    name:'old',
                    value:item.data.ip
                }]
            });
        });
    }
    var win = new Ext.Window({
        title:"Ping策略-修改信息",
        width:320,
        height:120,
        layout:'fit',
        modal:true,
        items: [formPanel],
        bbar:[
            new Ext.Toolbar.Fill(),
            new Ext.Button ({
                id:'update.ping.info',
                text : '修改',
                formBind:true,
                allowDepress : false,
                handler : function() {
                    if (formPanel.form.isValid()) {
                        Ext.MessageBox.show({
                            title:'信息',
                            width:200,
                            msg:'确定要修改以上内容?',
                            animEl:'update.ping.info',
                            buttons:{'ok':'确定','no':'取消'},
                            icon:Ext.MessageBox.WARNING,
                            closable:false,
                            fn:function(e){
                                if(e=='ok'){
                                    formPanel.getForm().submit({
                                        url :'../../HotBackupAction_updateList.action?type=pings',
                                        method :'POST',
                                        waitTitle :'系统提示',
                                        waitMsg :'正在保存...',
                                        success : function(form,action) {
                                            Ext.MessageBox.show({
                                                title:'信息',
                                                width:300,
                                                msg:action.result.msg,
                                                animEl:'update.ping.info',
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
                            animEl:'update.ping.info',
                            buttons:{'ok':'确定'},
                            icon:Ext.MessageBox.ERROR,
                            closable:false
                        });
                    }
                }
            }),
            new Ext.Button ({
                allowDepress : false,
                handler : function() {win.close();},
                text : '关闭'
            })
        ]
    });
    win.show();
}

function telnet_win() {
    var record = new Ext.data.Record.create([
        {name:'ip',mapping:'ip'},
        {name:'flag',mapping:'flag'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url:"../../HotBackupAction_queryList.action?type=telnet"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows"
    },record);
    var store = new Ext.data.GroupingStore({
        proxy : proxy,
        reader : reader
    });
    var start = 0;			//分页--开始数
	var pageSize = 5;		//分页--每页数
	store.load({
        params:{
            start:start,limit:pageSize
        }
    });
    var client_edit = new Ext.form.TextField({
        regex:/^((((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\:)(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]{1}|[1-9]))$/,
        regexText:'这个不是ip:port'
    });
	var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
	var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
	var colM = new Ext.grid.ColumnModel([
        rowNumber,
		boxM,
        {header:"服务器IP和端口",dataIndex:"ip",align:'center',editor:client_edit},
        {header:"操作标记",dataIndex:"flag",align:'center',renderer:show_url_telnet,width:40}
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
        id:'grid.telnet.info',
        plain:true,
        renderTo:Ext.getBody(),
        animCollapse:true,
        height:300,width:500,
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
                id:'btnAdd.telnet',
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
                id : 'btnRemove.telnet',
                text : '删除',
                iconCls : 'remove',
                handler : function() {
                    deleteTelnetRows(grid,store);
                }
            }),
            {xtype:"tbseparator"},
            new Ext.Button({
                id:'btnSave.telnet',
                text:'保存',
                iconCls:'save',
                handler:function(){
                    insertTelnetRows(grid,store);
                }
            })
        ],
        view:new Ext.grid.GroupingView({
            forceFit:true,
            groupingTextTpl:'{text}({[values.rs.length]}条记录)'
        }),
        bbar:page_toolbar
    });
    var win = new Ext.Window({
        title:'Telnet策略',
        width:510,height:333,
        frame:true,modal:true,
        items:[grid]
    }).show();
}

function show_url_telnet(value) {
    if(value==2){
        return "<a href='javascript:;' onclick='updateTelnetWin()' >修改信息</a>";
   }
}

function insertTelnetRows(grid,store){
    var selModel = grid.getSelectionModel();
    var count = selModel.getCount();
    if(count==0){
        Ext.MessageBox.show({
        	title:'信息',
        	width:200,
        	msg:'您没有勾选任何记录!',
        	animEl:'btnSave.telnet',
        	buttons:{'ok':'确定'},
        	icon:Ext.MessageBox.QUESTION,
        	closable:false
		});

    }else if(count > 0){
        var array = new Array();
        var record = grid.getSelectionModel().getSelections();
        for(var i = 0; i < record.length; i++){
            array[i] = record[i].get('ip');
        }
        Ext.MessageBox.show({
            title:'信息',
            width:230,
            msg:'确定要保存所选的所有记录?',
            animEl:'btnSave.telnet',
            buttons:{'ok':'确定','no':'取消'},
            icon:Ext.MessageBox.WARNING,
            closable:false,
            fn:function(e){
                if(e=='ok'){
                    var myMask = new Ext.LoadMask(Ext.getBody(),{
                        msg:'正在保存,请稍后...',
                        removeMask:true
                    });
                    myMask.show();
                    Ext.Ajax.request({
                        url : '../../HotBackupAction_insertList.action?type=telnets',
                        params :{array : array },
                        success : function(r,o){
                            var respText = Ext.util.JSON.decode(r.responseText);
                            var msg = respText.msg;
                            myMask.hide();
                            Ext.MessageBox.show({
                                title:'信息',
                                width:300,
                                msg:msg,
                                animEl:'btnSave.telnet',
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
                        }
                    });
                }
            }
        });
    }
}

function deleteTelnetRows(grid,store){
    var selModel = grid.getSelectionModel();
    var count = selModel.getCount();
    if(count==0){
    	Ext.MessageBox.show({
        	title:'信息',
        	width:200,
        	msg:'您没有勾选任何记录!',
        	animEl:'btnRemove.telnet',
        	buttons:{'ok':'确定'},
        	icon:Ext.MessageBox.QUESTION,
        	closable:false
		});

    }else if(count > 0){
        var array = new Array();
        var record = grid.getSelectionModel().getSelections();
        for(var i = 0; i < record.length; i++){
            array[i] = record[i].get('ip');
        }
        Ext.MessageBox.show({
            title:'信息',
            width:200,
            msg:'确定要删除所选的所有记录?',
            animEl:'btnRemove.telnet',
            buttons:{'ok':'确定','no':'取消'},
            icon:Ext.MessageBox.WARNING,
            closable:false,
            fn:function(e){
                if(e=='ok'){
                    var myMask = new Ext.LoadMask(Ext.getBody(),{
                        msg:'正在删除,请稍后...',
                        removeMask:true
                    });
                    myMask.show();
                    Ext.Ajax.request({
                        url : '../../HotBackupAction_deleteList.action?type=telnets',    // 删除 连接 到后台
                        params :{array : array },
                        success : function(r,o){
                            var respText = Ext.util.JSON.decode(r.responseText);
                            var msg = respText.msg;
                            myMask.hide();
                            Ext.MessageBox.show({
                                title:'信息',
                                width:300,
                                msg:msg,
                                animEl:'btnRemove.telnet',
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
                        }
                    });
                }
            }
        });
    }
}

function updateTelnetWin(){
    var grid = Ext.getCmp("grid.telnet.info");//获取对应grid
    var store = grid.getStore();
    var selModel = grid.getSelectionModel();
    var formPanel;
    if(selModel.hasSelection()){
        var selections = selModel.getSelections();
        Ext.each(selections,function(item){
            formPanel = new Ext.form.FormPanel({
                defaultType : 'textfield',
                labelWidth:110,
                frame:true,
                loadMask : { msg : '正在加载数据，请稍后.....' },
                height : 100,
                labelAlign:'right',
                defaults : {
                    width : 150,
                    allowBlank : false,
                    blankText : '该项不能为空！'
                },
                items : [{
                    fieldLabel:"服务器IP和端口",
                    name:'l',
                    value:item.data.ip,
                    emptyText:'--请输入ip:port--',
                    regex:/^((((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\:)(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]{1}|[1-9]))$/,
                    regexText:'这个不是Ip'
                },{
                    xtype:'hidden',
                    name:'old',
                    value:item.data.ip
                }]
            });
        });
    }
    var win = new Ext.Window({
        title:"Telnet策略-修改信息",
        width:320,
        height:120,
        layout:'fit',
        modal:true,
        items: [formPanel],
        bbar:[
            new Ext.Toolbar.Fill(),
            new Ext.Button ({
                id:'update.telnet.info',
                text : '修改',
                formBind:true,
                allowDepress : false,
                handler : function() {
                    if (formPanel.form.isValid()) {
                        Ext.MessageBox.show({
                            title:'信息',
                            width:200,
                            msg:'确定要修改以上内容?',
                            animEl:'update.telnet.info',
                            buttons:{'ok':'确定','no':'取消'},
                            icon:Ext.MessageBox.WARNING,
                            closable:false,
                            fn:function(e){
                                if(e=='ok'){
                                    formPanel.getForm().submit({
                                        url :'../../HotBackupAction_updateList.action?type=telnets',
                                        method :'POST',
                                        waitTitle :'系统提示',
                                        waitMsg :'正在保存...',
                                        success : function(form,action) {
                                            Ext.MessageBox.show({
                                                title:'信息',
                                                width:300,
                                                msg:action.result.msg,
                                                animEl:'update.telnet.info',
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
                            animEl:'update.telnet.info',
                            buttons:{'ok':'确定'},
                            icon:Ext.MessageBox.ERROR,
                            closable:false
                        });
                    }
                }
            }),
            new Ext.Button ({
                allowDepress : false,
                handler : function() {win.close();},
                text : '关闭'
            })
        ]
    });
    win.show();
}

function other_win() {
    var record = new Ext.data.Record.create([
        {name:'ip',mapping:'ip'},
        {name:'flag',mapping:'flag'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url:"../../HotBackupAction_queryList.action?type=other"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows"
    },record);
    var store = new Ext.data.GroupingStore({
        proxy : proxy,
        reader : reader
    });
    var start = 0;			//分页--开始数
	var pageSize = 5;		//分页--每页数
	store.load({
        params:{
            start:start,limit:pageSize
        }
    });
    var client_edit = new Ext.form.TextField({
//        regex:/^((((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\:)(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]{1}|[1-9]))$/,
//        regexText:'这个不是ip:port'
        blankText:'smb://guest:guest@192.168.2.220:514/source'
    });
	var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
	var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
	var colM = new Ext.grid.ColumnModel([
        rowNumber,
		boxM,
        {header:"文件共享URL",dataIndex:"ip",align:'center',editor:client_edit},
        {header:"操作标记",dataIndex:"flag",align:'center',renderer:show_url_other,width:40}
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
        id:'grid.other.info',
        plain:true,
        renderTo:Ext.getBody(),
        animCollapse:true,
        height:300,width:500,
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
                id:'btnAdd.other',
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
                id : 'btnRemove.other',
                text : '删除',
                iconCls : 'remove',
                handler : function() {
                    deleteOtherRows(grid,store);
                }
            }),
            {xtype:"tbseparator"},
            new Ext.Button({
                id:'btnSave.other',
                text:'保存',
                iconCls:'save',
                handler:function(){
                    insertOtherRows(grid,store);
                }
            })
        ],
        view:new Ext.grid.GroupingView({
            forceFit:true,
            groupingTextTpl:'{text}({[values.rs.length]}条记录)'
        }),
        bbar:page_toolbar
    });
    var win = new Ext.Window({
        title:'文件共享策略',
        width:510,height:333,
        frame:true,modal:true,
        items:[grid]
    }).show();
}

function show_url_other(value) {
    if(value==2){
        return "<a href='javascript:;' onclick='updateOtherWin()' >修改信息</a>";
   }
}

function insertOtherRows(grid,store){
    var selModel = grid.getSelectionModel();
    var count = selModel.getCount();
    if(count==0){
        Ext.MessageBox.show({
        	title:'信息',
        	width:200,
        	msg:'您没有勾选任何记录!',
        	animEl:'btnSave.other',
        	buttons:{'ok':'确定'},
        	icon:Ext.MessageBox.QUESTION,
        	closable:false
		});

    }else if(count > 0){
        var array = new Array();
        var record = grid.getSelectionModel().getSelections();
        for(var i = 0; i < record.length; i++){
            array[i] = record[i].get('ip');
        }
        Ext.MessageBox.show({
            title:'信息',
            width:230,
            msg:'确定要保存所选的所有记录?',
            animEl:'btnSave.other',
            buttons:{'ok':'确定','no':'取消'},
            icon:Ext.MessageBox.WARNING,
            closable:false,
            fn:function(e){
                if(e=='ok'){
                    var myMask = new Ext.LoadMask(Ext.getBody(),{
                        msg:'正在保存,请稍后...',
                        removeMask:true
                    });
                    myMask.show();
                    Ext.Ajax.request({
                        url : '../../HotBackupAction_insertList.action?type=others',
                        params :{array : array },
                        success : function(r,o){
                            var respText = Ext.util.JSON.decode(r.responseText);
                            var msg = respText.msg;
                            myMask.hide();
                            Ext.MessageBox.show({
                                title:'信息',
                                width:300,
                                msg:msg,
                                animEl:'btnSave.other',
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
                        }
                    });
                }
            }
        });
    }
}

function deleteOtherRows(grid,store){
    var selModel = grid.getSelectionModel();
    var count = selModel.getCount();
    if(count==0){
    	Ext.MessageBox.show({
        	title:'信息',
        	width:200,
        	msg:'您没有勾选任何记录!',
        	animEl:'btnRemove.other',
        	buttons:{'ok':'确定'},
        	icon:Ext.MessageBox.QUESTION,
        	closable:false
		});

    }else if(count > 0){
        var array = new Array();
        var record = grid.getSelectionModel().getSelections();
        for(var i = 0; i < record.length; i++){
            array[i] = record[i].get('ip');
        }
        Ext.MessageBox.show({
            title:'信息',
            width:200,
            msg:'确定要删除所选的所有记录?',
            animEl:'btnRemove.other',
            buttons:{'ok':'确定','no':'取消'},
            icon:Ext.MessageBox.WARNING,
            closable:false,
            fn:function(e){
                if(e=='ok'){
                    var myMask = new Ext.LoadMask(Ext.getBody(),{
                        msg:'正在删除,请稍后...',
                        removeMask:true
                    });
                    myMask.show();
                    Ext.Ajax.request({
                        url : '../../HotBackupAction_deleteList.action?type=others',    // 删除 连接 到后台
                        params :{array : array },
                        success : function(r,o){
                            var respText = Ext.util.JSON.decode(r.responseText);
                            var msg = respText.msg;
                            myMask.hide();
                            Ext.MessageBox.show({
                                title:'信息',
                                width:300,
                                msg:msg,
                                animEl:'btnRemove.other',
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
                        }
                    });
                }
            }
        });
    }
}

function updateOtherWin(){
    var grid = Ext.getCmp("grid.other.info");//获取对应grid
    var store = grid.getStore();
    var selModel = grid.getSelectionModel();
    var formPanel;
    if(selModel.hasSelection()){
        var selections = selModel.getSelections();
        Ext.each(selections,function(item){
            formPanel = new Ext.form.FormPanel({
                defaultType : 'textfield',
                labelWidth:110,
                frame:true,
                loadMask : { msg : '正在加载数据，请稍后.....' },
                height : 100,
                labelAlign:'right',
                defaults : {
                    width : 350,
                    allowBlank : false,
                    blankText : '该项不能为空！'
                },
                items : [{
                    fieldLabel:"文件共享URL",
                    name:'l',
                    value:item.data.ip,
                    emptyText:'smb://guest:guest@192.168.2.220:445/source'
//                    regex:/^((((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\:)(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]{1}|[1-9]))$/,
//                    regexText:'这个不是Ip'
                },{
                    xtype:'hidden',
                    name:'old',
                    value:item.data.ip
                }]
            });
        });
    }
    var win = new Ext.Window({
        title:"文件共享策略-修改信息",
        width:500,
        height:120,
        layout:'fit',
        modal:true,
        items: [formPanel],
        bbar:[
            new Ext.Toolbar.Fill(),
            new Ext.Button ({
                id:'update.other.info',
                text : '修改',
                formBind:true,
                allowDepress : false,
                handler : function() {
                    if (formPanel.form.isValid()) {
                        Ext.MessageBox.show({
                            title:'信息',
                            width:200,
                            msg:'确定要修改以上内容?',
                            animEl:'update.other.info',
                            buttons:{'ok':'确定','no':'取消'},
                            icon:Ext.MessageBox.WARNING,
                            closable:false,
                            fn:function(e){
                                if(e=='ok'){
                                    formPanel.getForm().submit({
                                        url :'../../HotBackupAction_updateList.action?type=others',
                                        method :'POST',
                                        waitTitle :'系统提示',
                                        waitMsg :'正在保存...',
                                        success : function(form,action) {
                                            Ext.MessageBox.show({
                                                title:'信息',
                                                width:300,
                                                msg:action.result.msg,
                                                animEl:'update.other.info',
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
                            animEl:'update.other.info',
                            buttons:{'ok':'确定'},
                            icon:Ext.MessageBox.ERROR,
                            closable:false
                        });
                    }
                }
            }),
            new Ext.Button ({
                allowDepress : false,
                handler : function() {win.close();},
                text : '关闭'
            })
        ]
    });
    win.show();
}