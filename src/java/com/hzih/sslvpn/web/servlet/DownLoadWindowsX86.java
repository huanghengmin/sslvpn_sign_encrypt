package com.hzih.sslvpn.web.servlet;

import com.hzih.sslvpn.entity.Version;
import com.hzih.sslvpn.utils.FileUtil;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.utils.VersionUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.StringTokenizer;


public class DownLoadWindowsX86 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Logger logger = Logger.getLogger(DownLoadWindowsX86.class);

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        String msg = null;
        String path = null;
        try {
            String Agent = request.getHeader("User-Agent");
            StringTokenizer st = new StringTokenizer(Agent, ";");
            st.nextToken();
            String userBrowser = st.nextToken();
            Version av = null;
            String android_version = StringContext.systemPath + "/client/windows/x86" + "/version.xml";
            File android_info = new File(android_version);
            if (android_info.exists()) {
                av = VersionUtils.readInfo(android_info);
                String name = av.getName();
                path = StringContext.systemPath + "/client/windows/x86" + "/" + name;
            }
            File source = new File(path);
            String name = source.getName();
            FileUtil.downType(response, name, userBrowser);
            response = FileUtil.copy(source, response);
            msg = "下载Windows 32位 SSLVPN 客户端成功"+request.getRemoteAddr();
            logger.info(msg+",时间:"+new Date());
        } catch (Exception e) {
            e.printStackTrace();
            msg = "下载Windows 32位 SSLVPN 客户端失败"+request.getRemoteAddr();
            logger.error("下载Windows 32位 SSLVPN 客户端失败"+request.getRemoteAddr(), e);
        }
        String json = "{success:true,msg:'" + msg + "'}";
        PrintWriter writer = response.getWriter();
        writer.write(json);
        writer.flush();
        writer.close();
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
