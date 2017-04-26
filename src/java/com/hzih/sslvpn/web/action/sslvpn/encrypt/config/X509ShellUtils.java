package com.hzih.sslvpn.web.action.sslvpn.encrypt.config;

import com.hzih.sslvpn.utils.StringContext;
import com.inetec.common.util.OSInfo;
import com.inetec.common.util.Proc;
import org.apache.log4j.Logger;

import java.math.BigInteger;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-7-4
 * Time: 上午6:55
 * To change this template use File | Settings | File Templates.
 */
public class X509ShellUtils {
    private static Logger logger = Logger.getLogger(X509ShellUtils.class);

    //生成uuid唯一序列号
    public static String build_uuid() {
        //使用UUID生成序列号
        UUID uuid = UUID.randomUUID();
        String ids[] = uuid.toString().split("-");
        StringBuffer strBuf = new StringBuffer();
        for (int i = 0; i < ids.length; i++) {
            strBuf.append(ids[i]);
        }
        BigInteger big = new BigInteger(strBuf.toString(), 16);
        return big.toString();
    }

    //签发根ca
    public static boolean build_selfsign_ca(String days, String bits, String keyfile, String certificate, String apply_conf) {
        Proc proc = new Proc();
        String command = null;
        if (OSInfo.getOSInfo().isWin()) {
            command = StringContext.systemPath + "/commands/windows/no_pass/build-selfsign-ca.bat " +
                    days + " " +
                    build_uuid() + " " +
                    bits + " " +
                    keyfile +" " +
                    certificate+ " " +
                    apply_conf;
        } else {
            command = StringContext.systemPath + "/commands/liunx/no_pass/build-selfsign-ca.sh " +
                    days + " " +
                    build_uuid() + " " +
                    bits + " " +
                    keyfile + " " +
                    certificate + " " +
                    apply_conf;
        }
        proc.exec(command);
        if (proc.getResultCode() != -1) {
           if(!proc.getErrorOutput().contains("error")&&!proc.getErrorOutput().contains("Error")){
                return true;
            } else {
                logger.equals(proc.getErrorOutput());
            }
        }
        return false;
    }

    //构建请求文件
    public static boolean build_csr(String bits, String keyfile, String csr_file, String apply_conf) {
        Proc proc = new Proc();
        String command = null;
        if (OSInfo.getOSInfo().isWin()) {
            command = StringContext.systemPath + "/commands/windows/no_pass/build-csr.bat " +
                    bits + " " +
                    keyfile + " " +
                    csr_file + " " +
                    apply_conf;
        } else {
            command = StringContext.systemPath + "/commands/liunx/no_pass/build-csr.sh " +
                    bits + " " +
                    keyfile + " " +
                    csr_file + " " +
                    apply_conf;
        }
        proc.exec(command);
        if (proc.getResultCode() != -1) {
            if(!proc.getErrorOutput().contains("error")&&!proc.getErrorOutput().contains("Error")){
                return true;
            } else {
                logger.equals(proc.getErrorOutput());
            }
        }
        return false;
    }

    //使用原有私钥构建请求文件
    public static boolean build_again_csr(String keyfile, String csr_file, String apply_conf) {
        Proc proc = new Proc();
        String command = null;
        if (OSInfo.getOSInfo().isWin()) {
            command = StringContext.systemPath + "/commands/windows/no_pass/build-again-csr.bat " +
                    keyfile + " " +
                    csr_file + " " +
                    apply_conf;
        } else {
            command = StringContext.systemPath + "/commands/liunx/no_pass/build-again-csr.sh " +
                    keyfile + " " +
                    csr_file+ " " +
                    apply_conf;
        }
        proc.exec(command);
        if (proc.getResultCode() != -1) {
           if(!proc.getErrorOutput().contains("error")&&!proc.getErrorOutput().contains("Error")){
                return true;
            } else {
                logger.equals(proc.getErrorOutput());
            }
        }
        return false;
    }

    //签发csr请求
    public static boolean build_sign_csr(String in_csr_file, String sign_ca_config, String extensions, String sign_ca_file, String sign_ca_key,  String out_cer, String days) {
        Proc proc = new Proc();
        String command = null;
        if (OSInfo.getOSInfo().isWin()) {
            command = StringContext.systemPath + "/commands/windows/no_pass/build-sign-csr.bat " +
                    build_uuid() + " " +
                    in_csr_file + " " +
                    sign_ca_config+ " " +
                    extensions + " " +
                    sign_ca_file + " " +
                    sign_ca_key + " " +
                    out_cer + " " +
                    days;
        } else {
            command = StringContext.systemPath + "/commands/liunx/no_pass/build-sign-csr.sh " +
                    build_uuid() + " " +
                    in_csr_file+ " " +
                    sign_ca_config+ " " +
                    extensions + " " +
                    sign_ca_file + " " +
                    sign_ca_key + " " +
                    out_cer + " " +
                    days;
        }
        proc.exec(command);
        if (proc.getResultCode() != -1) {
           if(!proc.getErrorOutput().contains("error")&&!proc.getErrorOutput().contains("Error")){
                return true;
            } else {
                logger.equals(proc.getErrorOutput());
            }
        }
        return false;
    }

    //列出crl列表
    public static String build_list_crl(String crl_file) {
        Proc proc = new Proc();
        String command = null;
        if (OSInfo.getOSInfo().isWin()) {
            command = StringContext.systemPath + "/commands/windows/no_pass/build-list-crl.bat " +crl_file;
        } else {
            command = StringContext.systemPath + "/commands/liunx/no_pass/build-list-crl.sh " +crl_file;
        }
        proc.exec(command);
        return proc.getOutput();
    }

    //构建crl列表
    public static boolean build_make_crl(String out_crl_file, String ca_key_file,String ca_certificate, String ca_config) {
        Proc proc = new Proc();
        String command = null;
        if (OSInfo.getOSInfo().isWin()) {
            command = StringContext.systemPath + "/commands/windows/no_pass/build-make-crl.bat " +
                    out_crl_file+ " " +
                    ca_key_file + " " +
                    ca_certificate + " " +
                    ca_config;
        } else {
            command = StringContext.systemPath + "/commands/liunx/no_pass/build-make-crl.sh " +
                    out_crl_file + " " +
                    ca_key_file + " " +
                    ca_certificate + " " +
                    ca_config;
        }
        proc.exec(command);
        if (proc.getResultCode() != -1) {
           if(!proc.getErrorOutput().contains("error")&&!proc.getErrorOutput().contains("Error")){
                return true;
            } else {
                logger.equals(proc.getErrorOutput());
            }
        }
        return false;
    }

    //导出带ca的pkcs文件(多个ca需要cat)
    public static boolean build_pkcs12_ca(String pfx_key_file,  String pfx_certificate, String cat_ca_file, String out_pfx_file) {
        Proc proc = new Proc();
        String command = null;
        if (OSInfo.getOSInfo().isWin()) {
            command = StringContext.systemPath + "/commands/windows/no_pass/build-pkcs12-ca.bat " +
                    pfx_key_file + " " +
                    pfx_certificate + " " +
                    cat_ca_file + " " +
                    out_pfx_file;
        } else {
            command = StringContext.systemPath + "/commands/liunx/no_pass/build-pkcs12-ca.sh " +
                    pfx_key_file + " " +
                    pfx_certificate + " " +
                    cat_ca_file + " " +
                    out_pfx_file;
        }
        proc.exec(command);
        if (proc.getResultCode() != -1) {
           if(!proc.getErrorOutput().contains("error")&&!proc.getErrorOutput().contains("Error")){
                return true;
            } else {
                logger.equals(proc.getErrorOutput());
            }
        }
        return false;
    }

    //导出pkcs文件
    public static boolean build_pkcs12(String pfx_key_file, String pfx_certificate, String out_pfx_file) {
        Proc proc = new Proc();
        String command = null;
        if (OSInfo.getOSInfo().isWin()) {
            command = StringContext.systemPath + "/commands/windows/no_pass/build-pkcs12.bat " +
                    pfx_key_file + " " +
                    pfx_certificate + " " +
                    out_pfx_file;
        } else {
            command = StringContext.systemPath + "/commands/liunx/no_pass/build-pkcs12.sh " +
                    pfx_key_file + " " +
                    pfx_certificate + " " +
                    out_pfx_file;
        }
        proc.exec(command);
        if (proc.getResultCode() != -1) {
           if(!proc.getErrorOutput().contains("error")&&!proc.getErrorOutput().contains("Error")){
                return true;
            } else {
                logger.equals(proc.getErrorOutput());

            }
        }
        return false;
    }

    //构建随机数文件
    public static boolean build_randfile(String out_rand_file, String rand_size) {
        Proc proc = new Proc();
        String command = null;
        if (OSInfo.getOSInfo().isWin()) {
            command = StringContext.systemPath + "/commands/windows/no_pass/build-randfile.bat " +
                    out_rand_file + " " +
                    rand_size;
        } else {
            command = StringContext.systemPath + "/commands/liunx/no_pass/build-randfile.sh " +
                    out_rand_file + " " +
                    rand_size;
        }
        proc.exec(command);
        if (proc.getResultCode() != -1) {
           if(!proc.getErrorOutput().contains("error")&&!proc.getErrorOutput().contains("Error")){
                return true;
            }  else {
                logger.equals(proc.getErrorOutput());
            }
        }
        return false;
    }

    //吊销证书
    public static boolean build_revoke(String revoke_file, String sign_ca_key, String sign_ca_certificate, String sign_ca_config) {
        Proc proc = new Proc();
        String command = null;
        if (OSInfo.getOSInfo().isWin()) {
            command = StringContext.systemPath + "/commands/windows/no_pass/build-revoke.bat " +
                    revoke_file + " " +
                    sign_ca_key + " " +
                    sign_ca_certificate + " " +
                    sign_ca_config;
        } else {
            command = StringContext.systemPath + "/commands/liunx/no_pass/build-revoke.sh " +
                    revoke_file + " " +
                    sign_ca_key + " " +
                    sign_ca_certificate + " " +
                    sign_ca_config;
        }
        proc.exec(command);
        if (proc.getResultCode() != -1) {
           if(!proc.getErrorOutput().contains("error")&&!proc.getErrorOutput().contains("Error")){
                return true;
            }else {
                logger.equals(proc.getErrorOutput());
            }
        }
        return false;
    }

    //校验证书吊销crl(cat多个ca和多个ca的crl列表)
    public static String build_verify_crl(String cat_ca_and_crl_file, String check_certificate) {
        Proc proc = new Proc();
        String command = null;
        if (OSInfo.getOSInfo().isWin()) {
            command = StringContext.systemPath + "/commands/windows/no_pass/build-verify-crl.bat " +
                    cat_ca_and_crl_file + " " +
                    check_certificate;
        } else {
            command = StringContext.systemPath + "/commands/liunx/no_pass/build-verify-crl.sh " +
                    cat_ca_and_crl_file + " " +
                    check_certificate;
        }
        proc.exec(command);
        return proc.getOutput();
    }

    //校验证书签名(多个ca需要cat)
    public static boolean build_verify(String cat_ca_file, String check_certificate) {
        Proc proc = new Proc();
        String command = null;
        if (OSInfo.getOSInfo().isWin()) {
            command = StringContext.systemPath + "/commands/windows/no_pass/build-verify.bat " +
                    cat_ca_file + " " +
                    check_certificate;
        } else {
            command = StringContext.systemPath + "/commands/liunx/no_pass/build-verify.sh " +
                    cat_ca_file + " " +
                    check_certificate;
        }
        proc.exec(command);
        if (proc.getResultCode() != -1) {
            if(!proc.getErrorOutput().contains("error")&&!proc.getErrorOutput().contains("Error")){
                return true;
            }else {
                logger.equals(proc.getErrorOutput());
            }
        }
        return false;
    }

    //读取CSR证书请求文件
    public static String build_decode_csr(String csr_path) {
        Proc proc = new Proc();
        String command = null;
        if (OSInfo.getOSInfo().isWin()) {
            command = StringContext.systemPath + "/commands/windows/no_pass/build-decode-csr.bat " +
                    csr_path;
        } else {
            command = StringContext.systemPath + "/commands/liunx/no_pass/build-decode-csr.sh " +
                    csr_path;
        }
        proc.exec(command);
        return proc.getOutput();
    }

    //构建BKS证书
    public static boolean build_bks(String CN, int alias, String superCN, String superDirectory) {
        Proc proc = new Proc();
        String command = StringContext.systemPath+"/shell/bks.sh " + StringContext.systemPath+"/ " + superDirectory +"/"+CN+" "+alias+" "+ superDirectory +"/"+ superCN +".crt";
        proc.exec(command);
        if (proc.getResultCode() != -1) {
            if(!proc.getErrorOutput().contains("error")&&!proc.getErrorOutput().contains("Error")){
                return true;
            }else {
                logger.equals(proc.getErrorOutput());
            }
        }
        return false;
    }
}
