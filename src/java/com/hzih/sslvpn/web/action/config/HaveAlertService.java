package com.hzih.sslvpn.web.action.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 钱晓盼 on 14-1-26.
 */
public class HaveAlertService extends Thread {
    final static Logger logger = LoggerFactory.getLogger(HaveAlertService.class);

    private boolean isRun = false;

    private StringBuffer buff;

    private int equ = 0;
    private int bus = 0;
    private int sec = 0;

    public StringBuffer getBuff() {
        buff = new StringBuffer();
        buff.append("{device:'");
        buff.append(equ);
        buff.append("',business:'");
        buff.append(bus);
        buff.append("',security:'");
        buff.append(sec);
        buff.append("'}");
        equ = 0;
        bus = 0;
        sec = 0;
        return buff;
    }

    public void init() {

    }

    public boolean isRunning() {
        return isRun;
    }

    public void close() {
        isRun = false;
    }

    public void run() {
        isRun = true;
        while (isRun) {
            equ = 0;
            bus = 0;
            sec = 0;
            try {
                Thread.sleep(1000*60*5);
            } catch (InterruptedException e) {
                logger.info(e.getMessage(),e);
            }
        }
    }
}
