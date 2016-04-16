package com.hzih.sslvpn.web.action;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 15-4-29.
 */
public class AccountLogUtils {
    private static final String Log_Flag = "logflag=sslvpn";
    private static final String Log_Source = "logsource=account";
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");

    public static String getResult(String account, String action, String auditmodel, String auditlevel, String audittype, String result, Date date) {
        String datetime = format.format(date);
        StringBuilder sb = new StringBuilder();
        sb.append(Log_Flag).append(" ");
        sb.append(Log_Source).append(" ");
        sb.append("account").append("=").append(account).append(" ");
        sb.append("action").append("=").append(action).append(" ");
        sb.append("auditmodel").append("=").append(auditmodel).append(" ");
        sb.append("auditlevel").append("=").append(auditlevel).append(" ");
        sb.append("audittype").append("=").append(audittype).append(" ");
        sb.append("result").append("=").append(result).append(" ");
        sb.append("datetime").append("=").append(datetime);
        return sb.toString();
    }
}
