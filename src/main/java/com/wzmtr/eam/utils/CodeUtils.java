package com.wzmtr.eam.utils;

import cn.hutool.extra.qrcode.QrConfig;

import java.awt.*;

public class CodeUtils {

    public static String getNextCode(String code) {
        String prefix = code.substring(0, 1);
        long suffix = Long.parseLong(code.substring(1));
        suffix += 1;
        return prefix + String.format("%0" + (code.length() - 1) +"d", suffix);
    }
}
