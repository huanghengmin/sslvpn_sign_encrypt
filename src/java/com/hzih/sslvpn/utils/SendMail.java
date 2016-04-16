package com.hzih.sslvpn.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.List;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 13-5-22
 * Time: 下午8:15
 * To change this template use File | Settings | File Templates.
 */
public class SendMail {
    public static final Logger logger = LoggerFactory.getLogger(SendMail.class);
    public MimeMessage mimeMsg;  //要发送的email信息
    private Session session;
    private Properties props;
    private boolean needAuth=false;

    private String username="";
    private String password="";

    private Multipart mp;  //存放邮件的title 内容和附件

    public SendMail(String stmp){
        setSmtpHost(stmp);
        createMimeMessage();
    }
    /**
     *
     * @param hostName
     */
    public void setSmtpHost(String hostName){
//        System.out.println("mail.stmp.host= "+hostName);
        if(props==null){
            props=System.getProperties();
        }
        props.put("mail.smtp.host",hostName);
    }

    public boolean createMimeMessage(){
        try{
//            System.out.println("Session begin-----------");
            session=Session.getInstance(props,null);
        }catch(Exception e){
            logger.info(e.getMessage(),e);
//            System.out.println("Session.getInstance faild!"+e);
//            LogLayout.error(logger,"itp","Session.getInstance faild!",e);
            return false;
        }
//        System.out.println("MimeMEssage begin---------!");
        try{
            mimeMsg=new MimeMessage(session);
            mp=new MimeMultipart();
            return true;
        }catch(Exception e){
//            System.out.println("MimeMessage fiald! "+e.toString());
//            LogLayout.error(logger,"itp","MimeMessage fiald! ",e);
            return false;
        }
    }
    /**
     *
     * @param need
     */
    public void setNeedAuth(boolean need){
//        System.out.println(":mail.smtp.auth="+need);
        if(props==null){
            props = System.getProperties();
        }
        if(need){
            props.put("mail.smtp.auth","true");
        }
        else{
            props.put("mail.smtp.auth","false");
        }
    }
    /**
     *
     * @param name
     * @param pass
     */
    public void setNamePass(String name,String pass){
        username=name;
        password=pass;
    }
    /**
     *
     * @param mailSubject
     * @return boolean
     */
    public boolean setSubject(String mailSubject){
        System.out.println("set title begin.");
        try{
            if(!mailSubject.equals("")&&mailSubject!=null){
                mimeMsg.setSubject(mailSubject);
            }
            return true;
        }catch(Exception e){
            logger.info(e.getMessage(),e);
//            System.out.println("set Title faild!");
//            LogLayout.error(logger,"itp","set Title faild!",e);
            return false;
        }
    }
    /**
     *  添加附件..
     * @param filename
     * @return
     */
    public boolean addFileAffix(String filename){
//        System.out.println("增加附件..");
        if(filename.equals("")||filename==null){
            return false;
        }
        String file[];
        file=filename.split(";");
//        System.out.println("你有 "+file.length+" 个附件!");
        try{
            for(int i=0;i<file.length;i++){
                BodyPart bp=new MimeBodyPart();
                FileDataSource fileds=new FileDataSource(file[i]);
                bp.setDataHandler(new DataHandler(fileds));
                bp.setFileName(fileds.getName());
                mp.addBodyPart(bp);
             }
             return true;
           }catch(Exception e){
            logger.info(e.getMessage(),e);
//             System.err.println("增加附件: "+filename+"--faild!"+e);
//            LogLayout.error(logger,"itp","增加附件: "+filename+"--faild!",e);
             return false;
        }
   }
    /**
     *
     * @param from
     * @return
     */
    public boolean setFrom(String from){
        System.out.println("Set From .");
        try{
            mimeMsg.setFrom(new InternetAddress(from));
            return true;
        }catch(Exception e){
            logger.info(e.getMessage(),e);
            return false;
        }
    }
    /**
     *
     * @param to
     * @return
     */
    public boolean setTo(String to){
        System.out.println("Set to.");
        if(to==null||to.equals("")){
            return false;
        }
        try{
            mimeMsg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));
            return true;
        }catch(Exception e){
            logger.info(e.getMessage(),e);
            return false;
        }
    }
    public boolean setCopyTo(String copyto){
        if(copyto.equals("")||copyto==null){
            return false;
        }
        try{
            String copy[];
            copy=copyto.split(";");
            for(int i=0;i<copy.length;i++){
                mimeMsg.setRecipients(Message.RecipientType.TO,(Address[])InternetAddress.parse(copy[i]));
            }
            return true;
        }catch(Exception e){
            logger.info(e.getMessage(),e);
            return false;
        }
    }


    /**
     * 设置信的内容!
     * @param mailBody
     * @return boolean
     */
    public boolean setBody(String mailBody){
        try{
            BodyPart bp=new MimeBodyPart();
            bp.setContent("<meta http-equiv=Context-Type context=text/html;charset=gb2312>"+mailBody,"text/html;charset=GB2312");
            mp.addBodyPart(bp);
            return true;
        }catch(Exception e){
            logger.info(e.getMessage(),e);
//            System.out.println("Set context Faild! "+e);
//            LogLayout.error(logger,"itp","Set context Faild! ",e);
            return false;
        }
    }
    /**
     *
     * @param htmlpath
     * @return boolean
     */
    public boolean setHtml(String htmlpath){
        try{
            if(!htmlpath.equals("")||htmlpath!=null){
                BodyPart mbp=new MimeBodyPart();
                DataSource ds=new FileDataSource(htmlpath);
                mbp.setDataHandler(new DataHandler(ds));
                mbp.setHeader("Context-ID","meme");
                mp.addBodyPart(mbp);
            }
            return true;
        }catch(Exception  e){
            logger.info(e.getMessage(),e);
//                System.err.println("Set Html Faild!" + e);
//            LogLayout.error(logger,"itp","Set Html Faild!", e);
            return false;
        }
    }

    public boolean setOut(){
        try{
            mimeMsg.setContent(mp);
            mimeMsg.saveChanges();
            System.out.println("正在SendMail.");
            Session mailSession=session.getInstance(props,null);
            Transport tp=mailSession.getTransport("smtp");
            tp.connect((String)props.getProperty("mail.stmp.host"),username,password);
            tp.sendMessage(mimeMsg,mimeMsg.getRecipients(Message.RecipientType.TO));
            //tp.sendMessage(mimeMsg,mimeMsg.getRecipients(Message.RecipientType.CC));
//            System.out.println("Send Mail 成功..");
//            LogLayout.info(logger,"itp","Send Mail 成功..");
            tp.close();
            return true;
        }catch(Exception e){
            logger.info(e.getMessage(),e);
//            LogLayout.error(logger,"itp","Send Mail 失败",e);
            return false;
        }
    }

    public static boolean sendMessage(String emailServer, String connectName, String password, boolean isNeedAuth,
                                       String fromEmail, String toEmail, String title, String text, List<String> fileAffix) {
        SendMail sm = new SendMail(emailServer);
        sm.setNamePass(connectName,password);
        sm.setFrom(fromEmail);
        sm.setTo(toEmail);
        sm.setSubject(title);
        sm.setBody(text);
        if(fileAffix!=null){
            for (String fileName : fileAffix) {
                sm.addFileAffix(fileName);
            }
        }
        sm.setNeedAuth(isNeedAuth);
        return sm.setOut();
    }

}
