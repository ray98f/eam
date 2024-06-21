package com.wzmtr.eam.dto.res.equipment;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 车辆里程统计开放类
 * @author  Ray
 * @version 1.0
 * @date 2024/06/05
 */
@Data
@ApiModel
public class SumDailyMileResDTO {
    /**
     * 当天运营里程总和
     */
    private BigDecimal sumDailyWorkMile;
    /**
     * 当天总里程总和
     */
    private BigDecimal sumDailyMile;
}
