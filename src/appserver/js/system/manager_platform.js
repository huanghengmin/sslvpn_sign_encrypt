/**
 * 平台管理 重启 关闭等
 */
Ext.onReady(function(){
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';
    var systemRestartPanel = new Ext.Panel({
        id:'sysRestart.info',
        html:"<div align='center'><a href='javascript:;' onclick='systemRestart();'><img src='../../img/png/reboot.png' alt='重启系统' title='重启系统!' /></a></br><font >重启系统</font></div>"
    });
    var equipmentRestartPanel = new Ext.Panel({
        id:'equiRestart.info',
        html:"<div align='center'><a href='javascript:;' onclick='equipmentRestart();'><img src='../../img/png/restart.png' alt='重启设备' title='重启设备!'/></a></br><font >重启设备</font></div>"
    });
    var equipmentShutdownPanel = new Ext.Panel({
        id:'equiShutdown.info',
        html:"<div align='center'><a href='javascript:;' onclick='equipmentShutdown();'><img src='../../img/png/shutdown.png' alt='关闭设备' title='关闭设备!' /></a></br><font >关闭设备</font></div>"
    });
    var recoveryPanel = new Ext.Panel({
        id:'recovery.info',
        html:"<div align='center'><a href='javascript:;' text-align:center; onclick='recovery();'><img  src='../../img/png/recovery.png' alt='恢复出厂' title='恢复出厂!' /></a></br><font >恢复出厂</font></div>"
    });
    new Ext.Viewport({
        renderTo:Ext.getBody(),
        layout:'fit',
        items:[
            {
                layout:'form',
                //title:"系统管理",
                frame:true,
                autoScroll:true,
                items:[
                    {plain:true,height:80},
                    {
                        layout:'column',
                        plain:true,
                        items:[
                            {items:[{width:1,html:"&nbsp;&nbsp;&nbsp;&nbsp;"}],columnWidth:.1},
                            {items:[systemRestartPanel],columnWidth:.2},
                            {items:[equipmentRestartPanel],columnWidth:.2},
                            {items:[equipmentShutdownPanel],columnWidth:.2},
                            {items:[recoveryPanel],columnWidth:.2},
                            {items:[{width:1,html:"&nbsp;&nbsp;&nbsp;&nbsp;"}],columnWidth:.1}
                        ]
                    }
                ]
            }
        ]
    });
});


function recovery() {
    Ext.Msg.confirm("提示", "确定恢复出厂设置？", function (sid) {
        if (sid == "yes") {
            Ext.Ajax.request({
                url: "../../RecoveryAction_recovery.action",
                timeout: 20 * 60 * 1000,
                method: "POST",
                success: function (r, o) {
                    var respText = Ext.util.JSON.decode(r.responseText);
                    var msg = respText.msg;
                    Ext.Msg.alert("提示", msg);
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

function systemRestart(){
    Ext.MessageBox.show({
        title:"信息",
        width:250,
        msg:"确定要重启系统吗?",
        animEl:'sysRestart.info',
        icon:Ext.MessageBox.WARNING,
        buttons:{'ok':'确定','no':'取消'},
        fn:function(e){
            if(e=='ok'){
                var myMask = new Ext.LoadMask(Ext.getBody(), {
                    msg: '正在重启系统,请稍后...',
                    removeMask: true //完成后移除
                });
                myMask.show();
                Ext.Ajax.request({
                    url:'../../PlatformAction_sysRestart.action',
                    method:'POST',
                    success:function(r,o){
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        myMask.hide();
                        Ext.MessageBox.show({
                            title:"信息",
                            msg:msg,
                            animEl:'sysRestart.info',
                            icon:Ext.MessageBox.INFO,
                            buttons:{'ok':'确定'}
                        });
                    }
                });
            }
        }
    });
}
function equipmentRestart(){
    Ext.MessageBox.show({
        title:"信息",
        width:250,
        msg:"确定要重启设备吗?",
        animEl:'equipRestart.info',
        icon:Ext.MessageBox.WARNING,
        buttons:{'ok':'确定','no':'取消'},
        fn : function(e){
            if(e=='ok'){
                var myMask = new Ext.LoadMask(Ext.getBody(), {
                    msg: '正在重启设备,请稍后...',
                    removeMask: true //完成后移除
                });
                myMask.show();
                Ext.Ajax.request({
                    url:'../../PlatformAction_equipRestart.action',
                    method:'POST',
                    success:function(r,o){
                        myMask.hide();
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.MessageBox.show({
                            title:"信息",
                            msg:msg,
                            animEl:'equipRestart.info',
                            buttons:{'ok':'确定'}
                        });
                    }
                });
            }
        }
    });
}

function equipmentShutdown(){
    Ext.MessageBox.show({
        title:"信息",
        width:250,
        msg:"确定要关闭设备吗?",
        animEl:'equipShutdown.info',
        icon:Ext.MessageBox.WARNING,
        buttons:{'ok':'确定','no':'取消'},
        fn:function(e){
            if(e=='ok'){
                var myMask = new Ext.LoadMask(Ext.getBody(), {
                    msg: '正在关闭设备,请稍后...',
                    removeMask: true //完成后移除
                });
                myMask.show();
                Ext.Ajax.request({
                    url:'../../PlatformAction_equipShutdown.action',
                    method:'POST',
                    success:function(r,o){
                        myMask.hide();
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.MessageBox.show({
                            title:"信息",
                            msg:msg,
                            animEl:'equipShutdown.info',
                            icon:Ext.MessageBox.INFO,
                            buttons:{'ok':'确定'}
                        });
                    }
                });
            }
        }
    });
}