package com.hzih.sslvpn.utils;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 12-8-27
 * Time: 上午9:42
 * To change this template use File | Settings | File Templates.
 */
public class ExtractZip {
    private static final Logger logger=Logger.getLogger(ExtractZip.class);
    private static final int SIZE = 1024*1024*2;

    public boolean Unzip(String location) throws IOException{
        boolean flag = false ;
//        logger.info("开始解压压缩包"+location);
        ZipFile zipFile = new ZipFile(location);
        Enumeration enumeration = zipFile.entries();
        while(enumeration.hasMoreElements()){
            ZipEntry zipEntry = (ZipEntry) enumeration.nextElement();
//            String floder = location.substring(0,location.lastIndexOf("."));
            String floder = location.substring(0,location.lastIndexOf("/"));
            File file = new File(floder);
            if(!file.exists()){
                file.mkdirs();
            }
            String filename = zipEntry.getName();
            if(filename.lastIndexOf("/") !=-1){
                String dir = floder+"/"+filename.substring(0,filename.lastIndexOf("/"));
                File file1 = new File(dir);
                if(!file1.exists()){
                    file1.mkdirs();
                }
            }
            if(zipEntry.isDirectory()){
                continue;
            }else{
                InputStream inputStream = zipFile.getInputStream(zipEntry);
                FileOutputStream fileOutputStream = new FileOutputStream(floder+"/"+filename);
                byte[] data = new byte[SIZE];
                int c = 0;
                while(( c = inputStream.read(data)) != -1 ){
                    fileOutputStream.write(data,0,c);
                }
                inputStream.close();
                fileOutputStream.close();
            }
        }
        zipFile.close();
//        logger.info("解压压缩包"+location+"完成");
        flag = true ;
        return flag;
    }
}
