package com.example.demo.util;

import java.util.UUID;

public final class TokenGenerator {

    private TokenGenerator() {
    }

    public static String generate() {
        return "TKN-" +
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 8)
                        .toUpperCase();
    }
}
