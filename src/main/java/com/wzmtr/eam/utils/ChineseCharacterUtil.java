package com.wzmtr.eam.utils;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 汉字转拼音工具类
 * @author  Ray
 * @version 1.0
 * @date 2024/06/12
 */
@Slf4j
public class ChineseCharacterUtil {

    /**
     * 获取汉字首字母或全拼大写字母
     * @param chinese 汉字
     * @param isFull  是否全拼 true:表示全拼 false表示：首字母
     * @return String 全拼或者首字母大写字符窜
     */
    public static String getUpperCase(String chinese, boolean isFull) {
        return convertHanzi2Pinyin(chinese, isFull).toUpperCase();
    }

    /**
     * 获取汉字首字母或全拼小写字母
     * @param chinese 汉字
     * @param isFull  是否全拼 true:表示全拼 false表示：首字母
     * @return String 全拼或者首字母小写字符窜
     */
    public static String getLowerCase(String chinese, boolean isFull) {
        return convertHanzi2Pinyin(chinese, isFull).toLowerCase();
    }

    /**
     * 将汉字转成拼音，取首字母或全拼
     * @param chinese  汉字字符串
     * @param isFull 是否全拼 true:表示全拼 false表示：首字母
     * @return String 拼音
     */
    private static String convertHanzi2Pinyin(String chinese, boolean isFull) {
        // ^[\u2E80-\u9FFF]+$ 匹配所有东亚区的语言
        // ^[\u4E00-\u9FFF]+$ 匹配简体和繁体
        // ^[\u4E00-\u9FA5]+$ 匹配简体
        String regExp = "^[\u4E00-\u9FFF]+$";
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isEmpty(chinese.trim())) {
            return "";
        }
        String pinyin = "";
        for (int i = 0; i < chinese.length(); i++) {
            char unit = chinese.charAt(i);
            //是汉字，则转拼音
            if (match(String.valueOf(unit), regExp)) {
                pinyin = convertSingleChinese2Pinyin(unit);
                if (isFull) {
                    sb.append(pinyin);
                } else {
                    sb.append(pinyin.charAt(0));
                }
            } else {
                sb.append(unit);
            }
        }
        return sb.toString();
    }

    /**
     * 单个汉字转成拼音
     * @param chinese 汉字字符
     * @return String 拼音
     */
    private static String convertSingleChinese2Pinyin(char chinese) {
        HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
        outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        String[] res;
        StringBuilder sb = new StringBuilder();
        try {
            res = PinyinHelper.toHanyuPinyinStringArray(chinese, outputFormat);
            //对于多音字，只用第一个拼音
            sb.append(res[0]);
        } catch (Exception e) {
            log.error("exception message", e);
            return "";
        }
        return sb.toString();
    }

    /***
     * 匹配，根据字符和正则表达式进行匹配
     * @param str 源字符串
     * @param regex 正则表达式
     * @return true：匹配成功  false：匹配失败
     */
    private static boolean match(String str, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

}

