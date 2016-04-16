package com.hzih.sslvpn.web.action.sslvpn.encrypt.config;

import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.web.action.sslvpn.encrypt.EncryptUtils;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-7-3
 * Time: 上午9:05
 * To change this template use File | Settings | File Templates.
 */
public class X509Context {
    /**
     * 证书存放路径
     */
    public static final String store_path = StringContext.systemPath + "/certificate";

    public static final String sign_path = StringContext.systemPath+"/sign";

    public static final String keyName = ".key";

    public static final String csrName = ".csr";

    public static final String certName = ".cer";

    public static final String pkcsName = ".pfx";

    public static final String crlName = ".crl";

    public static final String jksName = ".jks";

    public static final String bksName = ".bks";

    public static String encrypt_ca_file = store_path+ "/"+ EncryptUtils.getCA().getCN()+certName;
    public static String encrypt_server_file = store_path+"/"+ EncryptUtils.getCA().getCN()+ "/"+ EncryptUtils.getServer().getCN()+certName;
    public static String encrypt_key_file = store_path+"/"+ EncryptUtils.getCA().getCN()+ "/"+ EncryptUtils.getServer().getCN()+keyName;
    /**
     * ca配置文件
     */
    public static final String config_type_ca = ".config";
    /**
     * 请求配置文件
     */
    public static final String config_type_certificate = ".cnf";
    /**
     * 主数据库文件
     */
    public static final String database = "index.txt";
    /**
     * crl数据库文件
     */
    public static final String crl_database = "crlnumber";
    /**
     * 证书序列号数据库文件
     */
    public static final String serial_database = "serial";
    /**
     * 默认字符编码
     */
    public static final String charset = "utf-8";
    /**
     * 中国
     */
    public static final String default_country_code = "CN";

    /**
     * 签发证书默认有效期
     */
    public static final String default_certificate_validity = "1825";
    /**
     * CA默认有效期
     */
    public static final String default_ca_validity = "36500";
    /**
     * 私钥长度
     */
    public static final String key_length = "1024";

    /**
    证书类型
    */

    //根证书
    public static final String certificate_type_ca = "v3_ca";
    // 代码签名
    public static final String certificate_type_codeSignature = "certificate_codeSignature";
    //计算机
    public static final String certificate_type_computer = "certificate_computer";
    //服务器证书
    public static final String certificate_type_server = "certificate_server";
    // 客户端证书
    public static final String certificate_type_client = "certificate_client";
     // 信息列表签名
    public static final String certificate_type_trustListSignature = "certificate_trustListSignature";
    // 时间戳
    public static final String certificate_type_timestamp = "certificate_timestamp";
     // ipsec
    public static final String certificate_type_ipsec = "certificate_ipsec";
     // email
    public static final String certificate_type_email = "certificate_email";
     //智能卡登陆
    public static final String certificate_type_smartCardLogin = "certificate_smartCardLogin";
     // crl
    public static final String crl_extends = "crl_extends";


}
