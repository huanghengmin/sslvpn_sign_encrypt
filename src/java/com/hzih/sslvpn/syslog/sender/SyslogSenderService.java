package com.hzih.sslvpn.syslog.sender;

import com.hzih.sslvpn.entity.SysLogServer;
import com.hzih.sslvpn.utils.StringContext;
import org.apache.log4j.Logger;
import org.productivity.java.syslog4j.Syslog;
import org.productivity.java.syslog4j.SyslogConfigIF;
import org.productivity.java.syslog4j.SyslogIF;
import org.productivity.java.syslog4j.impl.net.udp.UDPNetSyslogConfig;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-27
 * Time: 下午3:07
 * To change this template use File | Settings | File Templates.
 */
public class SyslogSenderService implements Runnable {
    private static final Logger logger = Logger.getLogger(SyslogSenderService.class);
    private List<SysLogServer> sysLogs = new ArrayList<SysLogServer>();
    Queue<String> queue = new LinkedList<>();
    private String charset = "utf-8";
    private boolean isRunning = false;

    public void init() {
        SyslogConfigXML config = new SyslogConfigXML();
        this.sysLogs = config.findAll(StringContext.syslog_xml);
    }


    public void offer(String log) {
        queue.offer(log);
    }

    public void run() {
        isRunning = true;
        logger.info("syslog send service running");
        while (isRunning) {
            if (queue.size() > 0) {
                String log = queue.poll();
                work(log);
            }
            try {
                Thread.sleep(1000 * 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void work(String log) {
        if (log != null) {
            if (sysLogs != null) {
                int i = 0;
                for (SysLogServer ipPort : sysLogs) {
                    SyslogConfigIF config = new UDPNetSyslogConfig();
                    config.setHost(ipPort.getHost());
                    config.setCharSet(charset);
                    config.setPort(ipPort.getPort());
                    int j = i++;
                    SyslogIF syLog;
                    try {
                        syLog = Syslog.getInstance(String.valueOf(j));
                    } catch (Exception e) {
                        e.printStackTrace();
                        syLog = null;
                    }
                    if (syLog == null) {
                        syLog = Syslog.createInstance(String.valueOf(j), config);
                    }
                    syLog.info(log);
                    syLog.flush();
                    syLog.shutdown();
                }
            }
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void start() {
        isRunning = true;
    }

    public void close() {
        isRunning = false;
    }
}
