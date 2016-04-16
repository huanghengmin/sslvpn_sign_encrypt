package com.hzih.sslvpn.web.action.system;

import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.utils.FileUtil;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.action.AccountLogUtils;
import com.hzih.sslvpn.web.action.ActionBase;
import com.hzih.sslvpn.web.action.boot.LoaderListener;
import com.hzih.sslvpn.web.servlet.SiteContextLoaderServlet;
import com.inetec.common.client.ECommonUtil;
import com.inetec.common.client.ECommonUtilFactory;
import com.inetec.common.client.IECommonUtil;
import com.inetec.common.client.util.LicenseBean;
import com.inetec.common.client.util.XChange;
import com.inetec.common.security.License;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-6-11
 * Time: 下午1:57
 * To change this template use File | Settings | File Templates.
 */
public class LicenseAction extends ActionSupport{
    private static final Logger logger = Logger.getLogger(LicenseAction.class);

    private LogService logService;
    private final static String localSavePath = StringContext.systemPath+"/license";
    private File uploadFile;
    private String uploadFileFileName;
    private String uploadFileContentType;

    public String about() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase base = new ActionBase();
        String result =	base.actionBegin(request);
        String json = null;
        try {
            String os = System.getProperty("os.name")+" "+System.getProperty("os.version");
            logger.info("os:"+os);
            String center = License.getTpType() + " OS 3.0";//System.getProperty("os.arch");
            logger.info("License.getTyType:"+center);
            String product = License.getTpType();//"VMTP";
            logger.info("License.getTyType:"+product);
            json = "{success:true,total:1,rows:[{os:'"+os+"',product:'"+product+"',center:'"+center+"'}]}";
//            logService.newLog("INFO",  SessionUtils.getAccount(request).getUserName(), "平台说明","用户获取平台说明信息成功");
        } catch (Exception e) {
            logger.error("平台说明", e);
            logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "平台说明","用户获取平台说明信息失败");
        }
        base.actionEnd(response, json ,result);
        return null;
    }

    public String readLocal() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase base = new ActionBase();
        String result =	base.actionBegin(request);
        String json = null;
        try {
            json = getLocalLicense();
//            logService.newLog("INFO",  SessionUtils.getAccount(request).getUserName(), "证书管理","用户获取本地许可证信息成功");
        } catch (Exception e) {
            logger.error("证书管理", e);
            logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "证书管理","用户获取本地许可证信息失败");
        }
        base.actionEnd(response, json ,result);
        return null;
    }

    public String readRemote() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase base = new ActionBase();
        String result =	base.actionBegin(request);
        String json = null;
        try {
            json = getRemoteLicense();
//            logService.newLog("INFO",  SessionUtils.getAccount(request).getUserName(), "证书管理","用户获取远程许可证信息成功");
        } catch (Exception e) {
            logger.error("证书管理", e);
            logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "证书管理","用户获取远程许可证信息失败");
        }
        base.actionEnd(response, json ,result);
        return null;
    }

    public String uploadLocal() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase base = new ActionBase();
        String result =	base.actionBegin(request);
        String json = null;
        String msg = null;
        try {
            if("license".equals(uploadFileFileName)){
                FileUtil.upload(localSavePath, uploadFile, uploadFileFileName);
                msg = "上传成功!";
            }else{
                msg = "文件名必须是license!";
            }
            logService.newLog("INFO",  SessionUtils.getAccount(request).getUserName(), "证书管理","用户上传本地许可证信息成功");
            String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), "用户上传本地许可证信息成功", "证书管理", "info", "004", "1", new Date());
            SiteContextLoaderServlet.sysLogService.offer(log);
        } catch (Exception e) {
            msg = "用户上传本地许可证信息失败";
            logger.error(msg, e);
            logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "证书管理",msg);
            String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "证书管理", "info", "004", "0", new Date());
            SiteContextLoaderServlet.sysLogService.offer(log);
        }
        json = "{'success':true,'msg':'"+msg+"'}";
        base.actionEnd(response, json ,result);
        return null;
    }

    public String uploadRemote() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase base = new ActionBase();
        String result =	base.actionBegin(request);
        String json = null;
        String msg = null;
        try {
            if("license".equals(uploadFileFileName)){
                IECommonUtil ecu = ECommonUtilFactory.createECommonUtil();
                boolean isUpdate = ecu.updateLicense(FileUtil.readAsString(uploadFile));
                if (isUpdate) {
                    msg = "上传成功!";
                } else {
                    msg = "上传失败";
                }
            }else{
                msg = "文件名必须是license!";
            }
            logService.newLog("INFO",  SessionUtils.getAccount(request).getUserName(), "证书管理","用户上传远程许可证信息成功");
            String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), "用户上传远程许可证信息成功", "证书管理", "info", "004", "1", new Date());
            SiteContextLoaderServlet.sysLogService.offer(log);
        } catch (Exception e) {
            msg = "用户上传远程许可证信息失败";
            logger.error(msg, e);
            logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "证书管理",msg);
            String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "证书管理", "info", "004", "0", new Date());
            SiteContextLoaderServlet.sysLogService.offer(log);
        }
        json = "{'success':true,'msg':'"+msg+"'}";
        base.actionEnd(response, json ,result);
        return null;
    }

    private String getLocalLicense() throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss EEEE");
        String uKeyId = License.getUkeyId();
        String licenseId = License.getLicenseId();
        Date startDate = License.getStartDate();
        Date endDate = License.getEndDate();
        int licenseDays = License.getLicenseDays();

        logger.info(uKeyId+licenseId+startDate+endDate+licenseDays);

        return "{'success':true,'total':1,rows:[{'uKeyId':'"+uKeyId+
                "','licenseId':'"+licenseId+"','startDate':'"+sdf.format(startDate)+
                "','endDate':'"+sdf.format(endDate)+"','licenseDays':'"+licenseDays+"'}]}";
    }

    private String getRemoteLicense() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss EEEE");//yyyy-MM-dd HH:mm:ss.SSS EEEE aa
        String uKeyId = null;
        String licenseId = null;
        String sDate = null;
        String eDate = null;
        String licenseDays = null;
        LicenseBean bean = null;
        boolean isInternal = true;//判断标记
        if(isInternal){
            try {
                ECommonUtil ecu = new ECommonUtil();
                bean = ecu.getLicense();
            } catch (XChange e) {
                logger.warn(e.getMessage(),e);
            }
            Date startDate = bean.getStartDate();
            Date endDate = bean.getEndDate();
            uKeyId = bean.getUkeyid();
            licenseId = bean.getLicenseid();
            sDate = sdf.format(startDate);
            eDate = sdf.format(endDate);
            licenseDays = ""+bean.getLicenseDays();
        }else{
            uKeyId = "外网服务器不可访问远程服务器!";
            licenseId = "外网服务器不可访问远程服务器!";
            sDate = "外网服务器不可访问远程服务器!";
            eDate = "外网服务器不可访问远程服务器!";
            licenseDays = "外网服务器不可访问远程服务器!";
        }
        return "{'success':true,'total':1,rows:[{'uKeyId':'"+uKeyId+"','licenseId':'"+licenseId+"','startDate':'"+sDate+"','endDate':'"+eDate+"','licenseDays':'"+licenseDays+"'}]}";
    }

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public File getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(File uploadFile) {
        this.uploadFile = uploadFile;
    }

    public String getUploadFileFileName() {
        return uploadFileFileName;
    }

    public void setUploadFileFileName(String uploadFileFileName) {
        this.uploadFileFileName = uploadFileFileName;
    }

    public String getUploadFileContentType() {
        return uploadFileContentType;
    }

    public void setUploadFileContentType(String uploadFileContentType) {
        this.uploadFileContentType = uploadFileContentType;
    }
}
