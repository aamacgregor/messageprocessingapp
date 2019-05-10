package com.aamacgregor.messageprocessor.utils;

import java.util.Optional;

public class StringUtils {

    public static Optional<String> emptyToNull(String str) {
        return str == null || str.isEmpty() ? Optional.empty() : Optional.of(str);
    }
}
