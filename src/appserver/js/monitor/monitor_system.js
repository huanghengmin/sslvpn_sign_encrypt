/**
 * 系统状态
 */
Ext.onReady(function() {

	Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.chart.Chart.CHART_URL = '../../js/ext/resources/charts.swf';
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';

    var store_cpu = new Ext.data.Store({
        url:"../../MonitorAction_queryCpu.action",
        reader : new Ext.data.JsonReader({
            totalProperty:"total",
            root:"rows"
        },new Ext.data.Record.create([
            {name:'name',		 mapping:'name'},
            {name:'num1',	     mapping:'num1'},
            {name:'num2',	     mapping:'num2'},
            {name:'num3',	     mapping:'num3'},
            {name:'num4',	     mapping:'num4'},
            {name:'num5',	     mapping:'num5'},
            {name:'num6',	     mapping:'num6'},
            {name:'num7',	     mapping:'num7'}
        ]))
    });

    var store_mem = new Ext.data.Store({
        url:"../../MonitorAction_queryMem.action",
        reader : new Ext.data.JsonReader({
            totalProperty:"total",
            root:"rows"
        },new Ext.data.Record.create([
            {name:'name',		 mapping:'name'},
            {name:'num1',	     mapping:'num1'},
            {name:'num2',	     mapping:'num2'},
            {name:'num3',	     mapping:'num3'},
            {name:'num4',	     mapping:'num4'},
            {name:'num5',	     mapping:'num5'}
        ]))
    });

    var store_net = new Ext.data.Store({
        url:"../../MonitorAction_queryNet.action",
        reader : new Ext.data.JsonReader({
            totalProperty:"total",
            root:"rows"
        },new Ext.data.Record.create([
            {name:'name',		 mapping:'name'},
            {name:'num1',	     mapping:'num1'},
            {name:'num2',	     mapping:'num2'},
            {name:'num3',	     mapping:'num3'},
            {name:'num4',	     mapping:'num4'},
            {name:'num5',	     mapping:'num5'},
            {name:'num6',	     mapping:'num6'}
        ]))
    });

    var store_disk = new Ext.data.Store({
        url:"../../MonitorAction_queryDisk.action",
        reader : new Ext.data.JsonReader({
            totalProperty:"total",
            root:"rows"
        },new Ext.data.Record.create([
            {name:'name',		 mapping:'name'},
            {name:'num1',	     mapping:'num1'},
            {name:'num2',	     mapping:'num2'},
            {name:'num3',	     mapping:'num3'}
        ]))
    });

    var panel_cpu =  new Ext.Panel({
        title: 'CPU利用率',
        width:'60%',
        height:400,
        layout:'fit',
        style:'margin-left:2%',
        items:{
            xtype: 'linechart',
            url:Ext.chart.Chart.CHART_URL,
            store: store_cpu,
            xField: 'name',
            series:[
                {type:'line',displayName:'用户空间',yField:'num1',style:{color:0xCCFF00}},
                {type:'line',displayName:'内核空间',yField:'num2',style:{color:0xCC0000}},
                {type:'line',displayName:'优先变动进程',yField:'num3',style:{color:0x0033CC}},
                {type:'line',displayName:'空闲CPU',yField:'num4',style:{color:0x33DDAA}},
                {type:'line',displayName:'IO等待',yField:'num5',style:{color:0x8833FF}},
                {type:'line',displayName:'硬中断',yField:'num6',style:{color:0x66CC00}},
                {type:'line',displayName:'软中断',yField:'num7',style:{color:0x66CC00}}

            ],
            extraStyle: {
                legend:
                {
                    display: 'bottom',
                    padding: 5,
                    font:
                    {
                        family: 'Tahoma',
                        size: 13
                    }
                }
            },
            //定义图表样式
            chartStyle: {
                //不知道为啥没出来这个图示
                legend:{
                    display: "top"
                },
                xAxis: {
                    color: 0x69aBc8,
                    majorTicks: {color: 0x69aBc8, length:4},
                    minorTicks: {color: 0x69aBc8, length: 2},
                    majorGridLines:{size: 1, color: 0xeeeeee}
                },
                yAxis: {
                    color: 0x69aBc8,
                    majorTicks: {color: 0x69aBc8, length: 4},
                    minorTicks: {color: 0x69aBc8, length: 2},
                    majorGridLines: {size: 1, color: 0xdfe8f6}
                }
            }
        }
    });

    var panel_mem =  new Ext.Panel({
        title: '内存使用率',
        width:'60%',
        height:400,
        layout:'fit',
        style:'margin-left:2%',
        items:{
            xtype: 'linechart',
            url:Ext.chart.Chart.CHART_URL,
            store: store_mem,
            xField: 'name',
            series:[
                {type:'line',displayName:'物理',yField:'num1',style:{color:0xCCFF00}},
                {type:'line',displayName:'系统内核控制',yField:'num2',style:{color:0xCC0000}},
                {type:'line',displayName:'空闲',yField:'num3',style:{color:0x0033CC}},
                {type:'line',displayName:'缓存',yField:'num4',style:{color:0x33DDAA}},
                {type:'line',displayName:'可用内存',yField:'num5',style:{color:0x8833FF}}
            ],
            extraStyle:
            {
                legend:
                {
                    display: 'bottom',
                    padding: 5,
                    font:
                    {
                        family: 'Tahoma',
                        size: 13
                    }
                }
            }
        }
    });

    var panel_net =  new Ext.Panel({
        title: '网络流量',
        width:'60%',
        height:300,
        layout:'fit',
        style:'margin-left:2%',
        items:{
            xtype: 'linechart',
            url:Ext.chart.Chart.CHART_URL,
            store: store_net,
            xField: 'name',
            series:[
                {type:'line',displayName:'总接收量',yField:'num1',style:{color:0xCCFF00}},
                {type:'line',displayName:'总发送量',yField:'num2',style:{color:0xCC0000}},
                {type:'line',displayName:'实时接收',yField:'num3',style:{color:0x0033CC}},
                {type:'line',displayName:'实时发送',yField:'num4',style:{color:0x33DDAA}},
                {type:'line',displayName:'平均接收',yField:'num5',style:{color:0x8833FF}},
                {type:'line',displayName:'平均发送',yField:'num6',style:{color:0x66CC00}}
            ],
            extraStyle:
            {
                legend:
                {
                    display: 'bottom',
                    padding: 5,
                    font:
                    {
                        family: 'Tahoma',
                        size: 13
                    }
                }
            }
        }
    });

    var panel_disk =  new Ext.Panel({
        title: '磁盘利用率',
        width:'60%',
        height:200,
        layout:'fit',
        style:'margin-left:2%',
        items:{
            xtype: 'linechart',
            url:Ext.chart.Chart.CHART_URL,
            store: store_disk,
            xField: 'name',
            series:[
//                {type:'line',displayName:'/',yField:'num1',style:{color:0xCCFF00}},
//                {type:'line',displayName:'/var',yField:'num2',style:{color:0xCC0000}},
                {type:'line',displayName:'审计库',yField:'num3',style:{color:0x0033FF}}
            ],
            extraStyle:
            {
                legend:
                {
                    display: 'bottom',
                    padding: 5,
                    font:
                    {
                        family: 'Tahoma',
                        size: 13
                    }
                }
            }
        }
    });

    var port = new Ext.Viewport({
        layout:'fit',
        renderTo: Ext.getBody(),

        items:[{
        	autoScroll:true,
        	items:[panel_cpu,panel_mem,panel_net,panel_disk]
        }]
    });
    store_cpu.load();
    store_mem.load();
    store_net.load();
    store_disk.load();
    var task = {
		run : function() {
			store_cpu.reload();
			store_mem.reload();
			store_net.reload();
			store_disk.reload();
		},
		interval : 30000 // 30秒
	}
	Ext.TaskMgr.start(task);
});
function setHeight(){
	var h = document.body.clientHeight-8;
	return h-100;
}

function setWidth(){
    return document.body.clientWidth-8;
}