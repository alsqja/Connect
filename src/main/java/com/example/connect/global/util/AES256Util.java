package com.example.connect.global.util;

import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.BadRequestException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class AES256Util {
    @Value("${aes256.secret}")
    private String secretKey;

    @Value("${aes256.iv}")
    private String iv;

    public String decryptAES(String encryptedText) {
        try {
            byte[] encryptSecret = Base64.getDecoder().decode(secretKey);
            byte[] encryptIv = Base64.getDecoder().decode(iv);

            SecretKey key = new SecretKeySpec(encryptSecret, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(encryptIv);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

            byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);

            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }
    }

    public String encrypt(String data) {
        try {
            byte[] encryptSecret = Base64.getDecoder().decode(secretKey);
            byte[] encryptIv = Base64.getDecoder().decode(iv);

            SecretKey key = new SecretKeySpec(encryptSecret, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(encryptIv);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

            byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }
    }

    public <T> T objectMapping(String stringData, Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(stringData);

            return objectMapper.convertValue(jsonNode, clazz);
        } catch (Exception e) {
            throw new RuntimeException("복호화 실패", e);
        }
    }
}
