/**
 * 角色管理
 */
Ext.onReady(function() {

	Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var start = 0;
    var pageSize = 15;
    var record = new Ext.data.Record.create([
        {name:'id',			mapping:'id'},
        {name:'name',			mapping:'name'},
        {name:'description',			mapping:'description'},
        {name:'createTime',			mapping:'createTime'},
        {name:'modifiedTime',		mapping:'modifiedTime'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url:"../../RoleAction_select.action"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows",
        id:'id'
    },record);
    var store = new Ext.data.GroupingStore({
        id:"store.info",
        proxy : proxy,
        reader : reader
    });

    var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
//    var boxM = new Ext.grid.RadioboxSelectionModel();
    var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var colM = new Ext.grid.ColumnModel([
        boxM,
        rowNumber,
        {header:"角色名",			dataIndex:"name",		      align:'center',sortable:true,menuDisabled:true},
        {header:"描述信息",		dataIndex:"description",	  align:'center',menuDisabled:true},
        {header:'创建时间',		dataIndex:'createTime',	  align:'center',sortable:true,menuDisabled:true},
        {header:'修改时间',		dataIndex:'modifiedTime', align:'center',sortable:true,menuDisabled:true,     renderer:show_null},
        {header:'操作标记',		dataIndex:'id',			  align:'center',sortable:true,menuDisabled:true,		renderer:show_flag,	width:100}

    ]);
    var page_toolbar = new Ext.PagingToolbar({
        pageSize : pageSize,
        store:store,
        displayInfo:true,
        displayMsg:"显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg:"没有记录",
        beforePageText:"当前页",
        afterPageText:"共{0}页"
    });
    var grid_panel = new Ext.grid.GridPanel({
        id:'grid.info',
        plain:true,
        height:setHeight(),
        width:setWidth(),
        animCollapse:true,
        loadMask:{msg:'正在加载数据，请稍后...'},
        border:false,
        collapsible:false,
        cm:colM,
        sm:boxM,
        store:store,
        stripeRows:true,
        autoExpandColumn:2,
        disableSelection:true,
        bodyStyle:'width:100%',
        enableDragDrop:true,
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
                    insert_win(grid_panel,store);     //连接到 新增 面板
                }
            })
        ],
        view:new Ext.grid.GroupingView({
            forceFit:true,
            groupingTextTpl:'{text}({[values.rs.length]}条记录)'
        }),
        bbar:page_toolbar
    });
    var port = new Ext.Viewport({
        layout:'fit',
        renderTo: Ext.getBody(),
        items:[grid_panel]
    });
    store.load({
        params:{
            start:start,limit:pageSize
        }
    });
});
function setHeight(){
	var h = document.body.clientHeight-8;
	return h;
}

function setWidth(){
    return document.body.clientWidth-8;
}

function show_null(value){
    if(value == 'null'){
        return '';
    } else {
        return value;
    }
}

/**
 * 操作标记
 * @param value
 */
function show_flag(value, p, r){
    var name = r.get('name');
    if( name=='初始化管理员'||
        name=='授权管理员'||
        name=='审计管理员'||
        name=='配置管理员'){
        return String.format('<a href="javascript:void(0);" onclick="showUpdateRole(\''+value+'\');return false;" style="color: green;">修改</a>'
                    + '&nbsp;&nbsp;'
                    + '<font color="gray">删除</font>');
    }else{
        return String.format('<a href="javascript:void(0);" onclick="showUpdateRole(\''+value+'\');return false;" style="color: green;">修改</a>'
            + '&nbsp;&nbsp;'
            + '<a id="\''+value+'\'.delete.info" href="javascript:void(0);" onclick="deleteRole(\''+value+'\');return false;" style="color: green;">删除</a>');
    }
}

/**
 * 新增角色窗口
 * @param grid
 * @param store
 */
function insert_win(grid,store ){
    var record = new Ext.data.Record.create([
        {name:'id',			mapping:'id'},
        {name:'topName',		mapping:'topName'},
        {name:'secondName',	mapping:'secondName'},
        {name:'parentId',		mapping:'parentId'},
        {name:'checked',		mapping:'checked'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url:"../../RoleAction_permissionInsert.action"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows",
        id:'id'
    },record);
    var storeWin = new Ext.data.GroupingStore({
        proxy : proxy,
        reader : reader
    });
    var start = 0;
    var pageSize = 10;
    var SelectArray = new Array();//记录集:所有选中的行号
    var boxM = new Ext.grid.CheckboxSelectionModel({
        listeners:{
            rowselect:function(obj,rowIndex,record){
                SelectArray[record.data.id] = record.data.id;//往记录集中添加选中的行号,我这里直接保存了一个值
            },
            rowdeselect:function(obj,rowIndex,record){
                delete SelectArray[record.data.id];
            }
        }
    });   //复选框
    var colM = new Ext.grid.ColumnModel([
        boxM,
        {header:"一级菜单",		dataIndex:"topName",		  align:'center',menuDisabled:true},
        {header:"二级菜单",		dataIndex:"secondName",	  align:'center',menuDisabled:true}
    ]);
    var permission= new Ext.grid.GridPanel({
        id:'grid.insert.info',
        plain:true,
        autoScroll:true,
        height:265,
        loadMask:{msg:'正在加载数据，请稍后...'},
        border:false,
        cm:colM,
        sm:boxM,
        store:storeWin,
        stripeRows:true,
        autoExpandColumn:2,
        disableSelection:true,
        bodyStyle:'width:100%',
        selModel:new Ext.grid.RowSelectionModel({singleSelect:true}),
        bbar:new Ext.PagingToolbar({
            pageSize : pageSize,
            store:storeWin,
            displayInfo:true,
            displayMsg:"显示第{0}条记录到第{1}条记录，一共{2}条",
            emptyMsg:"没有记录",
            beforePageText:"当前页",
            afterPageText:"共{0}页"
        })
    });

    var formPanel = new Ext.form.FormPanel({
        plain:true,
        items:[{
            layout:'column',
            items:[{
                columnWidth:.5,
                layout:'form',
                plain:true,
                defaultType:'textfield',
                labelAlign:'right',
                labelWidth:60,
                defaults:{
                    width:200,
                    allowBlank:false,
                    blankText:'该项不能为空！'
                },
                items:[{
                    id:'role.name.insert.info',
                    fieldLabel:"角色名",
                    name:'role.name',
                    regex:/^.{2,30}$/,
                    regexText:'请输入任意2--30个字符',
                    emptyText:'请输入任意2--30个字符',
                    listeners:{
                        blur : function(){
                            var myMask = new Ext.LoadMask(Ext.getBody(),{
                                msg : '正在校验,请稍等..',
                                removeMask:true
                            });
                            myMask.show();
                            Ext.Ajax.request({
                                url:'../../RoleAction_check.action?name='+this.getValue(),
                                method:'POST',
                                success:function(r,o){
                                    var respText = Ext.util.JSON.decode(r.responseText);
                                    var msg = respText.msg;
                                    myMask.hide();
                                    if(msg != 'true'){
                                        Ext.MessageBox.show({
                                            title:'信息',
                                            width:250,
                                            msg:msg,
                                            buttons:{'ok':'确定'},
                                            icon:Ext.MessageBox.INFO,
                                            closable:false,
                                            fn:function(e){
                                                if(e=='ok'){
                                                    grid.render();
                                                    store.reload();
                                                    Ext.getCmp('role.name.insert.info').setValue('');
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                }]
            },{
                columnWidth:.5,
                layout:'form',
                plain:true,
                defaultType:'textfield',
                labelAlign:'right',
                labelWidth:70,
                defaults:{
                    width:200,
                    allowBlank:false,
                    blankText:'该项不能为空！'
                },
                items:[{
                    fieldLabel:"描述信息",
                    name:'role.description',
                    regex:/^.{2,30}$/,
                    regexText:'请输入任意2--30个字符',
                    emptyText:'请输入任意2--30个字符'
                }]
            }]
        }]
    });
    var win = new Ext.Window({
        title:"新增信息",
        width:630,
		layout:'fit',
        height:415,
        modal:true,
        items:[{
            frame:true,
            items:[formPanel,{
                layout:'fit',
                items:[{
                    xtype:'fieldset',
                    title:'菜单',
                    height:270,
                    items:[permission]
                }]
            }]
        }],
        bbar:[
        	'->',
        	{
        		id:'insert_win.info',
        		text:'保存',
        		handler:function(){
                    var count = SelectArray.length;
                    var ids = new Array();
                    if(count==0){
                        Ext.MessageBox.show({
                            title:'信息',
                            msg:'<font color="green">您没有勾选任何记录!</font>',
                            animEl:'insert_win.info',
                            buttons:{'ok':'返回','no':'取消'},
                            icon:Ext.MessageBox.WARNING,
                            closable:false ,
                            fn:function(e){
                                if(e=='ok'){
//                                    roleInsert(formPanel,grid,store,win,ids);
                                }
                            }
                        });
                    }else if(count>0){
                        var index = 0;
                        for(var i = 0; i < count; i++){
                            if(SelectArray[i]!=undefined){
                                ids[index++] = SelectArray[i];
                            }
                        }
                        if(index == 0){
                            Ext.MessageBox.show({
                                title:'信息',
                                msg:'<font color="green">您没有勾选任何记录!</font>',
                                animEl:'insert_win.info',
                                buttons:{'ok':'返回','no':'取消'},
                                icon:Ext.MessageBox.WARNING,
                                closable:false ,
                                fn:function(e){
                                    if(e=='ok'){
    //                                    roleInsert(formPanel,grid,store,win,ids);
                                    }
                                }
                            });
                        } else {
                            roleInsert(formPanel,grid,store,win,ids);
                        }
                    }

        		}
        	},{
                text:'关闭',
        		handler:function(){
                    win.close();
                }
            }
        ]
    }).show();
    storeWin.load({params:{start:start,limit:pageSize}});
    storeWin.addListener('load',function(){
        var size = storeWin.getCount();
        for(var i=0;i<size;i++){
            var _record = storeWin.getAt(i);
            var id = _record.data.id;
            if(SelectArray[id]==id){
                _record.data.checked = true;
                boxM.selectRow(storeWin.indexOf(_record),true);
            }
        }
    });
}
function roleInsert(formPanel,grid,store,win,ids){
    if (formPanel.form.isValid()) {
        formPanel.getForm().submit({
            url :'../../RoleAction_insert.action',
            method :'POST',
            params:{pIds:ids},
            waitTitle :'系统提示',
            waitMsg :'正在保存,请稍后...',
            success : function(form,action) {
                var msg = action.result.msg;
                Ext.MessageBox.show({
                    title:'信息',
                    width:250,
                    msg:msg,
                    animEl:'insert_win.info',
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
    } else {
        Ext.MessageBox.show({
            title:'信息',
            width:200,
            msg:'请填写完成再提交!',
            animEl:'insert_win.info',
            buttons:{'ok':'确定'},
            icon:Ext.MessageBox.ERROR,
            closable:false
        });
    }
}

/**
 * 修改角色窗口
 * @param value
 */
function showUpdateRole(value) {
    var record = new Ext.data.Record.create([
        {name:'id',			mapping:'id'},
        {name:'topName',		mapping:'topName'},
        {name:'secondName',	mapping:'secondName'},
        {name:'parentId',		mapping:'parentId'},
        {name:'checked',		mapping:'checked'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url:"../../RoleAction_permissionUpdate.action?id="+value
    });
    var reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows",
        id:'id'
    },record);
    var storeWin = new Ext.data.GroupingStore({
        proxy : proxy,
        reader : reader
    });
    var start = 0;
    var pageSize = 10;
    var SelectArray = new Array();//记录集:所有选中的行号
    var SelectDesArray = new Array(); //修改时 删除记录
    var boxM = new Ext.grid.CheckboxSelectionModel({
        listeners:{
            rowselect:function(obj,rowIndex,record){
                SelectArray[record.data.id] = record.data.id;//往记录集中添加选中的行号,我这里直接保存了一个值
                delete SelectDesArray[record.data.id];
            },
            rowdeselect:function(obj,rowIndex,record){
                SelectDesArray[record.data.id] = record.data.id;//往删除记录中添加
                delete SelectArray[record.data.id];
            }
        }
    });   //复选框
    var colM = new Ext.grid.ColumnModel([
        boxM,
        {header:"一级菜单",		dataIndex:"topName",		  align:'center',menuDisabled:true},
        {header:"二级菜单",		dataIndex:"secondName",	  align:'center',menuDisabled:true}
    ]);
    var permission= new Ext.grid.GridPanel({
        id:'grid.update.info',
        plain:true,
        autoScroll:true,
        height:265,
        animCollapse:true,
//        loadMask:true,
        loadMask:{msg:'正在加载数据，请稍后...'},
        border:false,
        collapsible:false,
        cm:colM,
        sm:boxM,
        store:storeWin,
        stripeRows:true,
        autoExpandColumn:2,
        disableSelection:true,
        bodyStyle:'width:100%',
        selModel:new Ext.grid.RowSelectionModel({singleSelect:true}),
        bbar:new Ext.PagingToolbar({
            pageSize : pageSize,
            store:storeWin,
            displayInfo:true,
            displayMsg:"显示第{0}条记录到第{1}条记录，一共{2}条",
            emptyMsg:"没有记录",
            beforePageText:"当前页",
            afterPageText:"共{0}页"
        })
    });
    var grid = Ext.getCmp('grid.info');
    var store = grid.getStore();
    var selModel_init = grid.getSelectionModel();
    var formPanel;
    if(selModel_init.hasSelection()){
        var selections_init = selModel_init.getSelections();
        Ext.each(selections_init,function(item){
            formPanel = new Ext.form.FormPanel({
                plain:true,
                items:[{
                    layout:'column',
                    items:[{
                        columnWidth:.5,
                        layout:'form',
                        plain:true,
                        defaultType:'textfield',
                        labelAlign:'right',
                        labelWidth:60,
                        defaults:{
                            width:200,
                            allowBlank:false,
                            blankText:'该项不能为空！'
                        },
                        items:[{
                            xtype:'hidden',
                            name:'role.id',
                            value:item.data.id
                        },{
                            fieldLabel:"角色名",
                            xtype:'displayfield',
                            value:item.data.name
                        },{
                            name:'role.name',
                            id:'role.name.info',
                            xtype:'hidden',
                            value:item.data.name
                        }]
                    },{
                        columnWidth:.5,
                        layout:'form',
                        plain:true,
                        defaultType:'textfield',
                        labelAlign:'right',
                        labelWidth:70,
                        defaults:{
                            width:200,
                            allowBlank:false,
                            blankText:'该项不能为空！'
                        },
                        items:[{
                            id:'role.description.info',
                            fieldLabel:"描述信息",
                            name:'role.description',value:item.data.description,
                            regex:/^.{2,30}$/,
                            regexText:'请输入任意2--30个字符',
                            emptyText:'请输入任意2--30个字符'
                        }]
                    }]
                }]
            });
        });
    }
    var win = new Ext.Window({
        title:"修改信息",
        width:630,
		layout:'fit',
        height:415,
        modal:true,
        items:[{frame:true,
            items:[formPanel,{
                layout:'fit',
                items:[{
                    xtype:'fieldset',
                    title:'菜单',
                    height:270,
                    items:[permission]
                }]
            }]
        }],
        bbar:[
        	'->',
        	{
        		id:'update_win.info',
        		text:'修改',
        		handler:function(){
                    var count = SelectArray.length;
                    var ids = new Array();
                    if(count==0){
                        Ext.MessageBox.show({
                            title:'信息',
                            msg:'<font color="green">您没有勾选任何记录!</font>',
                            animEl:'update_win.info',
                            buttons:{'ok':'返回','no':'取消'},
                            icon:Ext.MessageBox.WARNING,
                            closable:false,
                            fn:function(e){
                                if(e=='ok'){
//                                    roleUpdate(formPanel,grid,store,win,ids);
                                }
                            }
                        });
                    }else if(count>0){
                        var index = 0;
                        for(var i = 0; i < count; i++){
                            if(SelectArray[i]!=undefined){
                                ids[index++] = SelectArray[i];
                            }
                        }
                        if(index == 0){
                            Ext.MessageBox.show({
                                title:'信息',
                                msg:'<font color="green">您没有勾选任何记录!</font>',
                                animEl:'update_win.info',
                                buttons:{'ok':'返回','no':'取消'},
                                icon:Ext.MessageBox.WARNING,
                                closable:false,
                                fn:function(e){
                                    if(e=='ok'){
    //                                    roleUpdate(formPanel,grid,store,win,ids);
                                    }
                                }
                            });
                        } else {
                            Ext.MessageBox.show({
                                title:'信息',
                                msg:'<font color="green">是否修改角色信息?</font>',
                                width:250,
                                animEl:'update_win.info',
                                buttons:{'ok':'继续','no':'取消'},
                                icon:Ext.MessageBox.WARNING,
                                closable:false,
                                fn:function(e){
                                    if(e=='ok'){
                                        roleUpdate(formPanel,grid,store,win,ids);
                                    }
                                }
                            });
                        }
                    }
        		}
        	},{
                text:'关闭',
        		handler:function(){
                    win.close();
                }
            }
        ]
    }).show();
    storeWin.load({params:{start:start,limit:pageSize}});
    storeWin.addListener('load',function(){
        var size = storeWin.getCount();
        for(var i=0;i<size;i++){
            var _record = storeWin.getAt(i);
            var id = _record.data.id;
            if (SelectDesArray[_record.data.id]==undefined){
                var isChecked = _record.data.checked;
                if(isChecked) {
                    SelectArray[id] = id;
                } else {
                    SelectDesArray[id] = id;
                }
                if(SelectArray[id]==id){
                    _record.data.checked = true;
                    boxM.selectRow(storeWin.indexOf(_record),true);
                }
            } else if(SelectDesArray[_record.data.id]==id){
                _record.data.checked = false;
//                boxM.selectRow(storeWin.indexOf(_record),false);
            }
        }
    });
}
function roleUpdate(formPanel,grid,store,win,ids){
    if (formPanel.form.isValid()) {
        formPanel.getForm().submit({
            url :'../../RoleAction_update.action',
            method :'POST',
            params:{pIds:ids},
            waitTitle :'系统提示',
            waitMsg :'正在修改,请稍后...',
            success : function(form,action) {
                var msg = action.result.msg;
                Ext.MessageBox.show({
                    title:'信息',
                    width:250,
                    msg:msg,
                    animEl:'update_win.info',
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
    } else {
        Ext.MessageBox.show({
            title:'信息',
            width:200,
            msg:'请填写完成再提交!',
            animEl:'update_win.info',
            buttons:Ext.MessageBox.OK,
            buttons:{'ok':'确定'},
            icon:Ext.MessageBox.ERROR,
            closable:false
        });
    }
}

/**
 * 删除角色
 * @param value
 */
function deleteRole(value) {
    var grid = Ext.getCmp('grid.info');
    var store = grid.getStore();
    Ext.MessageBox.show({
        title:'信息',
        msg:'<font color="green">确定要删除所选记录？</font>',
        animEl:value+'.delete.info',
        width:260,
        buttons:Ext.Msg.YESNO,
        buttons:{'ok':'确定','no':'取消'},
        icon:Ext.MessageBox.INFO,
        closable:false,
        fn:function(e){
            if(e == 'ok'){
                var myMask = new Ext.LoadMask(Ext.getBody(),{
                    msg : '正在删除,请稍后...',
                    removeMask : true
                });
                myMask.show();
                Ext.Ajax.request({
                    url : 'RoleAction_delete.action',             // 删除 连接 到后台
                    params :{id:value},
                    method:'POST',
                    success : function(r,o){
                        myMask.hide();
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.MessageBox.show({
                            title:'信息',
                            width:250,
                            msg:msg,
                            animEl:value+'.delete.info',
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
                    }
                });
            }
        }
    });
}