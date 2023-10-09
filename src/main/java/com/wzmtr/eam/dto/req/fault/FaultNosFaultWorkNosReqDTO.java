package com.wzmtr.eam.dto.req.fault;

import com.wzmtr.eam.enums.OrderStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Author: Li.Wang
 * Date: 2023/8/15 17:18
 */
@Data
@ApiModel
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FaultNosFaultWorkNosReqDTO {
    @ApiModelProperty(value = "故障编号s")
    private Set<String> faultNos;
    @ApiModelProperty(value = "故障工单编号s")
    private Set<String> faultWorkNos;
    private OrderStatus type;
}
