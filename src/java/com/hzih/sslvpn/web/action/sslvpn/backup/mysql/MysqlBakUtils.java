package com.hzih.sslvpn.web.action.sslvpn.backup.mysql;

import com.hzih.sslvpn.utils.StringContext;

import java.io.*;

/**
 * Created by Administrator on 14-7-21.
 */
public class MysqlBakUtils {

    /**
     * 备份mysql数据库
     *
     * @throws java.io.IOException
     */
    public static boolean backup() throws IOException {
        String database = MysqlProperties.getProperties("jdbc.database");
        String user = MysqlProperties.getProperties("jdbc.user");
        String pwd = MysqlProperties.getProperties("jdbc.password");
        Runtime runtime = Runtime.getRuntime();
        //-u后面是用户名，-p是密码-p后面最好不要有空格，-database是数据库的名字
        Process process = runtime.exec("mysqldump -u " + user + " -p" + pwd + " " + database);
        InputStream inputStream = process.getInputStream();//得到输入流，写成.sql文件
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader br = new BufferedReader(reader);
        String s = null;
        StringBuffer sb = new StringBuffer();
        while ((s = br.readLine()) != null) {
            sb.append(s + "\r\n");
        }
        s = sb.toString();
//                System.out.println(s);
        File file = new File(StringContext.mysql_bak_sql);
        file.getParentFile().mkdirs();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(s.getBytes());
        fileOutputStream.close();
        br.close();
        reader.close();
        inputStream.close();
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 恢复mysql数据库
     *
     * @throws java.io.IOException
     */
    public static void recover() throws IOException {
        String database = MysqlProperties.getProperties("jdbc.database");
        String user = MysqlProperties.getProperties("jdbc.user");
        String pwd = MysqlProperties.getProperties("jdbc.password");
        Runtime runtime = Runtime.getRuntime();
        //-u后面是用户名，-p是密码-p后面最好不要有空格，-family是数据库的名字，--default-character-set=utf8，这句话一定的加
        // 我就是因为这句话没加导致程序运行成功，但是数据库里面的内容还是以前的内容，最好写上完成的sql放到cmd中一运行才知道报错了
        // 错误信息：
        // mysql: Character set 'utf-8' is not a compiled character set and is not specified in the '
        // C:\Program Files\MySQL\MySQL Server 5.5\share\charsets\Index.xml' file ERROR 2019 (HY000): Can't
        // initialize character set utf-8 (path: C:\Program Files\MySQL\MySQL Server 5.5\share\charsets\)，
        // 又是讨人厌的编码问题，在恢复的时候设置一下默认的编码就可以了。
        Process process = runtime.exec("mysql -u " + user + " -p" + pwd + " --default-character-set=utf8 " + database);
        OutputStream outputStream = process.getOutputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(StringContext.mysql_bak_sql)));
        String str = null;
        StringBuffer sb = new StringBuffer();
        while ((str = br.readLine()) != null) {
            sb.append(str + "\r\n");
        }
        str = sb.toString();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "utf-8");
        writer.write(str);
        writer.flush();
        outputStream.close();
        br.close();
        writer.close();
    }

    public static void recover_system() throws IOException {
        String database = MysqlProperties.getProperties("jdbc.database");
        String user = MysqlProperties.getProperties("jdbc.user");
        String pwd = MysqlProperties.getProperties("jdbc.password");
        Runtime runtime = Runtime.getRuntime();
        //-u后面是用户名，-p是密码-p后面最好不要有空格，-family是数据库的名字，--default-character-set=utf8，这句话一定的加
        // 我就是因为这句话没加导致程序运行成功，但是数据库里面的内容还是以前的内容，最好写上完成的sql放到cmd中一运行才知道报错了
        // 错误信息：
        // mysql: Character set 'utf-8' is not a compiled character set and is not specified in the '
        // C:\Program Files\MySQL\MySQL Server 5.5\share\charsets\Index.xml' file ERROR 2019 (HY000): Can't
        // initialize character set utf-8 (path: C:\Program Files\MySQL\MySQL Server 5.5\share\charsets\)，
        // 又是讨人厌的编码问题，在恢复的时候设置一下默认的编码就可以了。
        Process process = runtime.exec("mysql -u " + user + " -p" + pwd + " --default-character-set=utf8 " + database);
        OutputStream outputStream = process.getOutputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(StringContext.mysql_sql)));
        String str = null;
        StringBuffer sb = new StringBuffer();
        while ((str = br.readLine()) != null) {
            sb.append(str + "\r\n");
        }
        str = sb.toString();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "utf-8");
        writer.write(str);
        writer.flush();
        outputStream.close();
        br.close();
        writer.close();
    }
}
