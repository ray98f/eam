package com.wzmtr.eam.enums;

/**
 * 错误枚举类
 */
public enum HttpCode {

    // 成功
    OK(200, "SUCCESS"),
    // 没有登录
    NO_LOGIN(0, "用户未登录"),
    // 参数错误
    PARAM_FAIL(401, "参数有误"),
    // 服务有误
    SERVER_FAIL(402, "服务异常"),
    // 更新数据失败
    UP_DATA_FAIL(505, "更新数据失败"),

    // 未知异常
    UN_KNOW_ERROR(400, "出现系统未知错误"),
    INSERT_ERROR(506, "插入数据失败"),
    INSERT_DATA_REPEAT(506, "插入数据重复"),
    UPDATE_ERROR(507,"修改数据失败"),
    DELETE_ERROR(508,"删除数据失败"),
    CHECK_ERROR(509,"该数据已存在"),
    ROLE_ERROR(510,"用户无权限"),
    NO_RESULT(610,"该数据不存在"),
    FILE_PARSE_FAIL(401, "文件解析异常"),
    START_TIME_ERROR(511,"起始时间不能为空"),
    END_TIME_ERROR(511,"结束时间不能为空"),

    UP_DATA_FAIL_STATUS(10001,"专属状态修改失败，请排查问题"),
    UP_DATA_FAIL_ALARM(10002,"告警状态修改失败，请排查问题"),
    UP_DATA_FAIL_STATUS_TOO(10003,"状态修改失败，请排查问题"),

    ZABBIX_INSERT_ERROR(506,"数据库数据新增失败 请手动删除zabbix中的数据 防止数据不一致"),

    ZABBIX_UPDATE_ERROR(505,"数据库数据更新失败 请手动删除zabbix中的数据 防止数据不一致"),

    ;


    private int code;

    private String msg;


    HttpCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
