package com.hzih.sslvpn.web.action.sslvpn.log;

import com.hzih.sslvpn.dao.LogDao;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Timer;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-6
 * Time: 下午2:13
 * To change this template use File | Settings | File Templates.
 */
public class ClearUserLogs extends HttpServlet {

    private LogDao logDao;

    private Timer timer = null;

    public void destroy() {
        super.destroy();
        if(timer!=null){
            timer.cancel();
        }
    }

    /**
     * <p>
     * 在Servlet中注入对象的步骤:
     * 1.取得ServletContext
     * 2.利用Spring的工具类WebApplicationContextUtils得到WebApplicationContext
     * 3.WebApplicationContext就是一个BeanFactory,其中就有一个getBean方法
     * 4.有了这个方法就可像平常一样为所欲为了,哈哈!
     * </p>
     */
    @Override
    public void init() throws ServletException {
        super.init();

        ServletContext servletContext = this.getServletContext();

        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);

        logDao = (LogDao) ctx.getBean("logDao");

        timer = new Timer(true);
        //设置任务计划，启动和间隔时间
        timer.schedule(new ClearTask(logDao), 0, 1000 * 60*60*24);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {

    }

}
