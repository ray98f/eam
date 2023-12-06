package com.wzmtr.eam.utils;

import com.wzmtr.eam.constant.CommonConstants;

import java.text.SimpleDateFormat;

public class CodeUtils {

    public static String getNextCode(String code, Integer num) {
        String prefix = code.substring(0, num);
        long suffix = Long.parseLong(code.substring(num));
        suffix += 1;
        return prefix + String.format("%0" + (code.length() - num) +"d", suffix);
    }

    public static String getNextCode(String code,String head) {
        SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd");
        if (StringUtils.isEmpty(code) || !(CommonConstants.TWENTY_STRING + code.substring(CommonConstants.TWO, CommonConstants.EIGHT)).equals(day.format(System.currentTimeMillis()))) {
            code = head + day.format(System.currentTimeMillis()).substring(2) + "0001";
        } else {
            code = CodeUtils.getNextCode(code, 8);
        }
        return code;
    }

    public static void main(String[] args) {
        System.out.println(getNextCode("YH23080912345","YH"));
    }


}
