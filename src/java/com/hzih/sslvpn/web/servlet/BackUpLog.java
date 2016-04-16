package com.hzih.sslvpn.web.servlet;

import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.web.action.sslvpn.backup.TarUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 15-7-29.
 */
public class BackUpLog {

    private static Logger logger = Logger.getLogger(BackUpLog.class);

    /**
     * 网络配置 net
     * 黑名单及证书 ssl
     * 服务配置 service
     * 系统配置 sysconfig
     *
     * @return
     */
    public static boolean bak(String path, String file_name) {
        if (path.endsWith(File.separator)) {
            TarUtils tarUtils = new TarUtils();
            //单个文件打包
            List<String> array = new ArrayList<>();
            array.add(StringContext.localLogPath);
            tarUtils.execute(array, path + file_name + ".tar", path + file_name);
            return true;
        }
        return false;
    }

}
