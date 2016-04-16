package com.hzih.sslvpn.web.action.user;

import com.hzih.sslvpn.domain.Account;
import com.hzih.sslvpn.domain.Role;
import com.hzih.sslvpn.domain.SafePolicy;
import com.hzih.sslvpn.service.AccountService;
import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.service.SafePolicyService;
import com.hzih.sslvpn.utils.Constant;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.action.AccountLogUtils;
import com.hzih.sslvpn.web.action.ActionBase;
import com.hzih.sslvpn.web.action.audit.AuditFlagAction;
import com.hzih.sslvpn.web.action.boot.LoaderListener;
import com.hzih.sslvpn.web.servlet.SiteContextLoaderServlet;
import com.inetec.common.security.DesEncrypterAsPassword;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-6-11
 * Time: 下午1:04
 * To change this template use File | Settings | File Templates.
 */
public class AccountAction extends ActionSupport{
    private static final Logger logger = LoggerFactory.getLogger(AccountAction.class);
    private AccountService accountService;
    private SafePolicyService safePolicyService;
    private LogService logService;
    private Account account;
    private Role role;
    private int start;
    private int limit;
    private String userName;
    private String status;

    public String select() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = null;
        try {
            json = accountService.select(userName,status,start,limit);
//            logService.newLog("INFO",  SessionUtils.getAccount(request).getUserName(), "用户管理","用户获取所有账号信息成功");
        } catch (Exception e) {
            if(AuditFlagAction.getAuditFlag()) {
                logger.error("用户管理:" + e.getMessage(),e);
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "用户管理", "用户获取所有账号信息失败");
            }
        }
        actionBase.actionEnd(response, json, result);
        userName = null;
        return null;
    }

    public String update() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String msg = null;
        try {
            if(checkPassword(account.getPassword())){
                long[] rIds = {role.getId()};
                DesEncrypterAsPassword deap = new DesEncrypterAsPassword(Constant.S_PWD_ENCRYPT_CODE);
                String password = new String(deap.encrypt(account.getPassword().getBytes()));
                account.setPassword(password);
                msg = accountService.update(account,rIds);
                if(AuditFlagAction.getAuditFlag()) {
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户管理", "用户修改账户" + account.getUserName() + "信息成功");
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), "用户修改账户" + account.getUserName() + "信息成功", "用户管理", "info", "004", "1", new Date());
                    SiteContextLoaderServlet.sysLogService.offer(log);
                }
            }  else {

                msg = "<font color=\"red\">修改失败,密码不符合规则</font>";
                if(AuditFlagAction.getAuditFlag()) {
                    logService.newLog("WARN", SessionUtils.getAccount(request).getUserName(), "用户管理","用户修改账户"+account.getUserName()+"信息失败,密码不符合规则");
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), "用户修改账户" + account.getUserName() + "信息失败,密码不符合规则", "用户管理", "info", "004", "0", new Date());
                    SiteContextLoaderServlet.sysLogService.offer(log);
                }
            }
        } catch (Exception e) {
            msg = "<font color=\"red\">修改失败</font>";
            if(AuditFlagAction.getAuditFlag()) {
                logger.error("用户管理:" + e.getMessage(),e);
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "用户管理", "用户修改账户" + account.getUserName() + "信息失败");
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), "用户修改账户" + account.getUserName() + "信息失败", "用户管理", "info", "004", "0", new Date());
                SiteContextLoaderServlet.sysLogService.offer(log);
            }
        }
        String json = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response,json,result);
        userName = null;
        return null;
    }

    public String delete() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String msg = null;
        try {
            long id = Long.parseLong(request.getParameter("id"));
            Account se_account = accountService.getAccountById(id);
            se_account.setStatus("已删除");
            Set<Role> roles = se_account.getRoles();
            if(roles.size()>0&&roles!=null) {
                long[] rIds = {roles.iterator().next().getId()};
                accountService.update(se_account,rIds);
                msg  = "删除用户账户成功";
                if(AuditFlagAction.getAuditFlag()) {
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户管理", "用户删除账户" + userName + "信息成功");
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), "用户删除账户" + userName + "信息成功", "用户管理", "info", "004", "1", new Date());
                    SiteContextLoaderServlet.sysLogService.offer(log);
                }
            }
//            msg = accountService.delete(id);
        } catch (Exception e) {
            msg = "<font color=\"red\">删除失败</font>";
            if(AuditFlagAction.getAuditFlag()) {
                logger.error("用户管理:" + e.getMessage(),e);
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "用户管理", "用户删除账户" + userName + "信息失败");
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), "用户删除账户" + userName + "信息失败", "用户管理", "info", "004", "0", new Date());
                SiteContextLoaderServlet.sysLogService.offer(log);
            }
        }
        String json = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response,json,result);
        userName = null;
        return null;
    }

    /*public String indexPermission() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String msg = null;
        int size = 0;
        try {
            Account account = SessionUtils.getAccount(request);
            Set<Permission> permissions = account.getPermissions();
            SortedMap<Long,Permission> topMap = new TreeMap<Long, Permission>();
            SortedMap<Long,Permission> secondMap = null;
            SortedMap<Long,SortedMap<Long,Permission>> secondMaps = new TreeMap<Long, SortedMap<Long,Permission>>();
            for(Permission p : permissions) {
                long parentId = p.getParentId();
                if(parentId == 0) {
                    topMap.put(p.getId(), p);
                } else {
                    if(secondMaps.get(parentId)==null) {
                        secondMap = new TreeMap<Long, Permission>();
                        secondMaps.put(parentId,secondMap);
                    }
                    secondMaps.get(parentId).put(Long.valueOf(p.getOrder()), p);
                }
            }
            msg = "[";
            for(Map.Entry top : topMap.entrySet()) {
                Permission p = (Permission) top.getValue();
                msg += "{id:'"+p.getCode()+"',title:'"+p.getName()+"',iconCls:'"+p.getIconCls()+"',sun:[";
                SortedMap<Long,Permission> _secondMap = secondMaps.get(top.getKey());
                if(_secondMap != null){
                    for(Map.Entry second : _secondMap.entrySet()) {
                        Permission _p = (Permission) second.getValue();
                        msg += "{id:'"+_p.getCode()+"',title:'"+_p.getName()+"',url:'"+_p.getUrl()+"'},";
                    }
                }
                msg += "]},";
                size ++;
            }
            msg += "]";
            logService.newLog("INFO",  SessionUtils.getAccount(request).getUserName(), "用户管理","获取用户的权限信息成功");
        } catch (Exception e) {
            logger.error("sslvpn 用户管理:"+e.getMessage());
            logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "用户管理","获取用户的权限信息失败");
            msg = "<font color=\"red\">权限获取失败</font>";
        }
        String json = "{success:true,total:"+ size +",rows:"+msg+"}";
        actionBase.actionEnd(response,json,result);
        return null;
    }*/

    public String insert() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String msg = null;
        try {
            if(checkPassword(account.getPassword())){
                long[] rIds = {role.getId()};
                account.setCreatedTime(new Date());
                account.setModifiedPasswordTime(new Date());
                DesEncrypterAsPassword deap = new DesEncrypterAsPassword(Constant.S_PWD_ENCRYPT_CODE);
                String password = new String(deap.encrypt(account.getPassword().getBytes()));
                account.setPassword(password);
                msg = accountService.insert(account,rIds);
                if(AuditFlagAction.getAuditFlag()) {
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户管理", "用户新增账户" + account.getUserName() + "信息成功");
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), "用户新增账户" + account.getUserName() + "信息成功", "用户管理", "info", "004", "1", new Date());
                    SiteContextLoaderServlet.sysLogService.offer(log);
                }
            }  else {
                msg = "<font color=\"red\">新增失败,密码不符合规则</font>";
                if(AuditFlagAction.getAuditFlag()) {
                    logService.newLog("WARN", SessionUtils.getAccount(request).getUserName(), "用户管理", "用户新增账户" + account.getUserName() + "信息密码不符合规则");
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), "用户新增账户" + account.getUserName() + "信息失败,密码不符合规则", "用户管理", "info", "004", "0", new Date());
                    SiteContextLoaderServlet.sysLogService.offer(log);
                }
            }
        } catch (Exception e) {
            msg = "<font color=\"red\">新增失败</font>";
            if(AuditFlagAction.getAuditFlag()) {
                logger.error("用户管理:" + e.getMessage(),e);
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "用户管理", "用户新增账户" + account.getUserName() + "信息失败");
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), "用户新增账户" + account.getUserName() + "信息失败", "用户管理", "info", "004", "0", new Date());
                SiteContextLoaderServlet.sysLogService.offer(log);
            }
        }
        String json = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response,json,result);
        userName = null;
        return null;
    }

    /**
     * 符合的返回true,否则返回false
     * @param password
     * @return
     */
    private boolean checkPassword(String password) {
        SafePolicy safePolicy = safePolicyService.getData();
        if(password.matches(safePolicy.getPasswordRules())){
            return true;
        }
        return false;
    }

    public String checkUserName() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = "{success:false,msg:'校验失败'}";
        try {
            Account a = SessionUtils.getAccount(request);
            String userName = request.getParameter("userName");

            json = accountService.checkUserName(userName);
//            logService.newLog("INFO",  SessionUtils.getAccount(request).getUserName(), "用户管理","用户检查用户名是否存在成功");
        } catch (Exception e) {
            if(AuditFlagAction.getAuditFlag()) {
                logger.error("用户管理:" + e.getMessage(),e);
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "用户管理", "用户检查用户名是否存在失败");
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), "用户检查用户名是否存在失败", "用户管理", "info", "004", "0", new Date());
                SiteContextLoaderServlet.sysLogService.offer(log);
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String checkPassword() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String msg = null;
        try {
            String password = request.getParameter("password");
            if(checkPassword(password)){
                msg = "true";
            } else {
                msg = "不符合安全策略中的密码校验限制";
            }
//            logService.newLog("INFO",  SessionUtils.getAccount(request).getUserName(), "用户管理","用户校验密码是否符合规则成功");
        } catch (Exception e) {
            if(AuditFlagAction.getAuditFlag()) {
                logger.error("用户管理:" + e.getMessage(),e);
                msg = "校验失败";
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "用户管理", "用户校验密码是否符合规则失败");
            }
        }
        String json = "{success:false,msg:'"+msg+"'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String updatePwd() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String msg = null;
        try {
            Account a = SessionUtils.getAccount(request);
            String pwd = request.getParameter("newpwd");
            if(checkPassword(pwd)){
                long[] rIds = {};
                DesEncrypterAsPassword deap = new DesEncrypterAsPassword(Constant.S_PWD_ENCRYPT_CODE);
                String password = new String(deap.encrypt(pwd.getBytes()));
                a.setPassword(password);
                msg = accountService.update(a,rIds);
                if(AuditFlagAction.getAuditFlag()) {
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "修改密码", "用户修改密码成功");
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), "用户修改密码成功", "用户管理", "info", "004", "1", new Date());
                    SiteContextLoaderServlet.sysLogService.offer(log);
                }
            }  else {
                msg = "<font color=\"red\">修改失败,密码不符合规则</font>";
                if(AuditFlagAction.getAuditFlag()) {
                    logService.newLog("WARN", SessionUtils.getAccount(request).getUserName(), "用户管理", "用户用户修改密码不符合规则");
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), "用户用户修改密码不符合规则", "用户管理", "info", "004", "0", new Date());
                    SiteContextLoaderServlet.sysLogService.offer(log);
                }
            }
        } catch (Exception e) {
            msg = "<font color=\"red\">修改密码失败</font>";
            if(AuditFlagAction.getAuditFlag()) {
                logger.error("用户管理:" + e.getMessage(),e);
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "修改密码", "用户修改密码失败");
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), "用户修改密码失败", "用户管理", "info", "004", "0", new Date());
                SiteContextLoaderServlet.sysLogService.offer(log);
            }
        }
        String json = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response,json,result);
        return null;
    }

    public String selectUserNameKeyValue() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = null;
        try {
            json = accountService.selectUserNameKeyValue();
//            logService.newLog("INFO",  SessionUtils.getAccount(request).getUserName(), "用户日志审计","用户获取所有账号名列表成功");
        } catch (Exception e) {
            if(AuditFlagAction.getAuditFlag()) {
                logger.error("用户管理:" + e.getMessage(),e);
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "用户日志审计", "用户获取所有账号名列表失败");
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public SafePolicyService getSafePolicyService() {
        return safePolicyService;
    }

    public void setSafePolicyService(SafePolicyService safePolicyService) {
        this.safePolicyService = safePolicyService;
    }

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
