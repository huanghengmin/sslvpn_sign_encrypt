package com.hzih.sslvpn.web.action;

import cn.collin.commons.utils.DateUtils;
import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.SiteContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-6-14
 * Time: 下午2:33
 * To change this template use File | Settings | File Templates.
 */
public class CheckTimeoutAction extends ActionSupport{

    private static final Logger logger = Logger.getLogger(CheckTimeoutAction.class);
    private LogService logService;

    public String execute()throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        String result = "";
		long loginTime = SessionUtils.getLoginTime(request);

		if (DateUtils.getNow().getTime() - loginTime > SiteContext
				.getInstance().safePolicy.getTimeout() * 1000) {
			result = "true";
		}
        PrintWriter out = response.getWriter();
		out.print(result);
		out.close();
        return null;
    }


    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }
}
