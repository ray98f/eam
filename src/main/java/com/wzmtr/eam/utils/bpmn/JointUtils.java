package com.wzmtr.eam.utils.bpmn;


import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @Author lize
 * @Date 2023/3/29
 */
public class JointUtils {

    /**
     * 传入实体类，拼接成指定字符串
     *
     * @param object
     */
    public static StringBuilder jointEntity(Object object, Integer pageNum, Integer pageSize, Integer limit) {
        Field[] fields = object.getClass().getDeclaredFields();
        StringBuilder val = new StringBuilder("?");
        for (Field field : fields) {
            String name = field.getName();
            String value;
            try {
                Field declared = object.getClass().getDeclaredField(name);
                //抑制java语言访问检查，反射访问private访问权限的属性值
                declared.setAccessible(true);
                value = "null".equals(String.valueOf(declared.get(object))) ? "" : String.valueOf(declared.get(object));
                val.append(name).append("=").append(value).append("&");
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        val.append("page=").append(pageNum).append("&pageNum=").append(pageNum).append("&pageSize=").append(pageSize).append("&limit=").append(limit);
        return val;
    }

    /**
     * 传入实体类，拼接成指定字符串
     *
     * @param object
     */
    public static StringBuilder jointEntity(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        StringBuilder val = new StringBuilder("?");
        for (Field field : fields) {
            String name = field.getName();
            String value;
            try {
                Field declared = object.getClass().getDeclaredField(name);
                //抑制java语言访问检查，反射访问private访问权限的属性值
                declared.setAccessible(true);
                value = "null".equals(String.valueOf(declared.get(object))) ? "" : String.valueOf(declared.get(object));
                val.append(name).append("=").append(URLEncoder.encode(value)).append("&");
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        val.deleteCharAt(val.length() - 1);

        return val;
    }
}
