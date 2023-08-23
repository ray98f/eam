package com.wzmtr.eam.dto.result;

import com.wzmtr.eam.enums.HttpCode;
import lombok.Data;

@Data
public class CodeException extends RuntimeException {

    private int code;


    public CodeException(int code, String message) {
        super(message);
        this.code = code;
    }

    public CodeException(HttpCode httpStatus) {
        super(httpStatus.getMsg());
        this.code = httpStatus.getCode();
    }

    public CodeException(HttpCode httpStatus, String message) {
        super(message.equals("") ? httpStatus.getMsg() : message);
        this.code = httpStatus.getCode();
    }


}
