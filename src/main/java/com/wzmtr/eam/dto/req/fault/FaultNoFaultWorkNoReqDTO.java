package com.wzmtr.eam.dto.req.fault;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: Li.Wang
 * Date: 2023/8/15 17:18
 */
@Data
@ApiModel
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FaultNoFaultWorkNoReqDTO {
    private String faultNo;
    private String faultWorkNo;
}
