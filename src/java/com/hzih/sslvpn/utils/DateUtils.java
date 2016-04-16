package com.hzih.sslvpn.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 13-1-8
 * Time: 上午10:37
 * To change this template use File | Settings | File Templates.
 */
public class DateUtils {

    /**
     * 转时间Date类型成为format
     * @param date       需要转的时间
     * @param format     转成的格式
     * @return
     */
    public static String formatDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 比较 字符串格式时间 的大小
     * @param startDate
     * @param endDate
     * @param format    时间格式
     * @return     true: endDate >= startDate
     * @throws java.text.ParseException
     */
    public static boolean checkDate(String startDate, String endDate, String format) throws ParseException {
        Date start = parse(startDate,format);
        Date end = parse(endDate,format);
        if(start.getTime() - end.getTime() > 0){
            return false;
        }
        return true;
    }

    /**
     * 转换字符串格式时间为java.util.Date类型
     * @param startDateStr
     * @param format
     * @return
     * @throws java.text.ParseException
     */
    public static Date parse(String startDateStr, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(startDateStr);
    }

}
