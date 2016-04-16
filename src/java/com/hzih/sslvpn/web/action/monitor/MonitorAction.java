package com.hzih.sslvpn.web.action.monitor;

import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.action.ActionBase;
import com.hzih.sslvpn.web.thread.SystemStatusService;
import com.inetec.common.net.Ping;
import com.inetec.common.net.Telnet;
import com.inetec.common.util.OSInfo;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-2
 * Time: 下午5:40
 * 运行监控
 */
public class MonitorAction extends ActionSupport {

    private static final Logger logger = Logger.getLogger(MonitorAction.class);
    private LogService logService;
    private String id;
    private int start;
    private int limit;
    private String equipmentName;
    private String ip;
    private String otherIp;
    private int port;

    public String ping() throws IOException{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = null;
        String reMsg = null;
        try{
            String pingStr = Ping.exec(ip, 2);
            msg = getResult(pingStr);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(),"设备运行监控","连通测试设备"+equipmentName+"运行IP成功!");
        } catch (Exception e){
            logger.error("设备运行监控",e);
            logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(),"设备运行监控","连通测试设备"+equipmentName+"运行IP失败!");
            msg = "测试失败";
        }
        String json = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response,json,result);
        return null;
    }

    private String getResult(String pingStr) {
		String result = "";
		OSInfo osInfo = OSInfo.getOSInfo();
		if(osInfo.isLinux()){
			String[] pings = pingStr.split("\n");
			for (int i = 0; i < pings.length; i++) {
				if(i<pings.length - 1){
					result += pings[i].trim()+"<br>";
				}else{
					result += pings[i].trim();
				}
			}

		}else if(osInfo.isWin()){
			String[] pings = pingStr.split("\r\n");
			for (int i = 0; i < pings.length; i++) {
				if(i<pings.length - 1){
					result += pings[i].trim()+"<br>";
				}else{
					result += pings[i].trim();
				}
			}

		}
        if(( result.indexOf("ttl")>-1 && result.indexOf("time")>-1 )
                ||(result.indexOf("bytes=")>-1 && result.indexOf("time")>-1
                                                 && result.indexOf("TTL")>-1)){
            result += "<br><font color=\"green\">PING测试成功!</font>";
        } else {
            result += "<br><font color=\"red\">PING测试失败!</font>";
        }
		return result;
	}

    public String telnet() throws IOException{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = null;
        String reMsg = null;
        try{
            boolean isTelnet = Telnet.exec(ip, port);
            if(isTelnet){
                logger.info("IP"+ip+"上的端口"+port+"是打开的!");
                msg = "<font color=\"green\">端口是打开的!</font>";
            }else{
                logger.info("IP"+ip+"上的端口"+port+"是关闭的!");
                msg = "<font color=\"red\">端口是关闭的!</font>";
            }
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(),"设备运行监控","设备"+equipmentName+"运行测试端口"+port+"成功!");
        } catch (Exception e){
            logger.error("设备运行监控",e);
            logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(),"设备运行监控","设备"+equipmentName+"运行测试端口"+port+"失败!");
            msg = "测试失败";
        }
        String json = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response,json,result);
        return null;
    }

    public String selectCpu() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        int cpu = Integer.parseInt(request.getParameter("cpu"));
        String json = "{success:true,total:1,rows:[{season:'CPU使用',cpu:"+cpu+"},{season:'CPU空闲',cpu:"+(100-cpu)+"}]}";
        actionBase.actionEnd(response,json);
        return null;
    }

    public String queryCpu() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
//        String result = actionBase.actionBegin(request);
//        int cpu = Integer.parseInt(request.getParameter("cpu"));
        int total = SystemStatusService.cpuMap.size();

        String json = "{success:true,total:"+total+",rows:[";
        for(int i= 1; i<=total;i++){
            json += SystemStatusService.cpuMap.get("" + i);
        }
        json += "]}";
        actionBase.actionEnd(response,json);
        return null;
    }

    public String queryMem() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
//        String result = actionBase.actionBegin(request);
//        int cpu = Integer.parseInt(request.getParameter("cpu"));
//        String json = "{success:true,total:6,rows:[" +
//                "{name:'12:00',num1:0.5,num2:25}," +
//                "{name:'12:10',num1:0.5,num2:25}," +
//                "{name:'12:20',num1:0.4,num2:25}," +
//                "{name:'12:30',num1:0.5,num2:25}," +
//                "{name:'12:40',num1:0.6,num2:25}," +
//                "{name:'12:50',num1:0.7,num2:25}" +
//                "]}";
        int total = SystemStatusService.memMap.size();
        String json = "{success:true,total:"+total+",rows:[";
        for(int i= 1; i<=total;i++){
            json += SystemStatusService.memMap.get("" + i);
        }
        json += "]}";
        actionBase.actionEnd(response,json);
        return null;
    }

    public String queryDisk() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
//        String result = actionBase.actionBegin(request);
//        int cpu = Integer.parseInt(request.getParameter("cpu"));

//        String json = "{success:true,total:4,rows:[" +
//                "{name:'12:00',num1:2000,num2:1500,num3:3800}," +
//                "{name:'12:10',num1:2070,num2:1500,num3:3800}," +
//                "{name:'12:20',num1:2050,num2:1500,num3:3800}," +
//                "{name:'12:30',num1:2080,num2:1500,num3:3800}" +
//                "]}";
        int total = SystemStatusService.diskMap.size();
        String json = "{success:true,total:"+total+",rows:[";
        for(int i= 1; i<=total;i++){
            json += SystemStatusService.diskMap.get("" + i);
        }
        json += "]}";

        actionBase.actionEnd(response,json);
        return null;
    }

    public String queryNet() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
//        String result = actionBase.actionBegin(request);
//        int cpu = Integer.parseInt(request.getParameter("cpu"));
//        String json = "{success:true,total:4,rows:[" +
//                "{name:'12:00',num1:2000,num2:1500,num3:3800,num4:3000,num5:3800,num6:3000}," +
//                "{name:'12:10',num1:2070,num2:1500,num3:3850,num4:3000,num5:370,num6:3000}," +
//                "{name:'12:20',num1:2050,num2:1500,num3:3870,num4:3080,num5:3800,num6:3000}," +
//                "{name:'12:30',num1:2080,num2:1500,num3:3800,num4:3000,num5:3800,num6:3080}" +
//                "]}";

        int total = SystemStatusService.netMap.size();
        String json = "{success:true,total:"+total+",rows:[";
        for(int i= 1; i<=total;i++){
            json += SystemStatusService.netMap.get("" + i);
        }
        json += "]}";

        actionBase.actionEnd(response,json);
        return null;
    }

    public String selectDisk() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        int disk = Integer.parseInt(request.getParameter("disk"));
        int diskTotal = Integer.parseInt(request.getParameter("disk_total"));
        if(disk==0&&diskTotal==0){
            diskTotal = 100;
        }
        String json = "{success:true,total:1,rows:[{season:'硬盘已用',disk:"+disk+"},{season:'硬盘可用',disk:"+(diskTotal-disk)+"}]}";
        actionBase.actionEnd(response,json);
        return null;
    }

    public String selectMemory() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        int memory = Integer.parseInt(request.getParameter("memory"));
        int memoryTotal = Integer.parseInt(request.getParameter("memory_total"));
        if(memory==0&&memoryTotal==0){
            memoryTotal = 2;
        }
        String json = "{success:true,total:1,rows:[{season:'内存使用',memory:"+memory+"},{season:'内存空闲',memory:"+(memoryTotal-memory)+"}]}";
        actionBase.actionEnd(response,json);
        return null;
    }

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getOtherIp() {
        return otherIp;
    }

    public void setOtherIp(String otherIp) {
        this.otherIp = otherIp;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
