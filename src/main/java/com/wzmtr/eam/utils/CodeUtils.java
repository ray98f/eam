package com.wzmtr.eam.utils;

public class CodeUtils {

    public static String getNextCode(String code, Integer num) {
        String prefix = code.substring(0, num);
        long suffix = Long.parseLong(code.substring(num));
        suffix += 1;
        return prefix + String.format("%0" + (code.length() - 1) +"d", suffix);
    }
}
