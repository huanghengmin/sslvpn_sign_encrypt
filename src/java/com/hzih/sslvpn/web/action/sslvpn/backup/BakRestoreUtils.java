package com.hzih.sslvpn.web.action.sslvpn.backup;

import com.hzih.sslvpn.utils.FileUtil;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.web.action.sslvpn.backup.mysql.MysqlBakUtils;
import com.inetec.common.util.Proc;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 14-7-21.
 */
public class BakRestoreUtils {
    private static Logger logger = Logger.getLogger(BakRestoreUtils.class);

    /**
     * 网络配置 net
     * 黑名单及证书 ssl
     * 服务配置 service
     * 系统配置 sysconfig
     *
     * @return
     */
    public static boolean bak(String path, String file_name, String backup_all, String backup_server, String backup_pki, String backup_net) {
        boolean fg = false;
        if (backup_all.equals("1")) {
            try {
                fg = MysqlBakUtils.backup();
            } catch (IOException e) {
                logger.info(e.getMessage(),e);
                return false;
            }
            if (fg) {
                if (path.endsWith(File.separator)) {
                    TarUtils tarUtils = new TarUtils();
                    //单个文件打包
//                    tarUtils.execute(path + path, path + X509Context.bak_file + ".tar", path + X509Context.bak_file);
                    List<String> array = new ArrayList<>();
                    array.add(StringContext.client_config_path);
                    array.add(StringContext.server_config_path);
                    array.add(StringContext.config_path);
                    array.add(StringContext.sql_path);
                    //证书及黑名单信息
                    array.add(StringContext.server_sslPath);
                    //网络信息
                    array.add(StringContext.INTERFACE);
                    array.add(StringContext.IFSTATE);
                    tarUtils.execute(array, path + file_name + ".tar", path + file_name);
                } else {
                    path += File.separator;
                    TarUtils tarUtils = new TarUtils();
                    //单个文件打包
//                    tarUtils.execute(path, path + X509Context.bak_file + ".tar", path + X509Context.bak_file);
                    List<String> array = new ArrayList<>();
                    array.add(StringContext.client_config_path);
                    array.add(StringContext.server_config_path);
                    array.add(StringContext.config_path);
                    array.add(StringContext.sql_path);
                    //证书及黑名单信息
                    array.add(StringContext.server_sslPath);
                    //网络信息
                    array.add(StringContext.INTERFACE);
                    array.add(StringContext.IFSTATE);
                    //多个文件打包
                    tarUtils.execute(array, path + file_name + ".tar", path + file_name);
                }
                File f = new File(path + file_name);
                if (f.exists() && f.length() > 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            if (backup_server.equals("1")) {
                try {
                    fg = MysqlBakUtils.backup();
                } catch (IOException e) {
                    logger.info(e.getMessage(),e);
                    return false;
                }

                if (fg) {
                    if (path.endsWith(File.separator)) {
                        TarUtils tarUtils = new TarUtils();
                        //单个文件打包
//                    tarUtils.execute(path + path, path + X509Context.bak_file + ".tar", path + X509Context.bak_file);
                        List<String> array = new ArrayList<>();
                        array.add(StringContext.client_config_path);
                        array.add(StringContext.server_config_path);
                        array.add(StringContext.config_path);
                        array.add(StringContext.sql_path);
                        //证书及黑名单信息
                        if (backup_pki.equals("1")) {
                            array.add(StringContext.server_sslPath);
                        }
                        //网络信息
                        if (backup_net.equals("1")) {
                            array.add(StringContext.INTERFACE);
                            array.add(StringContext.IFSTATE);
                        }
                        tarUtils.execute(array, path + file_name + ".tar", path + file_name);
                    } else {
                        path += File.separator;
                        TarUtils tarUtils = new TarUtils();
                        //单个文件打包
//                    tarUtils.execute(path, path + X509Context.bak_file + ".tar", path + X509Context.bak_file);
                        List<String> array = new ArrayList<>();
                        array.add(StringContext.client_config_path);
                        array.add(StringContext.server_config_path);
                        array.add(StringContext.config_path);
                        array.add(StringContext.sql_path);
                        //证书及黑名单信息
                        if (backup_pki.equals("1")) {
                            array.add(StringContext.server_sslPath);
                        }
                        //网络信息
                        if (backup_net.equals("1")) {
                            array.add(StringContext.INTERFACE);
                            array.add(StringContext.IFSTATE);
                        }
                        //多个文件打包
                        tarUtils.execute(array, path + file_name + ".tar", path + file_name);
                    }
                    File f = new File(path + file_name);
                    if (f.exists() && f.length() > 0) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                if (backup_pki.equals("1")) {
                    if (path.endsWith(File.separator)) {
                        TarUtils tarUtils = new TarUtils();
                        //单个文件打包
//                    tarUtils.execute(path + path, path + X509Context.bak_file + ".tar", path + X509Context.bak_file);
                        List<String> array = new ArrayList<>();
                        //证书及黑名单信息
                        array.add(StringContext.server_sslPath);
                        //网络信息
                        if (backup_net.equals("1")) {
                            array.add(StringContext.INTERFACE);
                            array.add(StringContext.IFSTATE);
                        }
                        tarUtils.execute(array, path + file_name + ".tar", path + file_name);
                    } else {
                        path += File.separator;
                        TarUtils tarUtils = new TarUtils();
                        //单个文件打包
//                    tarUtils.execute(path, path + X509Context.bak_file + ".tar", path + X509Context.bak_file);
                        List<String> array = new ArrayList<>();
                        //证书及黑名单信息
                        array.add(StringContext.server_sslPath);
                        //网络信息
                        if (backup_net.equals("1")) {
                            array.add(StringContext.INTERFACE);
                            array.add(StringContext.IFSTATE);
                        }
                        //多个文件打包
                        tarUtils.execute(array, path + file_name + ".tar", path + file_name);
                    }
                    File f = new File(path + file_name);
                    if (f.exists() && f.length() > 0) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    if (backup_net.equals("1")) {
                        if (path.endsWith(File.separator)) {
                            TarUtils tarUtils = new TarUtils();
                            //单个文件打包
//                    tarUtils.execute(path + path, path + X509Context.bak_file + ".tar", path + X509Context.bak_file);
                            List<String> array = new ArrayList<>();
                            //网络信息
                            array.add(StringContext.INTERFACE);
                            array.add(StringContext.IFSTATE);
                            tarUtils.execute(array, path + file_name + ".tar", path + file_name);
                        } else {
                            path += File.separator;
                            TarUtils tarUtils = new TarUtils();
                            //单个文件打包
//                    tarUtils.execute(path, path + X509Context.bak_file + ".tar", path + X509Context.bak_file);
                            List<String> array = new ArrayList<>();
                            //网络信息
                            array.add(StringContext.INTERFACE);
                            array.add(StringContext.IFSTATE);
                            //多个文件打包
                            tarUtils.execute(array, path + file_name + ".tar", path + file_name);
                        }
                        File f = new File(path + file_name);
                        if (f.exists() && f.length() > 0) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean bakRestore(String path, String file_name, int backup_all, int backup_server, int backup_pki, int backup_net) {
        if (path.endsWith(File.separator)) {
            GZip.unTargzFile(path + file_name, path);
            if (backup_all == 1) {
                try {
                    //mysql
                    MysqlBakUtils.recover();
                    //net
                    File file = new File(StringContext.systemPath + "/" + StringContext.INTERFACE_NAME);
                    if (file.exists()) {
                        FileUtil.copy(file, StringContext.INTERFACE);
//                        file.delete();
                    }
                    File file_inState = new File(StringContext.systemPath + "/" + StringContext.IFSTATE_NAME);
                    if (file_inState.exists()) {
                        FileUtil.copy(file_inState, StringContext.IFSTATE);
//                        file_inState.delete();
                    }
                    if(file.exists()&&file_inState.exists()){
                        Proc proc = new Proc();
                        proc.exec("service networking restart");
                        file.delete();
                        file_inState.delete();
                    }
                } catch (IOException e) {
                    logger.info(e.getMessage(),e);
                    return false;
                }
                return true;
            }

        } else {
            path += File.separator;
            GZip.unTargzFile(path + file_name, path);
            if (backup_all == 1) {
                try {
                    //mysql
                    MysqlBakUtils.recover();
                    //net
                    File file = new File(StringContext.systemPath + "/" + StringContext.INTERFACE_NAME);
                    if (file.exists()) {
                        FileUtil.copy(file, StringContext.INTERFACE);
//                        file.delete();
                    }
                    File file_inState = new File(StringContext.systemPath + "/" + StringContext.IFSTATE_NAME);
                    if (file_inState.exists()) {
                        FileUtil.copy(file_inState, StringContext.IFSTATE);
//                        file_inState.delete();
                    }

                    if(file.exists()&&file_inState.exists()){
                        Proc proc = new Proc();
                        proc.exec("service networking restart");
                        file.delete();
                        file_inState.delete();
                    }
                } catch (IOException e) {
                    logger.info(e.getMessage(),e);
                    return false;
                }
                return true;
            } else {
                if (backup_server == 1) {
                    try {
                        //mysql
                        MysqlBakUtils.recover();
                    } catch (IOException e) {
                        logger.info(e.getMessage(),e);
                        return false;
                    }
                } else {
                    if (backup_net == 1) {
                        //net
                        File file = new File(StringContext.systemPath + "/" + StringContext.INTERFACE_NAME);
                        if (file.exists()) {
                            try {
                                FileUtil.copy(file, StringContext.INTERFACE);
                            } catch (IOException e) {
                                logger.error(e.getMessage());
                            }
//                            file.delete();
                        }
                        File file_inState = new File(StringContext.systemPath + "/" + StringContext.IFSTATE_NAME);
                        if (file_inState.exists()) {
                            try {
                                FileUtil.copy(file_inState, StringContext.IFSTATE);
                            } catch (IOException e) {
                                logger.error(e.getMessage(),e);
                            }
//                            file_inState.delete();
                        }

                        if(file.exists()&&file_inState.exists()){
                            Proc proc = new Proc();
                            proc.exec("service networking restart");
                            file.delete();
                            file_inState.delete();
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
