package com.wzmtr.eam.utils;

import java.util.regex.Pattern;

/**
 * @author Ray
 * 正则方法类
 */
public class RegularUtils {

    private static final Pattern NUMBER_PATTERN = Pattern.compile("[0-9]*");

    private static final Pattern DECIMAL_PATTERN = Pattern.compile("[0-9]*\\.?[0-9]+");

    /**
     * 数字判断
     * @return Pattern
     */
    public static Pattern getNumberPattern() {
        return NUMBER_PATTERN;
    }

    /**
     * 小数判断
     * @return Pattern
     */
    public static Pattern getDecimalPattern() {
        return DECIMAL_PATTERN;
    }

}