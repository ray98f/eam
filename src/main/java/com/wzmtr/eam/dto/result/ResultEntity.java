package com.wzmtr.eam.dto.result;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wzmtr.eam.enums.HttpCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultEntity<T> {

    @JsonProperty("code")
    @JsonAlias({"Code"})
    @JSONField(name = "code", alternateNames = {"Code"})
    private int code;


    @JsonProperty("msg")
    @JsonAlias({"msg", "Message", "Msg"})
    @JSONField(name = "msg", alternateNames = {"Message", "msg", "Msg"})
    private String msg;


    @JsonAlias({"Data", "data"})
    @JsonProperty("data")
    @JSONField(name = "data", alternateNames = {"Data", "data"})
    private T data;

    @JsonProperty("time")
    @JsonAlias({"Time"})
    private long time = System.currentTimeMillis();

    @JsonProperty("id")
    @JSONField(name = "id", alternateNames = {"Id"})
    private Long id;


    public static ResultEntity<Void> ok() {
        return ok(null);
    }

    public static ResultEntity<Void> noContent() {
        return ok(null);
    }


    public static <T> ResultEntity<T> ok(T data) {
        ResultEntity<T> entity = new ResultEntity<T>();
        entity.setCode(HttpCode.OK.getCode());
        entity.setMsg(HttpCode.OK.getMsg());
        entity.setData(data);
        return entity;
    }

    public static <T> ResultEntity<T> ok(int code, String message, T data) {
        ResultEntity<T> entity = new ResultEntity<T>();
        entity.setCode(code);
        entity.setMsg(message);
        entity.setData(data);
        return entity;
    }

    public static <T> ResultEntity<T> ok(int code, String message) {
        ResultEntity<T> entity = new ResultEntity<T>();
        entity.setCode(code);
        entity.setMsg(message);
        return entity;
    }

    public static <T> ResultEntity<T> msg(BaseHttpDTO dto) {
        ResultEntity<T> entity = new ResultEntity<T>();
        entity.setCode(dto.getCode());
        entity.setMsg(dto.getMsg());
        entity.setData((T) dto.getData());
        return entity;
    }


    public static ResultEntity fail(ResultEntity result) {
        result.data = null;
        return result;
    }


    @JsonIgnore
    @JSONField(serialize = false)
    public boolean isSuccess() {
        return HttpCode.OK.getCode() == code;
    }

    @JsonIgnore
    @JSONField(serialize = false)
    public void isSuccessThrow() {
        if (HttpCode.OK.getCode() != code) {
            throw new CodeException(code, msg);
        }
    }

    @JsonIgnore
    @JSONField(serialize = false)
    public void isSuccessThrow(Function<String, RuntimeException> function) {
        if (HttpCode.OK.getCode() != code) {
            throw function.apply(msg);
        }
    }

    public static ResultEntity<Void> fail(HttpCode httpCode, Throwable e) {
        ResultEntity<Void> entity = new ResultEntity<Void>();
        entity.setCode(httpCode.getCode());
        entity.setMsg(e == null ? httpCode.getMsg() : e.getMessage());
        return entity;
    }


    public static ResultEntity<Void> fail(HttpCode httpCode) {
        return fail(httpCode, null);
    }


    public static ResultEntity<Void> fail(Exception e) {
        return fail(HttpCode.UN_KNOW_ERROR, e);
    }

    public static ResultEntity<Void> fail(Throwable e) {
        if (e instanceof CodeException) {
            return fail(((CodeException) e));
        }
        return fail(HttpCode.UN_KNOW_ERROR, e);
    }

    public static ResultEntity<Void> fail(CodeException e) {
        ResultEntity<Void> entity = new ResultEntity<Void>();
        entity.setCode(e.getCode());
        entity.setMsg(e.getMessage());
        return entity;
    }

    public static ResultEntity<Void> fail(CommonException e) {
        ResultEntity<Void> entity = new ResultEntity<Void>();
        entity.setCode(e.getCode());
        entity.setMsg(e.getMessage());
        return entity;
    }


}
