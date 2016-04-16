package com.hzih.sslvpn.listener;

import com.hzih.sslvpn.domain.Account;
import com.hzih.sslvpn.web.ApplicationUtils;
import com.hzih.sslvpn.web.SessionUtils;

import java.util.ArrayList;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {

       public void sessionCreated(HttpSessionEvent event) {
              HttpSession session = event.getSession();
              ServletContext application = session.getServletContext();
             
              /*
               *  在application范围由一个ArrayList集保存所有的session
               */
              ArrayList<HttpSession> sessions = (ArrayList<HttpSession>) application.getAttribute("sessions");

              if (sessions == null) {
                     sessions = new ArrayList();
                     application.setAttribute("sessions", sessions);
              }
             
              // 新创建的session均添加到ArrayList集中
              sessions.add(session);
              // 可以在别处从application范围中取出sessions集合
              // 然后使用sessions.size()获取当前活动的session数，即为“在线人数”
       }

       public void sessionDestroyed(HttpSessionEvent event) {
              HttpSession session = event.getSession();
              ServletContext application = session.getServletContext();
              ArrayList<HttpSession> sessions = (ArrayList<HttpSession>) application.getAttribute("sessions");
             
              /*
               * 销毁的session均从ArrayList集中移除
               */
              Account account = (Account)session.getAttribute(SessionUtils.ACCOUNT_ATTRIBUTE);
              if (account!=null){
                     session.removeAttribute(SessionUtils.ACCOUNT_ATTRIBUTE);
                     session.invalidate();
                     ApplicationUtils.removeAccount(account.getUserName());
              }
              sessions.remove(session);
       }
} 