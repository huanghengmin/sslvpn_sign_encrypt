/*=============================================================
 * �ļ����: Constant
 * ��    ��: 1.0
 * ��    ��: ������
 * ����ʱ��: 2009-6-30 20:18:30
 * ============================================================
 * <p>��Ȩ����  (c) 2009 ���������Ϣ�������޹�˾</p>
 * <p>
 * ��Դ���ļ���Ϊ���������Ϣ�������޹�˾�����һ���֣����
 * �˱���˾�Ļ��ܺ�����Ȩ��Ϣ����ֻ�ṩ��˾���������û�ʹ�á�
 * </p>
 * <p>
 * ���ڱ����ʹ�ã��������ر�������˵��������������涨�����޺�
 * ������
 * </p>
 * <p>
 * �ر���Ҫָ�����ǣ�����Դӱ���˾��������߸�����Ĳ��������ߺ�
 * ����ȡ�ò���Ȩʹ�ñ����򡣵��ǲ��ý��и��ƻ���ɢ�����ļ���Ҳ��
 * ��δ������˾����޸�ʹ�ñ��ļ������߽��л��ڱ�����Ŀ���������
 * ���ǽ������ķ����޶��ڶ����ַ�����˾��Ȩ����Ϊ�������ߡ�
 * </p>
 * ==========================================================*/
package com.hzih.sslvpn.utils;

/**
 *
 * @version 1.0
 * @since 2009-6-30 20:18:30
 */
public class Constant {

    public final static String S_APPLICATION_INITIAL_SHOW = "initial_show";
    public final static String S_APPLICATION_INITIAL = "init";
    public final static String S_APPLICATION_EXTERNAL_INITIAL_SHOW = "external_initial_show";
    public final static String S_APPLICATION_EXTERNAL_INITIAL = "external_init";

    //datasource
    public final static String S_DATASOURCE_EDIT = "edit_datasource";
    public final static String S_DATASOURCE_ADD = "add_datasource";
    public final static String S_DATASOURCE_REMOVE = "remove_datasource";
    public final static String S_DATASOURCE_SAVE = "save_datasource";
    public final static String S_DATASOURCE_SHOW = "show_datasource";
    public final static String S_DATASOURCE_SHOW_ADD = "show_add_datasource";
    public final static String S_PWD_ENCRYPT_CODE = "inetec~!@#$%^&*()_+";

    //POST
    //add application
    public final static String S_ADD_APPLICATION_FIRST = "addApp_first";
    public final static String S_ADD_APPLICATION = "addApp";
    public final static String S_ADD_SOURCE_CONFIG = "addSourceConfig";
    //    public final static String S_ADD_SOURCE_CONFIG_SHOWAPP = "addSourceConfig_showApp";
    public final static String S_ADD_TARGET_CONFIG = "addTargetConfig";
    public final static String S_ADD_SOURCE_TABLE_PROP = "addSourceTableProp";
    public final static String S_ADD_SOURCE_TABLE_Merge = "addSourceTableMerge";
    public final static String S_ADD_TARGET_TABLE_PROP = "addTargetTableProp";
    public final static String S_ADD_SOURCE_TABLE = "addTargetTable";
    public final static String S_OPERATION_TRIGGER = "trigger";

    public final static String S_EDIT_APPLICATION = "editApp";
    public final static String S_EDIT_APPLICATION_FIRST = "editApp_first";
    public final static String S_EDIT_SOURCE_CONFIG = "editSourceConfig";
    public final static String S_EDIT_SOURCE_TABLE_PROP = "editSourceTableProp";
    public final static String S_EDIT_SOURCE_TABLE_Merge = "editSourceTableMerge";
    public final static String S_EDIT_SOURCE_TABLE = "editSourceTable";

    public final static String S_EDIT_TARGET_TABLE = "editTatgetTable";
    public final static String S_EDIT_TARGET_TABLE_PROP = "editTatgetTableProp";
    public final static String S_SHOW_TARGET_FIELD = "showTatgetTableProp";
    public final static String S_BACKTO_EDIT_SOURCE_CONFIG = "backToEditTatgetTable";

    public final static String S_SAVE_APPLICATION_DETAIL = "save_app_detail";
    public final static String S_SAVE_SOURCE_CONFIG = "save_source_config";

    public final static String S_DELETE_APPLICATION = "deleteApp";

    public final static String S_COMMAND = "step";
    public final static String LOGOFF = "logoff";

    public final static String S_CHANGE_PWD = "changePwd";
    public final static String S_SHOW_CHANGE_PWD = "showChangePwd";
    public final static String S_CHANGE_PWD_MSG = "msg";

    //GET
    public final static String S_LIST_SOURCE_APP_NAME = "listSourceAppName";
    public final static String S_SELECT_TARGET_FIELD = "selectTargetField";
    public final static String S_LIST_EDIT_APP_NAME = "listEditAppName";
    public final static String S_SELECT_SOURCE_TABLE_PROP = "selectSourceTableProp";
    public final static String S_SELECT_SOURCE_TABLE_Merge = "selectSourceTableMerge";
    public final static String S_SHOW_SOURCE_TABLE_PROP = "showSourceTableProp";
    public final static String S_SHOW_SOURCE_TABLE_Merge = "showSourceTableMerge";

    public final static String S_INITJDBC = "initJdbc";

    //Session Attribute Names

    public final static String S_SESSION_OPPOSITE_CONFIG = "opposite_config";
    public final static String S_SESSION_TARGET_CONFIG = "target_config";
    public final static String S_SESSION_ATTRIBUTE_INTERNAL_DATASOURCE = "session_internal_datasource";
    public final static String S_SESSION_ATTRIBUTE_EXTERNAL_DATASOURCE = "session_external_datasource";
    public final static String S_SESSION_ATTRIBUTE_INITIAL = "session_initial";
    public final static String S_SESSION_INTERNAL_OR_EXTERNAL = "session_internal_or_external";

    public final static String S_SESSION_CURRENT_CONFIG = "config";
    public final static String S_SESSION_APPNAME = "appName";
    public final static String S_SESSION_JDBCNAME = "jdbcName";
    public final static String S_SESSION_TARGET_TABLES = "targetTables";
    public final static String S_SESSION_TARGET_FIELDS = "targetFields";
    public final static String S_SESSION_SOURCE_TABLES = "sourceTables";
    public final static String S_SESSION_SOURCE_FIELDS = "sourceFields";
    public final static String S_SESSION_CHANGED_JDBCS = "changedJdbcNames";
    public final static String S_SESSION_SOURCE_OLDTABLES = "sourceOldTables";
    public final static String S_SESSION_TARGETDB = "targetdb";
    public final static String S_SESSION_OPERATION = "operation";

    //default value
    public final static String S_DEFAULT_TABLE_INTERVAL = "120";
    public final static String S_DEFAULT_TABLE_SEQNUMBERL = "1";
    public final static String S_DEFAULT_TABLE_MONITORDELETE = "true";
    public final static String S_DEFAULT_TABLE_MONITORUPDATE = "true";
    public final static String S_DEFAULT_TABLE_MONITORINSERT = "true";
    public final static String S_DEFAULT_TABLE_DELETEENABLE = "true";
    public final static String S_DEFAULT_TABLE_ONLYINSERT = "false";
    public final static String S_DEFAULT_TABLE_CONDITION = "";

    public final static String S_INTERNAL_PATH = StringContext.systemPath/*System.getProperty("ichange.home")*/ + "/repository/config.xml";
    public final static String S_EXTERNAL_PATH = StringContext.systemPath/*System.getProperty("ichange.home")*/ + "/repository/external/config.xml";

    //database
    public final static String S_DB_ORACLE = "oracle";
    public final static String S_DB_MSSQL = "mssql";
    public final static String S_DB_DB2 = "db2";
    public final static String S_DB_SYBASE = "sybase";

    public final static String S_TEST_CONNECTION = "testConnection";
    public final static String S_SETUP_PROCDURE = "INETEC_SETUP_PROCDURE";
    public final static String S_REMOVE_PROCDURE = "INETEC_REMOVE_PROCDURE";
    public final static String S_FLAG_COLUMN = "ICHANGE_FLAG";
    public final static String S_TRIGGER = "trigger";
    public final static String S_FLAG = "flag";
    public final static String S_MONITORDELETE = "d";
    public final static String S_MONITORUPDATE = "u";
    public final static String S_MONITORINSERT = "i";
    public final static String S_DROP_TRIGGER = "drop_trigger";
    public final static String S_PROP_INSERT_TRIGGER = "insert_trigger";
    public final static String S_PROP_UPDATE_TRIGGER = "update_trigger";
    public final static String S_PROP_DELETE_TRIGGER = "delete_trigger";
    public final static String S_PROP_QUERY_TRIGGER = "query_trigger";
    public final static String S_PROP_DROP_SEQUENCE = "drop_sequences";
    public final static String S_PROP_CREATE_SEQUENCE = "create_sequences";
    public final static String S_PROP_QUERY_SEQUENCE = "query_sequence";
    public final static String S_PROP_QUERY_TEMPTABLE = "query_temptable";
    public final static String S_PROP_CREATE_TEMPTABLE = "create_temptable";
    public final static String S_PROP_DROP_TEMPTABLE = "drop_temptable";
    public final static String S_PROP_DROP_TRIGGER = "drop_trigger";

    /*Request Parameters*/
    public final static String S_PARAMETER_VALUE_REMOVE_INTERNAL = "remove_internal";
    public final static String S_PARAMETER_VALUE_REMOVE_EXTERNAL = "remove_external";
    public static String S_PARAMETER_VALUE_REMOVE_BOTH = "remove_both";
    public final static String S_SHOW_CONFIG = "show_config";

    //DB
    public final static String DB_INTERNAL = "internal";
    public final static String DB_EXTERNAL = "external";
    public final static String DEFAULT_ENCODING = "utf-8";

    public final static String S_SESSION_IS_LOCAL = "islocal";
    public final static String S_OLD_TEMPTABLE = "old_temptable";
    public final static String S_TRIGGER_TEMPTABLE = "trigger_temptable";

    public final static String S_SOURCE_DB_CLASSNAME = "com.inetec.ichange.plugin.dbchange.DbChangeSource";
    public final static String S_TARGET_DB_CLASSNAME = "com.inetec.ichange.plugin.dbchange.DbChangeTarget";


    public final static String S_SOURCE_PROXY_CLASSNAME = "com.inetec.ichange.plugin.socketchange.SocketChangeSource";
    public final static String S_TARGET_PROXY_CLASSNAME = "com.inetec.ichange.plugin.socketchange.SocketChangeTarget";
    public final static String S_SOURCE_SIPPROXY_CLASSNAME = "com.inetec.ichange.plugin.sipchange.SipChangeSource";
    public final static String S_TARGET_SIPPROXY_CLASSNAME = "com.inetec.ichange.plugin.sipchange.SipChangeTarget";

    public final static String S_SOURCE_APROXY_CLASSNAME = "com.inetec.ichange.plugin.httpchange.HttpChangeSource";
    public final static String S_TARGET_APROXY_CLASSNAME = "com.inetec.ichange.plugin.httpchange.HttpChangeTarget";

    public final static String S_SOURCE_FTPPROXY_CLASSNAME = "com.inetec.ichange.plugin.ftpchange.FtpChangeSource";
    public final static String S_TARGET_FTPPROXY_CLASSNAME = "com.inetec.ichange.plugin.ftpchange.FtpChangeTarget";

    public final static String S_SOURCE_REPROXY_CLASSNAME = "com.inetec.ichange.plugin.rehttpchange.RehttpChangeSource";
    public final static String S_TARGET_REPROXY_CLASSNAME = "com.inetec.ichange.plugin.rehttpchange.RehttpChangeTarget";

    //manager
    public final static String S_SERVER_RESTART = "server_restart";

    public final static String S_INITIAL_RESTART = "initial_restart";

    public final static String S_APPTYPE = "appType";
    public final static String S_NEW_PWD = "newPwd";
    public final static String S_OLD_PWD = "oldPwd";
    public final static String S_USERNAME = "userName";
}
