package com.hzih.sslvpn.web.action.sslvpn.log;

import com.hzih.sslvpn.dao.LogDao;
import com.hzih.sslvpn.utils.DateUtils;
import com.hzih.sslvpn.web.action.audit.AuditFlagAction;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-6
 * Time: 下午2:15
 * To change this template use File | Settings | File Templates.
 */
public class ClearTask extends TimerTask {
    private Logger logger = Logger.getLogger(ClearTask.class);

    private LogDao logDao;

    public ClearTask(LogDao logDao) {
        this.logDao = logDao;
    }

    @Override
    public void run() {
        logDao.clearLogs();
        if(AuditFlagAction.getAuditFlag()) {
            logger.info("日志清理:清空三天前客户端连接日志成功,时间:" + DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        }
    }
}
