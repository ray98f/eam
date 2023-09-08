package com.wzmtr.eam.dto.req.bpmn;

import com.wzmtr.eam.entity.PageReqDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author lize
 * @Date 2023/8/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExamineListReq extends PageReqDTO {
    private String startTime;
    private String endTime;

}
