package com.hzih.sslvpn.syslog.sender;

import com.hzih.sslvpn.web.action.boot.LoaderListener;
import com.hzih.sslvpn.web.servlet.SiteContextLoaderServlet;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-27
 * Time: 下午3:07
 * To change this template use File | Settings | File Templates.
 */
public class SyslogSender {

    /**
     * 发送日志方法
     *
     * @param msg
     */
    public static void sysLog(String msg) {
        if (SiteContextLoaderServlet.sysLogService.isRunning()) {
            SiteContextLoaderServlet.sysLogService.offer(msg);
        }
    }
}
