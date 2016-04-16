Ext.onReady(function () {

    Ext.BLANK_IMAGE_URL = '../../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var start = 0;
    var pageSize = 5;
    var toolbar = new Ext.Toolbar({
        plain: true,
        width: 350,
        height: 30,
        items: [
            {
                xtype: 'button',
                text: '添加证书链',
                iconCls: 'add',
                handler: function () {
                    addTrust(grid_panel, store);
                }
            },
            {
                xtype: 'button',
                text: '证书链认证配置',
                iconCls: 'set',
                handler: function () {
                    auth_Trust(grid_panel, store);
                }
            }/*,
            {
                xtype: 'button',
                text: '网关证书配置',
                iconCls: 'setting',
                handler: function () {
                    server_certificate();
                }
            }*/
        ]
    });
    var record = new Ext.data.Record.create([
        {name: 'id', mapping: 'id'},
        {name: 'file', mapping: 'file'},
        {name: 'name', mapping: 'name'},
        {name: 'not_before', mapping: 'not_before'},
        {name: 'not_after', mapping: 'not_after'},
        {name: 'subject', mapping: 'subject'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url: "../../TrustCertificateAction_find.action"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty: "total",
        root: "rows"/*,
         id:'id'*/
    }, record);
    var store = new Ext.data.GroupingStore({
        id: "store.info",
        proxy: proxy,
        reader: reader
    });
//    var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
    var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var colM = new Ext.grid.ColumnModel([
//        boxM,
        rowNumber,
        {header:"证书文件",dataIndex:"name",  align:'center',sortable:true,menuDisabled:true},
        {header: "证书主题", dataIndex: "subject", align: 'center', sortable: true, width:150,menuDisabled: true},
        {header: "有效起始日期", dataIndex: "not_before", align: 'center', sortable: true,width:120, menuDisabled: true},
        {header: "有效截止日期", dataIndex: "not_after", align: 'center', sortable: true, width:120,menuDisabled: true},
        {header: "证书内容", dataIndex: "context", align: 'center', sortable: true, menuDisabled: true, renderer: show_trust},
        {
            header: '操作',
            dataIndex: "flag",
            align: 'center',
            sortable: true,
            menuDisabled: true,
            renderer: show_trust_flag
        }
    ]);
    var page_toolbar = new Ext.PagingToolbar({
        pageSize: pageSize,
        store: store,
        displayInfo: true,
        displayMsg: "显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg: "没有记录",
        beforePageText: "当前页",
        afterPageText: "共{0}页"
    });
    var grid_panel = new Ext.grid.GridPanel({
        id: 'trust.grid.info',
        //title: '证书链管理',
        plain: true,
        viewConfig: {
            forceFit: true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle: 'width:100%',
        loadMask: {msg: '正在加载数据，请稍后...'},
        cm: colM,
        //sm: boxM,
        store: store,
        tbar: toolbar,
        bbar: page_toolbar,
        //title: '资源配置',
        columnLines: true,
        autoScroll: true,
        border: false,
        collapsible: false,
        stripeRows: true,
        autoExpandColumn: 'Position',
        enableHdMenu: true,
        enableColumnHide: true,
        selModel: new Ext.grid.RowSelectionModel({singleSelect: true}),
        height: 300,
        frame: true,
        iconCls: 'icon-grid'
    });


    /*var panel = new Ext.Panel({
        plain: true,
        autoWidth: true,
        autoHeight: true,
        border: false,
        items: [
            {
                id: 'certificate.info',
                xtype: 'fieldset',
                //title: '证书管理',
                items: [{
                    id: 'trust.info',
                    xtype: 'fieldset',
                    title: '证书链管理',
                    items: [grid_panel]
                }]
            }
        ]
    });*/
 /*   new Ext.Viewport({
        layout: 'fit',
        renderTo: Ext.getBody(),
        autoScroll: true,
        items: [
            {
                frame: true,
                title: '证书链管理',
                autoScroll: true,
                items: [grid_panel]
            }
        ]
    });*/

    var port = new Ext.Viewport({
        layout: 'fit',
        renderTo: Ext.getBody(),
        items: [grid_panel]
    });

    store.load({
        params: {
            start: start, limit: pageSize
        }
    });
});

function addTrust(grid_panel, store) {
    var uploadWarForm = new Ext.form.FormPanel({
        frame: true,
        labelWidth: 150,
        labelAlign: 'right',
        fileUpload: true,
        border: false,
        defaults: {
            width: 200,
            allowBlank: false,
            blankText: '该项不能为空！'
        },
        items: [{
            xtype: 'displayfield',
            fieldLabel: '注意',
            value: '上传证书为PEM格式'
        }, {
            id: 'crtFile',
            name: 'crtFile',
            xtype: 'textfield',
            inputType: 'file',
            fieldLabel: "上传",
            listeners: {
                render: function () {
                    Ext.get('crtFile').on("change", function () {
                        var file = this.getValue();
                        var fs = file.split('.');
                        if (fs[fs.length - 1].toLowerCase() == 'pem' || fs[fs.length - 1].toLowerCase() == 'cer' || fs[fs.length - 1].toLowerCase() == 'crt') {
                            Ext.MessageBox.show({
                                title: '信息',
                                msg: '<font color="green">确定要上传文件:' + file + '？</font>',
                                width: 300,
                                buttons: {'ok': '确定', 'no': '取消'},
                                icon: Ext.MessageBox.WARNING,
                                closable: false,
                                fn: function (e) {
                                    if (e == 'ok') {
                                        if (uploadWarForm.form.isValid()) {
                                            uploadWarForm.getForm().submit({
                                                url: '../../TrustCertificateAction_upload.action',
                                                method: 'POST',
                                                waitTitle: '系统提示',
                                                waitMsg: '正在上传,请稍后...',
                                                success: function (form, action) {
                                                    var msg = action.result.msg;
                                                    Ext.MessageBox.show({
                                                        title: '信息',
                                                        width: 250,
                                                        msg: msg,
//                                                    animEl:'insert.win.info',
                                                        buttons: {'ok': '确定', 'no': '取消'},
                                                        icon: Ext.MessageBox.INFO,
                                                        closable: false,
                                                        fn: function (e) {
                                                            if (e == 'ok') {
                                                                grid_panel.render();
                                                                store.reload();
                                                                win.close();
                                                            } else {
                                                                Ext.getCmp('crtFile').setValue('');
                                                            }
                                                        }
                                                    });
                                                },
                                                failure: function (form, action) {
                                                    var msg = action.result.msg;
                                                    Ext.MessageBox.show({
                                                        title: '信息',
                                                        width: 250,
                                                        msg: msg,
//                                                    animEl:'insert.win.info',
                                                        buttons: {'ok': '确定', 'no': '取消'},
                                                        icon: Ext.MessageBox.INFO,
                                                        closable: false,
                                                        fn: function (e) {
                                                            if (e == 'ok') {
                                                                Ext.getCmp('crtFile').setValue('');
                                                            } else {
                                                                Ext.getCmp('crtFile').setValue('');
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        } else {
                                            Ext.MessageBox.show({
                                                title: '信息',
                                                width: 200,
                                                msg: '请填写完成再提交!',
//                                            animEl:'insert.win.info',
                                                buttons: {'ok': '确定'},
                                                icon: Ext.MessageBox.ERROR,
                                                closable: false
                                            });
                                        }
                                    }
                                }
                            });
                        } else {
                            Ext.MessageBox.show({
                                title: '信息',
                                width: 200,
                                msg: '上传文件格式不对,请重新选择!',
                                buttons: {'ok': '确定'},
                                icon: Ext.MessageBox.ERROR,
                                closable: false,
                                fn: function (e) {
                                    if (e == 'ok') {
                                        Ext.getCmp('crtFile').setValue('');
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
        title: '上传证书链',
        width: 400,
        height: 150,
        layout: 'fit',
        modal: true,
        items: [uploadWarForm],
        bbar: ['->', {
            text: '关闭',
            handler: function () {
                win.close();
            }
        }]
    }).show();
}

function show_trust() {
    return String.format(
        '<a id="show_trust.info" href="javascript:void(0);" onclick="show_trust_fun();return false;" style="color: green;">查看</a>&nbsp;&nbsp;&nbsp;'
    );
}

function show_trust_flag() {
    return String.format(
        '<a id="show_flag.info" href="javascript:void(0);" onclick="show_trust_flag_fun();return false;" style="color: green;">下载</a>&nbsp;&nbsp;&nbsp;' +
        '<a id="remove_trust_flag_fun.info" href="javascript:void(0);" onclick="remove_trust_flag_fun();return false;" style="color: green;">删除</a>&nbsp;&nbsp;&nbsp;'
    );
}

function show_trust_fun() {
    var grid = Ext.getCmp('trust.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    var file = recode.get("file");

    var record = new Ext.data.Record.create([
        {name: 'name', mapping: 'name'},
        {name: 'content', mapping: 'content'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url: "../../TrustCertificateAction_findTrustCertificate.action?file=" + file
    });

    var reader = new Ext.data.JsonReader({
        totalProperty: "total",
        root: "rows"
    }, record);

    var store = new Ext.data.GroupingStore({
        id: "store.info",
        proxy: proxy,
        reader: reader
    });
    store.load();


//    var boxM = new Ext.grid.CheckboxSelectionModel({singleSelect:true});   //复选框单选

    var rowNumber = new Ext.grid.RowNumberer();         //自动编号

    var colM = new Ext.grid.ColumnModel([
//        boxM,
        rowNumber,
        {header: "证书项", dataIndex: "name", align: 'center', width: 50, sortable: true, menuDisabled: true},
        {header: "详细信息", dataIndex: 'content', align: 'center', sortable: true, menuDisabled: true}
    ]);

    var page_toolbar = new Ext.PagingToolbar({
        store: store,
        displayInfo: true,
        displayMsg: "显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg: "没有记录",
        beforePageText: "当前页",
        afterPageText: "共{0}页"
    });
    var list_grid_panel = new Ext.grid.GridPanel({
        id: 'grid.downloadList.info',
        plain: true,
        height: 250,
        viewConfig: {
            forceFit: true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle: 'width:100%',
        loadMask: {msg: '正在加载数据，请稍后...'},
        border: true,
        columnLines: true,
        cm: colM,
//        sm:boxM,
        store: store,
        //tbar : tb,
        bbar: page_toolbar
    });
    var win = new Ext.Window({
        title: "证书信息",
        width: 500,
        layout: 'fit',
        height: 250,
        modal: true,
        items: list_grid_panel
    }).show();
}

function show_trust_flag_fun() {
    if (!Ext.fly('MergeFiles')) {
        var frm = document.createElement('form');
        frm.id = 'MergeFiles';
        frm.name = id;
        frm.style.display = 'none';
        document.body.appendChild(frm);
    }
    ;
    var grid = Ext.getCmp('trust.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    var id = recode.get("id");
    Ext.Ajax.request({
        url: '../../TrustCertificateAction_download.action',
        timeout: 20 * 60 * 1000,
        params: {id: id},
        form: Ext.fly('MergeFiles'),
        method: 'POST',
        isUpload: true
    });
}

function remove_trust_flag_fun() {
    var grid = Ext.getCmp('trust.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    if (!recode) {
        Ext.Msg.alert("提示", "请选择一条记录!");
    } else {
        Ext.Msg.confirm("提示", "确认删除吗？", function (sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url: '../../TrustCertificateAction_remove.action',
                    timeout: 20 * 60 * 1000,
                    method: "POST",
                    params: {id: recode.get("id")},
                    success: function (r, o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                        grid.getStore().reload();
                    },
                    failure: function (r, o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                    }
                });
            }
        });
    }
}

function auth_Trust(grid, store) {
    var start = 0;
    var pageSize = 5;
    var record = new Ext.data.Record.create([
        {name: 'id', mapping: 'id'},
        {name: 'file', mapping: 'file'},
        {name: 'name', mapping: 'name'},
        {name: 'status', mapping: 'status'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url: "../../TrustCertificateAction_find.action"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty: "total",
        root: "rows"/*,
         id:'id'*/
    }, record);
    var store = new Ext.data.GroupingStore({
        id: "store.info",
        proxy: proxy,
        reader: reader,
        listeners: {
            load: function () {
                var records = [];//存放选中记录
                for (var i = 0; i < store.getCount(); i++) {
                    var record = store.getAt(i);
                    if (record.data.status == 1) {//根据后台数据判断那些记录默认选中
                        records.push(record);
                    }
                }
                boxM.selectRecords(records);//执行选中记录
            }
        }
    });
    var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
    var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var colM = new Ext.grid.ColumnModel([
        boxM,
        rowNumber,
        {header: "证书名称", dataIndex: "name", align: 'center', sortable: true, menuDisabled: true},
        {
            header: "证书信息",
            dataIndex: "context",
            align: 'center',
            sortable: true,
            menuDisabled: true,
            renderer: auth_trust_config
        }
    ]);
    var page_toolbar = new Ext.PagingToolbar({
        pageSize: pageSize,
        store: store,
        displayInfo: true,
        displayMsg: "显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg: "没有记录",
        beforePageText: "当前页",
        afterPageText: "共{0}页"
    });
    var auth_panel = new Ext.grid.GridPanel({
        id: 'auth.grid.info',
        //title: '认证配置',
//        height:Ext.getBody().getHeight()/3,
        plain: true,
//        autoHeight:true,
//        height:setHeight(),
        viewConfig: {
            forceFit: true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle: 'width:100%',
        loadMask: {msg: '正在加载数据，请稍后...'},
        //border: true,
        cm: colM,
        sm: boxM,
        store: store/*,
         tbar : toolbar,
         listeners:{
         render:function(){
         tbar.render(this.tbar);
         }
         }*/,
        bbar: page_toolbar,
        columnLines: true,
        autoScroll: true,
        border: false,
        collapsible: false,
        stripeRows: true,
        //autoExpandColumn: 'Position',
        enableHdMenu: true,
        enableColumnHide: true,
        //selModel: new Ext.grid.RowSelectionModel({singleSelect: true}),
        height: 300,
        frame: true/*,
        iconCls: 'icon-grid'*/
    });

    var win = new Ext.Window({
        width: 600,
        layout: 'fit',
        title: '证书链认证配置',
        height: 300,
        modal: true,
        items: auth_panel,
        bbar: [
            '->',
            {
                id: 'insert_win.info',
                text: '保存配置',
                handler: function () {
                    var selected = new Array();
                    var recode = Ext.getCmp("auth.grid.info").getSelectionModel().getSelections();
                    for (var i = 0; i < recode.length; i++) {
                        selected[i] = recode[i].get("id");
                    }
                    Ext.Ajax.request({
                        url: '../../TrustCertificateAction_update.action',
                        params: {ids: selected},
                        timeout: 20 * 60 * 1000,
                        method: "POST",
                        success: function (form, action) {
                            Ext.MessageBox.show({
                                title:'信息',
                                width:250,
                                msg:"更新配置成功",
                                buttons:Ext.MessageBox.OK,
                                buttons:{'ok':'确定'},
                                icon:Ext.MessageBox.INFO,
                                closable:false,
                                fn:function(e){
                                    store.reload();
                                    win.close();
                                }
                            });
                        },
                        failure: function (result) {
                            Ext.Msg.alert("提示", "更新配置失败!");
                        }
                    });
                }
            }
        ]
    }).show();

    store.load();
}

function setHeight() {
    var h = document.body.clientHeight - 8;
    return h;
}

function setWidth() {
    return document.body.clientWidth - 8;
}

function auth_trust_config() {
    return String.format(
        '<a id="show_auth_trust_config.info" href="javascript:void(0);" onclick="show_auth_trust_config();return false;" style="color: green;">查看详细</a>&nbsp;&nbsp;&nbsp;'
    );
}

function show_auth_trust_config() {
    var grid = Ext.getCmp('auth.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    var file = recode.get("file");

    var record = new Ext.data.Record.create([
        {name: 'name', mapping: 'name'},
        {name: 'content', mapping: 'content'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url: "../../TrustCertificateAction_findTrustCertificate.action?file=" + file
    });

    var reader = new Ext.data.JsonReader({
        totalProperty: "total",
        root: "rows"
    }, record);

    var store = new Ext.data.GroupingStore({
        id: "store.info",
        proxy: proxy,
        reader: reader
    });
    store.load();


//    var boxM = new Ext.grid.CheckboxSelectionModel({singleSelect:true});   //复选框单选

    var rowNumber = new Ext.grid.RowNumberer();         //自动编号

    var colM = new Ext.grid.ColumnModel([
//        boxM,
        rowNumber,
        {header: "证书项", dataIndex: "name", width: 50, sortable: true, menuDisabled: true},
        {header: "详细信息", dataIndex: 'content', sortable: true, menuDisabled: true}
    ]);

    var page_toolbar = new Ext.PagingToolbar({
        store: store,
        displayInfo: true,
        displayMsg: "显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg: "没有记录",
        beforePageText: "当前页",
        afterPageText: "共{0}页"
    });
    var list_grid_panel = new Ext.grid.GridPanel({
        id: 'grid.downloadList.info',
        plain: true,
        height: 250,
        viewConfig: {
            forceFit: true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle: 'width:100%',
        loadMask: {msg: '正在加载数据，请稍后...'},
        border: true,
        columnLines: true,
        cm: colM,
//        sm:boxM,
        store: store,
        //tbar : tb,
        bbar: page_toolbar
    });
    var win = new Ext.Window({
        title: "证书信息",
        width: 500,
        layout: 'fit',
        height: 250,
        modal: true,
        items: list_grid_panel
    }).show();
}


function server_certificate(){
    var server_certificate_start =0;
    var server_certificate_pageSize = 5;
    var server_certificate_toolbar = new Ext.Toolbar({
        plain:true,
        width:350,
        items:[
            {
                id:'add_server.info',
                xtype:'button',
                iconCls: 'add',
                text:'添加网关证书',
                handler: function () {
                    add_pfx_server(server_certificate_store);
                }
            },
            {
                id:'update_server.info',
                xtype:'button',
                iconCls: 'add',
                text:'网关证书认证配置',
                handler: function () {
                    auth_server_pfx();
                }
            }
        ]
    });
    var server_certificate_record = new Ext.data.Record.create([
        {name:'id',			mapping:'id'},
        {name:'certificate',			mapping:'certificate'},
        {name:'name',			mapping:'name'} ,
        {name:'status',			mapping:'status'}
    ]);
    var server_certificate_proxy = new Ext.data.HttpProxy({
        url:"../../CertificateAction_find.action"
    });
    var server_certificate_reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows"/*,
         id:'id'*/
    },server_certificate_record);
    var server_certificate_store = new Ext.data.GroupingStore({
        id:"server.store.info",
        proxy : server_certificate_proxy,
        reader : server_certificate_reader
    });
//    var server_certificate_boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
    var server_certificate_rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var server_certificate_colM = new Ext.grid.ColumnModel([
//        server_certificate_boxM,
        server_certificate_rowNumber,
//        {header:"证书文件",			dataIndex:"pkcs_name",  align:'center',sortable:true,menuDisabled:true},
        {header:"证书名称",			dataIndex:"name",  align:'center',sortable:true,menuDisabled:true},
        {header:"证书内容",			dataIndex:"content",  align:'center',sortable:true,menuDisabled:true,renderer:show_server},
        {header:'动作',		dataIndex:"flag",	  align:'center',sortable:true,menuDisabled:true, renderer:show_server_certificate_flag,	width:100}
    ]);
    var server_certificate_page_toolbar = new Ext.PagingToolbar({
        pageSize : server_certificate_pageSize,
        store:server_certificate_store,
        displayInfo:true,
        displayMsg:"显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg:"没有记录",
        beforePageText:"当前页",
        afterPageText:"共{0}页"
    });
    var server_certificate_grid_panel = new Ext.grid.GridPanel({
        id:'server.grid.info',
        //title:'服务器证书',
        plain:true,
        //height:Ext.getBody().getHeight()/2,
        //autoHeight:true,
        height: 250,
//        height:setHeight(),
        viewConfig:{
            forceFit:true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle:'width:100%',
        loadMask:{msg:'正在加载数据，请稍后...'},
        border:true,
        cm:server_certificate_colM,
//        sm:server_certificate_boxM,
        store:server_certificate_store,
        tbar : server_certificate_toolbar,/*
         listeners:{
         render:function(){
         tbar.render(this.tbar);
         }
         },*/
        bbar:server_certificate_page_toolbar
    });

    var win = new Ext.Window({
        title: "网关证书",
        width: 600,
        layout: 'fit',
        height: 300,
        modal: true,
        items: server_certificate_grid_panel
    }).show();

    server_certificate_store.load({
        params:{
            start:server_certificate_start,limit:server_certificate_pageSize
        }
    });
}

function show_server_certificate_flag(){
    return String.format(
        '<a id="del_server_flag_fun.info" href="javascript:void(0);" onclick="del_server_certificate_flag_fun();return false;" style="color: green;">删除</a>&nbsp;&nbsp;&nbsp;'+
        '<a id="down_server_flag_fun.info" href="javascript:void(0);" onclick="down_server_certificate_flag_fun();return false;" style="color: green;">下载</a>&nbsp;&nbsp;&nbsp;'
    );
}

 function add_pfx_server(server_certificate_store){
    var upload_pfx_form = new Ext.form.FormPanel({
        frame:true,
        labelWidth:150,
        labelAlign:'right',
        fileUpload:true,
        border:false,
        defaults : {
            width : 200,
            allowBlank : false,
            blankText : '该项不能为空！'
        },
        items:[{
            id:'serverPfxFile',
            fieldLabel:"(注:格式为PKCS)",
            width:200,
            name : 'serverPfxFile',
            xtype:'textfield',
            inputType:'file',
            allowBlank:false,
            regexText:'(注:格式为PKCS)',
            listeners:{
                render:function () {
                    Ext.get('serverPfxFile').on("change", function () {
                        var file = this.getValue();
                        var fs = file.split('.');
                        if (fs[fs.length - 1].toLowerCase() != 'p12' && fs[fs.length - 1].toLowerCase() != 'pfx') {
                            Ext.MessageBox.show({
                                title:'信息',
                                width:200,
                                msg:'上传文件格式不对,请重新选择!',
                                buttons:{'ok':'确定'},
                                icon:Ext.MessageBox.ERROR,
                                closable:false,
                                fn:function (e) {
                                    if (e == 'ok') {
                                        Ext.getCmp('serverPfxFile').setValue('');
                                    }
                                }
                            });
                        }
                    });
                }
            }
        },{
            fieldLabel:'证书密码',
            name:'pwd',
            xtype:'textfield',
            width:200,
            inputType:'password',
            regex:/^\S{4,20}$/
        }]
    });
    var win = new Ext.Window({
        title:'更新网关证书',
        width:500,
        height:200,
        layout:'fit',
        modal:true,
        items:[upload_pfx_form],
        bbar:['->',{
            id:'addpfx_win.info',
            text:'更新',
            width:50,
            handler:function(){
                if (upload_pfx_form.form.isValid()) {
                    upload_pfx_form.getForm().submit({
                        url :'../../CertificateAction_upload.action',
                        timeout: 20*60*1000,
                        method :'POST',
                        waitTitle :'系统提示',
                        waitMsg :'正在连接...',
                        success : function(form, action) {
                            var msg = action.result.msg;
                            Ext.MessageBox.show({
                                title: '信息',
                                width: 250,
                                msg: msg,
                                buttons: {'ok': '确定', 'no': '取消'},
                                icon: Ext.MessageBox.INFO,
                                closable: false,
                                fn: function (e) {
                                    if (e == 'ok') {
                                        server_certificate_store.reload();
                                        win.close();
                                    } else {
                                        Ext.getCmp('crtFile').setValue('');
                                    }
                                }
                            });
                        },
                        failure : function(form, action) {
                            var msg = action.result.msg;
                            Ext.MessageBox.show({
                                title:'信息',
                                width:250,
                                msg:msg,
                                buttons:Ext.MessageBox.OK,
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
                        buttons:Ext.MessageBox.OK,
                        buttons:{'ok':'确定'},
                        icon:Ext.MessageBox.ERROR,
                        closable:false
                    });
                }
            }
        },{
            text:'重置',
            width:50,
            handler:function(){
                upload_pfx_form.getForm().reset();
            }
        }]
    }).show();
}

function del_server_certificate_flag_fun(){
    var grid = Ext.getCmp('server.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    if (!recode) {
        Ext.Msg.alert("提示", "请选择一条记录!");
    } else {
        Ext.Msg.confirm("提示", "确认删除吗？", function (sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url: '../../CertificateAction_remove.action',
                    timeout: 20 * 60 * 1000,
                    method: "POST",
                    params: {id: recode.get("id")},
                    success: function (r, o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                        grid.getStore().reload();
                    },
                    failure: function (r, o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                    }
                });
            }
        });
    }
}

function auth_server_pfx(){
    var start = 0;
    var pageSize = 5;
    var record = new Ext.data.Record.create([
        {name: 'id', mapping: 'id'},
        {name: 'file', mapping: 'file'},
        {name: 'name', mapping: 'name'},
        {name: 'status', mapping: 'status'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url: "../../CertificateAction_find.action"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty: "total",
        root: "rows"/*,
         id:'id'*/
    }, record);
    var store = new Ext.data.GroupingStore({
        id: "store.info",
        proxy: proxy,
        reader: reader,
        listeners: {
            load: function () {
                var records = [];//存放选中记录
                for (var i = 0; i < store.getCount(); i++) {
                    var record = store.getAt(i);
                    if (record.data.status == 1) {//根据后台数据判断那些记录默认选中
                        records.push(record);
                    }
                }
                boxM.selectRecords(records);//执行选中记录
            }
        }
    });
    var boxM = new Ext.grid.CheckboxSelectionModel({singleSelect: true});   //复选框
    var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var colM = new Ext.grid.ColumnModel([
        boxM,
        rowNumber,
        {header: "证书名称", dataIndex: "name", align: 'center', sortable: true, menuDisabled: true},
        {
            header: "证书信息",
            dataIndex: "context",
            align: 'center',
            sortable: true,
            menuDisabled: true,
            renderer: auth_gateway_config
        }
    ]);
    var page_toolbar = new Ext.PagingToolbar({
        pageSize: pageSize,
        store: store,
        displayInfo: true,
        displayMsg: "显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg: "没有记录",
        beforePageText: "当前页",
        afterPageText: "共{0}页"
    });
    var auth_panel = new Ext.grid.GridPanel({
        id: 'auth.server.grid.info',
        //title: '认证配置',
//        height:Ext.getBody().getHeight()/3,
        plain: true,
//        autoHeight:true,
//        height:setHeight(),
        viewConfig: {
            forceFit: true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle: 'width:100%',
        loadMask: {msg: '正在加载数据，请稍后...'},
        //border: true,
        cm: colM,
        sm: boxM,
        store: store/*,
         tbar : toolbar,
         listeners:{
         render:function(){
         tbar.render(this.tbar);
         }
         }*/,
        bbar: page_toolbar,
        columnLines: true,
        autoScroll: true,
        border: false,
        collapsible: false,
        stripeRows: true,
        //autoExpandColumn: 'Position',
        enableHdMenu: true,
        enableColumnHide: true,
        //selModel: new Ext.grid.RowSelectionModel({singleSelect: true}),
        height: 300,
        frame: true/*,
         iconCls: 'icon-grid'*/
    });

    var win = new Ext.Window({
        width: 500,
        layout: 'fit',
        title: '网关认证配置',
        height: 250,
        modal: true,
        items: auth_panel,
        bbar: [
            '->',
            {
                id: 'insert_win.info',
                text: '保存配置',
                handler: function () {
                    var selected = new Array();
                    var recode = Ext.getCmp("auth.server.grid.info").getSelectionModel().getSelected();
                    Ext.Ajax.request({
                        url: '../../CertificateAction_update.action',
                        params: {id: recode.get("id")},
                        timeout: 20 * 60 * 1000,
                        method: "POST",
                        success: function (form, action) {
                            Ext.MessageBox.show({
                                title:'信息',
                                width:250,
                                msg:"更新配置成功",
                                buttons:Ext.MessageBox.OK,
                                buttons:{'ok':'确定'},
                                icon:Ext.MessageBox.INFO,
                                closable:false,
                                fn:function(e){
                                    store.reload();
                                    win.close();
                                }
                            });
                        },
                        failure: function (result) {
                            Ext.Msg.alert("提示", "更新配置失败!");
                        }
                    });
                }
            }
        ]
    }).show();

    store.load();
}

function auth_gateway_config(){
    return String.format(
    '<a id="auth_server.info" href="javascript:void(0);" onclick="auth_server_fun();return false;" style="color: green;">查看</a>&nbsp;&nbsp;&nbsp;'
    );
}

function down_server_certificate_flag_fun(){
    if (!Ext.fly('test')) {
        var frm = document.createElement('form');
        frm.id = 'test';
        frm.name = id;
        frm.style.display = 'none';
        document.body.appendChild(frm);
    }
    ;
    var grid = Ext.getCmp('server.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    var id = recode.get("id");
    Ext.Ajax.request({
        url: '../../CertificateAction_download.action',
        timeout: 20 * 60 * 1000,
        params: {id: id},
        form: Ext.fly('test'),
        method: 'POST',
        isUpload: true
    });
}

function show_server(){
    return String.format(
        '<a id="show_server.info" href="javascript:void(0);" onclick="show_server_fun();return false;" style="color: green;">查看</a>&nbsp;&nbsp;&nbsp;'
    );
}

function show_server_fun(){
    var grid = Ext.getCmp('server.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    var id = recode.get("id");

    var record = new Ext.data.Record.create([
        {name: 'name', mapping: 'name'},
        {name: 'content', mapping: 'content'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url: "../../CertificateAction_findServerCertificate.action?id=" + id
    });

    var reader = new Ext.data.JsonReader({
        totalProperty: "total",
        root: "rows"
    }, record);

    var store = new Ext.data.GroupingStore({
        id: "store.info",
        proxy: proxy,
        reader: reader
    });
    store.load();


//    var boxM = new Ext.grid.CheckboxSelectionModel({singleSelect:true});   //复选框单选

    var rowNumber = new Ext.grid.RowNumberer();         //自动编号

    var colM = new Ext.grid.ColumnModel([
//        boxM,
        rowNumber,
        {header: "证书项", dataIndex: "name", align: 'center', width: 50, sortable: true, menuDisabled: true},
        {header: "详细信息", dataIndex: 'content', align: 'center', sortable: true, menuDisabled: true}
    ]);

    var page_toolbar = new Ext.PagingToolbar({
        store: store,
        displayInfo: true,
        displayMsg: "显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg: "没有记录",
        beforePageText: "当前页",
        afterPageText: "共{0}页"
    });
    var list_grid_panel = new Ext.grid.GridPanel({
        id: 'grid.server.downloadList.info',
        plain: true,
        height: 250,
        viewConfig: {
            forceFit: true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle: 'width:100%',
        loadMask: {msg: '正在加载数据，请稍后...'},
        border: true,
        columnLines: true,
        cm: colM,
//        sm:boxM,
        store: store,
        //tbar : tb,
        bbar: page_toolbar
    });
    var win = new Ext.Window({
        title: "证书信息",
        width: 500,
        layout: 'fit',
        height: 250,
        modal: true,
        items: list_grid_panel
    }).show();
}


function auth_server_fun(){
    var grid = Ext.getCmp('auth.server.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    var id = recode.get("id");

    var record = new Ext.data.Record.create([
        {name: 'name', mapping: 'name'},
        {name: 'content', mapping: 'content'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url: "../../CertificateAction_findServerCertificate.action?id=" + id
    });

    var reader = new Ext.data.JsonReader({
        totalProperty: "total",
        root: "rows"
    }, record);

    var store = new Ext.data.GroupingStore({
        id: "store.info",
        proxy: proxy,
        reader: reader
    });
    store.load();


//    var boxM = new Ext.grid.CheckboxSelectionModel({singleSelect:true});   //复选框单选

    var rowNumber = new Ext.grid.RowNumberer();         //自动编号

    var colM = new Ext.grid.ColumnModel([
//        boxM,
        rowNumber,
        {header: "证书项", dataIndex: "name", align: 'center', width: 50, sortable: true, menuDisabled: true},
        {header: "详细信息", dataIndex: 'content', align: 'center', sortable: true, menuDisabled: true}
    ]);

    var page_toolbar = new Ext.PagingToolbar({
        store: store,
        displayInfo: true,
        displayMsg: "显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg: "没有记录",
        beforePageText: "当前页",
        afterPageText: "共{0}页"
    });
    var list_grid_panel = new Ext.grid.GridPanel({
        id: 'grid.server.downloadList.info',
        plain: true,
        height: 250,
        viewConfig: {
            forceFit: true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle: 'width:100%',
        loadMask: {msg: '正在加载数据，请稍后...'},
        border: true,
        columnLines: true,
        cm: colM,
//        sm:boxM,
        store: store,
        //tbar : tb,
        bbar: page_toolbar
    });
    var win = new Ext.Window({
        title: "证书信息",
        width: 500,
        layout: 'fit',
        height: 250,
        modal: true,
        items: list_grid_panel
    }).show();
}