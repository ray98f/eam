package com.wzmtr.eam.soft.ipl.newsnotify.vo;

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
    String state;
    String content;
    ErrorMessage errorMessage;
}
