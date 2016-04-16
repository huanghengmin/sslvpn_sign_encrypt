package com.hzih.sslvpn.web.action.sslvpn.online;

import com.hzih.sslvpn.domain.User;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.web.action.ActionBase;
import com.inetec.common.util.OSInfo;
import com.inetec.common.util.Proc;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

public class OnlineUserAction extends ActionSupport {
    private Logger logger = Logger.getLogger(OnlineUserAction.class);
    public int start;
    public int limit;

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

    public List<String> getShellFileLine() throws Exception {
        Proc proc = new Proc();
        String command = null;
        if (OSInfo.getOSInfo().isWin()) {
            command = StringContext.systemPath + "/script/status_online.bat ";
        } else {
            command = StringContext.systemPath + "/script/status_online.sh ";
        }
        proc.exec(command);
        List<String> lines = new ArrayList<>();
        Scanner scanner = new Scanner(proc.getOutput());
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            lines.add(line);
        }
        return lines;
    }

    public String onlineUser() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        List<User> online_user = new ArrayList<>();
        List<String> lines = getShellFileLine();
        for (String s : lines) {
            String[] cols = s.split(",");
            if (cols.length == 5 && !s.startsWith("Common Name")) {
                User user = new User();
                user.setCn(cols[0]);
                user.setReal_address(cols[1].split(":")[0]);
                user.setByte_received(Long.parseLong(cols[2]));
                user.setByte_send(Long.parseLong(cols[3]));
                user.setConnected_since(getParseDate(cols[4]));
                online_user.add(user);
            }
            if (cols.length == 4 && !s.startsWith("Virtual Address")) {
                for (User u : online_user) {
                    if (u.getCn().equals(cols[1])) {
                        if (!cols[0].contains("/")) {
                            u.setVirtual_address(cols[0]);
                            u.setLast_ref(getParseDate(cols[3]));
                        }
                    }
                }
            }
        }
        StringBuilder sb = new StringBuilder("{'success':true,'totalCount':" + online_user.size() + ",'root':[");
        StringBuilder json = new StringBuilder("");
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒 E ");
        int end = 0;
        if (null != online_user) {
            end = start + limit;
            end = end > online_user.size() ? online_user.size() : end;
            for (int i = start; i < end; i++) {
                User user = online_user.get(i);
                if (user != null) {
                    sb.append("{");
                    String username = user.getCn();
                    if (null != username) {
                        sb.append("cn:'" + username + "',");
                    }
                    sb.append("real_address:'" + user.getReal_address() + "',");
                    //保留3位小数
                    double c = new Double(Math.round(user.getByte_received() / 1048576) / 1000.0);
                    double d = new Double(Math.round(user.getByte_send() / 1048576) / 1000.0);
                    sb.append("byte_received:'" + c + " MB',");
                    sb.append("byte_send:'" + d + " MB',");
                    if (null != user.getVirtual_address()) {
                        sb.append("connected_since:'" + format.format(user.getConnected_since()) + "',");
                        sb.append("virtual_address:'" + user.getVirtual_address() + "',");
                        sb.append("last_ref:'" + format.format(user.getLast_ref()) + "'");
                    } else {
                        sb.append("connected_since:'" + format.format(user.getConnected_since()) + "',");
                        sb.append("virtual_address:'',");
                        sb.append("last_ref:''");
                    }
                    sb.append("},");
                }
            }
        }
        if (sb.toString().endsWith(",")) {
            json.append(sb.substring(0, sb.length() - 1));
        }
        json.append("]}");
        actionBase.actionEnd(response, json.toString(), result);
        return null;
    }

    public Date getParseDate(String date) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.US);
        return format.parse(date);
    }
}