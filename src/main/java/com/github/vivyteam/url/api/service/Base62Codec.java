package com.github.vivyteam.url.api.service;

import org.springframework.stereotype.Component;

@Component
public class Base62Codec {
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final int BASE = CHARS.length();

    public String encode(long num) {
        StringBuilder encoded = new StringBuilder();
        while (num > 0) {
            encoded.append(CHARS.charAt((int) (num % BASE)));
            num /= BASE;
        }
        return encoded.length() > 0 ? encoded.reverse().toString() : CHARS.substring(0, 1);
    }

    public long decode(String str) {
        long decoded = 0;
        for (int i = 0; i < str.length(); i++) {
            decoded = decoded * BASE + CHARS.indexOf(str.charAt(i));
        }
        return decoded;
    }
}
