package com.wzmtr.eam.utils;

import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 编号生成工具类
 * @author  Ray
 * @version 1.0
 * @date 2024/06/12
 */
@Component
public class CodeUtils {

    /**
     * redis key前缀
     */
    private static String keyPrefix;
    @Value("${spring.redis.key-prefix}")
    public void setKeyPrefix(String keyPrefix) {
        CodeUtils.keyPrefix = keyPrefix;
    }

    private static StringRedisTemplate stringRedisTemplate;

    @Autowired
    public void redisTemplate(StringRedisTemplate stringRedisTemplate){
        CodeUtils.stringRedisTemplate = stringRedisTemplate;
    }

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

    /**
     * 获取递增code
     * @param code code值
     * @param head 头部
     * @return 递增code
     */
    public static String getNextCode(String code, String head) {
        if (StringUtils.isEmpty(code) || !(CommonConstants.TWENTY_STRING + code.substring(CommonConstants.TWO, CommonConstants.EIGHT)).equals(DateUtils.getNoDate())) {
            code = head + DateUtils.getNoDate().substring(2) + "0001";
        } else {
            code = CodeUtils.getNextCode(code, 8);
        }
        return code;
    }

    /**
     * 故障编号生成
     * @return 最新的故障编号
     */
    public static String generateFaultNo() {
        String key = keyPrefix + CommonConstants.S1 + CommonConstants.FAULT_NO;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(CommonConstants.FAULT_NO_PREFIX);
        // 获取当前日期
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.YYYYMMDD);
        String currentDate = dateFormat.format(new Date());
        // 截取日期格式为yyMMdd
        stringBuilder.append(currentDate.substring(CommonConstants.TWO));

        // 获取流水号
        Long increment = stringRedisTemplate.opsForValue().increment(key, 1);

        /*
          返回过期时间，单位为秒。
          如果返回-2，则表示该键不存在；
          如果返回-1，则表示该键没有设置过期时间；
         */
        Long expire = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
        if (StringUtils.isNull(expire) || expire == CommonConstants.NEGATIVE_TWO) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "redis faultNo 流水号异常");
        }
        if (expire == CommonConstants.NEGATIVE_ONE) {
            // 获取距离当天结束的秒数
            LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);
            long secondsToMidnight = LocalDateTime.now().until(endOfDay, ChronoUnit.SECONDS);
            //初始设置过期时间
            stringRedisTemplate.expire(key, secondsToMidnight, TimeUnit.SECONDS);
        }
        String format = String.format("%04d", increment);
        stringBuilder.append(format);
        return stringBuilder.toString();
    }

    /**
     * 故障工单编号生成
     * @return 最新的故障工单编号
     */
    public static String generateFaultWorkNo() {
        String key = keyPrefix + CommonConstants.S1 + CommonConstants.FAULT_WORK_NO;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(CommonConstants.FAULT_WORK_NO_PREFIX);
        // 获取当前日期
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.YYYYMMDD);
        String currentDate = dateFormat.format(new Date());
        // 截取日期格式为yyMMdd
        stringBuilder.append(currentDate.substring(CommonConstants.TWO));

        // 获取流水号
        Long increment = stringRedisTemplate.opsForValue().increment(key, 1);

        /*
          返回过期时间，单位为秒。
          如果返回-2，则表示该键不存在；
          如果返回-1，则表示该键没有设置过期时间；
         */
        Long expire = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
        if (StringUtils.isNull(expire) || expire == CommonConstants.NEGATIVE_TWO) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "redis faultWorkNo 流水号异常");
        }
        if (expire == CommonConstants.NEGATIVE_ONE) {
            // 获取距离当天结束的秒数
            LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);
            long secondsToMidnight = LocalDateTime.now().until(endOfDay, ChronoUnit.SECONDS);
            //初始设置过期时间
            stringRedisTemplate.expire(key, secondsToMidnight, TimeUnit.SECONDS);
        }
        String format = String.format("%04d", increment);
        stringBuilder.append(format);
        return stringBuilder.toString();
    }

}
