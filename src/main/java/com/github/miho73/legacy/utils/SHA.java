package com.github.miho73.legacy.utils;

import com.github.miho73.legacy.exception.LegacyException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class SHA {

    final int HASH_REPEAT = 123;

    public String SHA512(String msg, String salt) throws NoSuchAlgorithmException, LegacyException {
        byte[] saltByte = Base64.getDecoder().decode(salt);
        return doHash(msg, saltByte);
    }
    public String SHA512(String msg, byte[] salt) throws NoSuchAlgorithmException, LegacyException {
        return doHash(msg, salt);
    }
    public String MD5(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.reset();
        md.update(data);
        String preHash = Base64.getEncoder().encodeToString(md.digest());
        preHash = preHash.replace('/', '-');
        preHash = preHash.replace('+', '_');
        return preHash;
    }

    private String doHash(String msg, byte[] saltByte) throws NoSuchAlgorithmException, LegacyException {
        byte[] msgByte = msg.getBytes(StandardCharsets.UTF_8);

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.reset();
        byte[] nutrient;
        for(int i=0; i<HASH_REPEAT; i++) {
            if(i==0) nutrient = msgByte;
            else nutrient = Operations.XOR(msgByte, saltByte);
            md.update(nutrient);
            msgByte=md.digest();
        }
        return Base64.getEncoder().encodeToString(msgByte);
    }
}