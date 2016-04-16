package com.hzih.sslvpn.web.servlet;

import com.hzih.sslvpn.domain.SafePolicy;
import com.hzih.sslvpn.service.SafePolicyService;
import com.hzih.sslvpn.constant.AppConstant;
import com.hzih.sslvpn.constant.ServiceConstant;
import com.hzih.sslvpn.syslog.sender.SyslogSenderService;
import com.hzih.sslvpn.web.SiteContext;
import com.hzih.sslvpn.web.thread.SystemStatusService;
import com.inetec.common.util.OSInfo;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;
import java.io.IOException;

public class SiteContextLoaderServlet extends DispatcherServlet {
    private static final long serialVersionUID = 1L;
    public static boolean isRunSysLogSender = false;
    public static SyslogSenderService sysLogService = new SyslogSenderService();

    public void startSysLogSender() {
        if (isRunSysLogSender) {
            return;
        }
        sysLogService.init();
        Thread thread = new Thread(sysLogService);
        thread.start();
        isRunSysLogSender = true;
    }

    /**
     * 重启syslog进程
     */
    public static void restartSyslogSender() {
        if (isRunSysLogSender) {
            sysLogService.close();
            isRunSysLogSender = false;
        }
        sysLogService.init();
        Thread thread = new Thread(sysLogService);
        thread.start();
        isRunSysLogSender = true;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        SiteContext.getInstance().contextRealPath = config.getServletContext().getRealPath("/");
        servletContext.setAttribute("appConstant", new AppConstant());
        SafePolicyService service = (SafePolicyService) context.getBean(ServiceConstant.SAFEPOLICY_SERVICE);
        SafePolicy data = service.getData();
        SiteContext.getInstance().safePolicy = data;

        logger.info("LoaderListener 启动 [syslog发送服务] 开始...");
        startSysLogSender();
        logger.info("LoaderListener 启动 [syslog发送服务] 完成...");

        OSInfo osinfo = OSInfo.getOSInfo();
        if (osinfo.isLinux()) {
            new SystemStatusService().start();
        }
    }

    @Override
    public ServletConfig getServletConfig() {
        // do nothing
        return null;
    }

    @Override
    public void service(ServletRequest arg0, ServletResponse arg1) throws ServletException, IOException {
        // do nothing
    }

    @Override
    public String getServletInfo() {
        // do nothing
        return null;
    }

    @Override
    public void destroy() {
        // do nothing
    }
}
