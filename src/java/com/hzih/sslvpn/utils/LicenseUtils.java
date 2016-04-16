package com.hzih.sslvpn.utils;

import com.inetec.common.security.License;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class LicenseUtils {
    private static final Logger logger = Logger.getLogger(LicenseUtils.class);

    /**
     * 权限控制
     *
     * @param isExistLicense 是否存在 usb-key
     * @return
     */
    public List<String> getNeedsLicenses(boolean isExistLicense) {
        String qxManager = "TOP_QXGL:SECOND_YHGL:SECOND_JSGL:SECOND_AQCL:";     // 权限管理
        String wlManager = "TOP_WLGL:SECOND_JKGL:SECOND_LTCS:SECOND_LYGL:SECOND_PZGL:"; //网络管理
        String xtManager = "TOP_XTGL:SECOND_XTGL:SECOND_ZSGL:SECOND_FWGL:SECOND_SJRB:"; //系统管理
        String sjManager = "TOP_SJGL:SECOND_GLRZ:SECOND_RZXZ:SECOND_RZZJ:";         //审计管理
        String ztManager = "TOP_BJGL:SECOND_SBBJ:";     //报警管理
        String zyManager = "TOP_ZYGL:SECOND_ZWZY:";   //资源管理
        String pzManager = "TOP_FWGL:SECOND_FWZT:SECOND_JBPZ:SECOND_ZSPZ:";  //服务管理
        String yhManager = "TOP_ZDGL:SECOND_ZDYH:SECOND_ZDFZ:SECOND_ZDZX:SECOND_ZDRZ:SECOND_ZDJL:";   //终端管理
        String xpManager = "TOP_XTPZ:SECOND_CYPZ:SECOND_SJPZ:";   //系统配置
        String dxManager = "TOP_DXGL:SECOND_DXLB:SECOND_DXWJ:SECOND_DXGX:";   //吊销管理
        String jkManager = "TOP_JKGL:SECOND_ZJJK:SECOND_JKBJ:";   //监控管理
        String vsManager = "TOP_BBSJ:SECOND_KFBB:SECOND_FWBB:SECOND_BFHF";   //版本管理
        String permission = qxManager + wlManager + xtManager + sjManager + ztManager + zyManager + pzManager + yhManager + xpManager + dxManager + jkManager + vsManager;
        if (isExistLicense) {
            try {
                String license = License.getModules();//许可证允许的权限
                permission += license;
            } catch (Exception e) {
                logger.error("读取USB-KEY出错!",e);
            }
        }
        String[] permissions = permission.split(":");
        List<String> lps = new ArrayList<String>();
        for (int i = 0; i < permissions.length; i++) {
            lps.add(permissions[i]);
        }
        return lps;
    }
}
