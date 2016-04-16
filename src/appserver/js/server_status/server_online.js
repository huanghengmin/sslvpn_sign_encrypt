Ext.onReady(function () {
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var start = 0;
    var pageSize = 15;

    var record = new Ext.data.Record.create([
        {name:'cn', mapping:'cn'} ,
        {name:'real_address', mapping:'real_address'},
        {name:'byte_received', mapping:'byte_received'},
        {name:'byte_send', mapping:'byte_send'},
        {name:'connected_since', mapping:'connected_since'},
        {name:'virtual_address', mapping:'virtual_address'},
        {name:'last_ref', mapping:'last_ref'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url:"../../OnlineUserAction_onlineUser.action" ,
        timeout: 10*1000
    });

    var reader = new Ext.data.JsonReader({
        totalProperty:"totalCount",
        root:"root"
    }, record);

    var store = new Ext.data.GroupingStore({
        id:"store.info",
        proxy:proxy,
        reader:reader
    });

    store.load({
        params:{
            start:start, limit:pageSize
        }
    });

//    var boxM = new Ext.grid.CheckboxSelectionModel({singleSelect:true});   //复选框单选

    var rowNumber = new Ext.grid.RowNumberer();         //自动编号

    var colM = new Ext.grid.ColumnModel([
//        boxM,
        rowNumber,
        {header:"用户名", dataIndex:"cn",align:'center', sortable:true, menuDisabled:true,sort:true/*,renderer:show_name*/} ,
        {header:"IP地址", dataIndex:"real_address",align:'center', sortable:true, menuDisabled:true} ,
        {header:"上传流量", dataIndex:"byte_received",align:'center', sortable:true, menuDisabled:true} ,
        {header:"下载流量",dataIndex:"byte_send",align:'center', sortable:true, menuDisabled:true},
        {header:"VPN地址", dataIndex:"virtual_address",align:'center', sortable:true, menuDisabled:true} ,
        {header:"连接时间", dataIndex:"connected_since",align:'center', sortable:true, menuDisabled:true} ,
        {header:"最后操作时间", dataIndex:"last_ref",align:'center', sortable:true, menuDisabled:true} ,
        {header:'操作标记', dataIndex:'flag', align:'center',sortable:true, menuDisabled:true, renderer:show_flag, width:300}
    ]);

    var page_toolbar = new Ext.PagingToolbar({
        pageSize:pageSize,
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
        //title:'在线用户',
        //height:setHeight(),
        viewConfig:{
            forceFit:true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle:'width:100%',
        loadMask:{msg:'正在加载数据，请稍后...'},
        //border:true,
        cm:colM,
        //columnLines: true,
//        sm:boxM,
        store:store,
        bbar:page_toolbar,
        columnLines: true,
        autoScroll: true,
        border: false,
        collapsible: false,
        stripeRows: true,
        //autoExpandColumn: 'Position',
        enableHdMenu: true,
        enableColumnHide: true,
        //selModel: new Ext.grid.RowSelect.ionModel({singleSelect: true}),
        height: 300,
        frame: true/*,
        iconCls: 'icon-grid'*/
    });
    var task = {
        run : function() {
            grid_panel.getStore().reload();
        },
        interval : 60000
    }
    var taskRunner = new Ext.util.TaskRunner();
    taskRunner.start(task);

    var port = new Ext.Viewport({
        layout:'fit',
        renderTo:Ext.getBody(),
        items:[grid_panel]
    });
});

function show_flag(value, p, r){
    return String.format(
        //'<a id="view.info" href="javascript:void(0);" onclick="view();return false;"style="color: green;">截屏</a>&nbsp;&nbsp;&nbsp;'+
        '<a id="stop.info" href="javascript:void(0);" onclick="stop_connect();return false;"style="color: green;">阻断</a>&nbsp;&nbsp;&nbsp;'+
//            '<a id="remove_connect.info" href="javascript:void(0);" onclick="remove_connect();return false;"style="color: green;">禁止</a>&nbsp;&nbsp;&nbsp;'+
        '<a id="show.info" href="javascript:void(0);" onclick="show();return false;"style="color: green;">查看</a>&nbsp;&nbsp;&nbsp;'
    );
};


/*function show_name(value, p, r){
    if(value.indexOf("_")>-1){
        return value.substring(0,value.indexOf("_"));
    }else if(value.indexOf(" ")>-1){
        return value.substring(0,value.indexOf(" "));
    }else{
        return value;
    }
}*/


function show(){
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    var formPanel = new Ext.form.FormPanel({
        frame:true,
        width:500,
        autoScroll:true,
        baseCls : 'x-plain',
        labelWidth:150,
        labelAlign:'right',
        defaultWidth:300,
        layout:'form',
        border:false,
        defaults:{
            width:300
        },
        items:[
            new Ext.form.DisplayField({
                fieldLabel:'用户名',
                value:recode.get("cn")
            }),
            new Ext.form.DisplayField({
                fieldLabel:'IP地址',
                value:recode.get("real_address")
            }),
            new Ext.form.DisplayField({
                fieldLabel:'接收流量',
                value:recode.get("byte_received")
            }),
            new Ext.form.DisplayField({
                fieldLabel:'发送流量',
                value:recode.get("byte_send")
            }),
            new Ext.form.DisplayField({
                fieldLabel:'连接时间',
                value:recode.get("connected_since")
            }),
            new Ext.form.DisplayField({
                fieldLabel:'虚拟IP',
                value:recode.get("virtual_address")
            }),
            new Ext.form.DisplayField({
                fieldLabel:'最后操作时间',
                value:recode.get("last_ref")
            })
        ]
    });

    var select_Win = new Ext.Window({
        title:"用户详细",
        width:600,
        layout:'fit',
        height:300,
        modal:true,
        items:formPanel
    });
    select_Win.show();
}

function view(){
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    if (!recode) {
        Ext.Msg.alert("提示", "请选择一条记录!");
    } else {
        Ext.Msg.confirm("提示", "是否要求客户端截屏？", function (sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url:"../../ClientControl_view.action",
                    timeout:20 * 60 * 1000,
                    method:"POST",
                    params:{cn:recode.get("cn")},
                    success:function (form, action) {
                        Ext.Msg.alert("提示", "截屏信息发送成功!");
                        grid_panel.getStore().reload();
                    },
                    failure:function (result) {
                        Ext.Msg.alert("提示", "截屏信息发送失败!");
                    }
                });
            }
        });
    }
}

function stop_connect(){
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    if (!recode) {
        Ext.Msg.alert("提示", "请选择一条记录!");
    } else {
        Ext.Msg.confirm("提示", "是否阻断用户？", function (sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url:"../../ClientControl_disable.action",
                    timeout:20 * 60 * 1000,
                    method:"POST",
                    params:{cn:recode.get("cn")},
                    success:function (form, action) {
                        Ext.Msg.alert("提示", "阻断用户成功!");
                        grid_panel.getStore().reload();
                    },
                    failure:function (result) {
                        Ext.Msg.alert("提示", "阻断用户失败!");
                    }
                });
            }
        });
    }
}

/*function remove_connect(){
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    if (!recode) {
        Ext.Msg.alert("提示", "请选择一条记录!");
    } else {
        Ext.Msg.confirm("提示", "阻断用户？", function (sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url:"../../ClientControl_kill_user.action",
                    timeout:20 * 60 * 1000,
                    method:"POST",
                    params:{username:recode.get("username")},
                    success:function (form, action) {
                        Ext.Msg.alert("提示", "阻断用户成功!");
                        grid_panel.getStore().reload();
                    },
                    failure:function (result) {
                        Ext.Msg.alert("提示", "阻断用户失败!");
                    }
                });
            }
        });
    }
}*/

 function setHeight() {
 var h = document.body.clientHeight - 8;
 return h;
 } ;