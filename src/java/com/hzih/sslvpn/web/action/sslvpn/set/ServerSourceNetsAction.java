package com.hzih.sslvpn.web.action.sslvpn.set;

import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.ServerDao;
import com.hzih.sslvpn.dao.SourceNetDao;
import com.hzih.sslvpn.domain.Server;
import com.hzih.sslvpn.domain.SourceNet;
import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.utils.FileUtil;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.utils.VPNConfigUtil;
import com.hzih.sslvpn.utils.des.DesUtils;
import com.hzih.sslvpn.utils.md5.MD5Utils;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.action.AccountLogUtils;
import com.hzih.sslvpn.web.action.ActionBase;
import com.hzih.sslvpn.syslog.sender.SyslogSender;
import com.hzih.sslvpn.web.action.audit.AuditFlagAction;
import com.hzih.sslvpn.web.action.boot.LoaderListener;
import com.inetec.common.client.ECommonUtilFactory;
import com.inetec.common.client.IECommonUtil;
import com.inetec.common.client.util.LogBean;
import com.inetec.common.client.util.XChange;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts2.ServletActionContext;

import javax.naming.NamingEnumeration;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.Key;
import java.util.*;

/**
 * Created by Administrator on 15-4-14.
 */
public class ServerSourceNetsAction extends ActionSupport {
    private Logger logger = Logger.getLogger(ServerSourceNetsAction.class);
    private ServerDao serverDao;
    private SourceNetDao sourceNetDao;
    private LogService logService;
    private Server server;
    private File uploadFile;
    private String uploadFileFileName;
    private String uploadFileContentType;

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

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public ServerDao getServerDao() {
        return serverDao;
    }

    public void setServerDao(ServerDao serverDao) {
        this.serverDao = serverDao;
    }

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public SourceNetDao getSourceNetDao() {
        return sourceNetDao;
    }

    public void setSourceNetDao(SourceNetDao sourceNetDao) {
        this.sourceNetDao = sourceNetDao;
    }

    private int start;
    private int limit;

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

    public String update() throws Exception {
        HttpServletResponse response = ServletActionContext.getResponse();
        HttpServletRequest request = ServletActionContext.getRequest();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = "保存失败";
        String json = "{success:false}";
        String[] ids = request.getParameterValues("ids");
        Server server = serverDao.find();
        Set<SourceNet> sourceNetSet = server.getSourceNets();
        sourceNetSet.clear();
        if (null != ids && !"".equals(ids) && ids.length > 0) {
            for (int i = 0; i < ids.length; i++) {
                String id = ids[i];
                if (!"".equals(id)) {
                    SourceNet sourceNet = sourceNetDao.findById(Integer.parseInt(id));
                    if (!sourceNetSet.contains(sourceNet))
                        sourceNetSet.add(sourceNet);
                }
            }
        }
        serverDao.merge(server);
        server.setSourceNets(sourceNetSet);
        VPNConfigUtil.configServer(server, StringContext.server_config_file);
        FileUtil.copy(new File(StringContext.server_config_file),StringContext.server_config_file_bak);
        MD5Utils md5Utils = new MD5Utils();
        md5Utils.shellMd5(StringContext.server_config_file,StringContext.server_config_file_md5);
        msg = "保存成功";
        json = "{success:true,msg:'" + msg + "'}";
        if(AuditFlagAction.getAuditFlag()) {
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "服务资源", msg);
            String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "服务资源", "info", "004", "1", new Date());
            SyslogSender.sysLog(log);
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String export()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false}";

        //得到父路径
        String Agent = request.getHeader("User-Agent");
        StringTokenizer st = new StringTokenizer(Agent, ";");
        st.nextToken();
        //得到用户的浏览器名  MSIE  Firefox
        String userBrowser = st.nextToken();
        Key key = DesUtils.getKey();
        if(key!=null){
            DesUtils.encrypt(StringContext.server_config_file,StringContext.server_des_config_file,key);
            MD5Utils md5Utils = new MD5Utils();
            md5Utils.shellMd5(StringContext.server_des_config_file,StringContext.server_des_config_file_md5);
        }else {
            DesUtils.saveDesKey();
            Key new_key = DesUtils.getKey();
            DesUtils.encrypt(StringContext.server_config_file,StringContext.server_des_config_file,new_key);
            MD5Utils md5Utils = new MD5Utils();
            md5Utils.shellMd5(StringContext.server_des_config_file,StringContext.server_des_config_file_md5);
        }
        File des_file =  new File(StringContext.server_des_config_file);
        if (des_file.exists()) {
            FileUtil.downType(response, des_file.getName(), userBrowser);
            response = FileUtil.copy(des_file, response);
        }
        json = "{success:true}";
        actionBase.actionEnd(response, json, result);
        return null;

    }

    public String inport()throws Exception{
        HttpServletResponse response = ServletActionContext.getResponse();
        HttpServletRequest request = ServletActionContext.getRequest();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = "上传配置文件失败";
        String json = "{success:false,msg:'"+msg+"'}";
        FileUtil.copy(uploadFile,StringContext.server_des_config_file);
        File file = new File(StringContext.server_des_config_file);
        if(file.exists()){
            MD5Utils md5Utils = new MD5Utils();
            Boolean flag = md5Utils.checkMd5(StringContext.server_des_config_file_md5);
            if(flag){
                Key key = DesUtils.getKey();
                if(key!=null) {
                    DesUtils.decrypt(uploadFile, StringContext.server_des_config_file, key);
                    FileUtil.copy(new File(StringContext.server_des_config_file), StringContext.server_config_file);
                    msg = "上传配置文件成功";
                    json = "{success:true,msg:'" + msg + "'}";
                }
            }else {
                msg = "文件已被篡改,上传失败......";
                json = "{success:false,msg:'"+msg+"'}";
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String save() throws Exception {
        HttpServletResponse response = ServletActionContext.getResponse();
        HttpServletRequest request = ServletActionContext.getRequest();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String traffic_server = request.getParameter("traffic_server");
        String client_to_client = request.getParameter("client_to_client");
        String comp_lzo = request.getParameter("comp_lzo");
        String duplicate_cn = request.getParameter("duplicate_cn");
        String log_append = request.getParameter("log_append");
        String msg = "保存失败";
        String json = "{success:false}";
        Server server = serverDao.find();
        if (this.server.getListen() != null && this.server.getListen() != server.getListen()) {
            server.setListen(this.server.getListen());
        }
        if (this.server.getProtocol() != null && this.server.getProtocol() != server.getProtocol()) {
            server.setProtocol(this.server.getProtocol());
        }
        if (this.server.getPort() != server.getPort()) {
            server.setPort(this.server.getPort());
        }
        if (this.server.getServer_net() != null && this.server.getServer_net() != server.getServer_net()) {
            server.setServer_net(this.server.getServer_net());
        }
        if (this.server.getServer_mask() != null && this.server.getServer_mask() != server.getServer_mask()) {
            server.setServer_mask(this.server.getServer_mask());
        }
        if (this.server.getCheck_crl() != server.getCheck_crl()) {
            server.setCheck_crl(this.server.getCheck_crl());
        }
        if (traffic_server == null) {
            traffic_server = "0";
        }
        server.setTraffic_server(Integer.parseInt(traffic_server));

        if (client_to_client == null) {
            client_to_client = "0";
        }
        server.setClient_to_client(Integer.parseInt(client_to_client));

        if (comp_lzo == null) {
            comp_lzo = "0";
        }
        server.setComp_lzo(Integer.parseInt(comp_lzo));

        if (duplicate_cn == null) {
            duplicate_cn = "0";
        }
        server.setDuplicate_cn(Integer.parseInt(duplicate_cn));

        if (log_append == null) {
            log_append = "0";
        }
        server.setLog_append(Integer.parseInt(log_append));

        if (this.server.getLog_flag() != server.getLog_flag()) {
            server.setLog_flag(this.server.getLog_flag());
        }
        if (this.server.getVerb() != server.getVerb()) {
            server.setVerb(this.server.getVerb());
        }
        if (this.server.getMute() != 0 && this.server.getMute() != server.getMute()) {
            server.setMute(this.server.getMute());
        }
        if (this.server.getDefault_domain_suffix() != null && this.server.getDefault_domain_suffix() != server.getDefault_domain_suffix()) {
            server.setDefault_domain_suffix(this.server.getDefault_domain_suffix());
        }
        if (this.server.getKeep_alive_interval() != 0 && this.server.getKeep_alive_interval() != server.getKeep_alive_interval()) {
            server.setKeep_alive_interval(this.server.getKeep_alive_interval());
        }
        if (this.server.getKeep_alive() != 0 && this.server.getKeep_alive() != server.getKeep_alive()) {
            server.setKeep_alive(this.server.getKeep_alive());
        }
        if (this.server.getClient_dns_type() != server.getClient_dns_type()) {
            server.setClient_dns_type(this.server.getClient_dns_type());
        }
        if (this.server.getClient_first_dns() != null && this.server.getClient_first_dns() != server.getClient_first_dns()) {
            server.setClient_first_dns(this.server.getClient_first_dns());
        }
        if (this.server.getClient_second_dns() != null && this.server.getClient_second_dns() != server.getClient_second_dns()) {
            server.setClient_second_dns(this.server.getClient_second_dns());
        }
        try {
            serverDao.modify(server);
            VPNConfigUtil.configServer(server, StringContext.server_config_file);
            FileUtil.copy(new File(StringContext.server_config_file),StringContext.server_config_file_bak);
            MD5Utils md5Utils = new MD5Utils();
            md5Utils.shellMd5(StringContext.server_config_file,StringContext.server_config_file_md5);
            msg = "保存成功";
            json = "{success:true,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "服务资源", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "服务资源", "info", "004", "1", new Date());
                SyslogSender.sysLog(log);
            }
        } catch (Exception e) {
            msg = "保存失败";
            json = "{success:false,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info(e.getMessage(),e);
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "服务资源", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "服务资源", "info", "004", "0", new Date());
                SyslogSender.sysLog(log);
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public boolean existObject(Set<SourceNet> sourceNets, SourceNet sourceNet) {
        if (sourceNets != null && sourceNets.size() > 0) {
            for (SourceNet s : sourceNets) {
                if (s.getNet().equals(sourceNet.getNet()) && s.getNet_mask().equals(sourceNet.getNet_mask())) {
                    return true;
                }
            }
        }
        return false;
    }

    public String findNets() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        int pageIndex = start / limit + 1;
        PageResult pageResult = sourceNetDao.listByPage(pageIndex, limit);
        if (pageResult != null) {
            Server server = serverDao.find();
            Set<SourceNet> sourceNetSet = null;
            if (server != null)
                sourceNetSet = server.getSourceNets();
            List<SourceNet> list = pageResult.getResults();
            int count = pageResult.getAllResultsAmount();
            if (list != null) {
                String json = "{success:true,total:" + count + ",rows:[";
                Iterator<SourceNet> raUserIterator = list.iterator();
                while (raUserIterator.hasNext()) {
                    SourceNet log = raUserIterator.next();
                    boolean flag = existObject(sourceNetSet, log);
                    if (sourceNetSet != null && flag) {
                        if (raUserIterator.hasNext()) {
                            json += "{" +
                                    "id:'" + log.getId() +
                                    "',net:'" + log.getNet() +
//                                    "',level:'" + log.getLevel() +
                                    "',checked:" + true +
                                    ",net_mask:'" + log.getNet_mask() + "'" +
                                    "},";
                        } else {
                            json += "{" +
                                    "id:'" + log.getId() +
                                    "',net:'" + log.getNet() +
//                                    "',level:'" + log.getLevel() +
                                    "',checked:" + true +
                                    ",net_mask:'" + log.getNet_mask() + "'" +
                                    "}";
                        }
                    } else {
                        if (raUserIterator.hasNext()) {
                            json += "{" +
                                    "id:'" + log.getId() +
                                    "',net:'" + log.getNet() +
//                                    "',level:'" + log.getLevel() +
                                    "',checked:" + false +
                                    ",net_mask:'" + log.getNet_mask() + "'" +
                                    "},";
                        } else {
                            json += "{" +
                                    "id:'" + log.getId() +
                                    "',net:'" + log.getNet() +
//                                    "',level:'" + log.getLevel() +
                                    "',checked:" + false +
                                    ",net_mask:'" + log.getNet_mask() + "'" +
                                    "}";
                        }
                    }

                }
                json += "]}";
                actionBase.actionEnd(response, json, result);
            }
        }
        return null;
    }

    public String find() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        Server server = serverDao.find();
        StringBuilder sb = new StringBuilder("{'success':true,'totalCount':1,'root':[");
        if (server != null) {
            sb.append("{");
            sb.append("listen:'" + server.getListen() + "',");
            sb.append("protocol:'" + server.getProtocol() + "',");
            sb.append("port:" + server.getPort() + ",");
            sb.append("server_net:'" + server.getServer_net() + "',");
            sb.append("server_mask:'" + server.getServer_mask() + "',");
            sb.append("check_crl:" + server.getCheck_crl() + ",");
            sb.append("traffic_server:" + server.getTraffic_server() + ",");
            sb.append("client_to_client:" + server.getClient_to_client() + ",");
            sb.append("duplicate_cn:" + server.getDuplicate_cn() + ",");
            sb.append("keep_alive:" + server.getKeep_alive() + ",");
            sb.append("keep_alive_interval:" + server.getKeep_alive_interval() + ",");
            sb.append("cipher:'" + server.getCipher() + "',");
            sb.append("comp_lzo:" + server.getComp_lzo() + ",");
            sb.append("max_clients:'" + server.getMax_clients() + "',");
            sb.append("log_append:" + server.getLog_append() + ",");
            sb.append("log_flag:" + server.getLog_flag() + ",");
            sb.append("verb:" + server.getVerb() + ",");
            sb.append("mute:" + server.getMute() + ",");
            sb.append("client_dns_type:" + server.getClient_dns_type() + ",");
            sb.append("client_first_dns:'" + server.getClient_first_dns() + "',");
            sb.append("client_second_dns:'" + server.getClient_second_dns() + "',");
            sb.append("default_domain_suffix:'" + server.getDefault_domain_suffix() + "'");
            sb.append("}");
        }
        sb.append("]}");
        actionBase.actionEnd(response, sb.toString(), result);
        return null;
    }
}
