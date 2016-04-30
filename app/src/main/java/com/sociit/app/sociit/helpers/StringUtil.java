package com.sociit.app.sociit.helpers;

/**
 * Created by Manuel on 29/04/2016.
 */
public class StringUtil {
    public StringUtil() {
    }

    public static boolean isNullOrWhitespace(String string) {
        return string == null || string.isEmpty() || string.trim().isEmpty();
    }
}
