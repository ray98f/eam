package com.wzmtr.eam.soft.csm.planWork.vo;

import lombok.Data;

/**
 * description:
 *
 * @author zhangxin
 * @version 1.0
 * @date 2022/4/20 11:03
 */
@Data
public class ResponseMessage {
    String content;
    ErrorMessage errorMessage;
    String state;
}
