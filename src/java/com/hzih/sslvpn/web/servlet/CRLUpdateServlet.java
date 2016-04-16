package com.hzih.sslvpn.web.servlet;

import com.hzih.sslvpn.utils.StringContext;
import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

/**
 * Created by Administrator on 15-5-26.
 */
public class CRLUpdateServlet extends HttpServlet {
    private Logger logger = Logger.getLogger(CRLUpdateServlet.class);

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
        timer = new Timer(true);
        SAXReader saxReader = new SAXReader();
        Document doc = null;
        try {
            doc = saxReader.read(new File(StringContext.crl_xml));
        } catch (DocumentException e) {
            logger.error(e.getMessage(),e);
        }
        if (doc != null) {
            Element selectSingleNode = (Element) doc.selectSingleNode("/config/auto_update");
            if (selectSingleNode != null) {
                Attribute attribute = selectSingleNode.attribute("enable");
                Element conf_type_el = selectSingleNode.element("conf_type");
                Element hours_el = selectSingleNode.element("hours");
                Element minutes_el = selectSingleNode.element("minutes");
                Element seconds_el = selectSingleNode.element("seconds");
                Element conf_time_el = selectSingleNode.element("conf_time");
                Element conf_day_el = selectSingleNode.element("conf_day");
                Element conf_time2_el = selectSingleNode.element("conf_time2");
                Element conf_month_day_el = selectSingleNode.element("conf_month_day");
                Element conf_time3_el = selectSingleNode.element("conf_time3");
                String auto_flag = attribute.getValue();
                if (auto_flag.equals("on")) {
                    Calendar calendar = Calendar.getInstance();
                    String conf_type = conf_type_el.getText();
                    if (conf_type.equals("day")) {
                        String conf_time = conf_time_el.getText();
                        if (conf_time.contains(":")) {
                            String[] ss = conf_time.split(":");
                            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(ss[0]));
                            calendar.set(Calendar.MINUTE, Integer.parseInt(ss[1].substring(0, 1)));
                            calendar.set(Calendar.SECOND, Integer.parseInt(ss[1].substring(1, 2)));
                        }
                    } else if (conf_type.equals("week")) {
                        String conf_day = conf_day_el.getText();
                        String conf_time2 = conf_time2_el.getText();
                        calendar.set(Calendar.DAY_OF_WEEK, Integer.parseInt(conf_day));
                        if (conf_time2.contains(":")) {
                            String[] ss = conf_time2.split(":");
                            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(ss[0]));
                            calendar.set(Calendar.MINUTE, Integer.parseInt(ss[1].substring(0, 1)));
                            calendar.set(Calendar.SECOND, Integer.parseInt(ss[1].substring(1, 2)));
                        }
                    } else if (conf_type.equals("month")) {
                        String conf_month_day = conf_month_day_el.getText();
                        String conf_time3 = conf_time3_el.getText();
                        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(conf_month_day));
                        if (conf_time3.contains(":")) {
                            String[] ss = conf_time3.split(":");
                            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(ss[0]));
                            calendar.set(Calendar.MINUTE, Integer.parseInt(ss[1].substring(0, 1)));
                            calendar.set(Calendar.SECOND, Integer.parseInt(ss[1].substring(1, 2)));
                        }
                    }
                    Date time = calendar.getTime();
                    timer.schedule(new CRLUpdateTask(), time);

                }
        }
        } else {
            return;
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {

    }

}
