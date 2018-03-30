package com.honvay.cola.cloud.framework.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * <ul>
 * <li>BASE64的加密解密是双向的，可以求反解。</li>
 * <li>MD5、SHA以及HMAC是单向加密，任何数据加密后只会产生唯一的一个加密串，通常用来校验数据在传输过程中是否被修改。</li>
 * <li>HMAC算法有一个密钥，增强了数据传输过程中的安全性，强化了算法外的不可控因素。</li>
 * <li>DES DES-Data Encryption Standard,即数据加密算法。 DES算法的入口参数有三个:Key、Data、Mode。
 * <ul>
 * <li>Key:8个字节共64位,是DES算法的工作密钥;</li>
 * <li>Data:8个字节64位,是要被加密或被解密的数据;</li>
 * <li>Mode:DES的工作方式,有两种:加密或解密。</li>
 * </ul>
 * </li>
 * <ul>
 */
public class CryptUtils {

    private static final Logger log = LoggerFactory.getLogger(CryptUtils.class);

    private static final String KEY_MD5 = "MD5";
    private static final String KEY_SHA = "SHA";
    /**
     * MAC算法可选以下多种算法
     * <p>
     * <pre>
     *
     * HmacMD5
     * HmacSHA1
     * HmacSHA256
     * HmacSHA384
     * HmacSHA512
     * </pre>
     */
    private static final String KEY_MAC = "HmacMD5";

    private static String KEY_VALUE = "";

    static {
        try {
            KEY_VALUE = initKey("v7Pg5fiYwek");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * BASE64解密
     */
    private static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    /**
     * BASE64 加密
     */
    private static String encryptBASE64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    /**
     * MD5加密
     */
    private static byte[] encryptMD5(byte[] data) throws Exception {

        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
        md5.update(data);

        return md5.digest();

    }

    /**
     * MD5加密
     */
    public static String encryptMD5(String data) throws Exception {
        //加密后的字符串
        String result = new String(DigestUtils.md5Hex(data));
        return result;
    }

    /**
     * SHA加密
     */
    private static byte[] encryptSHA(byte[] data) throws Exception {

        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        sha.update(data);

        return sha.digest();

    }

    /**
     * 初始化HMAC密钥
     */
    private static String initMacKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC);
        SecretKey secretKey = keyGenerator.generateKey();
        return encryptBASE64(secretKey.getEncoded());
    }

    /**
     * HMAC 加密
     */
    private static byte[] encryptHMAC(byte[] data, String key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(decryptBASE64(key), KEY_MAC);
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        return mac.doFinal(data);
    }

    /**
     * DES 算法 <br> 可替换为以下任意一种算法，同时key值的size相应改变。 <p>
     * <pre>
     * DES                  key size must be equal to 56
     * DESede(TripleDES)    key size must be equal to 112 or 168
     * AES                  key size must be equal to 128, 192 or 256,but 192 and 256 bits may not be
     * available
     * Blowfish             key size must be multiple of 8, and can only range from 32 to 448
     * (inclusive)
     * RC2                  key size must be between 40 and 1024 bits
     * RC4(ARCFOUR)         key size must be between 40 and 1024 bits
     * </pre>
     */
    private static final String ALGORITHM = "DES";

    /**
     * DES 算法转换密钥<br>
     */
    private static Key toKey(byte[] key) throws Exception {
        SecretKey secretKey = null;
        if (ALGORITHM.equals("DES") || ALGORITHM.equals("DESede")) {
            DESKeySpec dks = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            secretKey = keyFactory.generateSecret(dks);
        } else {
            // 当使用其他对称加密算法时，如AES、Blowfish等算法时，用下述代码替换上述三行代码
            secretKey = new SecretKeySpec(key, ALGORITHM);
        }
        return secretKey;
    }

    /**
     * DES 算法解密
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return 解密后的数据
     */
    public static byte[] decrypt(byte[] data, String key) throws Exception {
        Key k = toKey(decryptBASE64(key));
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, k);
        return cipher.doFinal(data);
    }

    /**
     * 解密数据
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return 解密后的数据
     */
    public static String decrypt(String data, String key) throws Exception {
        // 执行解密操作
        return new String(decrypt(Base64.decodeBase64(data), key), "UTF-8");
    }

    /**
     * DES 算法加密
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return 加密后的数据
     */
    public static byte[] encrypt(byte[] data, String key) throws Exception {
        Key k = toKey(decryptBASE64(key));
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, k);
        return cipher.doFinal(data);
    }

    /**
     * 加密数据
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return 加密后的数据
     */
    public static String encrypt(String data, String key) throws Exception {
        return Base64.encodeBase64String(encrypt(data.getBytes("UTF-8"), key));
    }

    /**
     * DES 算法生成密钥
     */
    private static String initKey() throws Exception {
        return initKey(null);
    }

    /**
     * DES 算法生成密钥
     */
    public static String initKey(String seed) throws Exception {
        SecureRandom secureRandom = null;
        if (seed != null) {
            secureRandom = new SecureRandom(decryptBASE64(seed));
        } else {
            secureRandom = new SecureRandom();
        }
        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM);
        kg.init(secureRandom);
        SecretKey secretKey = kg.generateKey();
        return encryptBASE64(secretKey.getEncoded());
    }

    public static String encryptHMAC(String password) {
        String result = null;
        if (!StringUtils.isEmpty(password)) {
            try {
                byte[] c = encryptHMAC(password.getBytes(), initMacKey());
                result = new BigInteger(c).toString(16);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String encrypt(String password) {
        String result = null;
        try {
            byte[] c = encrypt(password.getBytes(), KEY_VALUE);
            result = new BigInteger(c).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        try {
            String s = "{}";
            String b = CryptUtils.encryptBASE64(s.getBytes("UTF-8"));
            //log.info("BASE64加密后:" + b);
            byte[] c = CryptUtils.decryptBASE64(b);
            //log.info("BASE64解密后:" + new String(c, "UTF-8"));

            //c = encryptMD5(s.getBytes());
            //log.info("MD5   加密后:" + new BigInteger(c).toString(16));
            //
            //c = encryptSHA(s.getBytes());
            //log.info("SHA   加密后:" + new BigInteger(c).toString(16));
            //
            String key = initMacKey();
            //log.info("HMAC密匙:" + key);
            //c = encryptHMAC(s.getBytes(), key);
            //log.info("HMAC  加密后:" + new BigInteger(c).toString(16));
            //
            //log.info("111111111111HMAC  加密后:" + encryptHMAC("123"));

            key = initKey();
            System.out.println(ALGORITHM + "密钥:\t" + key);
            String en = encrypt(s, key);
            System.out.println(ALGORITHM + " String   加密后:" + en);

            String de = decrypt(en, key);
            System.out.println(ALGORITHM + " String  解密后:" + de);
            //log.info(ALGORITHM + "密钥:\t" + key);
            c = encrypt(s.getBytes("UTF-8"), key);
            System.out.println(ALGORITHM + "   加密后:" + new BigInteger(c).toString(16));
            //log.info(ALGORITHM + "   加密后:" + new BigInteger(c).toString(16));
            c = decrypt(c, key);
            System.out.println(ALGORITHM + "   解密后:" + new String(c, "UTF-8"));
            //log.info(ALGORITHM + "   解密后:" + new String(c, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}