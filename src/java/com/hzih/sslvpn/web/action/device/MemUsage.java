package com.hzih.sslvpn.web.action.device;

import java.io.*;

/**
 * 采集内存使用率
 */
public class MemUsage {

    public static double getMemUsage() throws Exception {
        long memUsed = 0;
        long memTotal = 0;
        double memUsage = 0.0;
        Runtime rt = Runtime.getRuntime();
        Process p = rt.exec("top -b -n 1");// call "top" command in linux
        BufferedReader in = null;
        in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String str = null;
        int linecount = 0;
        while ((str = in.readLine()) != null) {
            linecount++;
            if (linecount == 4) {
                String[] s = str.split("k ");
                String memUsedstr = s[1];
                String memTotalstr = s[0];
                String memUsedstr1[] = memUsedstr.split(" ");
                memUsed = Long.parseLong(memUsedstr1[memUsedstr1.length - 1]);
                String memTotalstr1[] = memTotalstr.split(" ");
                memTotal = Long.parseLong(memTotalstr1[memTotalstr1.length - 1]);
                memUsage = memUsed * 100 / memTotal;
                break;
            }
        }
        return memUsage;
    }
}