package com.luckkids.api.service.security;

import com.luckkids.jwt.dto.UserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class SecurityService {

    public static final String alg = "AES/CBC/PKCS5Padding";
    private final String iv;

    public SecurityService(@Value("${aes.key-value}") String keyValue) {
        this.iv = keyValue.substring(0, 16);
    }

    public String encrypt(String text) {
        try {
            Cipher cipher = Cipher.getInstance(alg);
            SecretKeySpec keySpec = new SecretKeySpec(iv.getBytes(), "AES");
            IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);

            byte[] encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().withoutPadding().encodeToString(encrypted);
        } catch (Exception e){
            throw new RuntimeException("암호화 처리중 에러가 발생했습니다.");
        }
    }

    public String decrypt(String cipherText) {
        try {
            Cipher cipher = Cipher.getInstance(alg);
            SecretKeySpec keySpec = new SecretKeySpec(iv.getBytes(), "AES");
            IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

            byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
            byte[] decrypted = cipher.doFinal(decodedBytes);
            return new String(decrypted, StandardCharsets.UTF_8);
        }catch (Exception e){
            throw new RuntimeException("복호화 처리중 에러가 발생했습니다.");
        }
    }

    public UserInfo getCurrentUserInfo() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserInfo) {
            return (UserInfo) principal;
        }

        throw new RuntimeException("Unknown principal type: " + principal.getClass().getName());
    }
}
