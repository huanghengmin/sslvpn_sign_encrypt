package com.hzih.sslvpn.web.action.config;

import cn.collin.commons.utils.DateUtils;
import com.hzih.sslvpn.constant.AppConstant;
import com.hzih.sslvpn.domain.Account;
import com.hzih.sslvpn.domain.Role;
import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.SiteContext;
import com.hzih.sslvpn.web.action.ActionBase;
import com.hzih.sslvpn.web.action.boot.LoaderListener;
import com.inetec.common.util.OSInfo;
import com.inetec.common.util.Proc;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.sql.*;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-6-19
 * Time: 上午9:55
 * To change this template use File | Settings | File Templates.
 * 审计库容量检查
 */
public class AlertAction extends ActionSupport {
    private static final Logger logger = LoggerFactory.getLogger(AlertAction.class);
    private LogService logService;
    private int start;
    private int limit;

    public String refreshAlerts() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        request.getCharacterEncoding();
        String json = null;
        try {
            StringBuffer buffer = LoaderListener.haveAlertService.getBuff();
            JSONObject obj = JSONObject.fromObject(buffer.toString());
            String time = DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
            int device = obj.getInt("device");//设备报警
            int business = obj.getInt("business");//业务报警
            int security = obj.getInt("security");//安全报警
            json = "{success:true,time:'" + time + "',device:" + device + ",business:" + business + ",security:" + security + "}";
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            json = "{success:true,time:'',device:0,business:0,security:0}";
        }
        actionBase.actionEnd(response, json);
        return null;
    }

    public String refreshDiskUseAlerts() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        request.getCharacterEncoding();
        String json = null;
        String diskMsg = "审计库所在磁盘空间充足";
        int alert = 0;
        int dbUsed = 0;
        Date date = new Date();
        String time = DateUtils.format(date, "yyyy年MM月dd日 HH:mm:ss");
        try {
            Account account = SessionUtils.getAccount(request);
            if (account != null) {
                Set<Role> roles = account.getRoles();
                Iterator<Role> iterator = roles.iterator();
                Role role = null;
                while (iterator.hasNext()) {
                    role = iterator.next();
                    break;
                }
                if (role.getName().equals("审计管理员") || role.getName().indexOf("审计") > -1) {
                    SAXReader reader = new SAXReader();
                    String fileName = SiteContext.getInstance().contextRealPath + AppConstant.XML_DB_CONFIG_PATH;
                    Document doc = reader.read(new File(fileName));
                    dbUsed = Integer.parseInt(doc.selectSingleNode("//config/maindb/dbused").getText()); //审计库容量告警%值
                    int diskUsed = getDataBaseAtDisk(doc);            //审计库所在磁盘使用容量(%)
                    if (diskUsed >= dbUsed) {
                        diskMsg = "审计库总容量达到警戒容量";
                        alert = 1; //告警
                    }
                } else {
                    alert = 0; //没有告警
                }
            } else {
                alert = 2;//没有登录
            }
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            alert = 0;
        }
        json = "{success:true,dbUsed:" + dbUsed + ",time:'" + time + "',diskMsg:'" + diskMsg + "',alert:" + alert + "}";
        actionBase.actionEnd(response, json);
        return null;
    }

    private int getDataBaseAtDisk(Document doc) {
        String ip = doc.selectSingleNode("//config/maindb/dbip").getText();
        String port = doc.selectSingleNode("//config/maindb/dbport").getText();
        String dbName = "information_schema";
        String userName = doc.selectSingleNode("//config/maindb/username").getText();
        String password = doc.selectSingleNode("//config/maindb/password").getText();
        String dataDir = null;
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + ip
                    + ":" + port + "/" + dbName
                    + "?useUnicode=true&characterEncoding=utf-8";
            conn = DriverManager.getConnection(url, userName, password);
            stat = conn.createStatement();
            rs = stat.executeQuery("select VARIABLE_VALUE from `GLOBAL_VARIABLES` where VARIABLE_NAME = 'datadir';");
            if (rs != null && rs.next()) {
                dataDir = rs.getString(1);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        } finally {
            try {
                if (null != rs) {
                    rs.close();
                    rs = null;
                }
                if (null != stat) {
                    stat.close();
                    stat = null;
                }
                if (null != conn) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException e) {
                logger.info(e.getMessage(),e);
            }
        }
        int used = 100;
        Proc proc;
        OSInfo osinfo = OSInfo.getOSInfo();
        if (osinfo.isWin()) {
        }
        if (osinfo.isLinux()) {
            proc = new Proc();
            proc.exec("df " + dataDir);
            String result = proc.getOutput();
            String[] lines = result.split("\n");
            String[] str = lines[1].split("\\s");
            for (int i = 0; i < str.length; i++) {
                if (str[i].endsWith("%")) {
                    used = Integer.parseInt(str[i].split("%")[0]);
                }
            }
        }
        return used;
    }

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
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

}
