/**
 * Created by Administrator on 15-3-9.
 */
/**
 * 日志下载
 */
Ext.onReady(function () {
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

//    var pageStart = 0;
//    var pageSize = 15;
    var internal_record = new Ext.data.Record.create([
        {name: 'version', mapping: 'version'},
        {name: 'name', mapping: 'name'},
        {name: 'os', mapping: 'os'}
    ]);
    var internal_proxy = new Ext.data.HttpProxy({
        url: "../../ClientVersionAction_find.action"
    });
    var internal_reader = new Ext.data.JsonReader({
        totalProperty: "totalCount",
        root: "root"
    }, internal_record);
    var internal_store = new Ext.data.Store({
        proxy: internal_proxy,
        reader: internal_reader
    });
    internal_store.load();

    //var internal_logBoxM = new Ext.grid.CheckboxSelectionModel({singleSelect: true});   //复选框
    //var internal_logBoxM = new Ext.grid.RadioboxSelectionModel();
    var internal_logRowNumber = new Ext.grid.RowNumberer({singleSelect: true});         //自动 编号
    var internal_logColM = new Ext.grid.ColumnModel({
        columns: [
            //internal_logBoxM,
            internal_logRowNumber,
            {header: "系统", dataIndex: "os", align: 'center', sortable: true, menuDisabled: true},
            {header: "版本号", dataIndex: "version", align: 'center', sortable: true, menuDisabled: true},
            {
                header: "文件名称",
                dataIndex: "name",
                align: 'center',
                sortable: true,
                menuDisabled: true,
                renderer: internal_DownloadShowUrl
            }
        ],
        defaults: {sortable: false}//不允许客户端点击列头排序，可以打开
    });
    var internal_logGrid = new Ext.grid.GridPanel({
        id: 'grid.info',
        store: internal_store,
        cm: internal_logColM,
        //sm: internal_logBoxM,
        height: setHeight,
        columnLines: true,
        autoScroll: true,
//        frame:true,
        loadMask: true,
        border: false,
        collapsible: false,
        stripeRows: true,
        autoExpandColumn: 'Position',
        enableHdMenu: true,
        enableColumnHide: true,
        //bodyStyle: 'width:100%',
        //selModel: new Ext.grid.RowSelectionModel({singleSelect: true}),
        tbar: [{
            id: 'upload.zip.info',
            text: '客户端升级',
            iconCls: 'upload',
            handler: function () {
                upgradeZipRow(internal_store);
            }
        }],
        viewConfig: {
            forceFit: true
        }
    });
    internal_logGrid.loadMask.msg = '正在加载数据，请稍后...';
    new Ext.Viewport({
        border: false,
        renderTo: Ext.getBody(),
        layout: 'fit',
        items: [internal_logGrid]
    });
});

function setHeight() {
    var h = document.body.clientHeight - 8;
    return h;
}

function internal_DownloadShowUrl(value) {
    return "<a href='javascript:;'style='color: green;' onclick='download_log();'>" + value + "</a>";
}

function download_log() {
    var grid = Ext.getCmp('grid.info');
    var recode = grid.getSelectionModel().getSelected();
    var os = recode.get("os");
    var name = recode.get("name");
    if (!Ext.fly('MergeFiles')) {
        var frm = document.createElement('form');
        frm.id = 'MergeFiles';
        frm.name = id;
        frm.style.display = 'none';
        document.body.appendChild(frm);
    }
    Ext.Ajax.request({
        url: '../../ClientVersionAction_download.action',
        params: {os: os, name: name},
        form: Ext.fly('MergeFiles'),
        method: 'POST',
        isUpload: true
    });
}

function upgradeZipRow(internal_store) {
    var find_data = [
        {'id': 'android', 'name': 'android'},
        {'id': 'x64', 'name': 'x64'},
        {'id': 'linuxCz', 'name': 'linuxCz'},
        {'id': 'linuxMobile', 'name': 'linuxMobile'},
        {'id': 'x86', 'name': 'x86'}
    ];

    var find_store = new Ext.data.JsonStore({
        data: find_data,
        fields: ['id', 'name']
    });

    var uploadZipForm = new Ext.form.FormPanel({
        frame: true,
        labelWidth: 100,
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
            value: '上传文件为[*.zip]'
        },
            new Ext.form.ComboBox({
                border: true,
                frame: true,
                editable: false,
                fieldLabel: '客户端型号',
                id: 'modify.os',
                triggerAction: "all",// 是否开启自动查询功能
                store: find_store,// 定义数据源
                displayField: "name", // 关联某一个逻辑列名作为显示值
                valueField: "id", // 关联某一个逻辑列名作为显示值
                mode: "local",// 如果数据来自本地用local 如果来自远程用remote默认为remote
                name: 'os',
//                hiddenName: 'protocol',
                allowBlank: false,
                blankText: "请选择"
            }), {
                id: 'formFile',
                fieldLabel: "升级文件",
                name: 'uploadFile',
                xtype: 'textfield',
                inputType: 'file',
                listeners: {
                    render: function () {
                        Ext.get('formFile').on("change", function () {
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
                            if (fs[fs.length - 1].toLowerCase() == 'zip') {
                                Ext.MessageBox.show({
                                    title: '信息',
                                    msg: '<font color="green">确定要上传文件:' + file + '？</font>',
                                    width: 300,
                                    buttons: {'ok': '确定', 'no': '取消'},
                                    icon: Ext.MessageBox.WARNING,
                                    closable: false,
                                    fn: function (e) {
                                        if (e == 'ok') {
                                            if (uploadZipForm.form.isValid()) {
                                                uploadZipForm.getForm().submit({
                                                    url: '../../ClientVersionAction_upload.action',
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
                                                                    internal_store.reload();
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
        title: '客户端软件包',
        width: 400,
        height: 150,
        layout: 'fit',
        modal: true,
        items: [uploadZipForm],
        bbar: ['->', {
//    		id:'insert.win.info',
//    		text:'上传',
//    		handler:function(){
//
//    		}
//    	},{
            text: '关闭',
            handler: function () {
                win.close();
            }
        }]
    }).show();
}
