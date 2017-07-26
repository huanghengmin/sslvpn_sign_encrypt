package com.hzih.sslvpn.utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


public class MergeFiles {

    public static final int BUFSIZE = 1024 * 8;

    public static void mergeFiles(String crl, String folder) {
        FileChannel outChannel = null;
        try {
            File crl_file = new File(crl);
            if(!crl_file.exists())
                crl_file.createNewFile();
            outChannel = new FileOutputStream(crl).getChannel();
            final File file = new File(folder);
            if(file.exists()) {
                File[] files = file.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        if(pathname.getName().endsWith(".crl")){
                            return true;
                        }
                        return false;
                    }
                });
                for (File file2 : files) {
                    if (file2.isFile()) {
                      FileChannel fc = new FileInputStream(file2).getChannel();
                      ByteBuffer bb = ByteBuffer.allocate(BUFSIZE);
                      while (fc.read(bb) != -1) {
                          bb.flip();
                          outChannel.write(bb);
                          bb.clear();
                      }
                      fc.close();
                    }
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {if (outChannel != null) {outChannel.close();}} catch (IOException ignore) {}
        }
    }

    public static void mergeFiles(String outFile, String[] files) {
        FileChannel outChannel = null;
        try {
            outChannel = new FileOutputStream(outFile).getChannel();
            for(String f : files){
                FileChannel fc = new FileInputStream(f).getChannel();
                ByteBuffer bb = ByteBuffer.allocate(BUFSIZE);
                while(fc.read(bb) != -1){
                    bb.flip();
                    outChannel.write(bb);
                    bb.clear();
                }
                fc.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {if (outChannel != null) {outChannel.close();}} catch (IOException ignore) {}
        }
    }

    /**
     * 追加
     * @param outFile
     * @param files
     */
    public static void appendMergeFiles(String outFile,String[] files){
        FileChannel outChannel = null;
        try {
            outChannel = new FileOutputStream(outFile,true).getChannel();
            for(String f : files){
                FileChannel fc = new FileInputStream(f).getChannel();
                ByteBuffer bb = ByteBuffer.allocate(BUFSIZE);
                while(fc.read(bb) != -1){
                    bb.flip();
                    outChannel.write(bb);
                    bb.clear();
                }
                fc.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {if (outChannel != null) {outChannel.close();}} catch (IOException ignore) {}
        }
    }

    public static void main(String[] args) {
//        mergeFiles("D:/output.pem", new String[]{"D:/ca.crt", "D:/server.crt"});
        mergeFiles("/Users/huanghengmin/work/fartec/app/sslvpn_sign_encrypt/pki/crl/crl.pem","/Users/huanghengmin/work/fartec/app/sslvpn_sign_encrypt/pki/crl/");
    }
}  