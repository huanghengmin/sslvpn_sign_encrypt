package com.hzih.sslvpn.web.action.sslvpn.set;

import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.ServerDao;
import com.hzih.sslvpn.dao.TrustCertificateDao;
import com.hzih.sslvpn.domain.TrustCertificate;
import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.utils.FileUtil;
import com.hzih.sslvpn.utils.MergeFiles;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.action.AccountLogUtils;
import com.hzih.sslvpn.web.action.ActionBase;
import com.hzih.sslvpn.syslog.sender.SyslogSender;
import com.hzih.sslvpn.web.action.audit.AuditFlagAction;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Administrator on 15-4-21.
 */
public class TrustCertificateAction extends ActionSupport {

    private static final Logger logger = Logger.getLogger(TrustCertificateAction.class);
    private LogService logService;
    private int start;
    private int limit;
    private ServerDao serverDao;
    private TrustCertificateDao trustCertificateDao;

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

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public ServerDao getServerDao() {
        return serverDao;
    }

    public void setServerDao(ServerDao serverDao) {
        this.serverDao = serverDao;
    }

    public TrustCertificateDao getTrustCertificateDao() {
        return trustCertificateDao;
    }

    public void setTrustCertificateDao(TrustCertificateDao trustCertificateDao) {
        this.trustCertificateDao = trustCertificateDao;
    }

    /**
     * TrustCertificate
     */
    private File crtFile;
    private String crtFileFileName;
    private String crtFileContentType;


    public File getCrtFile() {
        return crtFile;
    }

    public void setCrtFile(File crtFile) {
        this.crtFile = crtFile;
    }

    public String getCrtFileFileName() {
        return crtFileFileName;
    }

    public void setCrtFileFileName(String crtFileFileName) {
        this.crtFileFileName = crtFileFileName;
    }

    public String getCrtFileContentType() {
        return crtFileContentType;
    }

    public void setCrtFileContentType(String crtFileContentType) {
        this.crtFileContentType = crtFileContentType;
    }

    public String find() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        int pageIndex = start / limit + 1;
        PageResult pageResult = trustCertificateDao.listByPage(pageIndex, limit);
        if (pageResult != null) {
            List<TrustCertificate> list = pageResult.getResults();
            int count = pageResult.getAllResultsAmount();
            if (list != null) {
                String json = "{success:true,total:" + count + ",rows:[";
                Iterator<TrustCertificate> raUserIterator = list.iterator();
                while (raUserIterator.hasNext()) {
                    TrustCertificate log = raUserIterator.next();
                    if (raUserIterator.hasNext()) {
                        json += "{" +
                                "id:'" + log.getId() +
                                "',file:'" + log.getFile() +
                                "',name:'" + log.getName() +
                                "',status:'" + log.getStatus() +
                                "',subject:'" + log.getSubject() +
                                "',not_before:'" + log.getNotBefore() +
                                "',not_after:'" + log.getNotAfter() + "'" +
                                "},";
                    } else {
                        json += "{" +
                                "id:'" + log.getId() +
                                "',file:'" + log.getFile() +
                                "',name:'" + log.getName() +
                                "',status:'" + log.getStatus() +
                                "',subject:'" + log.getSubject() +
                                "',not_before:'" + log.getNotBefore() +
                                "',not_after:'" + log.getNotAfter() + "'" +
                                "}";
                    }
                }
                json += "]}";
                actionBase.actionEnd(response, json, result);
            }
        }
        return null;
    }

    public String update() throws Exception {
        HttpServletResponse response = ServletActionContext.getResponse();
        HttpServletRequest request = ServletActionContext.getRequest();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false}";
        String msg = "更新失败";
        String[] ids = request.getParameterValues("ids");
        trustCertificateDao.modify_check_no();
        if (null != ids && !"".equals(ids) && ids.length > 0) {
            for (int i = 0; i < ids.length; i++) {
                String id = ids[i];
                if (!"".equals(id))
                    trustCertificateDao.modify_check_on(Integer.parseInt(id));
            }
        }
        List<TrustCertificate> checks = trustCertificateDao.findAllCheck();
        if (null != checks && checks.size() > 0) {
            String[] ss = new String[checks.size()];
            for (int i = 0; i < ss.length; i++) {
                ss[i] = checks.get(i).getFile();
            }
            MergeFiles.mergeFiles(StringContext.sign_ca_file, ss);
            msg = "更新成功";
            json = "{success:true,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "信任证书链", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "信任证书链", "info", "004", "1", new Date());
                SyslogSender.sysLog(log);
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String upload() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = "保存失败";
        String json = "{success:false,msg:'" + msg + "'}";
        TrustCertificate trustCertificate = trustCertificateDao.find_name(crtFileFileName);
        if (trustCertificate != null) {
            msg = "保存失败,文件名称已在在,请改名后重新上传";
            json = "{success:false,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "信任证书链", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "信任证书链", "info", "004", "0", new Date());
                SyslogSender.sysLog(log);
            }
        } else {
            boolean crtFlag = FileUtil.saveUploadFile(crtFile, StringContext.server_sslPath + "/" + crtFileFileName);
            if (crtFlag) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM日dd日HH时mm分ss秒");
                CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");
                FileInputStream server = new FileInputStream(crtFile);
                X509Certificate cert = (X509Certificate) certificatefactory.generateCertificate(server);
                String subject = cert.getSubjectDN().getName();
                Date notBefore = cert.getNotBefore();//得到开始有效日期
                Date notAfter = cert.getNotAfter();  //得到截止日期
                String s_notBefore = "";
                String s_notAfter = "";
                if (null != notBefore) {
                    s_notBefore = format.format(notBefore);
                }
                if (null != notBefore) {
                    s_notAfter = format.format(notAfter);
                }
                TrustCertificate tr = new TrustCertificate();
                tr.setFile(StringContext.server_sslPath + "/" + crtFileFileName);
                tr.setName(crtFileFileName);
                tr.setSubject(subject);
                tr.setNotAfter(s_notAfter);
                tr.setNotBefore(s_notBefore);
                trustCertificateDao.add(tr);
                msg = "保存证书链文件成功";
                json = "{success:true,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "信任证书链", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "信任证书链", "info", "004", "1", new Date());
                    SyslogSender.sysLog(log);
                }
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String remove() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'删除失败'}";
        String msg = null;
        try {
            String id = request.getParameter("id");
            TrustCertificate trustCertificate = trustCertificateDao.findById(Integer.parseInt(id));
            File ca_file = new File(trustCertificate.getFile());
            if (ca_file.exists()) {
                ca_file.delete();
            }
            trustCertificateDao.delete(trustCertificate);
            msg = "删除成功";
            json = "{success:true,msg:'"+msg+"'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "信任证书链", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "信任证书链", "info", "004", "1", new Date());
                SyslogSender.sysLog(log);
            }
        } catch (Exception e) {
            msg = "信任证书链,删除失败";
            json = "{success:true,msg:'"+msg+"'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info(e.getMessage(),e);
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "信任证书链", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "信任证书链", "info", "004", "0", new Date());
                SyslogSender.sysLog(log);
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String download() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false}";
        String id = request.getParameter("id");
        TrustCertificate trustCertificate = trustCertificateDao.findById(Integer.parseInt(id));
        String Agent = request.getHeader("User-Agent");
        StringTokenizer st = new StringTokenizer(Agent, ";");
        st.nextToken();
        //得到用户的浏览器名  MSIE  Firefox
        String userBrowser = st.nextToken();
        File file = new File(trustCertificate.getFile());
        FileUtil.downType(response, trustCertificate.getName(), userBrowser);
        response = FileUtil.copy(file, response);
        json = "{success:true}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String findTrustCertificate()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String file = request.getParameter("file");
        File f = new File(file);
        String json = "{success:true,total:" + 1 + ",rows:[";
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM日dd日HH时mm分ss秒");
        if (f.exists()) {
            CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");
            FileInputStream server = null;
            X509Certificate cert = null;
            try {
                server = new FileInputStream(f);
                cert = (X509Certificate) certificatefactory.generateCertificate(server);
            } catch (FileNotFoundException e) {
                logger.info(e.getMessage(), e);
            }finally {
                try {
                    server.close();
                } catch (IOException e) {
                    logger.info(e.getMessage(),e);
                }
            }
            String subject = cert.getSubjectDN().getName();
            Date notBefore = cert.getNotBefore();//得到开始有效日期
            Date notAfter = cert.getNotAfter();  //得到截止日期
            String s_notBefore = "";
            String s_notAfter = "";
            if (null != notBefore) {
                s_notBefore = format.format(notBefore);
            }

            if (null != notBefore) {
                s_notAfter = format.format(notAfter);
            }
            String serialNumber = cert.getSerialNumber().toString(16).toUpperCase();//得到序列号   16进制
            int version = cert.getVersion();
            String cn = cert.getIssuerDN().toString();
            json += "{" +
                    "name:'证书版本'," +
                    "content:' V" + version + "'" +
                    "},";
            json += "{" +
                    "name:'证书序列号'," +
                    "content:'" + serialNumber + "'" +
                    "},";
            json += "{" +
                    "name:'证书颁发者'," +
                    "content:'" + cn + "'" +
                    "},";
            json += "{" +
                    "name:'有效起始日期'," +
                    "content:'" + s_notBefore.toString() + "'" +
                    "},";
            json += "{" +
                    "name:'有效终止日期'," +
                    "content:'" + s_notAfter.toString() + "'" +
                    "},";
            json += "{" +
                    "name:'证书主题'," +
                    "content:'" + subject + "'" +
                    "}";
        } else {
            json += "{" +
                    "name:'证书版本'," +
                    "content:'未找到证书,可能已被删除!'" +
                    "},";
            json += "{" +
                    "name:'证书序列号'," +
                    "content:'未找到证书,可能已被删除!'" +
                    "},";
            json += "{" +
                    "name:'证书颁发者'," +
                    "content:'未找到证书,可能已被删除!'" +
                    "},";
            json += "{" +
                    "name:'有效起始日期'," +
                    "content:'未找到证书,可能已被删除!'" +
                    "},";
            json += "{" +
                    "name:'有效终止日期'," +
                    "content:'未找到证书,可能已被删除!'" +
                    "},";
            json += "{" +
                    "name:'证书主题'," +
                    "content:'未找到证书,可能已被删除!'" +
                    "}";
        }
        json += "]}";
        actionBase.actionEnd(response, json, result);
        return null;
    }
}
