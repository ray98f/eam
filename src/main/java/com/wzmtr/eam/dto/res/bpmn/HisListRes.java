package com.wzmtr.eam.dto.res.bpmn;

import lombok.Data;

/**
 * @Author lize
 * @Date 2023/8/25
 */
@Data
public class HisListRes {
    /**
     *    procId
     */
    private String procId;
    /**
     *    标题
     */
    private String procDefName;
    /**
     *    开始时间
     */
    private String createTime;


}
