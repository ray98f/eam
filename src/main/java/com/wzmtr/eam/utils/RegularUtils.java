package com.wzmtr.eam.utils;

import java.util.regex.Pattern;

/**
 * @author Ray
 * 正则方法类
 */
public class RegularUtils {

    private static final Pattern NUMBER_PATTERN = Pattern.compile("[0-9]*");

    /**
     * 数字判断
     * @return Pattern
     */
    public static Pattern getNumberPattern() {
        return NUMBER_PATTERN;
    }

}