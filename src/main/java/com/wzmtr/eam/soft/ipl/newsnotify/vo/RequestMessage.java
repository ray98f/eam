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
public class RequestMessage {
    String verb;
    String noun;
    User user;
    Message message;
}
