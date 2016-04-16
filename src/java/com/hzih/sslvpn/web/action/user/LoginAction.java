package com.hzih.sslvpn.web.action.user;

import cn.collin.commons.utils.DateUtils;
import com.hzih.sslvpn.domain.Account;
import com.hzih.sslvpn.domain.UserOperLog;
import com.hzih.sslvpn.service.AuditService;
import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.service.LoginService;
import com.hzih.sslvpn.utils.Constant;
import com.hzih.sslvpn.web.ApplicationUtils;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.SiteContext;
import com.hzih.sslvpn.web.action.AccountLogUtils;
import com.hzih.sslvpn.syslog.sender.SyslogSender;
import com.hzih.sslvpn.web.action.audit.AuditFlagAction;
import com.inetec.common.net.NicMac;
import com.inetec.common.security.DesEncrypterAsPassword;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * 用户登录
 * 
 * @author collin.code@gmail.com
 * @struts.action type="org.springframework.web.struts.DelegatingActionProxy"
 *                scope="request" path="/login" validate="false"
 * @struts.action-forward name="login" path="/loginError.jsp"
 * @struts.action-forward name="index" path="/" redirect="true"
 */
public class LoginAction extends ActionSupport {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(LoginAction.class);
	private LoginService loginService;
	private LogService logService;
    private AuditService auditService;
//    private SecurityService securityService;

	public String execute() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
		String name = request.getParameter("name");
		String pwd = request.getParameter("pwd");
		String vcode = request.getParameter("vcode");

		if (SessionUtils.getAccount(request) != null) {
			SessionUtils.removeAccount(request);
		}

		final String ip = request.getRemoteAddr();

        int errorLimit = SiteContext.getInstance().safePolicy.getErrorLimit();

		int lockTime = SiteContext.getInstance().safePolicy.getLockTime();

        Object obj = SiteContext.getInstance().loginErrorList.get(ip);
        if (obj != null && ((Integer) obj).intValue() >= errorLimit) {
            if(AuditFlagAction.getAuditFlag()) {
                String log = AccountLogUtils.getResult(name, "用户登录", "用户登录,登录超过3次口令错误", "ERROR", "012", "0", new Date());
                SyslogSender.sysLog(log);
                logger.info("超过" + errorLimit + "次口令错误，请联系系统管理员！ ");
//                securityService.addSecurityAlert("登录SSLVPN服务器",new Date(),"0010","在"+ getIpAddr(request)+ "登录超过3次口令错误","N",getIpAddr(request));
                logService.newLog("WARN", name, "用户登录", "在" + getIpAddr(request) + "登录超过3次口令错误");
            }
           request.setAttribute("message", "超过" + errorLimit	+ "次口令错误,系统锁定,请联系系统管理员!");
            new Timer().schedule(new TimerTask(){
                public void run() {
                    SiteContext.getInstance().loginErrorList.remove(ip);
                }
            }, lockTime*60*60*1000);     //ip 停用 lockTime 小时
            return "failure";
        }

        if (null == name || null == pwd || null == vcode) {
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("用户名、密码或验证码为空！");
            }
            request.setAttribute("message", "用户名、密码或验证码为空！");
            return "failure";
        }

        if (!vcode.equalsIgnoreCase(SessionUtils.getVcode(request))) {
            if(AuditFlagAction.getAuditFlag()) {
                logger.info(" 验证码出错！");
            }
            request.setAttribute("message", " 验证码出错！");
            return "failure";
        }

            DesEncrypterAsPassword deap = new DesEncrypterAsPassword(Constant.S_PWD_ENCRYPT_CODE);
            String password = new String(deap.encrypt(pwd.getBytes()));
            Account account = loginService.getAccountByNameAndPwd(name, password);
            if (account != null) {
                ServletContext application = request.getServletContext();
                ArrayList<HttpSession> sessions = (ArrayList<HttpSession>) application.getAttribute("sessions");
                if(ApplicationUtils.getAccount(name)!=null){
                    /**
                     * 抢占式登陆
                     */
                    HttpSession session = ApplicationUtils.getAccount(name);
                    session.removeAttribute(SessionUtils.ACCOUNT_ATTRIBUTE);
                    session.invalidate();
                    ApplicationUtils.removeAccount(name);

                    if (AuditFlagAction.getAuditFlag()) {
                        logger.info(" 用户名和密码验证通过！");
                        logger.debug(SiteContext.getInstance().safePolicy.getRemoteDisabled() + "|" + account.getRemoteIp() + "|" + ip);
                    }
                    if (SiteContext.getInstance().safePolicy.getMacDisabled()) {
                        NicMac nicMac = new NicMac(ip);
                        if (account.getMac() != null) {
                            String mac = nicMac.GetRemoteMacAddr();
                            if (!mac.equals(account.getMac())) {
                                request.setAttribute("message", "MAC地址错误!");
                                return "failure";
                            }
                        } else {
                            request.setAttribute("message", "MAC地址错误!");
                            return "failure";
                        }
                    }
                    /**
                     *  isTime == true 工作时间(允许访问的时间)
                     *  isIp == true 允许访问的ip
                     */
                    boolean isTime = checkTime(account);
                    boolean isIp = checkIp(account, request);
                    if (isTime) {
                        if (isIp) {
                            if (account.getStatus().equals("有效")) {
                                UserOperLog userOperLog = auditService.selectModelLast(account.getUserName(), "用户登录");
                                if(userOperLog!=null){
                                    account.setLastMsg("上次登录 时间:"+userOperLog.getLogTime()+",信息:"+userOperLog.getAuditInfo());
                                }else {
                                    account.setLastMsg("首次登录 ");
                                }

                                if (AuditFlagAction.getAuditFlag()) {
                                    logger.info("用户名和密码验证通过!");
                                    logService.newLog("INFO", account.getUserName(), "用户登录", "用户登录成功");
                                    String log = AccountLogUtils.getResult(account.getUserName(), "用户登录成功", "用户登录", "info", "004", "1", new Date());
                                    SyslogSender.sysLog(log);
                                    log = AccountLogUtils.getResult(name, "用户登录", "用户鉴别成功" + account.getUserName(), "info", "012", "1", new Date());
                                    SyslogSender.sysLog(log);
                                }
                                SessionUtils.setAccount(request, account);
                                SessionUtils.setLoginTime(request, DateUtils.getNow().getTime());
                                ApplicationUtils.setAccount(account.getUserName(),request.getSession());

                                // 登录错误数置零
                                Object _obj = SiteContext.getInstance().loginErrorList.get(ip);
                                if (_obj != null) {
                                    SiteContext.getInstance().loginErrorList.put(ip, new Integer(0));
                                }
                                return "success";
                            } else if (account.getStatus().equals("无效")) {
                                logger.info("无效用户登录！" + account.getName());
                                logService.newLog("INFO", name, "用户登录", "无效用户登录" + account.getName());
                                String log = AccountLogUtils.getResult(name, "用户鉴别", "无效用户登录", "ERROR", "012", "0", new Date());
                                SyslogSender.sysLog(log);
                                request.setAttribute("message", "无效用户登录！" + account.getName());
                                return "failure";
                            } else if (account.getStatus().equals("已删除")) {
                                logger.info("已删除用户登录！" + account.getName());
                                logService.newLog("INFO", name, "用户登录", "已删除用户登录" + account.getName());
                                String log = AccountLogUtils.getResult(name, "用户鉴别", "已删除用户登录", "ERROR", "012", "0", new Date());
                                SyslogSender.sysLog(log);
                                request.setAttribute("message", "已删除用户登录！" + account.getName());
                                return "failure";
                            }
                        } else {
                            if (AuditFlagAction.getAuditFlag()) {
                                logger.info("非法登录IP!");
                                logService.newLog("INFO", name, "用户登录", " IP地址不允许访问");
                                String log = AccountLogUtils.getResult(name, "用户鉴别", "非法登录IP", "ERROR", "012", "0", new Date());
                                SyslogSender.sysLog(log);
                            }
                            request.setAttribute("message", "非法登录IP!");
                        }
                    } else {
                        request.setAttribute("message", "非法登录时间!");
                        if (AuditFlagAction.getAuditFlag()) {
                            logger.info("非法登录时间!");
                            logService.newLog("INFO", name, "用户登录", " 该时间不允许访问");
                            String log = AccountLogUtils.getResult(name, "用户鉴别", "该时间不允许访问", "ERROR", "012", "0", new Date());
                            SyslogSender.sysLog(log);
                        }
                    }
                } else {
                    if (AuditFlagAction.getAuditFlag()) {
                        logger.info(" 用户名和密码验证通过！");
                        logger.debug(SiteContext.getInstance().safePolicy.getRemoteDisabled() + "|" + account.getRemoteIp() + "|" + ip);
                    }
                    if (SiteContext.getInstance().safePolicy.getMacDisabled()) {
                        NicMac nicMac = new NicMac(ip);
                        if (account.getMac() != null) {
                            String mac = nicMac.GetRemoteMacAddr();
                            if (!mac.equals(account.getMac())) {
                                request.setAttribute("message", "MAC地址错误!");
                                return "failure";
                            }
                        } else {
                            request.setAttribute("message", "MAC地址错误!");
                            return "failure";
                        }
                    }
                    /**
                     *  isTime == true 工作时间(允许访问的时间)
                     *  isIp == true 允许访问的ip
                     */
                    boolean isTime = checkTime(account);
                    boolean isIp = checkIp(account, request);
                    if (isTime) {
                        if (isIp) {
                            if (account.getStatus().equals("有效")) {
                                UserOperLog userOperLog = auditService.selectModelLast(account.getUserName(), "用户登录");
                                if(userOperLog!=null){
                                    account.setLastMsg("上次登录 时间:"+userOperLog.getLogTime()+",信息:"+userOperLog.getAuditInfo());
                                }else {
                                    account.setLastMsg("首次登录 ");
                                }

                                if (AuditFlagAction.getAuditFlag()) {
                                    logger.info("用户名和密码验证通过!");
                                    logService.newLog("INFO", account.getUserName(), "用户登录", "用户登录成功");
                                    String log = AccountLogUtils.getResult(account.getUserName(), "用户登录成功", "用户登录", "info", "004", "1", new Date());
                                    SyslogSender.sysLog(log);
                                    log = AccountLogUtils.getResult(name, "用户登录", "用户鉴别" + account.getUserName(), "info", "012", "1", new Date());
                                    SyslogSender.sysLog(log);
                                }
                                SessionUtils.setAccount(request, account);
                                SessionUtils.setLoginTime(request, DateUtils.getNow().getTime());
                                ApplicationUtils.setAccount(account.getUserName(),request.getSession());

                                // 登录错误数置零
                                Object _obj = SiteContext.getInstance().loginErrorList.get(ip);
                                if (_obj != null) {
                                    SiteContext.getInstance().loginErrorList.put(ip, new Integer(0));
                                }
                                return "success";
                            } else if (account.getStatus().equals("无效")) {
                                logger.info("无效用户登录！" + account.getName());
                                logService.newLog("INFO", name, "用户登录", "无效用户登录" + account.getName());
                                String log = AccountLogUtils.getResult(name, "用户鉴别", "无效用户登录", "ERROR", "012", "0", new Date());
                                SyslogSender.sysLog(log);
                                request.setAttribute("message", "无效用户登录！" + account.getName());
                                return "failure";
                            } else if (account.getStatus().equals("已删除")) {
                                logger.info("已删除用户登录！" + account.getName());
                                logService.newLog("INFO", name, "用户登录", "已删除用户登录" + account.getName());
                                String log = AccountLogUtils.getResult(name, "用户鉴别", "已删除用户登录", "ERROR", "012", "0", new Date());
                                SyslogSender.sysLog(log);
                                request.setAttribute("message", "已删除用户登录！" + account.getName());
                                return "failure";
                            }
                        } else {
                            if (AuditFlagAction.getAuditFlag()) {
                                logger.info("非法登录IP!");
                                logService.newLog("INFO", name, "用户登录", "IP地址不允许访问");
                                String log = AccountLogUtils.getResult(name, "用户鉴别", "非法登录IP", "ERROR", "012", "0", new Date());
                                SyslogSender.sysLog(log);
                            }
                            request.setAttribute("message", "非法登录IP!");
                        }
                    } else {
                        request.setAttribute("message", "非法登录时间!");
                        if (AuditFlagAction.getAuditFlag()) {
                            logger.info("非法登录时间!");
                            logService.newLog("INFO", name, "用户登录", "该时间不允许访问");
                            String log = AccountLogUtils.getResult(name, "用户鉴别", "该时间不允许访问", "ERROR", "012", "0", new Date());
                            SyslogSender.sysLog(log);
                        }
                    }
                }
            } else {
                Account _account = loginService.getAccountByName(name);
                if (_account != null) {
                    if (AuditFlagAction.getAuditFlag()) {
                        logger.info("密码错误！");
                        int i = 1;
                        if (obj != null) {
                            i =  ((Integer) obj).intValue()+1;
                        }
                        if (AuditFlagAction.getAuditFlag()) {
                            logService.newLog("INFO", name, "用户登录", "密码错误次数:" + i);
                            String log = AccountLogUtils.getResult(name, "用户鉴别", "密码错误", "ERROR", "012", "0", new Date());
                            SyslogSender.sysLog(log);
                        }
                    }
                } else {
                    if (AuditFlagAction.getAuditFlag()) {
                        logger.info(" 用户名不存在！ ");
                        logService.newLog("INFO", name, "用户登录", "用户名不存在");
                        String log = AccountLogUtils.getResult(name, "用户鉴别", "用户名不存在", "ERROR", "012", "0", new Date());
                        SyslogSender.sysLog(log);
                    }
                }
                request.setAttribute("message", "用户名、密码错误！");
                Object _obj = SiteContext.getInstance().loginErrorList.get(ip);
                if (_obj == null) {
                    SiteContext.getInstance().loginErrorList.put(ip, new Integer(1));
                } else {
                    SiteContext.getInstance().loginErrorList.put(ip, new Integer(((Integer) _obj).intValue() + 1));
                }
            }
            return "failure";
	}

	public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private boolean checkTime(Account account) {
		int startHour = account.getStartHour();
		int endHour = account.getEndHour();
        Calendar ca = Calendar.getInstance();
        int hour = ca.get(Calendar.HOUR_OF_DAY);
		if(hour >= startHour && hour <= endHour){
			return true;
		}
		return false;
	}

	private boolean checkIp(Account account,HttpServletRequest request) {
		String ip = this.getIpAddr(request);
        if(account.getIpType()==1){
            String startIp = account.getStartIp();
            String endIp = account.getEndIp();
            try {
                long ipLong = getIP(InetAddress.getByName(ip));
                long startIpLong = getIP(InetAddress.getByName(startIp));
                long endIpLong = getIP(InetAddress.getByName(endIp));
                if(ipLong >= startIpLong && ipLong <= endIpLong){
                    return true;
                }
            } catch (UnknownHostException e) {
                logger.error("用户登录",e);
            }
        } else {
            String remoteIp = account.getRemoteIp();
            try {
                long ipLong = getIP(InetAddress.getByName(ip));
                long remoteIpLong = getIP(InetAddress.getByName(remoteIp));
                if(ipLong==remoteIpLong) {
                    return true;
                }
            } catch (UnknownHostException e) {
                logger.error("用户登录",e);
            }
        }
		return false;
	}

    /**
	 * ip转成long类型
	 * @param ip
	 * @return
	 */
	public long getIP(InetAddress ip) {
        byte[] b = ip.getAddress();
        long l = b[0] << 24L & 0xff000000L | b[1] << 16L & 0xff0000L
                | b[2] << 8L & 0xff00 | b[3] << 0L & 0xff;
        return l;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    public LoginService getLoginService() {
        return loginService;
    }

    public LogService getLogService() {
        return logService;
    }

//    public void setSecurityService(SecurityService securityService) {
//        this.securityService = securityService;
//    }


    public AuditService getAuditService() {
        return auditService;
    }

    public void setAuditService(AuditService auditService) {
        this.auditService = auditService;
    }
}
