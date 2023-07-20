package com.wzmtr.eam.entity;

import lombok.Data;

/**
 * @author zhanghongquan
 * @data 2022/7/6 16:56
 * @describe
 */
@Data
public class HttpResult {

    private static Integer successCode = 200;

    private static Integer failCode = 0;

    private static Boolean successStatus= true;

    private static String successMessage= "success";

    private Integer code;


    private String message;

    private Object data;

    public static HttpResult success(){
        HttpResult httpResult = new HttpResult();
        httpResult.setCode(successCode);
        httpResult.setMessage(successMessage);
        return httpResult;
    }

    public static HttpResult success(Object o){
        HttpResult httpResult = new HttpResult();
        httpResult.setCode(successCode);
        httpResult.setMessage(successMessage);
        httpResult.setData(o);
        return httpResult;
    }


//    public static HashMap<String, Object> successList(List o){
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("code",successCode);
//        map.put("message",successMessage);
//        map.put("data",o);
//        return map;
//    }



    public static HttpResult fail( String message){
        HttpResult httpResult = new HttpResult();
        httpResult.setCode(failCode);
        httpResult.setMessage(message);
        return httpResult;
    }





}
