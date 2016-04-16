package com.hzih.sslvpn.web.action.system;
import com.hzih.sslvpn.domain.Account;
import com.hzih.sslvpn.domain.Role;
import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.service.LoginService;
import com.hzih.sslvpn.utils.Constant;
import com.hzih.sslvpn.utils.FileUtil;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.action.AccountLogUtils;
import com.hzih.sslvpn.web.action.ActionBase;
import com.hzih.sslvpn.web.action.audit.AuditFlagAction;
import com.hzih.sslvpn.web.action.boot.LoaderListener;
import com.hzih.sslvpn.web.servlet.SiteContextLoaderServlet;
import com.inetec.common.client.ECommonUtilFactory;
import com.inetec.common.client.IECommonUtil;
import com.inetec.common.client.util.LogBean;
import com.inetec.common.client.util.XChange;
import com.inetec.common.security.DesEncrypterAsPassword;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-6-11
 * Time: 下午1:04
 * To change this template use File | Settings | File Templates.
 */
public class DownLoadAction extends ActionSupport{
    private static final Logger logger = Logger.getLogger(DownLoadAction.class);
    private LogService logService;
    private LoginService loginService;

    public LoginService getLoginService() {
        return loginService;
    }

    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    //    private int start;
//    private int limit;
    private String type;
    private String logName;

    public String readLocalLogName() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = null;
        try {
            json = FileUtil.readFileNames(StringContext.localLogPath);
//            logService.newLog("INFO",  SessionUtils.getAccount(request).getUserName(), "日志下载","用户获取所有本地日志名称、大小信息成功");
        } catch (Exception e) {
            if(AuditFlagAction.getAuditFlag()) {
                logger.error("日志下载", e);
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "日志下载", "用户获取所有本地日志名称、大小信息失败");
            }
        }
        actionBase.actionEnd(response, json, result);
        return "readLocalLogName";
    }

    public String readRemoteLogName() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = null;
        try {
            json = FileUtil.readRemoteFileNames();
//            logService.newLog("INFO",  SessionUtils.getAccount(request).getUserName(), "日志下载","用户获取所有远程日志名称、大小信息成功");
        } catch (Exception e) {
            if(AuditFlagAction.getAuditFlag()) {
                logger.error("日志下载", e);
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "日志下载", "用户获取所有远程日志名称、大小信息失败");
            }
        }
        actionBase.actionEnd(response, json, result);
        return "readRemoteLogName";
    }


    public String clear() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String msg = null;
        try {
            String password = request.getParameter("password");
            String fileName = request.getParameter("fileName");
            DesEncrypterAsPassword deap = new DesEncrypterAsPassword(Constant.S_PWD_ENCRYPT_CODE);
            String _password = new String(deap.encrypt(password.getBytes()));
            Account account = SessionUtils.getAccount(request);
            if(account!=null){
                boolean flag = false;
                Set<Role> roles = account.getRoles();
                for (Role role:roles){
                    if(role.getName().equals("审计管理员")){
                        flag = true;
                    }
                }
                if(flag){
                    Account auditAccount = loginService.getAccountByNameAndPwd(SessionUtils.getAccount(request).getUserName(), _password);
                    if(auditAccount!=null) {
                        String file = StringContext.localLogPath +"/" + fileName.split("\\(")[0];
                        File source = new File(file);
                        FileOutputStream out = new FileOutputStream(source);
                        out.write(0);
                        out.flush();
                        out.close();
                        msg = "用户"+auditAccount.getName()+" 清空成功,点击[确定]返回列表!";
                        if(AuditFlagAction.getAuditFlag()) {
                            logger.info(msg);
                            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "日志下载", msg);
                            String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "日志下载", "info", "004", "1", new Date());
                            SiteContextLoaderServlet.sysLogService.offer(log);
                        }
                    } else {
                        msg = "用户清空"+logName+"日志失败,密码错误!";
                        if(AuditFlagAction.getAuditFlag()) {
                            logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "日志下载", msg);
                            String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "日志下载", "info", "004", "0", new Date());
                            SiteContextLoaderServlet.sysLogService.offer(log);
                        }
                    }
                }  else {
                    msg = "非审计员不允许清空日志,清空失败";
                }
            }
        } catch (Exception e) {
            msg =  "用户清空"+logName+"日志失败!";
            if(AuditFlagAction.getAuditFlag()) {
                logger.error(msg, e);
                logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "日志下载", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "日志下载", "info", "004", "0", new Date());
                SiteContextLoaderServlet.sysLogService.offer(log);
            }
        }
        String json = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response,json,result);
        return null;
    }


    public String download() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = "{success:false}";
        try {
//            if(logName!=null){
//                logger.info("下载:" + logName+"开始");
//            }
            String Agent = request.getHeader("User-Agent");
            StringTokenizer st = new StringTokenizer(Agent,";");
            st.nextToken();
            //得到用户的浏览器名  MSIE  Firefox
            String userBrowser = st.nextToken();
            String path = null;
            if ("internal_log".equals(type)) {
                path = StringContext.localLogPath +"/" + logName.split("\\(")[0];
                File source = new File(path);
                String name = source.getName();
                FileUtil.downType(response,name,userBrowser);
                response = FileUtil.copy(source, response);
//                logger.info("下载" + logName.split("\\(")[0] + "成功!");
            } else if ("external_log".equals(type)) {
                IECommonUtil ecu = ECommonUtilFactory.createECommonUtil();
                LogBean bean = new LogBean();
                bean.setLogFileName(logName.split("\\(")[0]);
                bean.setLogFileLocation(0);
                int loglen = 0;
                boolean isLogEnd = false;
                OutputStream out=response.getOutputStream();
                FileUtil.downType(response,logName.split("\\(")[0],userBrowser);
                while (!isLogEnd) {
                    try {
                        bean.setLogFileLocation(loglen);
                        byte[] files = ecu.getLogFile(bean);
//                        logger.info("文件长度: "+files.length);
                        if (files.length > 0) {
                            loglen += files.length;
                            out.write(files);
                        } else {
                            isLogEnd = true;
                            out.flush();
                        }
                    } catch (XChange e) {
                        logger.warn(e.getMessage(),e);
                    }
                }
//                logger.info("下载" + logName.split("\\(")[0] + "成功!");
            }
            if(AuditFlagAction.getAuditFlag()) {
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "日志下载", "用户下载日志"+logName.split("\\(")[0]+"成功");
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), "用户下载日志成功", "日志下载", "info", "004", "1", new Date());
                SiteContextLoaderServlet.sysLogService.offer(log);
            }
            json = "{success:true}";
        } catch (Exception e) {
            if(AuditFlagAction.getAuditFlag()) {
                logger.error("日志下载", e);
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "日志下载", "用户下载日志失败");
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), "用户下载日志失败", "日志下载", "info", "004", "0", new Date());
                SiteContextLoaderServlet.sysLogService.offer(log);
            }
        }
        actionBase.actionEnd(response, json, result);
        return "download";
    }

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }
}
