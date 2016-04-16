/**
 * 日志下载
 */
Ext.onReady(function(){
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget ='side';

//    var pageStart = 0;
//    var pageSize = 15;
	var internal_record = new Ext.data.Record.create([
        {name:'fileName',mapping:'fileName'}
    ]);
    var internal_proxy = new Ext.data.HttpProxy({
        url:"../../DownLoadAction_readLocalLogName.action"
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
	
	//var internal_logBoxM = new Ext.grid.CheckboxSelectionModel();   //复选框
//    var internal_logBoxM = new Ext.grid.RadioboxSelectionModel();
    var internal_logRowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var internal_logColM = new Ext.grid.ColumnModel({
        columns:[
            //internal_logBoxM,
            internal_logRowNumber,
            {header:"下载本地日志文件名",dataIndex:"fileName",align:'center',sortable:true,menuDisabled:true,renderer : internal_logDownloadShowUrl}
        ],
        defaults:{sortable:false}//不允许客户端点击列头排序，可以打开s
    });
    var internal_logGrid = new Ext.grid.GridPanel({
        id:'grid.info',
        store:internal_store,
        cm:internal_logColM,
        //sm:internal_logBoxM,
        //height:setHeight,
        //columnLines:true,
//        frame:true,
//        autoScroll:true,
        loadMask:true,
        border:false,
        collapsible:false,
        stripeRows:true,
        //autoExpandColumn:'Position',
        //enableHdMenu:true,
        //enableColumnHide:true,
        //bodyStyle:'width:100%',
        //selModel:new Ext.grid.RowSelectionModel({singleSelect:true}),
        tbar:[
            new Ext.Button ({
                id : 'clear.log.info',
                text : '清空日志',
                iconCls : 'recycle',
                handler : function() {
                    clear_logs(internal_logGrid,internal_store);
                }
            })
        ],
        viewConfig:{
            forceFit:true
//            enableRowBody:true,
//            getRowClass:function(record,rowIndex,p,store){
//                return 'x-grid3-row-collapsed';
//            }
        },
        columnLines: true,
        autoScroll: true,
        border: false,
        collapsible: false,
        stripeRows: true,
        autoExpandColumn: 'Position',
        enableHdMenu: true,
        enableColumnHide: true,
        //selModel: new Ext.grid.RowSelectionModel({singleSelect: true}),
        height: 300,
        frame: true,
        iconCls: 'icon-grid'
    });
    internal_logGrid.loadMask.msg='正在加载数据，请稍后...';
    /*var external_record = new Ext.data.Record.create([
        {name:'externalLog',mapping:'externalLog'}
    ]);
    var external_proxy = new Ext.data.HttpProxy({
    	url:"../../DownLoadAction_readRemoteLogName.action"
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
                                              	
    var external_logBoxM = new Ext.grid.CheckboxSelectionModel();   //复选框
    var external_logRowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var external_logColM = new Ext.grid.ColumnModel([
    	external_logBoxM,
    	external_logRowNumber,
        {header:"下载远程日志文件名",dataIndex:"fileName",align:'center',renderer : external_logDownloadShowUrl}
    ]);

    var external_logGrid = new Ext.grid.GridPanel({
    	plain:true,
    	animCollapse:true,
    	height:300,
    	loadMask:{msg:'正在加载数据，请稍后...'},
    	border:false,
    	collapsible:false,
    	cm:external_logColM,
    	sm:external_logBoxM,
    	store:external_store,
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
    	}
    });*/
    new Ext.Viewport({
    	border:false,
    	renderTo:Ext.getBody(),
        layout:'fit',
        items:[internal_logGrid]
        /*[{
        	layout:'column',
        	items:[
        		{height:setHeight(),border:false,items:[internal_logGrid],columnWidth:.5},
        		{height:setHeight(),border:false,items:[external_logGrid],columnWidth:.5}
        	]
    	}]*/
    });
});

function setHeight(){
	var h = document.body.clientHeight-8;
	return h;
}

function internal_logDownloadShowUrl(value){
	var type = 'internal_log';
	return "<a href='javascript:;'style='color: green;' onclick='download_log(\""+value+"\",\""+type+"\");'>"+value+"</a>";
}
function external_logDownloadShowUrl(value){
	var type = 'external_log';
	return "<a href='javascript:;' style='color: green;' onclick='download_log(\""+value+"\",\""+type+"\");'>"+value+"</a>";
}
function download_log(logName,type){
    if (!Ext.fly('MergeFiles')) {
        var frm = document.createElement('form');
        frm.id = 'MergeFiles';
        frm.name = id;
        frm.style.display = 'none';
        document.body.appendChild(frm);
    }
    Ext.Ajax.request({
        url: '../../DownLoadAction_download.action',
        params:{type:type,logName:logName },
        form: Ext.fly('MergeFiles'),
        method: 'POST',
        isUpload: true
    });
}

function clear_logs(grid,store){
    var grid = Ext.getCmp('grid.info');
    var recode = grid.getSelectionModel().getSelected();
    if(!recode){
        Ext.MessageBox.alert("信息","请选择清空日志条目！");
    }else{
    var fileName =recode.get("fileName");
    Ext.MessageBox.show({
        title:'信息',
        msg:"<font color='green'>确定要清空日志?</font>",
        animEl:'truncate.tb.info',
        buttons:{'ok':'确定','no':'取消'},
        icon:Ext.MessageBox.WARNING,
        closable:false,
        fn:function(e){
            if(e=='ok'){
                var formPanel = new Ext.form.FormPanel({
                    frame:true,
                    labelAlign:'right',
                    autoScroll:true,
                    labelWidth:100,
                    defaults:{
                        width:200,
                        allowBlank:false,
                        blankText:'该项不能为空！'
                    },
                    items:[{
                        id:'password.info',
                        fieldLabel:"请输入您的密码",
                        xtype:'textfield',
                        inputType:'password',
                        name:'password',
                        emptyText :'请输入您的密码'
                    }]
                });
                var win = new Ext.Window({
                    title:"提示信息",
                    width:400,
                    height:110,
                    layout:'fit',
                    modal:true,
                    items: [formPanel],
                    bbar:[
                        new Ext.Toolbar.Fill(),
                        new Ext.Button ({
                            id:'ok.info',
                            text : '确定',
                            allowDepress : false,
                            handler : function() {
                                if(formPanel.form.isValid()){
                                    var myMask = new Ext.LoadMask(Ext.getBody(),{
                                        msg : '正在处理,请稍后...',
                                        removeMask : true
                                    });
                                    myMask.show();
                                    var password = Ext.getCmp('password.info').getValue();
                                    Ext.Ajax.request({
                                        url : '../../DownLoadAction_clear.action',
                                        params : {password:password,fileName:fileName},
                                        method :'POST',
                                        success:function(r,o){
                                            myMask.hide();
                                            var respText = Ext.util.JSON.decode(r.responseText);
                                            var msg = respText.msg;
                                            Ext.MessageBox.show({
                                                title:'信息',
                                                width:250,
                                                msg:msg,
                                                animEl:'ok.info',
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
                        }),
                        new Ext.Button ({
                            allowDepress : false,
                            text : '关闭',
                            handler : function() {win.close();}
                        })
                    ]
                }).show();
            }
        }
    });
    }
}
