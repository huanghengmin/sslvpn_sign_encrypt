package com.hzih.sslvpn.utils.des;

import com.hzih.sslvpn.utils.StringContext;

import java.io.*;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;

public class DesUtils {
    public static final String crypt_key = StringContext.systemPath + "/des/crypt_key.xml";

    public static void saveDesKey() {
        try {
            SecureRandom sr = new SecureRandom();
            // 为我们选择的DES算法生成一个KeyGenerator对象
            KeyGenerator kg = KeyGenerator.getInstance("DES");
            kg.init(sr);
            FileOutputStream fos = new FileOutputStream(crypt_key);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            // 生成密钥
            Key key = kg.generateKey();
            oos.writeObject(key);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Key getKey() {
        File file = new File(crypt_key);
        if(file.exists()) {
            Key kp = null;
            try {
                FileInputStream is = new FileInputStream(crypt_key);
                ObjectInputStream oos = new ObjectInputStream(is);
                kp = (Key) oos.readObject();
                oos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return kp;
        }
        return null;
    }

    public static void encrypt(String file, String dest,Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        InputStream is = new FileInputStream(file);
        OutputStream out = new FileOutputStream(dest);
        CipherInputStream cis = new CipherInputStream(is, cipher);
        byte[] buffer = new byte[1024];
        int r;
        while ((r = cis.read(buffer)) > 0) {
            out.write(buffer, 0, r);
        }
        cis.close();
        is.close();
        out.close();
    }

    public static void decrypt(File file, String dest,Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        InputStream is = new FileInputStream(file);
        OutputStream out = new FileOutputStream(dest);
        CipherOutputStream cos = new CipherOutputStream(out, cipher);
        byte[] buffer = new byte[1024];
        int r;
        while ((r = is.read(buffer)) >= 0) {
            cos.write(buffer, 0, r);
        }
        cos.close();
        out.close();
        is.close();
    }
}
