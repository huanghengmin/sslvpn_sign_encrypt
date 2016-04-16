Ext.onReady(function () {
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var start = 0;
    var pageSize = 15;
    var record = new Ext.data.Record.create([
        {name: 'cn', mapping: 'cn'},
        {name: 'virtual_address', mapping: 'virtual_address'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url: "../../IppAction_getAllIps.action",
        timeout: 10 * 1000
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

    store.load({
        params: {
            start: start, limit: pageSize
        }
    });

    //var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框单选

    var rowNumber = new Ext.grid.RowNumberer();         //自动编号

    var colM = new Ext.grid.ColumnModel([
        //boxM,
        rowNumber,
        {header: "通用名称", dataIndex: "cn", align: 'center', sortable: true, menuDisabled: true, sort: true},
        {header: "虚拟IP地址", dataIndex: "virtual_address", align: 'center', sortable: true, menuDisabled: true}/*,
        {header: '管控状态', dataIndex: 'flag', align: 'center', sortable: true, menuDisabled: true, renderer: show_flag}*/
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

    var tb = new Ext.Toolbar({
        autoWidth: true,
        autoHeight: true,
        items: [
           /* {
                id: 'add_key.info',
                xtype: 'button',
                text: '管控',
                iconCls: 'add',
                handler: function () {
                    addsourceNet(grid_panel, store);
                }
            }*/
        ]
    });

    var grid_panel = new Ext.grid.GridPanel({
        id: 'grid.info',
        plain: true,
        height: 300,
        viewConfig: {
            forceFit: true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle: 'width:100%',
        loadMask: {msg: '正在加载数据，请稍后...'},
        cm: colM,
        //sm: boxM,
        store: store,
        tbar: tb,
        bbar: page_toolbar,
        columnLines: true,
        autoScroll: true,
        border: false,
        collapsible: false,
        stripeRows: true,
        autoExpandColumn: 'Position',
        enableHdMenu: true,
        enableColumnHide: true,
        frame: true,
        iconCls: 'icon-grid'
    });

    var port = new Ext.Viewport({
        layout: 'fit',
        renderTo: Ext.getBody(),
        items: [grid_panel]
    });
});

function show_flag(value, p, r) {
    return String.format(
        ""
    );
}
