package com.wzmtr.eam.dto.result;

import com.wzmtr.eam.enums.HttpCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommonException extends RuntimeException {

    private Integer code;

    private String message;

    private String[] params;

    public CommonException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public CommonException(HttpCode httpCode) {
        this.code = httpCode.getCode();
        this.message = httpCode.getMsg();
    }

    public CommonException(HttpCode httpCode, String... params) {
        this.code = httpCode.getCode();
        this.message = httpCode.getMsg();
        this.params = params;
    }

}
