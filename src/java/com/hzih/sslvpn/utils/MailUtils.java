package com.hzih.sslvpn.utils;

import org.apache.commons.mail.*;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;

/**
 * Created by Administrator on 15-4-24.
 */
public class MailUtils {
    public static final Logger logger = LoggerFactory.getLogger(MailUtils.class);

    public static boolean sendSimpleEmail(String emailServer,int port, String connectName, String password, boolean isNeedAuth,
                                          String fromEmail, String toEmail, String cc, String bcc, String charset, String title, String text) {
        SimpleEmail email = new SimpleEmail();
        email.setHostName(emailServer);
        email.setSmtpPort(port);
        if (isNeedAuth)
            email.setAuthenticator(new DefaultAuthenticator(connectName, password));
        try {
            email.setFrom(fromEmail); //发送方,这里可以写多个
            email.addTo(toEmail); // 接收方
            if (cc != null && cc.length() > 0)
                email.addCc(cc); // 抄送方
            if (bcc != null && bcc.length() > 0)
                email.addBcc(bcc); // 秘密抄送方
            email.setCharset(charset);
            email.setSubject(title); // 标题
            email.setMsg(text);// 内容
            email.send();
            return true;
        } catch (EmailException e) {
            logger.info(e.getMessage(),e);
            return false;
        }
    }

    public static boolean sendEmailAttachmentFileAndPath(String emailServer, int port,String connectName, String password, boolean isNeedAuth,
                                          String fromEmail, String toEmail, String cc, String bcc, String charset,
                                          String title, String text,List<String> fileAffix,List<URL> fileUrls) {
        EmailAttachment attachment = null;
        if((fileAffix!=null&&fileAffix.size()>0)||(fileUrls!=null&&fileUrls.size()>0)){
            attachment = new EmailAttachment();
            if(fileAffix!=null&&fileAffix.size()>0) {
                for (String s : fileAffix) {
                    attachment.setPath(s);
                }
            }
            if(fileUrls!=null&&fileUrls.size()>0){
                for (URL s:fileUrls){
                    attachment.setURL(s);
                }
            }
        }
        MultiPartEmail email = new MultiPartEmail();
        email.setHostName(emailServer);
        email.setSmtpPort(port);
        if (isNeedAuth)
            email.setAuthenticator(new DefaultAuthenticator(connectName, password));
        try {
            email.setFrom(fromEmail); //发送方,这里可以写多个
            email.addTo(toEmail); // 接收方
            if (cc != null && cc.length() > 0)
                email.addCc(cc); // 抄送方
            if (bcc != null && bcc.length() > 0)
                email.addBcc(bcc); // 秘密抄送方
            email.setCharset(charset);
            email.setSubject(title); // 标题
            email.setMsg(text);// 内容
            if(attachment!=null) {
                attachment.setDisposition(EmailAttachment.ATTACHMENT);
                email.attach(attachment);
            }
            email.send();
            return true;
        } catch (EmailException e) {
            logger.error(e.getMessage(),e);
            return false;
        }
    }


    public static boolean sendHtmlEmail(String emailServer,int port, String connectName, String password, boolean isNeedAuth,
                                                         String fromEmail, String toEmail, String cc, String bcc, String charset,
                                                         String title, String text,String htmlText) {

        HtmlEmail email = new HtmlEmail();
        email.setHostName(emailServer);
        email.setSmtpPort(port);
        if (isNeedAuth)
            email.setAuthenticator(new DefaultAuthenticator(connectName, password));
        try {
            email.setFrom(fromEmail); //发送方,这里可以写多个
            email.addTo(toEmail); // 接收方
            if (cc != null && cc.length() > 0)
                email.addCc(cc); // 抄送方
            if (bcc != null && bcc.length() > 0)
                email.addBcc(bcc); // 秘密抄送方
            email.setCharset(charset);
            email.setSubject(title); // 标题
            email.setHtmlMsg(htmlText);
            email.setTextMsg(text);// 内容
            email.send();
            return true;
        } catch (EmailException e) {
            logger.info(e.getMessage(),e);
            return false;
        }
    }

    public static boolean sendSimpleEmailList(String emailServer,int port, String connectName, String password, boolean isNeedAuth,
                                              String fromEmail, List<Node> toEmails, String cc, String bcc, String charset, String title, String text) {
        SimpleEmail email = new SimpleEmail();
        email.setHostName(emailServer);
        email.setSmtpPort(port);
        if (isNeedAuth)
            email.setAuthenticator(new DefaultAuthenticator(connectName, password));
        try {
            email.setFrom(fromEmail); //发送方,这里可以写多个
            if(toEmails!=null&&toEmails.size()>0) {
                for (Node s : toEmails) {
                    email.addTo(s.getText()); // 接收方
                }
            }
            if (cc != null && cc.length() > 0)
                email.addCc(cc); // 抄送方
            if (bcc != null && bcc.length() > 0)
                email.addBcc(bcc); // 秘密抄送方
            email.setCharset(charset);
            email.setSubject(title); // 标题
            email.setMsg(text);// 内容
            email.send();
            return true;
        } catch (EmailException e) {
            logger.info(e.getMessage(),e);
            return false;
        }
    }
}
