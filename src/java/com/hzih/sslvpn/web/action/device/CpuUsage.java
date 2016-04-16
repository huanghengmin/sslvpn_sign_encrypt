package com.hzih.sslvpn.web.action.device;

import java.io.*;

import org.apache.log4j.Logger;

/**
 * 采集CPU使用率
 */
public class CpuUsage {

    public static double getCpuUsage() throws Exception {
        double cpuUsed = 0;
        double idleUsed = 0.0;
        Runtime rt = Runtime.getRuntime();
        Process p = rt.exec("top -b -n 1");// call "top" command in linux
        BufferedReader in = null;
        in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String str = null;
        int linecount = 0;
        while ((str = in.readLine()) != null) {
            linecount++;
            if (linecount == 3) {
                String[] s = str.split("%");
                String idlestr = s[3];
                String idlestr1[] = idlestr.split(" ");
                idleUsed = Double.parseDouble(idlestr1[idlestr1.length - 1]);
                cpuUsed = 100 - idleUsed;
                break;

            }

        }
        return cpuUsed;
    }
}