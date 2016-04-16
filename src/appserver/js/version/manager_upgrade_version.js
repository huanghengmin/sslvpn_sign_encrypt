/**
 * 版本升级
 */
Ext.onReady(function(){
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget ='side';

    var warStart = 0;			//分页--开始数
    var warPageSize = 15;		//分页--每页数
    //================================== -- war文件 -- ==========================================================
    var warRecord = new Ext.data.Record.create([
        {name:'warName',   mapping:'warName'},
        {name:'warVersion',mapping:'warVersion'},
        {name:'newVersion',mapping:'newVersion'},
        {name:'buildDate',mapping:'buildDate'},
        {name:'upgradeTime',mapping:'upgradeTime'},
        {name:'flag',mapping:'flag'}
    ]);
    var warProxy = new Ext.data.HttpProxy({
        url:"../../UpgradeAction_selectWar.action"
    });
    var warReader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows"
    },warRecord);
    var warStore = new Ext.data.GroupingStore({
        id:"warStore.info",
        proxy : warProxy,
        reader : warReader
    });

	//var warBoxM = new Ext.grid.CheckboxSelectionModel();   //复选框
//    var warBoxM = new Ext.grid.RadioboxSelectionModel();   //单选框
	var warRowNumber = new Ext.grid.RowNumberer();         //自动 编号
	var warColM = new Ext.grid.ColumnModel([
        warRowNumber,
		//warBoxM,
        {header:"WAR文件名",dataIndex:"warName",align:'center'},
        {header:"生成时间",dataIndex:"buildDate",align:'center'},
        {header:"当前版本号",dataIndex:"warVersion",align:'center'},
        {header:"上传版本号",dataIndex:"newVersion",align:'center'},
        {header:"上传时间",dataIndex:"upgradeTime",align:'center'},
        {header:"操作标记",dataIndex:"flag",align:'center',renderer:show_flag}
    ]);
    warColM.defaultSortable = true;
    var warPage_toolbar = new Ext.PagingToolbar({
        pageSize : warPageSize,
        store:warStore,
        displayInfo:true,
        displayMsg:"显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg:"没有记录",
        beforePageText:"当前页",
        afterPageText:"共{0}页"
    });
    var warGrid = new Ext.grid.GridPanel({
        collapsible:false,
        id:'warGrid.info',
        height:setHeight(),
        //autoHeight:true,
        autoScroll:true,
        animCollapse:true,
        loadMask:{msg:'正在加载数据,请稍后...'},
        border:false,
        cm:warColM,
        //sm:warBoxM,
        store:warStore,
        columnLines: true,
        autoExpandColumn:6,
        disableSelection:true,
        enableDragDrop:true,
        selModel:new Ext.grid.RowSelectionModel({singleSelect:true}),
        viewConfig:{
            autoFill:true,
            forceFit:true,
            enableRowBody:true,
            getRowClass:function(record,rowIndex,p,store){
            return 'x-grid3-row-collapsed';
            }
        },
        tbar:[
            new Ext.Button ({
                id : 'btnRemove.war.info',
                text : '上传',
                iconCls : 'add',
                handler : function() {
                    uploadWarRow(warGrid,warStore);
                }
            }),
            new Ext.Button ({
                id : 'btnUpgrade.war.info',
                text : '升级',
                iconCls : 'upgrade',
                handler : function() {
                    upgradeWarRow(warGrid,warStore);
                }
            })
        ],
        view:new Ext.grid.GroupingView({
            forceFit:true,
            groupingTextTpl:'{text}({[values.rs.length]}条记录)'
        }),
        bbar:warPage_toolbar,
        //columnLines: true,
        //autoScroll: true,
        //border: false,
        //collapsible: false,
        stripeRows: true,
        //autoExpandColumn: 'Position',
        enableHdMenu: true,
        enableColumnHide: true,
        //selModel: new Ext.grid.RowSelect.ionModel({singleSelect: true}),
        //height: 300,
        frame: true/*,
     iconCls: 'icon-grid'*/
    });

    //================================== --jar文件 -- ==========================================================
    var jarStart = 0;			//分页--开始数
    var jarPageSize = 15;		//分页--每页数
    var jarRecord = new Ext.data.Record.create([
        {name:'warName',mapping:'warName'},
        {name:'jarName',mapping:'jarName'},
        {name:'jarVersion',mapping:'jarVersion'}
    ]);
    var jarProxy = new Ext.data.HttpProxy({
        url:"../../UpgradeAction_selectJar.action"
    });
    var jarReader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows"
    },jarRecord);
    var jarStore = new Ext.data.GroupingStore({
        id:"jarStore.info",
        proxy : jarProxy,
        reader : jarReader
    });

	//var jarBoxM = new Ext.grid.CheckboxSelectionModel();   //复选框
//    var jarBoxM = new Ext.grid.RadioboxSelectionModel();   //单选框
	var jarRowNumber = new Ext.grid.RowNumberer();         //自动 编号
	var jarColM = new Ext.grid.ColumnModel([
        jarRowNumber,
		//jarBoxM,
        {header:"自有JAR文件名",dataIndex:"jarName",align:'center'},
        {header:"版本号",dataIndex:"jarVersion",align:'center'},
        {header:"所属WAR文件名",dataIndex:"warName",align:'center'}
    ]);
    jarColM.defaultSortable = true;
    var jarPage_toolbar = new Ext.PagingToolbar({
        pageSize : jarPageSize,
        store:jarStore,
        displayInfo:true,
        displayMsg:"显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg:"没有记录",
        beforePageText:"当前页",
        afterPageText:"共{0}页"
    });
    var jarGrid = new Ext.grid.GridPanel({
        id:'jarGrid.info',
        //title:'JAR',
        height:setHeight(),
        //height:300,
        autoScroll:true,
        animCollapse:true,
        loadMask:{msg:'正在加载数据,请稍后...'},
        border:false,
        cm:jarColM,
        //sm:jarBoxM,
        store:jarStore,
        columnLines: true,
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
        //tbar:[{}],
        view:new Ext.grid.GroupingView({
            forceFit:true,
            groupingTextTpl:'{text}({[values.rs.length]}条记录)'
        }),
        bbar:jarPage_toolbar,
        //columnLines: true,
        //autoScroll: true,
        //border: false,
        collapsible: false,
        stripeRows: true,
        //autoExpandColumn: 'Position',
        enableHdMenu: true,
        enableColumnHide: true,
        //selModel: new Ext.grid.RowSelect.ionModel({singleSelect: true}),
        //height: 300,
        frame: true/*,
         iconCls: 'icon-grid'*/
    });

    new Ext.Viewport({
        renderTo:Ext.getBody(),
        layout:'fit',
        autoScroll:true,
        items:[
        	{
                autoScroll:true,
	        	layout:'column',
	        	items:[
	        		{autoScroll:true,items:[warGrid],columnWidth:.6},
	        		{autoScroll:true,items:[jarGrid],columnWidth:.4}
	        	]
        	}
        ]
    });
    warStore.load({
        params:{
            start:warStart,limit:warPageSize
        }
    });
    jarStore.load({
        params:{
            start:jarStart,limit:jarPageSize
        }
    });
});

function setHeight(){
	var h = document.body.clientHeight-8;
	return h;
}

function show_flag(value){
    if(value){
        return '<a href="javascript:;" onclick="backVersion();">回退</a>';
    }else{
        return '<font color="gray">回退</font>';
    }
}

function uploadWarRow(grid,store){
    var uploadWarForm = new Ext.form.FormPanel({
        frame:true,
        labelWidth:100,
        labelAlign:'right',
        fileUpload:true,
        border:false,
        defaults : {
            width : 200,
            allowBlank : false,
            blankText : '该项不能为空！'
        },
        items:[{
            xtype:'displayfield',
            value:'上传文件为[*.war]'
        },{
            id:'formFile',
            fieldLabel:"导入文件",
            name:'uploadFile',
            xtype:'textfield',
            inputType: 'file',
            listeners:{
                render:function(){
                    Ext.get('formFile').on("change",function(){
                        var file = this.getValue();
//                        var fso = new ActiveXObject("Scripting.FileSystemObject");
//                        var f = fso.GetFile(file);
//                        var f = new File(file);
//                        var isSize = true;
//                        if(f.size>1024*1024*2){
//                            alert(f.size+" Bytes");
//                            isSize = false;
//                        }
                        var fs = file.split('.');
                        if(fs[fs.length-1].toLowerCase()=='war'){
                            Ext.MessageBox.show({
                                title:'信息',
                                msg:'<font color="green">确定要上传文件:'+file+'？</font>',
                                width:300,
                                buttons:{'ok':'确定','no':'取消'},
                                icon:Ext.MessageBox.WARNING,
                                closable:false,
                                fn:function(e){
                                    if(e == 'ok'){
                                        if (uploadWarForm.form.isValid()) {
                                            uploadWarForm.getForm().submit({
                                                url :'../../UpgradeAction_uploadWar.action',
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
                                                                grid.render();
                                                                store.reload();
                                                                win.close();
                                                            } else {
                                                                Ext.getCmp('formFile').setValue('');
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
//                                            animEl:'insert.win.info',
                                                buttons:{'ok':'确定'},
                                                icon:Ext.MessageBox.ERROR,
                                                closable:false
                                            });
                                        }
                                    }
                                }
                            });
                        } else {
                            Ext.MessageBox.show({
                                title:'信息',
                                width:200,
                                msg:'上传文件格式不对,请重新选择!',
                                buttons:{'ok':'确定'},
                                icon:Ext.MessageBox.ERROR,
                                closable:false,
                                fn:function(e){
                                    if(e=='ok'){
                                        Ext.getCmp('formFile').setValue('');
                                    }
                                }
                            });
                        }
                    });
                }
            }
        }]
    });
	var win = new Ext.Window({
		title:'更新WAR包',
		width:400,
		height:150,
		layout:'fit',
        modal:true,
		items:[uploadWarForm],
		bbar:['->',{
//    		id:'insert.win.info',
//    		text:'上传',
//    		handler:function(){
//
//    		}
//    	},{
    		text:'关闭',
    		handler:function(){
    			win.close();
    		}
    	}]
	}).show();
}

function upgradeWarRow(grid,store){
    Ext.MessageBox.show({
        title:'信息',
        width:250,
        msg:'确定要升级?',
        animEl:'btnUpgrade.war.info',
        buttons:{'ok':'确定','no':'取消'},
        icon:Ext.MessageBox.WARNING,
        closable:false,
        fn:function(e){
            if(e=='ok'){
                var myMask = new Ext.LoadMask(Ext.getBody(), {
                	msg: '正在升级,请稍后...',
                	removeMask: true //完成后移除
                });
                myMask.show();
                Ext.Ajax.request({
                    url : '../../UpgradeAction_upgrade.action',
                    method :'POST',
                    success : function(r,o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        myMask.hide();
                        Ext.MessageBox.show({
                            title:'信息',
                            width:250,
                            msg:msg,
                            animEl:'btnUpgrade.war.info',
                            buttons:{'ok':'确定'},
                            icon:Ext.MessageBox.INFO,
                            closable:false,
                            fn:function(e){
                                if(e=='ok'){

                                }
                            }
                        });
                    }
                });
            }
        }
   	});
}

function backVersion(){
    var grid = Ext.getCmp('warGrid.info');
    var store = grid.getStore();
    var jarGrid = Ext.getCmp('jarGrid.info');
    var jarStore = jarGrid.getStore();
    var warName;
    var selModel = grid.getSelectionModel();
    if(selModel.hasSelection()){
        var selections = selModel.getSelections();
        Ext.each(selections,function(item){
            warName = item.data.warName;
        });
    }
    warName += '.war';
    Ext.MessageBox.show({
        title:'信息',
        width:250,
        msg:'确定要回退到上一版本?',
        animEl:'btnUpgrade.war.info',
        buttons:{'ok':'确定','no':'取消'},
        icon:Ext.MessageBox.WARNING,
        closable:false,
        fn:function(e){
            if(e=='ok'){
                var myMask = new Ext.LoadMask(Ext.getBody(), {
                    msg: '正在处理,请稍后...',
                    removeMask: true //完成后移除
                });
                myMask.show();
                Ext.Ajax.request({
                    url : '../../UpgradeAction_backup.action?warName='+warName,
                    method :'POST',
                    success : function(r,o) {
                        myMask.hide();
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.MessageBox.show({
                            title:'信息',
                            width:300,
                            msg:msg,
                            buttons:{'ok':'确定'},
                            icon:Ext.MessageBox.INFO,
                            closable:false,
                            fn:function(e){
                                if(e=='ok'){
                                    grid.render();
                                    jarGrid.render();
                                    store.reload();
                                    jarStore.reload();
                                }
                            }
                        });
                    }
                });
            }
        }
    });
}