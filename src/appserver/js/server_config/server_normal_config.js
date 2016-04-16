Ext.onReady(function () {
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var record = new Ext.data.Record.create([
        {name: 'listen', mapping: 'listen'},
        {name: 'protocol', mapping: 'protocol'},
        {name: 'port', mapping: 'port'},
        {name: 'server_net', mapping: 'server_net'},
        {name: 'server_mask', mapping: 'server_mask'},
        {name: 'check_crl', mapping: 'check_crl'},
        {name: 'traffic_server', mapping: 'traffic_server'},
        {name: 'client_to_client', mapping: 'client_to_client'},
        {name: 'duplicate_cn', mapping: 'duplicate_cn'},
        {name: 'keep_alive', mapping: 'keep_alive'},
        {name: 'keep_alive_interval', mapping: 'keep_alive_interval'},
        {name: 'cipher', mapping: 'cipher'},
        {name: 'comp_lzo', mapping: 'comp_lzo'},
        {name: 'max_clients', mapping: 'max_clients'},
        {name: 'log_append', mapping: 'log_append'},
        {name: 'log_flag', mapping: 'log_flag'},
        {name: 'verb', mapping: 'verb'},
        {name: 'mute', mapping: 'mute'},
        {name: 'client_dns_type', mapping: 'client_dns_type'},
        {name: 'client_first_dns', mapping: 'client_first_dns'},
        {name: 'client_second_dns', mapping: 'client_second_dns'},
        {name: 'default_domain_suffix', mapping: 'default_domain_suffix'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url: '../../ServerSourceNetsAction_find.action'
    });

    var reader = new Ext.data.JsonReader({
        totalProperty: "totalCount",
        root: "root"
    }, record);

    var store = new Ext.data.GroupingStore({
        id: "store.info",
        proxy: proxy,
        reader: reader
    });

    store.load();
    store.on('load', function () {
        var protocol = store.getAt(0).get('protocol');
        var listen = store.getAt(0).get('listen');
        var port = store.getAt(0).get('port');
        var server_net = store.getAt(0).get('server_net');
        var server_mask = store.getAt(0).get('server_mask');
        var check_crl = store.getAt(0).get('check_crl');
        var traffic_server = store.getAt(0).get('traffic_server');
        var client_to_client = store.getAt(0).get('client_to_client');
        var duplicate_cn = store.getAt(0).get('duplicate_cn');
        var keep_alive = store.getAt(0).get('keep_alive');
        var keep_alive_interval = store.getAt(0).get('keep_alive_interval');
        var comp_lzo = store.getAt(0).get('comp_lzo');
        var log_append = store.getAt(0).get('log_append');
        var log_flag = store.getAt(0).get('log_flag');
        var verb = store.getAt(0).get('verb');
        var mute = store.getAt(0).get('mute');
        var client_dns_type = store.getAt(0).get('client_dns_type');
        var client_first_dns = store.getAt(0).get('client_first_dns');
        var client_second_dns = store.getAt(0).get('client_second_dns');
        Ext.getCmp('server_protocol').setValue(protocol);
        Ext.getCmp('server_listen').setValue(listen);
        Ext.getCmp('server_server_net').setValue(server_net);
        Ext.getCmp('server_server_mask').setValue(server_mask);
        Ext.getCmp('server_check_crl').setValue(check_crl);
        Ext.getCmp('server_port').setValue(port);
        Ext.getCmp('server_traffic_server').setValue(traffic_server);
        Ext.getCmp('server_client_to_client').setValue(client_to_client);
        Ext.getCmp('server_duplicate_cn').setValue(duplicate_cn);
        Ext.getCmp('server_keep_alive').setValue(keep_alive);
        Ext.getCmp('server_keep_alive_interval').setValue(keep_alive_interval);
        Ext.getCmp('server_comp_lzo').setValue(comp_lzo);
        Ext.getCmp('server_log_append').setValue(log_append);
        Ext.getCmp('server_log_flag').setValue(log_flag);
        Ext.getCmp('server_verb').setValue(verb);
        //Ext.getCmp('server_mute').setValue(keep_alive_interval);
        Ext.getCmp('server_client_dns_type').setValue(client_dns_type);
        Ext.getCmp('server_client_first_dns').setValue(client_first_dns);
        Ext.getCmp('server_client_second_dns').setValue(client_second_dns);
    });

    var interface = new Ext.data.JsonStore({
        fields: ["eth", "interface"],
        url: '../../InterfaceManagerAction_readInterfaceAll.action',
        autoLoad: true,
        root: "rows",
        listeners: {
            load: function (store, records, options) {// 读取完数据后设定默认值
                var value = Ext.getCmp("server_listen").getValue();
                Ext.getCmp("server_listen").setValue(value);
            }
        }
    });

    var find_data = [
        {'id': 'tcp', 'name': 'TCP'},
        {'id': 'udp', 'name': 'UDP'}
    ];

    var find_store = new Ext.data.JsonStore({
        data: find_data,
        fields: ['id', 'name']
    });

    var check_data = [
        {'id': 1, 'name': '验证黑名单'},
        {'id': 0, 'name': '不验证黑名单'}
    ];

    var check_store = new Ext.data.JsonStore({
        data: check_data,
        fields: ['id', 'name']
    });

    var level_data = [
        {'id': 5, 'name': '错误'},
        {'id': 4, 'name': '警告'},
        {'id': 3, 'name': '普通'}
    ];

    var level_store = new Ext.data.JsonStore({
        data: level_data,
        fields: ['id', 'name']
    });

    var log_data = [
        {'id': 0, 'name': '覆盖记录'},
        {'id': 1, 'name': '追加记录'}
    ];

    var log_store = new Ext.data.JsonStore({
        data: log_data,
        fields: ['id', 'name']
    });

    var formPanel = new Ext.form.FormPanel({
        frame: true,
        autoWidth: true,
        //title: '基本参数',
        bodyPadding: 5,
        plain: true,
        labelAlign: 'right',
        buttonAlign: 'center',
        labelWidth: 180,
        defaultType: 'textfield',
        defaults: {
            anchor: '80%',
            allowBlank: false,
            blankText: '该项不能为空!'
        },
        items: [
            new Ext.form.ComboBox({
                hiddenName: 'server.listen',
                id: 'server_listen',
                fieldLabel: "监听地址",
                emptyText: '监听地址',
                store: interface,
                valueField: "eth",
                displayField: "interface",
                triggerAction: "all",
                allowBlank: false
            }),
            new Ext.form.ComboBox({
                border: true,
                frame: true,
                editable: false,
                emptyText: '通迅协议',
                fieldLabel: '通迅协议',
                id: 'server_protocol',
                triggerAction: "all",// 是否开启自动查询功能
                store: find_store,// 定义数据源
                displayField: "name", // 关联某一个逻辑列名作为显示值
                valueField: "id", // 关联某一个逻辑列名作为显示值
                mode: "local",// 如果数据来自本地用local 如果来自远程用remote默认为remote
//                name: 'protocol',
                hiddenName: 'server.protocol',
                allowBlank: false,
                blankText: "请选择"
            }),
            {
                fieldLabel: '监听端口',
                name: 'server.port',
                emptyText: '请输入端口号(0~65535)',
                regexText: '请输入端口号(0~65535)',
                id: "server_port",
                allowBlank: false,
                blankText: "请输入端口号(0~65535)",
                regex: /^(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$/,
                allowBlank: false,
                listeners: {
                    blur: function () {
                        var cmp = this;
                        var value = cmp.getValue();
                        if ((0 <= value && value <= 1024) || value == 8000 || value == 8080 || value == 8443 || value >= 65535) {
                            Ext.MessageBox.show({
                                title: '提示',
                                width: 400,
                                msg: '0-1024端口可能被系统占用,且端口不能为8000,8080,8443出产服务已监听,且端口不能大于65535!',
                                buttons: Ext.MessageBox.OK,
                                buttons: {'ok': '确定'},
                                icon: Ext.MessageBox.INFO,
                                closable: false,
                                fn: function (e) {
                                    if (e == 'ok') {
                                        cmp.setValue('');
                                    }
                                }
                            });
                        }
                    }
                }
            },
            {
                labelAlign: 'right',
                xtype: 'textfield',
                id: 'server_server_net',
                name: 'server.server_net',
                regex: /^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                regexText: '这个不是IP',
                emptyText: '虚拟网网段',
                fieldLabel: '虚拟网网段'
            },
            {
                fieldLabel: '虚拟网掩码',
                emptyText: '虚拟网掩码',
                xtype: 'textfield',
                id: 'server_server_mask',
                regex: /^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                name: 'server.server_mask'
            },
            new Ext.form.ComboBox({
                border: true,
                frame: true,
                editable: false,
                emptyText: '黑名单验证',
                fieldLabel: '黑名单验证',
                id: 'server_check_crl',
                triggerAction: "all",// 是否开启自动查询功能
                store: check_store,// 定义数据源
                displayField: "name", // 关联某一个逻辑列名作为显示值
                valueField: "id", // 关联某一个逻辑列名作为显示值
                mode: "local",// 如果数据来自本地用local 如果来自远程用remote默认为remote
//                name: 'protocol',
                hiddenName: 'server.check_crl',
                allowBlank: false,
                blankText: "请选择"
            }),
            {
                xtype: 'checkboxgroup',
                fieldLabel: '客户端',
                columns: 2,
                allowBlank: true,
                items: [
                    {
                        boxLabel: '客户端网关', id: 'server_traffic_server', name: 'traffic_server',
                        inputValue: 1
                    },
                    {
                        boxLabel: '客户端通信',
                        id: 'server_client_to_client',
                        name: 'client_to_client',
                        inputValue: 1
                    }
                ]
            },
            {
                xtype: 'checkboxgroup',
                fieldLabel: '其它',
                columns: 3,
                allowBlank: true,
                items: [
                    {
                        boxLabel: '数据压缩', id: 'server_comp_lzo', name: 'comp_lzo',
                        inputValue: 1
                    },
                    {
                        boxLabel: '多点登陆', id: 'server_duplicate_cn', name: 'duplicate_cn',
                        inputValue: 1
                    },
                     {
                     boxLabel: '日志追加',
                     id: 'server_log_append',
                     name: 'log_append',
                     //提交时传送的参数值
                     inputValue: 1
                     }
                ]
            },
            new Ext.form.ComboBox({
                border: true,
                frame: true,
                editable: false,
                emptyText: '日志类型',
                fieldLabel: '日志类型',
                id: 'server_log_flag',
                triggerAction: "all",// 是否开启自动查询功能
                store: log_store,// 定义数据源
                displayField: "name", // 关联某一个逻辑列名作为显示值
                valueField: "id", // 关联某一个逻辑列名作为显示值
                mode: "local",// 如果数据来自本地用local 如果来自远程用remote默认为remote
//                name: 'protocol',
                hiddenName: 'server.log_flag',
                allowBlank: false,
                blankText: "请选择"
            }),
            new Ext.form.ComboBox({
                border: true,
                frame: true,
                editable: false,
                emptyText: '日志级别',
                fieldLabel: '日志级别',
                id: 'server_verb',
                triggerAction: "all",// 是否开启自动查询功能
                store: level_store,// 定义数据源
                displayField: "name", // 关联某一个逻辑列名作为显示值
                valueField: "id", // 关联某一个逻辑列名作为显示值
                mode: "local",// 如果数据来自本地用local 如果来自远程用remote默认为remote
//                name: 'protocol',
                hiddenName: 'server.verb',
                allowBlank: false,
                blankText: "请选择"
            })
            /*,{
             labelAlign: 'right',
             xtype: 'textfield',
             id: 'server_mute',
             name: 'server.mute',
             regexText: '这个不是IP',
             emptyText: '重复日志记录次数',
             fieldLabel: '重复日志记录次数'
             }*/
            /*,
             {
             xtype: 'textfield',
             name: 'server.default_domain_suffix',
             value: 'sslvpn.com',
             id: 'server_default_domain_suffix',
             fieldLabel: '数据域名后缀'
             }*/,
            {
                xtype: 'textfield',
                name: 'server.keep_alive_interval',
                id: 'server_keep_alive_interval',
                emptyText: '客户端检测间隔(秒)',
                fieldLabel: '客户端检测间隔(秒)'
            },
            {
                xtype: 'textfield',
                name: 'server.keep_alive',
                id: 'server_keep_alive',
                emptyText: '客户端超时时间(秒)',
                fieldLabel: '客户端超时时间(秒)'
            },
            new Ext.form.RadioGroup({
                id: 'server_client_dns_type',
                fieldLabel: "客户端DNS", //RadioGroup.fieldLabel 标签与 Radio.boxLabel 标签区别
                //                hideLabel : true,   //隐藏RadioGroup标签
                layout: 'anchor',
                defaults: {
                    anchor: '100%',
                    labelStyle: 'padding-left:4px;'
                },
                columns: 1,
                //collapsible:true,
                collapsed: true,
                items: [
                    new Ext.form.Radio({                          //三个必须项
                        checked: true, //设置当前为选中状态,仅且一个为选中.
                        boxLabel: "客户端自定义DNS", //Radio标签
                        name: "server.client_dns_type", //用于form提交时传送的参数名
                        inputValue: 0, //提交时传送的参数值
                        listeners: {
                            check: function (checkbox, checked) {        //选中时,调用的事件
                                if (checked) {
                                    var dns_fieldset = Ext.getCmp("dns_fieldset");
                                    if (dns_fieldset.isVisible()) {
                                        dns_fieldset.hide();
                                    }
                                }
                            }
                        }
                    }),
                    new Ext.form.Radio({            //以上相同
                        boxLabel: "客户端同步服务器DNS",
                        name: "server.client_dns_type",
                        inputValue: 1,
                        listeners: {
                            check: function (checkbox, checked) {
                                if (checked) {
                                    var dns_fieldset = Ext.getCmp("dns_fieldset");
                                    if (dns_fieldset.isVisible()) {
                                        dns_fieldset.hide();
                                    }
                                }
                            }
                        }
                    }),
                    new Ext.form.Radio({
                        boxLabel: "客户端设定DNS",
                        name: "server.client_dns_type",
                        inputValue: 2,
                        listeners: {
                            check: function (checkbox, checked) {
                                if (checked) {
                                    var dns_fieldset = Ext.getCmp("dns_fieldset");
                                    if (!dns_fieldset.isVisible()) {
                                        dns_fieldset.show();
                                    }
                                }
                            }
                        }
                    })]
            }),
            {
                xtype: 'fieldset',
                id: 'dns_fieldset',
                hidden: true,
                border: false,
                labelWidth: 200,
                defaultType: 'textfield',
                defaults: {
                    width: 200,
                    blankText: '该项不能为空!'
                },
                defaultType: 'textfield',
                layout: 'form',
                items: [
                    {
                        fieldLabel: '首选DNS服务器',
                        name: 'server.client_first_dns',
                        //emptyText:'首选DNS服务器',
                        id: "server_client_first_dns",
                        regex: /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/,
                        regexText: '请输入正确的DNS',
//                                allowBlank:false,
                        blankText: "首选DNS服务器"
                    },
                    {
                        fieldLabel: '备用DNS服务器',
                        name: 'server.client_second_dns',
//                                emptyText:'备用DNS服务器',
                        regex: /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/,
                        regexText: '请输入正确的DNS',
                        id: "server_client_second_dns",
//                                allowBlank:false,
                        blankText: "备用DNS服务器"
                    }
                ]
            },
            {fieldLabel: '网络资源', xtype: 'displayfield', value: '<a href="javascript:;" onclick="config_nets();">配置</a>'}
        ],
        buttons: [
            //'->',
            {
                id: 'insert_win.info',
                text: '保存配置',
                autoWidth: true,
                handler: function () {
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url: "../../ServerSourceNetsAction_save.action",
                            method: 'POST',
                            waitTitle: '系统提示',
                            waitMsg: '正在连接...',
                            success: function () {
                                Ext.MessageBox.show({
                                    title: '信息',
                                    width: 250,
                                    msg: '保存成功,点击返回页面!',
                                    buttons: Ext.MessageBox.OK,
                                    buttons: {'ok': '确定'},
                                    icon: Ext.MessageBox.INFO,
                                    closable: false
                                });
                            },
                            failure: function () {
                                Ext.MessageBox.show({
                                    title: '信息',
                                    width: 250,
                                    msg: '保存失败，请与管理员联系!',
                                    buttons: Ext.MessageBox.OK,
                                    buttons: {'ok': '确定'},
                                    icon: Ext.MessageBox.ERROR,
                                    closable: false
                                });
                            }
                        });
                    } else {
                        Ext.MessageBox.show({
                            title: '信息',
                            width: 200,
                            msg: '请填写完成再提交!',
                            buttons: Ext.MessageBox.OK,
                            buttons: {'ok': '确定'},
                            icon: Ext.MessageBox.ERROR,
                            closable: false
                        });
                    }
                }
            },
            {
                id: 'export_win.info',
                text: '导出配置',
                autoWidth: true,
                handler: function () {
                    show_file();
                }
            },
            {
                id: 'import_win.info',
                text: '导入配置',
                autoWidth: true,
                handler: function () {
                    import_file();
                }
            }
        ]
    });


    new Ext.Viewport({
        layout: 'fit',
        renderTo: Ext.getBody(),
        autoScroll: true,
        items: [{
            frame: true,
            border: true,
            autoScroll: true,
            items: [formPanel]
        }]
    });
});


function import_file() {
    var formPanel = new Ext.form.FormPanel({
        frame: true,
        autoScroll: true,
        labelWidth: 150,
        labelAlign: 'right',
        defaultWidth: 300,
        autoWidth: true,
        fileUpload: true,
        layout: 'form',
        border: false,
        defaults: {
            width: 250,
            allowBlank: false,
            blankText: '该项不能为空！'
        },
        items: [
             {
                id: 'uploadFile',
                fieldLabel: '配置文件',
                xtype: 'textfield',
                inputType: 'file',
                editable: false,
                allowBlank: true
            }
        ]
    });
    var win = new Ext.Window({
        title: "导入配置",
        width: 500,
        layout: 'fit',
        height: 150,
        modal: true,
        items: formPanel,
        bbar: [
            '->',
            {
                id: 'insert_win.info',
                text: '导入',
                handler: function () {
                    var myMask = new Ext.LoadMask(Ext.getBody(), {
                        msg: '正在上传,请稍后...',
                        removeMask: true
                    });
                    myMask.show();
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url: "../../ServerSourceNetsAction_inport.action",
                            timeout: 20 * 60 * 1000,
                            method: 'POST',
                            success: function (form, action) {
                                myMask.hide();
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title: '信息',
                                    width: 200,
                                    msg: msg,
                                    buttons: Ext.MessageBox.OK,
                                    buttons: {'ok': '确定'},
                                    fn: function (id, msg) {
                                       win.close();
                                    },
                                    icon: Ext.MessageBox.INFO,
                                    closable: false
                                });

                            },
                            failure: function (form, action) {
                                myMask.hide();
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title: '信息',
                                    width: 250,
                                    msg: msg,
                                    buttons: Ext.MessageBox.OK,
                                    buttons: {'ok': '确定'},
                                    icon: Ext.MessageBox.ERROR,
                                    closable: false
                                });
                            }
                        });
                    }
                }
            }, {
                text: '重置',
                handler: function () {
                    formPanel.getForm().reset();
                }
            }
        ]
    }).show();
};

function show_file() {
    if (!Ext.fly('test')) {
        var frm = document.createElement('form');
        frm.id = 'test';
        frm.style.display = 'none';
        document.body.appendChild(frm);
    }
    ;
    Ext.Ajax.request({
        url: "../../ServerSourceNetsAction_export.action",
        timeout: 20 * 60 * 1000,
        form: Ext.fly('test'),
        method: 'POST',
        isUpload: true
    });
}

function config_nets() {
    var start = 0;
    var pageSize = 5;

    var record = new Ext.data.Record.create([
        {name: 'id', mapping: 'id'},
        {name: 'net', mapping: 'net'},
        //{name: 'level', mapping: 'level'},
        {name: 'checked', mapping: 'checked'},
        {name: 'net_mask', mapping: 'net_mask'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url: "../../ServerSourceNetsAction_findNets.action"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty: "total",
        root: "rows",
        id: 'id'
    }, record);

    var store = new Ext.data.GroupingStore({
        id: "sourceNet.store.info",
        proxy: proxy,
        reader: reader,
        listeners: {
            load: function () {
                var records = [];//存放选中记录
                for (var i = 0; i < store.getCount(); i++) {
                    var record = store.getAt(i);
                    if (record.data.checked == true) {//根据后台数据判断那些记录默认选中
                        records.push(record);
                    }
                }
                boxM.selectRecords(records);//执行选中记录
            }
        }
    });

    var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
    //var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var colM = new Ext.grid.ColumnModel([
        boxM,
        //rowNumber,
        {header: "网络地址", dataIndex: "net", align: 'center', sortable: true, menuDisabled: true},
        {header: "子网掩码", dataIndex: "net_mask", align: 'center', sortable: true, menuDisabled: true}
        //{header: '操作标记', dataIndex: "flag", align: 'center', sortable: true, menuDisabled: true, renderer: show_flag, width: 100}
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
        id: 'grid.info',
        //title: '资源配置',
        plain: true,
        height: 200,
        viewConfig: {
            forceFit: true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle: 'width:100%',
        loadMask: {msg: '正在加载数据，请稍后...'},
        cm: colM,
        sm: boxM,
        store: store,
        //tbar: toolbar,
        bbar: page_toolbar,
        columnLines: true,
        autoScroll: true,
        loadMask: {msg: '正在加载数据，请稍后...'},
        border: false,
        collapsible: false,
        stripeRows: true,
        autoExpandColumn: 'Position',
        enableHdMenu: true,
        enableColumnHide: true,
        frame: true,
        iconCls: 'icon-grid'
    });

    var win = new Ext.Window({
        width: 600,
        layout: 'fit',
        title: '资源配置',
        height: 300,
        modal: true,
        items: grid_panel,
        bbar: [
            '->',
            {
                id: 'insert_win.info',
                text: '保存配置',
                handler: function () {
                    var selected = new Array();
                    var recode = Ext.getCmp("grid.info").getSelectionModel().getSelections();
                    for (var i = 0; i < recode.length; i++) {
                        selected[i] = recode[i].get("id");
                    }
                    Ext.Ajax.request({
                        url: '../../ServerSourceNetsAction_update.action',
                        params: {ids: selected},
                        timeout: 20 * 60 * 1000,
                        method: "POST",
                        success: function (form, action) {
                            Ext.Msg.alert("提示", "更新配置成功!");
                            store.reload();
                            win.close();
                        },
                        failure: function (result) {
                            Ext.Msg.alert("提示", "更新配置失败!");
                        }
                    });
                }
            }
        ]
    }).show();

    store.load({
        params: {
            start: start, limit: pageSize
        }
    });
}


function setHeight() {
    var h = document.body.clientHeight - 8;
    return h;
}

function setWidth() {
    return document.body.clientWidth - 8;
}