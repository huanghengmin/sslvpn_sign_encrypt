package com.hzih.sslvpn.web;

import cn.collin.commons.web.servlet.ImageVerifyCodeServlet;
import com.hzih.sslvpn.domain.Account;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

public class SessionUtils {
	public static final String ACCOUNT_ATTRIBUTE = "account";
	private static final String LOGIN_TIME = "loginTime";


	public static Account getAccount(HttpServletRequest request) {
		return (Account) request.getSession().getAttribute(ACCOUNT_ATTRIBUTE);
	}

	public static void removeAccount(HttpServletRequest request) {
		request.getSession().removeAttribute(ACCOUNT_ATTRIBUTE);
	}

	public static void setAccount(HttpServletRequest request, Account account) {
		request.getSession().setAttribute(ACCOUNT_ATTRIBUTE, account);
	}

	public static String getVcode(HttpServletRequest request) {
		return request.getSession().getAttribute(
				ImageVerifyCodeServlet.SESSION_ATT_NAME).toString();

	}

	public static Account getAccount(PageContext pageContext) {
		HttpSession session = pageContext.getSession();
		if (session != null) {
			Object obj = session.getAttribute(ACCOUNT_ATTRIBUTE);
			if (obj != null)
				return (Account) obj;
		}
		return null;
	}

	public static void invalidateSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session != null)
			session.invalidate();
	}

	public static void setLoginTime(HttpServletRequest request, long time) {
		request.getSession().setAttribute(LOGIN_TIME, time);
	}

	public static long getLoginTime(HttpServletRequest request) {
		return (Long) request.getSession().getAttribute(LOGIN_TIME);
	}

}
