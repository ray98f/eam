package com.wzmtr.eam.dto.result;

import lombok.Data;

@Data
public class BaseHttpDTO {

    private String msg;
    private Integer code;
    private String data;
}
