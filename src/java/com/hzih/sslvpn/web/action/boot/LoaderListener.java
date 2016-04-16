package com.hzih.sslvpn.web.action.boot;

import com.hzih.sslvpn.web.action.config.HaveAlertService;
import com.hzih.sslvpn.syslog.sender.SyslogSenderService;
import com.hzih.sslvpn.web.thread.SystemStatusService;
import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * boot 监听
 */
public class LoaderListener implements ServletContextListener {
    private Logger logger = Logger.getLogger(LoaderListener.class);

    /**
     * syslog线程
     */
//    public static boolean isRunSysLogSender = false;
//    public static SyslogSenderService sysLogService = new SyslogSenderService();


    public static boolean isRunSystemStatus = false;
    public static SystemStatusService systemStatusService = new SystemStatusService();

    /**
     * 报警线程
     */
    public static boolean isHaveAlertService = false;
    public static HaveAlertService haveAlertService = new HaveAlertService();

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
//        sysLogService.close();
//        isRunSysLogSender = false;
        haveAlertService.close();
        isHaveAlertService = false;
    }

    /**
     * 运行syslog进程
     */
    /*public void startSysLogSender() {
        if (isRunSysLogSender) {
            return;
        }
        sysLogService.init();
        Thread thread = new Thread(sysLogService);
        thread.start();
        isRunSysLogSender = true;
    }*/

    public void startSystemStatus(){
        if (isRunSystemStatus) {
            return;
        }
        systemStatusService.start();
    }

    /**
     *
     */
    private void runHaveAlertService() {
        if (isHaveAlertService) {
            return;
        }
        haveAlertService.init();
        haveAlertService.start();
        isHaveAlertService = true;
    }

    /**
     * 重启syslog进程
     */
    /*public static void restartSyslogSender() {
        if (isRunSysLogSender) {
            sysLogService.close();
            isRunSysLogSender = false;
        }
        sysLogService.init();
        Thread thread = new Thread(sysLogService);
        thread.start();
        isRunSysLogSender = true;
    }*/

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        //启动日志发送线程
//        logger.info("LoaderListener 启动 [syslog发送服务] 开始...");
//        startSysLogSender();
//        logger.info("LoaderListener 启动 [syslog发送服务] 完成...");

        logger.info("LoaderListener 启动 [报警服务服务] 开始...");
        runHaveAlertService();
        logger.info("LoaderListener 启动 [报警服务服务] 完成...");

//        logger.info("LoaderListener 启动 [系统状态服务] 开始...");
//        startSystemStatus();
//        logger.info("LoaderListener 启动 [系统状态服务] 完成...");
        /*logger.info("LoaderListener 启动 [SyslogServer服务服务] 开始...");
        String host = SyslogServerXMLUtils.getValue(SyslogServerXMLUtils.host);
        String port = SyslogServerXMLUtils.getValue(SyslogServerXMLUtils.port);
        logger.info("syslog server host:" + host + ",port:" + port);
        if (host != null && port != null) {
            SyslogServer syslog = new SyslogServer();
            syslog.config(host, Integer.parseInt(port));
            syslog.start();
            ShellUtils.add_syslog_server("udp", port);
        }
        logger.info("LoaderListener 启动 [SyslogServer服务服务] 成功...");*/
    }
}
