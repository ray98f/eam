package com.wzmtr.eam.utils;

import java.util.regex.Pattern;

/**
 * 正则方法类
 * @author  Ray
 * @version 1.0
 * @date 2024/06/12
 */
public class RegularUtils {

    /**
     * 数字判断
     * @param num 数字
     * @return 是否有效
     */
    public static boolean isValidNumber(String num) {
        return Pattern.matches("[0-9]*", num);
    }

    /**
     * 小数判断
     * @param decimal 小数
     * @return 是否有效
     */
    public static boolean isValidDecimal(String decimal) {
        return Pattern.matches("[0-9]*\\.?[0-9]+", decimal);
    }

    /**
     * 判断手机号是否有效
     * @param phone 手机号
     * @return 是否有效
     */
    public static boolean isValidPhoneNumber(String phone) {
        return Pattern.matches("^1[3|4|5|6|7|8|9][0-9]\\d{8}$", phone);
    }

    /**
     * 判断邮箱是否有效
     * @param email 邮箱
     * @return 是否有效
     */
    public static boolean isValidEmail(String email) {
        return Pattern.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", email);
    }

    /**
     * 判断密码是否有效
     * 密码必须包含大小写字母和数字的组合，不能使用特殊字符，长度在6-20之间
     * @param password 密码
     * @return 是否有效
     */
    public static boolean isValidPassword(String password) {
        return Pattern.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$", password);
    }

    /**
     * 判断姓名是否有效，验证字符串是否由2-4个汉字组成
     * @param name 姓名
     * @return 是否有效
     */
    public static boolean isValidName(String name) {
        return Pattern.matches("^[\\u4e00-\\u9fa5]{2,4}$", name);
    }

    /**
     * 判断身份证号是否有效
     * '^':表示匹配字符串的开头
     * '[1-9]':表示匹配1-9之间的数字，确保身份证号码的前6位不为0
     * '\\d{5}':表示匹配5位数字，确保身份证号码的前6位为数字，用于匹配地区码
     * '(18|19|([23]\\d))':表示匹配18或19或20-23之间的数字，确保身份证号码的前2位为18或19或20-23之间的数字，用于匹配年份
     * '\\d{2}':表示接下来的2位是月份，范围是01-12之间的数字
     *  '((0[1-9])|(1[0-2]))':表示匹配月份，范围是01-12之间的数字
     *  '(([0-2][1-9])|10|20|30|31)':表示日期，前两位0-2表示01-29，10、20、30、31分别单独列出
     *  '\\d{3}':表示匹配3位数字，确保身份证号码的第18位为数字，用于匹配顺序码
     * '[0-9Xx]':表示匹配0-9或X或x，确保身份证号码的第17位为数字或X或x，用于匹配校验位
     * @param idCard 身份证
     * @return 是否有效
     */
    public static boolean isValidIdCard(String idCard) {
        return Pattern.matches("^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$", idCard);
    }

    /**
     * 判断车牌号是否有效
     * 用于验证车牌号码的格式是否符合中国大陆的车牌号规则
     * @param carNumber 车牌号码
     * @return 是否有效
     */
    public static boolean isValidCarNumber(String carNumber) {
        return Pattern.matches("^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$", carNumber);
    }

    /**
     * 判断银行卡号是否有效
     * 用于验证银行卡号的格式是否符合中国银行卡号的规则
     * @param bankCardNumber 银行卡号
     * @return 是否有效
     */
    public static boolean isValidBankCardNumber(String bankCardNumber) {
        return Pattern.matches("^\\d{16,19}$", bankCardNumber);
    }

    /**
     * 判断IP地址是否有效
     * @param ipAddress IP地址
     * @return 是否有效
     */
    public static boolean isValidIpAddress(String ipAddress) {
        return Pattern.matches("^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$", ipAddress);
    }

    /**
     * 判断邮政编码是否有效
     * @param postalCode 邮政编码
     * @return 是否有效
     */
    public static boolean isValidPostalCode(String postalCode) {
        return Pattern.matches("^[1-9]\\d{5}$", postalCode);
    }

    public static void main(String[] args) {
        System.out.println(isValidNumber("1"));
        System.out.println(isValidNumber("-1"));
        System.out.println(isValidDecimal("1"));
        System.out.println(isValidDecimal("-1.1"));
        System.out.println(isValidDecimal("1.111"));
    }

}