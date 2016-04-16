/**
 * Created by IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-6-19
 * Time: 上午10:19
 * To change this template use File | Settings | File Templates.
 * 用户日志审计(用户操作审计表)
 */
Ext.onReady(function() {

    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';

    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var start = 0;
    var pageSize = 15;
    var record = new Ext.data.Record.create([
        {name:'id',			mapping:'id'},
        {name:'cn',		mapping:'cn'},
        {name:'serial_number',		    mapping:'serial_number'},
        {name:'subject_dn',	mapping:'subject_dn'},
        {name:'start_time',	mapping:'start_time'},
        {name:'end_time',		mapping:'end_time'},
        {name:'trusted_ip',		mapping:'trusted_ip'},
        {name:'trusted_port',		mapping:'trusted_port'} ,
        {name:'protocol',		mapping:'protocol'},
        {name:'remote_ip',		mapping:'remote_ip'},
        {name:'remote_netmask',		mapping:'remote_netmask'},
        {name:'bytes_received',		mapping:'bytes_received'},
        {name:'bytes_sent',		mapping:'bytes_sent'},
        {name:'status',		mapping:'status'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url:"../../UserLogAction_findLogs.action"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows"/*,
        id:'id'*/
    },record);
    var store = new Ext.data.GroupingStore({
        id:"store.info",
        proxy : proxy,
        reader : reader
    });
    store.load({
        params:{
            start:start,limit:pageSize
        }
    });
//    var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
    var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var colM = new Ext.grid.ColumnModel([
//        boxM,
        rowNumber,
        {header:"用户名",			dataIndex:"cn",       align:'center',sortable:true,menuDisabled:true},
        {header:'登录时间',		dataIndex:'start_time',		   align:'center',sortable:true,menuDisabled:true},
        {header:'下线时间',	    dataIndex:'end_time',	   align:'center',sortable:true,menuDisabled:true},
        {header:"证书序列号",		dataIndex:"serial_number",	       align:'center',sortable:true,menuDisabled:true},
//        {header:"证书主题",		dataIndex:"subject_dn",	       align:'center',sortable:true,menuDisabled:true},
        {header:"通信IP",		dataIndex:"trusted_ip",	       align:'center',sortable:true,menuDisabled:true},
        {header:"通信端口",		dataIndex:"trusted_port",	       align:'center',sortable:true,menuDisabled:true},
        {header:"通信协议",		dataIndex:"protocol",	       align:'center',sortable:true,menuDisabled:true},
        {header:"虚拟IP",		dataIndex:"remote_ip",	       align:'center',sortable:true,menuDisabled:true},
        {header:"虚拟子网掩码",		dataIndex:"remote_netmask",	       align:'center',sortable:true,menuDisabled:true},
        {header:"上传流量",		dataIndex:"bytes_received",	       align:'center',sortable:true,menuDisabled:true},
        {header:"下载流量",		dataIndex:"bytes_sent",	       align:'center',sortable:true,menuDisabled:true}
    ]);
    /*for(var i=6;i<14;i++){
     colM.setHidden(i,!colM.isHidden(i));                // 加载后 不显示 该项
     }
     colM.defaultSortable = true;*/
    var page_toolbar = new Ext.PagingToolbar({
        pageSize : pageSize,
        store:store,
        displayInfo:true,
        displayMsg:"显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg:"没有记录",
        beforePageText:"当前页",
        afterPageText:"共{0}页"
    });


    var tbar = new Ext.Toolbar({
        autoWidth :true,
        autoHeight:true,
        items:[
            '终端名称',
            new Ext.form.TextField({
                name : 'common_name',
                id:'tbar.common_name'
            }),
            '终端IP',
            new Ext.form.TextField({
                name : 'trust_ip',
                id:'tbar.trust_ip'
            }),
            {
                id:'tbar.info',
                xtype:'button',
                iconCls:'query',
                text:'查询',
                handler:function () {
                    var common_name = Ext.getCmp('tbar.common_name').getValue();
                    var trust_ip = Ext.getCmp('tbar.trust_ip').getValue();
                    store.setBaseParam('common_name', common_name);
                    store.setBaseParam('trust_ip', trust_ip);
                    store.load({
                        params : {
                            start : start,
                            limit : pageSize
                        }
                    });
                }
            }]
    });
    var grid_panel = new Ext.grid.GridPanel({
        id:'grid.info',
        //title:'用户日志',
        plain:true,
        //height:setHeight(),
        width:setWidth(),
        animCollapse:true,
        loadMask:{msg:'正在加载数据，请稍后...'},
        border:false,
        collapsible:false,
        columnLines: true,
        cm:colM,
//        sm:boxM,
        store:store,
        stripeRows:true,
        autoExpandColumn:'Position',
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
        tbar:tbar,
        view:new Ext.grid.GroupingView({
            forceFit:true,
            groupingTextTpl:'{text}({[values.rs.length]}条记录)'
        }),
        bbar:page_toolbar,
        //columnLines: true,
        autoScroll: true,
        //border: false,
        //collapsible: false,
        //stripeRows: true,
        //autoExpandColumn: 'Position',
        enableHdMenu: true,
        enableColumnHide: true,
        //selModel: new Ext.grid.RowSelect.ionModel({singleSelect: true}),
        height: 300,
        frame: true/*,
         iconCls: 'icon-grid'*/
    });
    var port = new Ext.Viewport({
        layout:'fit',
        renderTo: Ext.getBody(),
        items:[grid_panel]
    });
});

function setHeight(){
    var h = document.body.clientHeight-8;
    return h;
}

function setWidth(){
    return document.body.clientWidth-8;
}
