package com.github.miho73.legacy.utils;

import com.github.miho73.legacy.exception.LegacyException;
import org.springframework.stereotype.Component;

@Component
public class Operations {
    public static byte[] XOR(byte[] a, byte[] b) throws LegacyException {
        if(a.length != b.length) throw new LegacyException("Two array must have same length");
        int len = a.length;
        byte[] ret = new byte[len];
        for(int i=0; i<len; i++) {
            ret[i] = (byte)(a[i]^b[i]);
        }
        return ret;
    }
}