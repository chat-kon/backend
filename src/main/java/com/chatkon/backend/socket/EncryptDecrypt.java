package com.chatkon.backend.socket;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class EncryptDecrypt {

    private static final String SECRET_KEY_1 = "ssdkF$HUy2A#D%kd";
    private static final String SECRET_KEY_2 = "weJiSEvR5yAC5ftB";

    private final IvParameterSpec ivParameterSpec;
    private final SecretKeySpec secretKeySpec;
    private final Cipher cipher;

    public EncryptDecrypt() throws Exception {
        ivParameterSpec = new IvParameterSpec(SECRET_KEY_1.getBytes(StandardCharsets.UTF_8));
        secretKeySpec = new SecretKeySpec(SECRET_KEY_2.getBytes(StandardCharsets.UTF_8), "AES");
        cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    }

    public String encrypt(String toBeEncrypt) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(toBeEncrypt.getBytes());
        return Base64.encodeBase64String(encrypted);
    }

    public String decrypt(String encrypted) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decryptedBytes = cipher.doFinal(Base64.decodeBase64(encrypted));
        return new String(decryptedBytes);
    }
}