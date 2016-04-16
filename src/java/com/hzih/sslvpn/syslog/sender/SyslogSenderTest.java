package com.hzih.sslvpn.syslog.sender;

import org.productivity.java.syslog4j.Syslog;
import org.productivity.java.syslog4j.SyslogConfigIF;
import org.productivity.java.syslog4j.SyslogIF;
import org.productivity.java.syslog4j.impl.net.udp.UDPNetSyslogConfig;

/**
 * Created by Administrator on 15-4-17.
 */
public class SyslogSenderTest {
    private static final String charset = "UTF-8";
    private static final String ip = "127.0.0.1";
    private static final int port = 1554;

    public static void main(String[] args) throws Exception {
        /**
         * 审计功能的开启与关闭
         * action 动作
         * result 结果 1成功 0失败
         */
        String account_log_001_type = "logflag=sslvpn,logsource=account,account=admin,action=开启审计日志,result=1,auditmodel=审计管理模块,auditlevel=info,audittype=001,datetime=2013年05月23日18时53分17秒";
        String account_log_001_type_false = "logflag=sslvpn,logsource=account,account=admin,action=关闭审计日志,result=0,auditmodel=审计管理模块,auditlevel=info,audittype=001,datetime=2013年05月23日18时53分18秒";

        /**
         * 用户鉴别失败事件
         * result="0" 鉴别失败
         */
        String client_log_002_type = "logflag=sslvpn,logsource=client,CN=zhang,SN=123131414324342434324,O=杭州创谐,OU=研发部,L=杭州,ST=浙江,phone=13345616754,email=zhang@163.com,idcard=467890987678908765,sourceip=192.168.1.1,sourceport=5555,accessurl=-,result=0,upbytes=-,downbytes=-,audittype=002,datetime=2013年05月23日18时53分18秒";


        /**
         * 授权用户一般操作
         * result 操作结果 1为成功 0为失败
         */
        String client_log_003_type_true = "logflag=sslvpn,logsource=client,CN=zhang,SN=123131414324342434324,O=杭州创谐,OU=研发部,L=杭州,ST=浙江,phone=13345616754,email=zhang@163.com,idcard=467890987678908765,sourceip=192.168.1.1,sourceport=5555,accessurl=tcp://192.168.2.1:8888,result=1,upbytes=666,downbytes=77788,audittype=003,datetime=2013年05月23日18时53分17秒";
        String client_log_003_type_false = "logflag=sslvpn,logsource=client,CN=zhang,SN=123131414324342434324,O=杭州创谐,OU=研发部,L=杭州,ST=浙江,phone=13345616754,email=zhang@163.com,idcard=467890987678908765,sourceip=192.168.1.1,sourceport=5555,accessurl=tcp://192.168.2.1:8888,result=0,upbytes=666,downbytes=77788,audittype=003,datetime=2013年05月23日18时53分18秒";

        /**
         * 系统管理员，系统安全员，审计员和一般操作员所实施的操作
         * action 操作动作
         * result 操作结果 1为成功 0为失败
         */
        String account_log_004_type = "logflag=sslvpn,logsource=account,account=admin,action=管理员操作XXX,result=1,auditmodel=审计管理模块,auditlevel=info,audittype=004,datetime=2013年05月23日18时53分17秒";
        String account_log_004_type_auditadmin = "logflag=sslvpn,logsource=account,account=auditadmin,action=审计管理员操作XXX,result=1,auditmodel=审计管理模块,auditlevel=info,audittype=004,datetime=2013年05月23日18时53分18秒";
        String account_log_004_type_safeadmin = "logflag=sslvpn,logsource=account,account=safeadmin,action=安全管理员操作XXX,result=1,auditmodel=审计管理模块,auditlevel=info,audittype=004,datetime=2013年05月23日18时53分18秒";

        /**
         * VPN隧道的建立与删除
         * result="1" 建立隧道
         * result="0" 删除隧道
         */
        String client_log_005_type_add = "logflag=sslvpn,logsource=client,CN=zhang,SN=123131414324342434324,O=杭州创谐,OU=研发部,L=杭州,ST=浙江,phone=13345616754,email=zhang@163.com,idcard=467890987678908765,sourceip=192.168.1.1,sourceport=5555,accessurl=-,result=1,upbytes=-,downbytes=-,audittype=005,datetime=2013年05月23日18时53分17秒";
        String client_log_005_type_delete = "logflag=sslvpn,logsource=client,CN=zhang,SN=123131414324342434324,O=杭州创谐,OU=研发部,L=杭州,ST=浙江,phone=13345616754,email=zhang@163.com,idcard=467890987678908765,sourceip=192.168.1.1,sourceport=5555,accessurl=-,result=0,upbytes=-,downbytes=-,audittype=005,datetime=2013年05月23日18时53分18秒";

        /**
         * 连续对同一VPN隧道的建立与删除
         * result="1" 建立隧道 number 次数
         * result="0" 删除隧道 number 次数
         */
        String client_log_006_type_add = "logflag=sslvpn,logsource=client,CN=zhang,SN=123131414324342434324,O=杭州创谐,OU=研发部,L=杭州,ST=浙江,phone=13345616754,email=zhang@163.com,idcard=467890987678908765,sourceip=192.168.1.1,sourceport=5555,accessurl=-,result=1,number=3,upbytes=-,downbytes=-,audittype=006,datetime=2013年05月23日18时53分17秒";
        String client_log_006_type_delete = "logflag=sslvpn,logsource=client,CN=zhang,SN=123131414324342434324,O=杭州创谐,OU=研发部,L=杭州,ST=浙江,phone=13345616754,email=zhang@163.com,idcard=467890987678908765,sourceip=192.168.1.1,sourceport=5555,accessurl=-,result=0,number=3,upbytes=-,downbytes=-,audittype=006,datetime=2013年05月23日18时53分18秒";

        /**
         * 用户数据完整性校验失败
         *  result="0" 为校验失败
         */
        String client_log_007_type = "logflag=sslvpn,logsource=client,CN=zhang,SN=123131414324342434324,O=杭州创谐,OU=研发部,L=杭州,ST=浙江,phone=13345616754,email=zhang@163.com,idcard=467890987678908765,sourceip=192.168.1.1,sourceport=5555,accessurl=-,result=0,upbytes=-,downbytes=-,audittype=007,datetime=2013年05月23日18时53分18秒";

        /**
         * 用户数据解密失败
         * result="0" 为解密失败
         */
        String client_log_008_type = "logflag=sslvpn,logsource=client,CN=zhang,SN=123131414324342434324,O=杭州创谐,OU=研发部,L=杭州,ST=浙江,phone=13345616754,email=zhang@163.com,idcard=467890987678908765,sourceip=192.168.1.1,sourceport=5555,accessurl=-,result=0,upbytes=-,downbytes=-,audittype=008,datetime=2013年05月23日18时53分18秒";


        /**
         * 根据策略，数据包被丢弃事件
         * result="0" 包被丢弃
         */
        String client_log_009_type = "logflag=sslvpn,logsource=client,CN=zhang,SN=123131414324342434324,O=杭州创谐,OU=研发部,L=杭州,ST=浙江,phone=13345616754,email=zhang@163.com,idcard=467890987678908765,sourceip=192.168.1.1,sourceport=5555,accessurl=-,result=0,upbytes=-,downbytes=-,audittype=009,datetime=2013年05月23日18时53分17秒";

        /**
         * 审计日志存储失败
         * result="0" 表示存储失败
         */
        String account_log_010_type = "logflag=sslvpn,logsource=account,account=auditadmin,action=存储审计日志,result=0,auditmodel=审计管理模块,auditlevel=info,audittype=010,datetime=2013年05月23日18时53分18秒";


        /**
         * 重放攻击
         * result="0" 重放
         */
        String client_log_011_type = "logflag=sslvpn,logsource=client,CN=zhang,SN=123131414324342434324,O=杭州创谐,OU=研发部,L=杭州,ST=浙江,phone=13345616754,email=zhang@163.com,idcard=467890987678908765,sourceip=192.168.1.1,sourceport=5555,accessurl=-,result=0,upbytes=-,downbytes=-,audittype=011,datetime=2013年05月23日18时53分17秒";


        int i = 0;
        SyslogConfigIF config = new UDPNetSyslogConfig();
        config.setHost(ip);
        config.setCharSet(charset);
        config.setPort(port);
            int j = i++;
            SyslogIF syLog;
            try {
                syLog = Syslog.getInstance(String.valueOf(j));
            } catch (Exception e) {e.printStackTrace();
                syLog = null;
            }
            if (syLog == null) {
                syLog = Syslog.createInstance(String.valueOf(j), config);
            }
            syLog.info(account_log_001_type);
            syLog.flush();
            syLog.shutdown();
    }
}
