package com.wzmtr.eam.utils;

import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;

import java.util.Collection;
import java.util.Map;

/**
 * 断言类，为方便异常抛出并提高代码可读性，不满足条件则throw new CommonException
 * Author: Li.Wang
 * Date: 2023/11/22 9:05
 */
public class Assert {

    // ********************** 不为空值  **********************

    /**
     * String a = notnull
     * String b = null
     * a不为null,条件为真，不抛出异常,并返回对象
     * Assert.notNull(a，ErrorCode.NORMAL_ERROR);
     * 由于b为null 条件为假，抛出ErrorCode.NORMAL_ERROR
     * Assert.notNull(b,ErrorCode.NORMAL_ERROR);
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static final <T> T notNull(T obj) {
        return notNull(obj, ErrorCode.NORMAL_ERROR);
    }

    public static final <T> T notNull(T obj, String reason) {
        return notNull(obj, ErrorCode.NORMAL_ERROR, reason);
    }

    public static final <T> T notNull(T obj, ErrorCode errcode) {
        if (null == obj) {
            throw new CommonException(errcode);
        }
        return obj;
    }

    public static final <T> T notNull(T obj, ErrorCode errcode, String reason) {
        if (null == obj) {
            throw new CommonException(errcode, reason);
        }
        return obj;
    }

    public static final <T> T notNull(T obj, ErrorCode errcode, Throwable cause) {
        if (null == obj) {
            throw new CommonException(errcode, cause);
        }
        return obj;
    }

    public static final <T> T notNull(T obj, ErrorCode errcode, Throwable cause, String reason) {
        if (null == obj) {
            throw new CommonException(errcode, cause, reason);
        }
        return obj;
    }

    // ********************** 为空值  **********************

    public static final <T> void isNull(T obj) {
        isNull(obj, ErrorCode.NORMAL_ERROR);
    }

    public static final <T> void isNull(T obj, String reason) {
        isNull(obj, ErrorCode.NORMAL_ERROR, reason);
    }

    public static final <T> void isNull(T obj, ErrorCode errcode) {
        if (null != obj) {
            throw new CommonException(errcode);
        }
    }

    public static final <T> void isNull(T obj, ErrorCode errcode, String reason) {
        if (null != obj) {
            throw new CommonException(errcode, reason);
        }
    }

    public static final <T> void isNull(T obj, ErrorCode errcode, Throwable cause) {
        if (null != obj) {
            throw new CommonException(errcode, cause);
        }
    }

    public static final <T> void isNull(T obj, ErrorCode errcode, Throwable cause, String reason) {
        if (null != obj) {
            throw new CommonException(errcode, cause, reason);
        }
    }

    // ********************** 字符串不为空 **********************

    public static final String isNotEmpty(String text) {
        return isNotEmpty(text, ErrorCode.NORMAL_ERROR);
    }

    public static final String isNotEmpty(String text, String reason) {
        return isNotEmpty(text, ErrorCode.NORMAL_ERROR, reason);
    }

    public static final String isNotEmpty(String text, ErrorCode errcode) {
        if (!StringUtils.isNotEmpty(text)) {
            throw new CommonException(errcode);
        }
        return text;
    }

    public static final String isNotEmpty(String text, ErrorCode errcode, String reason) {
        if (!StringUtils.isNotEmpty(text)) {
            throw new CommonException(errcode, reason);
        }
        return text;
    }

    public static final String isNotEmpty(String text, ErrorCode errcode, Throwable cause) {
        if (!StringUtils.isNotEmpty(text)) {
            throw new CommonException(errcode, cause);
        }
        return text;
    }

    public static final String isNotEmpty(String text, ErrorCode errcode, Throwable cause, String reason) {
        if (!StringUtils.isNotEmpty(text)) {
            throw new CommonException(errcode, cause, reason);
        }
        return text;
    }

    // ********************** 字符串为空 **********************

    public static final String isEmpty(String text) {
        return isEmpty(text, ErrorCode.NORMAL_ERROR);
    }

    public static final String isEmpty(String text, String reason) {
        return isEmpty(text, ErrorCode.NORMAL_ERROR, reason);
    }

    public static final String isEmpty(String text, ErrorCode errcode) {
        if (StringUtils.isNotEmpty(text)) {
            throw new CommonException(errcode);
        }
        return text;
    }

    public static final String isEmpty(String text, ErrorCode errcode, String reason) {
        if (StringUtils.isNotEmpty(text)) {
            throw new CommonException(errcode, reason);
        }
        return text;
    }

    public static final String isEmpty(String text, ErrorCode errcode, Throwable cause) {
        if (StringUtils.isNotEmpty(text)) {
            throw new CommonException(errcode, cause);
        }
        return text;
    }

    public static final String isEmpty(String text, ErrorCode errcode, Throwable cause, String reason) {
        if (StringUtils.isNotEmpty(text)) {
            throw new CommonException(errcode, cause, reason);
        }
        return text;
    }

    // ********************** 为真  **********************

    public static final boolean isTrue(boolean expression) {
        return isTrue(expression, ErrorCode.NORMAL_ERROR);
    }

    public static final boolean isTrue(boolean expression, String reason) {
        return isTrue(expression, ErrorCode.NORMAL_ERROR, reason);
    }

    public static final boolean isTrue(boolean expression, ErrorCode errcode) {
        if (!expression) {
            throw new CommonException(errcode);
        }
        return expression;
    }

    public static final boolean isTrue(boolean expression, ErrorCode errcode, String reason) {
        if (!expression) {
            throw new CommonException(errcode, reason);
        }
        return expression;
    }

    public static final boolean isTrue(boolean expression, ErrorCode errcode, Throwable cause) {
        if (!expression) {
            throw new CommonException(errcode, cause);
        }
        return expression;
    }

    public static final boolean isTrue(boolean expression, ErrorCode errcode, Throwable cause, String reason) {
        if (!expression) {
            throw new CommonException(errcode, cause, reason);
        }
        return expression;
    }

    // ********************** 为假  **********************

    public static final boolean isFalse(boolean expression) {
        return isFalse(expression, ErrorCode.NORMAL_ERROR);
    }

    public static final boolean isFalse(boolean expression, String reason) {
        return isFalse(expression, ErrorCode.NORMAL_ERROR, reason);
    }

    public static final boolean isFalse(boolean expression, ErrorCode errcode) {
        if (expression) {
            throw new CommonException(errcode);
        }
        return expression;
    }

    public static final boolean isFalse(boolean expression, ErrorCode errcode, String reason) {
        if (expression) {
            throw new CommonException(errcode, reason);
        }
        return expression;
    }

    public static final boolean isFalse(boolean expression, ErrorCode errcode, Throwable cause) {
        if (expression) {
            throw new CommonException(errcode, cause);
        }
        return expression;
    }

    public static final boolean isFalse(boolean expression, ErrorCode errcode, Throwable cause, String reason) {
        if (expression) {
            throw new CommonException(errcode, cause, reason);
        }
        return expression;
    }

    // ********************** 列表不为空或者null  **********************

    public static final <T> Collection<T> notEmpty(Collection<T> collection) {
        return notEmpty(collection, ErrorCode.NORMAL_ERROR);
    }

    public static final <T> Collection<T> notEmpty(Collection<T> collection, String reason) {
        return notEmpty(collection, ErrorCode.NORMAL_ERROR, reason);
    }

    public static final <T> Collection<T> notEmpty(Collection<T> collection, ErrorCode errcode) {
        if (StringUtils.isEmpty(collection)) {
            throw new CommonException(errcode);
        }
        return collection;
    }

    public static final <T> Collection<T> notEmpty(Collection<T> collection, ErrorCode errcode, String reason) {
        if (StringUtils.isEmpty(collection)) {
            throw new CommonException(errcode, reason);
        }
        return collection;
    }

    public static final <T> Collection<T> notEmpty(Collection<T> collection, ErrorCode errcode, Throwable cause) {
        if (StringUtils.isEmpty(collection)) {
            throw new CommonException(errcode, cause);
        }
        return collection;
    }

    public static final <T> Collection<T> notEmpty(Collection<T> collection, ErrorCode errcode, Throwable cause, String reason) {
        if (StringUtils.isEmpty(collection)) {
            throw new CommonException(errcode, cause, reason);
        }
        return collection;
    }

    // ********************** 列表为空或者null  **********************

    public static final void isEmpty(Collection<?> collection) {
        isEmpty(collection, ErrorCode.NORMAL_ERROR);
    }

    public static final void isEmpty(Collection<?> collection, String reason) {
        isEmpty(collection, ErrorCode.NORMAL_ERROR, reason);
    }

    public static final void isEmpty(Collection<?> collection, ErrorCode errcode) {
        if (!StringUtils.isEmpty(collection)) {
            throw new CommonException(errcode);
        }
    }

    public static final void isEmpty(Collection<?> collection, ErrorCode errcode, String reason) {
        if (!StringUtils.isEmpty(collection)) {
            throw new CommonException(errcode, reason);
        }
    }

    public static final void isEmpty(Collection<?> collection, ErrorCode errcode, Throwable cause) {
        if (!StringUtils.isEmpty(collection)) {
            throw new CommonException(errcode, cause);
        }
    }

    public static final void isEmpty(Collection<?> collection, ErrorCode errcode, Throwable cause, String reason) {
        if (!StringUtils.isEmpty(collection)) {
            throw new CommonException(errcode, cause, reason);
        }
    }

    // ********************** map不为空或者null  **********************

    public static final <K, V> Map<K, V> notEmpty(Map<K, V> map) {
        return notEmpty(map, ErrorCode.NORMAL_ERROR);
    }

    public static final <K, V> Map<K, V> notEmpty(Map<K, V> map, String reason) {
        return notEmpty(map, ErrorCode.NORMAL_ERROR, reason);
    }

    public static final <K, V> Map<K, V> notEmpty(Map<K, V> map, ErrorCode errcode) {
        if (StringUtils.isEmpty(map)) {
            throw new CommonException(errcode);
        }
        return map;
    }

    public static final <K, V> Map<K, V> notEmpty(Map<K, V> map, ErrorCode errcode, String reason) {
        if (StringUtils.isEmpty(map)) {
            throw new CommonException(errcode, reason);
        }
        return map;
    }

    public static final <K, V> Map<K, V> notEmpty(Map<K, V> map, ErrorCode errcode, Throwable cause) {
        if (StringUtils.isEmpty(map)) {
            throw new CommonException(errcode, cause);
        }
        return map;
    }

    public static final <K, V> Map<K, V> notEmpty(Map<K, V> map, ErrorCode errcode, Throwable cause, String reason) {
        if (StringUtils.isEmpty(map)) {
            throw new CommonException(errcode, cause, reason);
        }
        return map;
    }


}
