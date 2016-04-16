package com.hzih.sslvpn.utils;

import cn.collin.commons.utils.DateUtils;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.SiteContext;

import javax.servlet.http.HttpServletRequest;

public class CheckTimeResult {
	public String getResult(HttpServletRequest request){
		 String result = "";
	        long loginTime = SessionUtils.getLoginTime(request);
	        if (DateUtils.getNow().getTime() - loginTime > SiteContext
					.getInstance().safePolicy.getTimeout() * 1000) {
				result = "true";
			}
	        SessionUtils.setLoginTime(request, DateUtils.getNow().getTime());
	     return result;
	}
}
