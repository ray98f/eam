package com.wzmtr.eam.dto.res.statistic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 10:27
 */
@Data
public class FailureRateDetailResDTO {
    ReliabilityDetailResDTO vehicleRate;
    ReliabilityDetailResDTO signalRate;
    ReliabilityDetailResDTO powerRate;
    ReliabilityDetailResDTO PSDrate;
    ReliabilityDetailResDTO exitingRate;
}