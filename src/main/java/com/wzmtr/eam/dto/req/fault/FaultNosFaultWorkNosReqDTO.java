package com.wzmtr.eam.dto.req.fault;

import com.wzmtr.eam.enums.OrderStatus;
import io.swagger.annotations.ApiModel;
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
    private Set<String> faultNos;
    private Set<String> faultWorkNos;
    private OrderStatus type;
}
