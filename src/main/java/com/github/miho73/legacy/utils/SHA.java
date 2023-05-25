package com.github.miho73.legacy.utils;

import com.github.miho73.legacy.exception.LegacyException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class SHA {

    public String SHA256(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.reset();
        md.update(data);
        String preHash = Base64.getEncoder().encodeToString(md.digest());
        preHash = preHash.replace('/', '-');
        preHash = preHash.replace('+', '_');
        return preHash;
    }
}