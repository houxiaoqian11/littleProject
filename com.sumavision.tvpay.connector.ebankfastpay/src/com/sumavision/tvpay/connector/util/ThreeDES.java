package com.sumavision.tvpay.connector.util;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class ThreeDES {

    private static final String Algorithm = "DESede";

    public static String decryptHex(String sdata) throws Exception {
        return sdata==null ? null:decryptHex(sdata,KEY);
    }

    public static String encryptHex(String data) throws Exception {
        return data==null ? null:encryptHex(data,KEY);
    }

    public static String decryptHex(String sdata, String key) throws Exception {
        SecretKey deskey = new SecretKeySpec(new BASE64Decoder().decodeBuffer(key), Algorithm);
        //解密
        Cipher c1 = Cipher.getInstance(Algorithm);
        c1.init(Cipher.DECRYPT_MODE, deskey);
        return new String(c1.doFinal(HexUtil.fromHex(sdata.toLowerCase())));
    }

    public static String encryptHex(String data, String key) throws Exception {
        SecretKey deskey = new SecretKeySpec(new BASE64Decoder().decodeBuffer(key), Algorithm);
        //加密
        Cipher c1 = Cipher.getInstance(Algorithm);
        c1.init(Cipher.ENCRYPT_MODE, deskey);
        return HexUtil.toHexString(c1.doFinal(data.getBytes())).toUpperCase();
    }


    public static String encryptMode(String sdata) throws Exception {
        return encryptMode(sdata, vipkey);
    }

    public static String decryptMode(String sdata) throws Exception {
        return decryptMode(sdata, vipkey);
    }
    /**
     * 加密数据
     * @param data	需要加密的数据
     * @param skey	密钥
     * @return
     */
    public static String encryptMode(String data, String skey) throws Exception{

        byte[] key = new BASE64Decoder().decodeBuffer(skey);
        byte[] datas = data.getBytes("UTF-8");

        Key deskey = null;

        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(Algorithm);

        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance("desede/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, deskey);
        byte[] bOut = cipher.doFinal(datas);
        return new BASE64Encoder().encode(bOut);

    }

    /**
     * 解密需要的数据
     * @param sdata	需要解密的数据
     * @param skey	密钥
     * @return
     */
    public static String decryptMode(String sdata, String skey) throws Exception{

        byte[] key = new BASE64Decoder().decodeBuffer(skey);
        byte[] data = new BASE64Decoder().decodeBuffer(sdata);

        Key deskey = null;

        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(Algorithm);

        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/ECB/PKCS5Padding");

        cipher.init(Cipher.DECRYPT_MODE, deskey);
        byte[] bOut = cipher.doFinal(data);
        return new String(bOut, "UTF-8");
    }

}
