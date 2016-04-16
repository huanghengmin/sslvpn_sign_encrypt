package com.hzih.sslvpn.web.action.user;

import com.hzih.sslvpn.domain.Account;
import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.web.ApplicationUtils;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.action.AccountLogUtils;
import com.hzih.sslvpn.syslog.sender.SyslogSender;
import com.hzih.sslvpn.web.action.audit.AuditFlagAction;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 *   用户退出系统
 */

public class LogoutAction extends ActionSupport {
	private LogService logService;
	private Logger logger = Logger.getLogger(LogoutAction.class);

	public String execute() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
		Account account = SessionUtils.getAccount(request);
		String userName = null;
		String msg = null;
		if(account!=null){
			userName = account.getUserName();
			SessionUtils.removeAccount(request);
			SessionUtils.invalidateSession(request);
			ApplicationUtils.removeAccount(account.getUserName());
			msg = "用户退出成功";
			if(AuditFlagAction.getAuditFlag()) {
				logService.newLog("INFO", userName, "用户登录", "用户退出成功");
				logger.info("管理员" + userName + ",操作时间:" + new Date() + ",操作信息:" + msg);
				String log = AccountLogUtils.getResult(userName, msg, "用户登陆", "info", "004", "1", new Date());
				SyslogSender.sysLog(log);
			}
        }
		return "success";
	}

	public void setLogService(LogService logService) {
		this.logService = logService;
	}
}
