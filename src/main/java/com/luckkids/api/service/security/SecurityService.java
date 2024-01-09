package com.luckkids.api.service.security;

import com.luckkids.jwt.dto.LoginUserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;

@Service
public class SecurityService {

    private static final String ALG = "AES/CBC/PKCS5Padding";
    private final String iv;

    public SecurityService(@Value("${aes.key-value}") String keyValue) {
        this.iv = keyValue.substring(0, 16);
    }

    public LoginUserInfo getCurrentLoginUserInfo() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof LoginUserInfo) {
            return (LoginUserInfo) principal;
        }

        throw new RuntimeException("Unknown principal type: " + principal.getClass().getName());
    }

    public String encrypt(String text) {
        try {
            Cipher cipher = createCipher(ENCRYPT_MODE);
            byte[] encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().withoutPadding().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred during encryption: " + e.getMessage(), e);
        }
    }

    public String decrypt(String cipherText) {
        try {
            Cipher cipher = createCipher(DECRYPT_MODE);
            byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
            byte[] decrypted = cipher.doFinal(decodedBytes);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred during decryption: " + e.getMessage(), e);
        }
    }

    private Cipher createCipher(int cipherMode) throws Exception {
        Cipher cipher = Cipher.getInstance(ALG);
        SecretKeySpec keySpec = new SecretKeySpec(iv.getBytes(StandardCharsets.UTF_8), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
        cipher.init(cipherMode, keySpec, ivParamSpec);
        return cipher;
    }
}
