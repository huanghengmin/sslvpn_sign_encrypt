package com.hzih.sslvpn.web.action.system;

import com.hzih.sslvpn.entity.JarBean;
import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.utils.FileUtil;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.action.AccountLogUtils;
import com.hzih.sslvpn.web.action.ActionBase;
import com.hzih.sslvpn.web.action.audit.AuditFlagAction;
import com.hzih.sslvpn.web.action.boot.LoaderListener;
import com.hzih.sslvpn.web.servlet.SiteContextLoaderServlet;
import com.inetec.common.exception.Ex;
import com.inetec.common.util.OSInfo;
import com.inetec.common.util.Proc;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-6-11
 * Time: 下午1:57
 * To change this template use File | Settings | File Templates.
 */
public class UpgradeAction extends ActionSupport{
    private static final Logger logger = Logger.getLogger(UpgradeAction.class);
    private LogService logService;
    private File uploadFile;
    private String uploadFileFileName;
    private String uploadFileContentType;
    private String upgradeTime;
    /**
     * 获取 服务软件包名列表
     * */
    public String selectWar() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = "{success:true,total:0,rows:[]}";
        try {
            int start = ServletRequestUtils.getIntParameter(request, "start");
            int limit = ServletRequestUtils.getIntParameter(request, "limit");

            json = selectWars(StringContext.webPath,start,limit);
//            logService.newLog("INFO",  SessionUtils.getAccount(request).getUserName(), "版本升级","用户获取服务软件包名列表成功 ");
        } catch (Exception e) {
            if(AuditFlagAction.getAuditFlag()) {
                logger.error("版本升级", e);
                logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "版本升级", "用户获取服务软件包名列表不成功 ");
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    /**
     *
     * @param path     war包在tomcat下的路劲
     * @param start    分页开始页
     * @param limit    分页大小
     * @return
     * @throws Exception
     */
    private String selectWars(String path, int start, int limit) throws Exception {
        File webapps = new File(path);
        File[] wars = webapps.listFiles(new FilenameFilter() {
            public boolean accept(File file, String s) {
                if (s.endsWith(".war")) {
                    return true;
                }
                return false;
            }
        });
        int total = wars.length;
        String json = "{'success':true,'total':"+total+",'rows':[";
        if(total==0){
            json+=",";
        }if(total > 0){
            int index = 0;
            for (int i = 0;i<total;i ++) {
                if(i == start && i < limit){
                    start ++;
                    index ++;
                    File f = wars[i];
                    long modifiedTime = f.lastModified();
                    Date date=new Date(modifiedTime);
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String upgradeTime=sdf.format(date);
                    ZipFile file = new ZipFile(f);
                    ZipEntry entry = file.getEntry("META-INF/version.properties");
                    String warName = f.getName().substring(0,f.getName().lastIndexOf('.'));
                    boolean isExistOld = isExistOld(webapps,warName);
                    String version = null;
                    String buildDate = null;
                    String newVersion = null;
                    if(entry!=null){
                        InputStream in = file.getInputStream(entry);                       //"/META-INF/version.properties"
                        Properties config = new Properties();
                        config.load(in);
                        version = config.getProperty("version");
                        buildDate = config.getProperty("builddate");
                        in.close();
                    }else {
                        version = "低版本,没有版本说明";
                        buildDate = "低版本,没有版本说明";
                    }
                    newVersion = getNewVersion(path,f.getName());
                    file.close();
                    json += "{warName:'"+warName+"',upgradeTime:'"+upgradeTime+
                            "',warVersion:'"+version+"',buildDate:'"+buildDate+
                            "',newVersion:'"+newVersion+"',flag:"+isExistOld+"},";
                }
            }
        }
        json += "]}";
        return json;
    }

    private String getNewVersion(String path, String name) throws IOException {
        File f = new File(path + "/" + name + "_tmp");
        String version = null;
        if(f.exists()){
            ZipFile file = new ZipFile(f);
            ZipEntry entry = file.getEntry("META-INF/version.properties");
            if(entry!=null){
                InputStream in = file.getInputStream(entry);                       //"/META-INF/version.properties"
                Properties config = new Properties();
                config.load(in);
                version = config.getProperty("version");
                in.close();
            }else {
                version = "低版本,没有版本说明";
            }
        }else {
            version = "没有上传升级版本";
        }
        return version;
    }

    private boolean isExistOld(File webapps, String warName) {
        File[] warOlds = webapps.listFiles(new FilenameFilter() {
            public boolean accept(File file, String s) {
                if (s.endsWith(".war_old")) {
                    return true;
                }
                return false;
            }
        });
        if(warOlds.length>0){
            for (int i = 0;i<warOlds.length;i++){
                if(warOlds[i].getName().equals(warName+".war_old")){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取 自有Jar软件包列表名
     * */
    public String selectJar() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = "{success:true,total:0,rows:[]}";
        try {
            int start = ServletRequestUtils.getIntParameter(request, "start");
            int limit = ServletRequestUtils.getIntParameter(request, "limit");
            json = selectJars(StringContext.webPath, start, limit);
//            logService.newLog("INFO",  SessionUtils.getAccount(request).getUserName(), "版本升级","用户获取自有Jar软件包列表名成功 ");
        } catch (Exception e) {
            if(AuditFlagAction.getAuditFlag()) {
                logger.error("版本升级", e);
                logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "版本升级", "用户获取自有Jar软件包名列表不成功 ");
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    private String selectJars(String path, int start, int limit) throws IOException {
        File webapps = new File(path);
        File[] wars = webapps.listFiles(new FilenameFilter() {
            public boolean accept(File file, String s) {
                if (s.endsWith(".war")) {
                    return true;
                }
                return false;
            }
        });
        List<JarBean> jarBeans = new ArrayList<JarBean>();
        for (int i = 0;i<wars.length;i ++) {
            File f = wars[i];
            ZipFile file = new ZipFile(f);
            ZipEntry entry = file.getEntry("META-INF/version.properties");
            String jar = null;
            if(entry!=null){
                InputStream in = file.getInputStream(entry);                       //"/META-INF/version.properties"
                Properties config = new Properties();
                config.load(in);
                jar = config.getProperty("jar");
                in.close();
                jarBeans = toJarBeanList(wars[i].getName(),jar,jarBeans);
            }
            file.close();
        }
        int total = jarBeans.size();
        String json = "{'success':true,'total':"+total+",'rows':[";
        if(total==0){
            json+=",";
        }if(total > 0){
            int index = 0;
            for (int i = 0;i<total;i ++) {
                if(i == start && i < limit){
                    start ++;
                    index ++;
                    json += "{jarName:'" + jarBeans.get(i).getJarName() +
                            "',warName:'" + jarBeans.get(i).getWarName() +
                            "',jarVersion:'" + jarBeans.get(i).getJarVersion() + "'},";
                }
            }
        }
        json +="]}";
        return json;
    }

    private List<JarBean> toJarBeanList(String warName, String jar, List<JarBean> jarBeans) {
        String[] jars = jar.split(",");
        for (int i = 0;i<jars.length;i ++){
            JarBean jarBean = new JarBean();
            jarBean.setWarName(warName);
            jarBean.setJarName(jars[i].split("-")[0]);
            jarBean.setJarVersion(jars[i].split("-")[1]);
            jarBeans.add(jarBean);
        }
        return jarBeans;
    }

    /**
     * 上传文件*.war
     */
    public String uploadWar() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase base = new ActionBase();
        String result =	base.actionBegin(request);
        String msg = "上传失败";
        try {
            if(uploadFileFileName.endsWith(".war")){
                boolean isFile = checkFile();
                if(isFile){
                    FileUtil.upload(StringContext.webPath, uploadFile, uploadFileFileName + "_tmp");
                    msg = "上传成功,点击[确定]返回页面!";
                    if(AuditFlagAction.getAuditFlag()) {
                        logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "版本升级", "用户上传需要更新的文件" + uploadFileFileName + "成功");
                        String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), "用户上传需要更新的文件" + uploadFileFileName + "成功", "版本升级", "info", "004", "1", new Date());
                        SiteContextLoaderServlet.sysLogService.offer(log);
                    }
                } else {
                    msg = "上传的文件是非法的!";
                    if(AuditFlagAction.getAuditFlag()) {
                        logService.newLog("WARN", SessionUtils.getAccount(request).getUserName(), "版本升级", "用户上传需要更新的文件" + uploadFileFileName + "是非法的");
                        String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), "用户上传需要更新的文件" + uploadFileFileName + "是非法的", "版本升级", "info", "004", "0", new Date());
                        SiteContextLoaderServlet.sysLogService.offer(log);
                    }
                }
            }else{
                msg = "上传的文件不是[*.war]文件";
            }
        } catch (Exception e) {
            if(AuditFlagAction.getAuditFlag()) {
                msg = "上传失败" + e.getMessage();
                logger.error("版本升级", e);
                logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "版本升级", "用户上传需要更新的文件" + uploadFileFileName + "不成功");
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), "用户上传需要更新的文件" + uploadFileFileName + "不成功", "版本升级", "info", "004", "0", new Date());
                SiteContextLoaderServlet.sysLogService.offer(log);
            }
        }
        String json = "{success:true,msg:'"+msg+"'}";

        base.actionEnd(response, json, result);
        return null;
    }

    /**
     * 校验上传的文件是不是所需的文件
     * 1.打开包
     * 2.计算*.war的MD5
     * 3.比较MD5
     * @return
     */
    private boolean checkFile() {

        return true;
    }

    /**
     * 升级
     */
    public String upgrade() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String msg = null;
        try {
            File dir = new File(StringContext.webPath);
            boolean isOk = checkTime(dir); //true,可以升级
            if(isOk){
                File[] files = dir.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File file, String s) {
                        if (s.endsWith(".war_tmp")) {
                            return true;
                        }
                        return false;
                    }
                });
                if(files.length>0){
                    renameWarFiles(StringContext.webPath,files);
                    renameWarTmpFiles(files);
                    if(msg==null){
                        upgrade(StringContext.service_name);
                        msg = "升级成功,点击[确定]返回页面!";
                        if(AuditFlagAction.getAuditFlag()) {
                            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "版本升级", "用户升级成功");
                            String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), "用户升级成功", "版本升级", "info", "004", "0", new Date());
                            SiteContextLoaderServlet.sysLogService.offer(log);
                        }
                    }
                }else {
                    msg = "没有需要升级的服务";
                    if(AuditFlagAction.getAuditFlag()) {
                        logService.newLog("INFO",  SessionUtils.getAccount(request).getUserName(), "版本升级","用户升级失败,没有需要升级的服务");
                        String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), "用户升级失败,没有需要升级的服务", "版本升级", "info", "004", "0", new Date());
                        SiteContextLoaderServlet.sysLogService.offer(log);
                    }
                }
            } else {
                msg = "升级过于频繁,请于"+upgradeTime+"后升级!";
                if(AuditFlagAction.getAuditFlag()) {
                    logService.newLog("WARN",  SessionUtils.getAccount(request).getUserName(), "版本升级","用户升级失败,升级过于频繁:10分中内多次升级");
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), "用户升级失败,升级过于频繁:10分中内多次升级", "版本升级", "info", "004", "0", new Date());
                    SiteContextLoaderServlet.sysLogService.offer(log);
                }
            }
        } catch (Exception e) {
            msg = "升级失败" + e.getMessage();
            if(AuditFlagAction.getAuditFlag()) {
                logger.error("版本升级", e);
                logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "版本升级", "用户升级不成功");
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), "用户升级不成功", "版本升级", "info", "004", "0", new Date());
                SiteContextLoaderServlet.sysLogService.offer(log);
            }
        }
        String json = "{success:true,msg:'"+msg+"'}";

        actionBase.actionEnd(response, json, result);
        return null;
    }

    private boolean checkTime(File dir) {
        File[] files = dir.listFiles(new FilenameFilter() {
            public boolean accept(File file, String s) {
                if(s.endsWith(".war")){
                    return true;
                }
                return false;
            }
        });
        long[] times = new long[files.length];
        for (int i=0;i<files.length;i++ ){
            long time = files[i].lastModified();
            times[i] = System.currentTimeMillis()-time;
        }
        Arrays.sort(times);
        long time = times[0];
        if(time < 10 * 60 * 1000){
            long m = time/1000/60;
            long s = time/1000 - m*60;
            upgradeTime = ( 10 - m ) +"分"+ ( 60 - s ) + "秒";
            return false;
        }
        return true;
    }


    /**
     * 改*.war成为*.war_old;
     * @param path
     */
    private void renameWarFiles(String path,File[] files) {
        for(int i=0;i<files.length;i++){
            File tmpFile = files[i];
            File newFile = new File(path+"/" + tmpFile.getName().split("_tmp")[0] + "_old");
            // 改变*.war 成为*.war_old
            if(newFile.exists()){
                newFile.delete();
            }
            File oldFile = new File(path+"/" + tmpFile.getName().split("_tmp")[0]);
            oldFile.renameTo(newFile);
        }
    }

    /**
     * 改*.war_tmp为*.war
     * @param files  *.war_tmp
     */
    private void renameWarTmpFiles(File[] files) {
        for(int i=0;i<files.length;i++){
            File tmpFile = files[i];
            File newFile = new File(StringContext.webPath+"/"+tmpFile.getName().split("_tmp")[0]);//*.war
            // 改变*.war_tmp 成为*.war
            if(newFile.exists()){
                newFile.delete();
            }
            tmpFile.renameTo(newFile);
        }
    }

    private void upgrade(String service) throws InterruptedException, Ex {
        Proc proc;
        OSInfo osinfo = OSInfo.getOSInfo();
        if (osinfo.isWin()) {
            proc = new Proc();
            proc.exec("nircmd service upgrade "+service);
        }
        if (osinfo.isLinux()) {
            proc = new Proc();
            proc.exec("service "+service+" upgrade");
        }
        Thread.sleep(1000*6);
    }

    /**
     * 版本回退
     */
    public String backup() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String msg = null;
        try {
            String warName = ServletRequestUtils.getStringParameter(request,"warName");
            File oldFile = new File(StringContext.webPath+"/"+warName+"_old");
            File tmpFile = new File(StringContext.webPath+"/"+warName+"_tmp");
            if(tmpFile.exists()){
                tmpFile.delete();
            }
            oldFile.renameTo(tmpFile);
            if(msg==null){
                msg = "回退"+warName+"准备完成,点击升级按钮可以完成回退";
                if(AuditFlagAction.getAuditFlag()) {
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "版本升级", "回退" + warName + "准备完成");
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), "回退" + warName + "准备完成", "版本升级", "info", "004", "1", new Date());
                    SiteContextLoaderServlet.sysLogService.offer(log);
                }
            }
        } catch (Exception e) {
            msg = "回退准备失败"+e.getMessage();
            if(AuditFlagAction.getAuditFlag()) {
                logger.error("版本升级", e);
                logService.newLog("ERROR",  SessionUtils.getAccount(request).getUserName(), "版本升级","回退准备失败");
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), "回退准备失败", "版本升级", "info", "004", "0", new Date());
                SiteContextLoaderServlet.sysLogService.offer(log);
            }
        }
        String json = "{success:true,msg:'"+msg+"'}";

        actionBase.actionEnd(response, json, result);
        return null;
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
