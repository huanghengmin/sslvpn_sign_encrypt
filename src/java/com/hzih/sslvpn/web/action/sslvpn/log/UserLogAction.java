package com.hzih.sslvpn.web.action.sslvpn.log;

import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.LogDao;
import com.hzih.sslvpn.domain.Log;
import com.hzih.sslvpn.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-23
 * Time: 下午12:31
 * To change this template use File | Settings | File Templates.
 */
public class UserLogAction extends ActionSupport {

    private LogDao logDao;

    private int start;

    private int limit;

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

    public LogDao getLogDao() {
        return logDao;
    }

    public void setLogDao(LogDao logDao) {
        this.logDao = logDao;
    }

    public String findLogs()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String common_name = request.getParameter("common_name");
        String trust_ip = request.getParameter("trust_ip");
        int pageIndex = start/limit+1;
        PageResult pageResult = logDao.listByPage(pageIndex,common_name,trust_ip,limit);
        if (pageResult != null) {
            List<Log> list = pageResult.getResults();
            int count = pageResult.getAllResultsAmount();
            if (list != null) {
                String json = "{success:true,total:" + count + ",rows:[";
                Iterator<Log> raUserIterator = list.iterator();
                while (raUserIterator.hasNext()) {
                    Log log = raUserIterator.next();
                    if (raUserIterator.hasNext()) {
                        json += "{" +
                                "id:'" + log.getId() +
                                "',cn:'" + log.getCn() +
                                "',serial_number:'" + log.getSerial_number() +
                                "',subject_dn:'" + log.getSubject_dn() +
                                "',start_time:'" + log.getStart_time() +
                                "',end_time:'" + log.getEnd_time() +
                                "',trusted_ip:'" + log.getTrusted_ip() +
                                "',trusted_port:'" + log.getTrusted_port() +
                                "',protocol:'" + log.getProtocol() +
                                "',remote_ip:'" + log.getRemote_ip() +
                                "',remote_netmask:'" + log.getRemote_netmask() +
                                "',bytes_received:'" + log.getBytes_received()/1048576 +
                                " MB',bytes_sent:'" + log.getBytes_sent()/1048576 +
                                " MB',status:'" + log.getStatus() + "'" +
                                "},";
                    } else {
                        json += "{" +
                                "id:'" + log.getId() +
                                "',cn:'" + log.getCn() +
                                "',serial_number:'" + log.getSerial_number() +
                                "',subject_dn:'" + log.getSubject_dn() +
                                "',start_time:'" + log.getStart_time() +
                                "',end_time:'" + log.getEnd_time() +
                                "',trusted_ip:'" + log.getTrusted_ip() +
                                "',trusted_port:'" + log.getTrusted_port() +
                                "',protocol:'" + log.getProtocol() +
                                "',remote_ip:'" + log.getRemote_ip() +
                                "',remote_netmask:'" + log.getRemote_netmask() +
                                "',bytes_received:'" + log.getBytes_received()/1048576 +
                                " MB',bytes_sent:'" + log.getBytes_sent()/1048576 +
                                " MB',status:'" + log.getStatus() + "'" +
                                "}";
                    }
                }
                json += "]}";
                actionBase.actionEnd(response, json, result);
            }
        }
        return null;
    }
}
