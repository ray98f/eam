package com.wzmtr.eam.utils;

import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanMap;

/**
 * bean工具类
 * @author  Ray
 * @version 1.0
 * @date 2023/06/12
 */
@Slf4j
public class BeanUtils {

    /**
     * @return T
     * @MethodName: copy
     * @Description: 属性拷贝，将from中的属性全部拷贝至to
     */
    public static <T, F> T copy(F from, T to) {
        try {
            BeanMap.create(to).putAll(BeanMap.create(from));
            return to;
        } catch (Exception e) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "class " + from.getClass().getCanonicalName() + " can not copy to class " + to.getClass().getCanonicalName());
        }
    }

    /**
     * @MethodName: convert
     * @Description: 对象转换，将from转换为to
     */
    public static <T, F> T convert(F from, Class<T> clazz) {
        try {
            T to = clazz.newInstance();
            BeanMap.create(to).putAll(BeanMap.create(from));
            return to;
        } catch (Exception e) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "class " + from.getClass().getCanonicalName() + " can not convert to class " + clazz.getCanonicalName());
        }
    }
}
