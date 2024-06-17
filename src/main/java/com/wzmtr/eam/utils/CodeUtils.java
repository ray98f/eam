package com.wzmtr.eam.utils;

import com.wzmtr.eam.constant.CommonConstants;

/**
 * 编号生成工具类
 * @author  Ray
 * @version 1.0
 * @date 2024/06/12
 */
public class CodeUtils {

    /**
     * 根据code 获取递增code
     * @param code code
     * @param codeNum code前缀长度
     * @return 递增code
     */
    public static String getNextCode(String code, Integer codeNum) {
        String prefix = code.substring(0, codeNum);
        long suffix = Long.parseLong(code.substring(codeNum));
        suffix += 1;
        return prefix + String.format("%0" + (code.length() - codeNum) + "d", suffix);
    }

    /**
     * 根据code及递增长度 获取递增code
     * @param code code
     * @param codeNum code前缀长度
     * @param addNum 递增的长度
     * @return 递增code
     */
    public static String getNextCodeByAddNum(String code, Integer codeNum, Integer addNum) {
        String prefix = code.substring(0, codeNum);
        long suffix = Long.parseLong(code.substring(codeNum));
        suffix += addNum;
        return prefix + String.format("%0" + (code.length() - codeNum) + "d", suffix);
    }

    public static String getNextCode(String code, String head) {
        if (StringUtils.isEmpty(code) || !(CommonConstants.TWENTY_STRING + code.substring(CommonConstants.TWO, CommonConstants.EIGHT)).equals(DateUtils.getNoDate())) {
            code = head + DateUtils.getNoDate().substring(2) + "0001";
        } else {
            code = CodeUtils.getNextCode(code, 8);
        }
        return code;
    }

}
