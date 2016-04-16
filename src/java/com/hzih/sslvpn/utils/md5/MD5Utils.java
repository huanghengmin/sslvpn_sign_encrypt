package com.hzih.sslvpn.utils.md5;

import com.hzih.sslvpn.utils.StringContext;
import com.inetec.common.util.OSInfo;
import com.inetec.common.util.Proc;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

public class MD5Utils {
    private Logger logger = Logger.getLogger(MD5Utils.class);

    public String getMd5ByFile(File file) throws FileNotFoundException {
        String value = null;
        FileInputStream in = new FileInputStream(file);
        try {
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    public boolean shellMd5(String sourceFile,String md5File){
        Proc proc = new Proc();
        String command = null;
        if (OSInfo.getOSInfo().isWin()) {
            command = StringContext.systemPath + "/script/md5_get.bat " +
                    sourceFile + " " +
                    md5File;
        } else {
            command = StringContext.systemPath + "/script/md5_get.sh " +
                    sourceFile + " " +
                    md5File;
        }
        proc.exec(command);
        if (proc.getResultCode() != -1) {
            File file = new File(md5File);
            if(file.exists()&&file.length()>0){
                return true;
            }
        }
        return false;
    }

    public boolean checkMd5(String md5File){
        Proc proc = new Proc();
        String command = null;
        if (OSInfo.getOSInfo().isWin()) {
            command = StringContext.systemPath + "/script/md5_check.bat " +
                    md5File;
        } else {
            command = StringContext.systemPath + "/script/md5_check.sh " +
                    md5File;
        }
        proc.exec(command);
        if (proc.getResultCode() != -1) {
            if(proc.getOutput().contains("确定")){
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

}
