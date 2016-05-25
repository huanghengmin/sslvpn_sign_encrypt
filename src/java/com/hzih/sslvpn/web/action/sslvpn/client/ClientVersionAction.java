package com.hzih.sslvpn.web.action.sslvpn.client;

import com.hzih.sslvpn.entity.Version;
import com.hzih.sslvpn.utils.*;
import com.hzih.sslvpn.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 12-9-25
 * Time: 下午12:22
 * To change this template use File | Settings | File Templates.
 */
public class ClientVersionAction extends ActionSupport {
    private Logger logger = Logger.getLogger(ClientVersionAction.class);
    private File uploadFile;
    private static final int BUFFER_SIZE = 1 * 1024;
    private String fileContentType;
    private String uploadFileFileName;

    public String upload() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        if (!uploadFileFileName.endsWith("zip")) {
            String msg = "上传的文件格式不对";
            String json = "{success:true,msg:'" + msg + "'}";
            actionBase.actionEnd(response, json, result);
        }
        String os = request.getParameter("os");
        String toSrc = null;
        if (os.equals("android"))
            toSrc = StringContext.systemPath + "/client/android" + "/" + uploadFileFileName;
        else if (os.equals("x86")) {
            toSrc = StringContext.systemPath + "/client/windows/x86" + "/" + uploadFileFileName;
        }else if (os.equals("x64")) {
            toSrc = StringContext.systemPath + "/client/windows/x64" + "/" + uploadFileFileName;
        }else if (os.equals("linuxMobile")) {
            toSrc = StringContext.systemPath + "/client/linux/linuxMobile" + "/" + uploadFileFileName;
        }else if (os.equals("linuxCz")) {
            toSrc = StringContext.systemPath + "/client/linux/linuxCz" + "/" + uploadFileFileName;
        }
        File toFile = new File(toSrc);
        if (!toFile.exists()) {
            toFile.getParentFile().mkdirs();
        }
        try {
            writeFile(this.uploadFile, toFile);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = "上传失败";
            String json = "{success:true,msg:'" + msg + "'}";
            actionBase.actionEnd(response, json, result);
        }
        ExtractZip extractZip = new ExtractZip();
        if (extractZip.Unzip(toSrc)) {
            String msg = "上传成功";
            String json = "{success:true,msg:'" + msg + "'}";
            actionBase.actionEnd(response, json, result);
            toFile.delete();
        } else {
            String msg = "上传失败";
            String json = "{success:true,msg:'" + msg + "'}";
            actionBase.actionEnd(response, json, result);
        }
        return null;
    }

    public String find() throws Exception {
        /**
         * 返回三个客户端文件名和版本信息
         */
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        int totalCount = 0;
        totalCount = totalCount + 3;
        StringBuilder json = new StringBuilder("{totalCount:" + totalCount + ",root:[");
        Version av = null;
        Version windows = null;
        StringBuilder sb = new StringBuilder();
        String android_version = StringContext.systemPath +"/client/android" + "/version.xml";
        File android_info = new File(android_version);
        if (android_info.exists()) {
            av = VersionUtils.readInfo(android_info);
            if (av != null) {
                sb.append("{");
                sb.append("version:'" + av.getVersion() + "',");
                sb.append("name:'" + av.getName() + "',");
                sb.append("os:'android'");
                sb.append("}");
            }
        }

        String windows_version = StringContext.systemPath +"/client/windows/x86" + "/version.xml";
        File windows_info = new File(windows_version);
        if (windows_info.exists()) {
            windows = VersionUtils.readInfo(windows_info);
            if (windows != null) {
                if (av != null)
                    sb.append(",");
                sb.append("{");
                sb.append("version:'" + windows.getVersion() + "',");
                sb.append("name:'" + windows.getName() + "',");
                sb.append("os:'x86'");
                sb.append("}");
            }
        }


        String windows64_version = StringContext.systemPath +"/client/windows/x64" + "/version.xml";
        File windows64_info = new File(windows64_version);
        if (windows64_info.exists()) {
            windows = VersionUtils.readInfo(windows64_info);
            if (windows != null) {
                if (av != null)
                    sb.append(",");
                sb.append("{");
                sb.append("version:'" + windows.getVersion() + "',");
                sb.append("name:'" + windows.getName() + "',");
                sb.append("os:'x64'");
                sb.append("}");
            }
        }


        String linuxMobile_version = StringContext.systemPath +"/client/linux/linuxMobile" + "/version.xml";
        File linuxMobile_info = new File(linuxMobile_version);
        if (linuxMobile_info.exists()) {
            windows = VersionUtils.readInfo(linuxMobile_info);
            if (windows != null) {
                if (av != null) {
                    sb.append(",");
                }
                sb.append("{");
                sb.append("version:'" + windows.getVersion() + "',");
                sb.append("name:'" + windows.getName() + "',");
                sb.append("os:'linuxMobile'");
                sb.append("}");
            }
        }
        String linuxCz_version = StringContext.systemPath +"/client/linux/linuxCz" + "/version.xml";
        File linuxCz_info = new File(linuxCz_version);
        if (linuxCz_info.exists()) {
            windows = VersionUtils.readInfo(linuxCz_info);
            if (windows != null) {
                if (av != null)
                    sb.append(",");
                sb.append("{");
                sb.append("version:'" + windows.getVersion() + "',");
                sb.append("name:'" + windows.getName() + "',");
                sb.append("os:'linuxCz'");
                sb.append("}");
            }
        }
        json.append(sb.toString());
        json.append("]}");
        try {
            actionBase.actionEnd(response, json.toString(), result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String download() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false}";
        String os = request.getParameter("os");
        String name = request.getParameter("name");
        try {
            String Agent = request.getHeader("User-Agent");
            StringTokenizer st = new StringTokenizer(Agent, ";");
            st.nextToken();
            //得到用户的浏览器名  MSIE  Firefox
            String userBrowser = st.nextToken();
            String path = null;
            if (os.equals("android")) {
                path = StringContext.systemPath +"/client/android" + "/" + name;
            } else if (os.equals("x86")) {
                path = StringContext.systemPath +"/client/windows/x86" + "/" + name;
            } else if (os.equals("x64")) {
                path = StringContext.systemPath +"/client/windows/x64" + "/" + name;
            } else if (os.equals("linuxMobile")) {
                path = StringContext.systemPath +"/client/linux/linuxMobile" + "/" + name;
            } else if (os.equals("linuxCz")) {
                path = StringContext.systemPath +"/client/linux/linuxCz" + "/" + name;
            }
            File source = new File(path);
            if (source.exists()) {
                FileUtil.downType(response, name, userBrowser);
                response = FileUtil.copy(source, response);
                json = "{success:true}";
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return null;
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public File getUploadFile() {
        return this.uploadFile;
    }

    public void setUploadFile(File file) {
        this.uploadFile = file;
    }

    public String getUploadFileFileName() {
        return uploadFileFileName;
    }

    public void setUploadFileFileName(String uploadFileFileName) {
        this.uploadFileFileName = uploadFileFileName;
    }

    private static void writeFile(File src, File dst) {
        try {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
                out = new BufferedOutputStream(new FileOutputStream(dst), BUFFER_SIZE);
                byte[] buffer = new byte[BUFFER_SIZE];
                while (in.read(buffer) > 0) {
                    out.write(buffer);
                }
            } finally {
                if (null != in) {
                    in.close();
                }
                if (null != out) {
                    out.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
