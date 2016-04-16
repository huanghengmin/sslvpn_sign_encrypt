package com.hzih.sslvpn.web.servlet;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class TimerCountReportServlet extends DispatcherServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(TimerCountReportServlet.class);

	@Override
	public void init(ServletConfig config) throws ServletException {
		ServletContext servletContext = config.getServletContext();
		Thread buildReportThread = new Thread(new BuildReportThread(
				servletContext));
		buildReportThread.start();
	}

	@Override
	public ServletConfig getServletConfig() {
		// do nothing
		return null;
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException {
		// do nothing
	}

	@Override
	public String getServletInfo() {
		// do nothing
		return null;
	}

	@Override
	public void destroy() {

	}

}

class BuildReportThread implements Runnable {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(BuildReportThread.class);

	private ServletContext servletContext;

	public BuildReportThread() {

	}

	public BuildReportThread(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public void run() {
		WebApplicationContext context = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
//
//		BusinessReportService brs = (BusinessReportService) context
//				.getBean(ServiceConstant.BUSINESS_REPORT_SERVICE);
//		EquipmentReportService ers = (EquipmentReportService) context
//				.getBean(ServiceConstant.EQUIPMENT_REPORT_SERVICE);

		while (true) {
			Calendar now = Calendar.getInstance();
			now.add(Calendar.DATE, -1);
			Date date = now.getTime();

			try {
				//brs.updBuildReport(DateUtils.getNow());// only for MergeFiles
				//ers.updBuildReport(DateUtils.getNow());// only for MergeFiles

				Calendar trigger = Calendar.getInstance();
				trigger.setTimeInMillis(System.currentTimeMillis());
				if (trigger.get(Calendar.HOUR_OF_DAY) == 2) {
//					brs.updBuildReport(date);
//					ers.updBuildReport(date);
				}
				if(trigger.get(Calendar.HOUR_OF_DAY) > 2){
					logger.debug("开始report修复逻辑");
//					brs.updBuildReport2(date);
//					ers.updBuildReport2(date);
					logger.debug("结束report修复逻辑");
				}
				Thread.sleep(3600 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}