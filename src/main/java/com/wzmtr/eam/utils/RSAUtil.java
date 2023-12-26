package com.wzmtr.eam.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class RSAUtil {


    public static final String RSA = "RSA";
    /**
     * PKCS1
     */
    public static final String ECB_PADDING = "RSA/ECB/PKCS1Padding";
    public static final String PUBLIC_KEY = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJriKRcr8gLRcxksjG6caUtn+K440SMvdzWMerlru4MH+ErA8HFJKEroziIZrb913P4VXu00qTZFIl/AD0VsFT8CAwEAAQ==";
    public static final String PRIVATE_KEY = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAmuIpFyvyAtFzGSyMbpxpS2f4rjjRIy93NYx6uWu7gwf4SsDwcUkoSujOIhmtv3Xc/hVe7TSpNkUiX8APRWwVPwIDAQABAkBJeZSoq25Jq/cAMEQGjSjeXtp4O/fqyy+wNY5avCLeS4IRGSqeyabMKiXnGl31QCgQ++LK/Uv5cg7RewHZQU/RAiEA1+LaUkfyU5oBhnNYCKiJPddWiHr8AbAFF5dLbwfNXpUCIQC3qZD8IUznYlNHV80uMQJZWWcDlCD7QnuW4f/WkUkzgwIgVJiXdqCsy6fQqy/tsk7goLQOO6L9t2eTR0BJFfQXvNUCIHUTqbGfxLdHLZEv/kKwyS+N1yYn2jJxOfl/zafI66HjAiEAnZd78b8FbPMHDicGDpSk/eigjqaUh2Wu1WKfr0Ex7FM=";

    /**
     * 密钥位数
     */
    private static final int KEY_SIZE = 512;
    private static final int RESERVE_BYTES = 11;
    private static final int DECRYPT_BLOCK = KEY_SIZE / 8;
    private static final int ENCRYPT_BLOCK = DECRYPT_BLOCK - RESERVE_BYTES;

    /**
     * 随机生成RSA密钥对
     *
     * @param keySize 密钥长度，范围：512-2048,一般2048
     */
    public static KeyPair generateKeyPair(int keySize) {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA);
            kpg.initialize(keySize);
            return kpg.genKeyPair();
        } catch (Exception e) {
            log.error("generateKeyPair exception.", e);
            return null;
        }
    }

    /**
     * 用公钥对字符串进行加密
     *
     * @param data 原文
     */
    public static byte[] encryptWithPublicKey(byte[] data, byte[] key) throws Exception {
        Cipher cp = Cipher.getInstance(ECB_PADDING);
        cp.init(Cipher.ENCRYPT_MODE, getPublicKey(key));
        return cp.doFinal(data);
    }

    /**
     * 用公钥对字符串进行加密
     *
     * @param plain
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String encryptWithPublicKey(String plain, String publicKey) throws Exception {
        byte[] key = Base64.decodeBase64(publicKey);
        byte[] data = plain.getBytes(StandardCharsets.UTF_8);
        return Base64.encodeBase64String(encryptWithPublicKey(data, key));
    }

    /**
     * 公钥解密
     *
     * @param data 待解密数据
     * @param key  密钥
     */
    public static byte[] decryptWithPublicKey(byte[] data, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance(ECB_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, getPublicKey(key));
        return cipher.doFinal(data);
    }

    /**
     * 私钥加密
     *
     * @param data 待加密数据
     * @param key  密钥
     */
    public static byte[] encryptWithPrivateKey(byte[] data, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance(ECB_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, getPrivateKey(key));
        return cipher.doFinal(data);
    }

    /**
     * 私钥解密
     *
     * @param data 待解密数据
     * @param key  密钥
     */
    public static byte[] decryptWithPrivateKey(byte[] data, byte[] key) throws Exception {
        Cipher cp = Cipher.getInstance(ECB_PADDING);
        cp.init(Cipher.DECRYPT_MODE, getPrivateKey(key));
        return cp.doFinal(data);
    }

    /**
     * 用私钥对字符串进行解密
     *
     * @param plain
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String decryptWithPrivateKey(String plain, String publicKey) throws Exception {
        byte[] key = Base64.decodeBase64(publicKey);
        byte[] data = plain.getBytes(StandardCharsets.UTF_8);
        return Base64.encodeBase64String(decryptWithPrivateKey(data, key));
    }

    public static PublicKey getPublicKey(byte[] key) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return keyFactory.generatePublic(keySpec);
    }

    public static PrivateKey getPrivateKey(byte[] key) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 公钥分块加密
     *
     * @param plain
     * @param publicKey
     */
    public static String encryptWithPublicKeyBlock(String plain, String publicKey) throws Exception {
        byte[] data = plain.getBytes(StandardCharsets.UTF_8);
        byte[] key = Base64.decodeBase64(publicKey);
        byte[] result = encryptWithPublicKeyBlock(data, key);
        return Base64.encodeBase64String(result);
    }

    /**
     * 私钥分块解密
     *
     * @param plain
     * @param privateKey
     * @return
     */
    public static String decryptWithPrivateKeyBlock(String plain, String privateKey) {
        try {
            byte[] data = Base64.decodeBase64(plain.getBytes(StandardCharsets.UTF_8));
            byte[] key = Base64.decodeBase64(privateKey);
            byte[] bytes = decryptWithPrivateKeyBlock(data, key);
            return new String(bytes);
        } catch (Exception e) {
            log.error("decryptWithPrivateKeyBlock exception.", e);
        }
        return "";
    }

    /**
     * 私钥分块解密
     *
     * @param plain
     * @param publicKey
     * @return
     */
    public static String decryptWithPublicKeyBlock(String plain, String publicKey) {
        try {
            byte[] data = Base64.decodeBase64(plain.getBytes(StandardCharsets.UTF_8));
            byte[] key = Base64.decodeBase64(publicKey);
            byte[] bytes = decryptWithPublicKey(data, key);
            return new String(bytes);
        } catch (Exception e) {
            log.error("decryptWithPublicKeyBlock exception.", e);
        }
        return "";
    }

    public static void main(String[] args) throws Exception {
        System.out.println("公钥：" + PUBLIC_KEY);
        System.out.println("私钥：" + PRIVATE_KEY);
        String s = "7b9172640fe7be97c0ed48378abf6460,Uly1*0%sQ,1618452245";
        String s1 = RSAUtil.encryptWithPublicKeyBlock(s, PUBLIC_KEY);
        System.out.println("加密后：" + s1);
        System.out.println("解密后：" + decryptWithPrivateKeyBlock(s1, PRIVATE_KEY));

    }

    public static Map<Integer, String> genKeyPair() {
        // 用于封装随机产生的公钥与私钥
        Map<Integer, String> keyMap = new HashMap<>();
        try {
            // 生成一个密钥对，保存在keyPair中
            KeyPair keyPair = generateKeyPair(KEY_SIZE);
            // 得到私钥
            RSAPrivateKey privateKey = (RSAPrivateKey) Objects.requireNonNull(keyPair).getPrivate();
            // 得到公钥
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            // 得到公钥字符串
            String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
            // 得到私钥字符串
            String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
            // 将公钥和私钥保存到Map
            // 0表示公钥
            keyMap.put(0, publicKeyString);
            // 1表示私钥
            keyMap.put(1, privateKeyString);
        } catch (Exception e) {
            log.info("生成公钥私钥异常：" + e.getMessage());
            return null;
        }
        return keyMap;
    }

    /**
     * 分块加密
     *
     * @param data
     * @param key
     */
    public static byte[] encryptWithPublicKeyBlock(byte[] data, byte[] key) throws Exception {
        int blockCount = (data.length / ENCRYPT_BLOCK);
        if ((data.length % ENCRYPT_BLOCK) != 0) {
            blockCount += 1;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream(blockCount * ENCRYPT_BLOCK);
        Cipher cipher = Cipher.getInstance(ECB_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(key));
        for (int offset = 0; offset < data.length; offset += ENCRYPT_BLOCK) {
            int inputLen = (data.length - offset);
            if (inputLen > ENCRYPT_BLOCK) {
                inputLen = ENCRYPT_BLOCK;
            }
            byte[] encryptedBlock = cipher.doFinal(data, offset, inputLen);
            bos.write(encryptedBlock);
        }
        bos.close();
        return bos.toByteArray();
    }

    /**
     * 分块加密
     *
     * @param data
     * @param key
     */
    public static byte[] encryptWithPrivateKeyBlock(byte[] data, byte[] key) throws Exception {
        int blockCount = (data.length / ENCRYPT_BLOCK);
        if ((data.length % ENCRYPT_BLOCK) != 0) {
            blockCount += 1;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream(blockCount * ENCRYPT_BLOCK);
        Cipher cipher = Cipher.getInstance(ECB_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, getPrivateKey(key));
        for (int offset = 0; offset < data.length; offset += ENCRYPT_BLOCK) {
            int inputLen = (data.length - offset);
            if (inputLen > ENCRYPT_BLOCK) {
                inputLen = ENCRYPT_BLOCK;
            }
            byte[] encryptedBlock = cipher.doFinal(data, offset, inputLen);
            bos.write(encryptedBlock);
        }
        bos.close();
        return bos.toByteArray();
    }

    /**
     * 分块解密
     *
     * @param data
     * @param key
     */
    public static byte[] decryptWithPublicKeyBlock(byte[] data, byte[] key) throws Exception {
        int blockCount = (data.length / DECRYPT_BLOCK);
        if ((data.length % DECRYPT_BLOCK) != 0) {
            blockCount += 1;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream(blockCount * DECRYPT_BLOCK);
        Cipher cipher = Cipher.getInstance(ECB_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, getPublicKey(key));
        for (int offset = 0; offset < data.length; offset += DECRYPT_BLOCK) {
            int inputLen = (data.length - offset);
            if (inputLen > DECRYPT_BLOCK) {
                inputLen = DECRYPT_BLOCK;
            }
            byte[] decryptedBlock = cipher.doFinal(data, offset, inputLen);
            bos.write(decryptedBlock);
        }
        bos.close();
        return bos.toByteArray();
    }

    /**
     * 分块解密
     *
     * @param data
     * @param key
     */
    public static byte[] decryptWithPrivateKeyBlock(byte[] data, byte[] key) throws Exception {
        int blockCount = (data.length / DECRYPT_BLOCK);
        if ((data.length % DECRYPT_BLOCK) != 0) {
            blockCount += 1;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream(blockCount * DECRYPT_BLOCK);
        Cipher cipher = Cipher.getInstance(ECB_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(key));
        for (int offset = 0; offset < data.length; offset += DECRYPT_BLOCK) {
            int inputLen = (data.length - offset);

            if (inputLen > DECRYPT_BLOCK) {
                inputLen = DECRYPT_BLOCK;
            }
            byte[] decryptedBlock = cipher.doFinal(data, offset, inputLen);
            bos.write(decryptedBlock);
        }
        bos.close();
        return bos.toByteArray();
    }
}