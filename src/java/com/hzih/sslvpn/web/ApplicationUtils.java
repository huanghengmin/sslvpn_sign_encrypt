package com.hzih.sslvpn.web;

import org.apache.struts2.ServletActionContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

public class ApplicationUtils {

	public static HttpSession getAccount(String account) {
		ServletContext application = ServletActionContext.getServletContext();
		return (HttpSession)application.getAttribute(account);
	}

	public static void removeAccount(String account) {
		ServletContext application = ServletActionContext.getServletContext();
		application.removeAttribute(account);
	}

	public static void setAccount(String account, HttpSession session) {
		ServletContext application = ServletActionContext.getServletContext();
		application.setAttribute(account, session);
	}
}
