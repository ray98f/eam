package com.wzmtr.eam.dto.res.statistic;

import lombok.Data;

/**
 * 故障详情导出列表
 * @author  Ray
 * @version 1.0
 * @date 2024/03/04
 */
@Data
public class RamsTrainReliabilityResDTO {
    /**
     * 统计周期累计运营里程
     */
    private Double totalMile;
    /**
     * 3分钟或以上延误百万车公里次数-实际指标
     */
    private Double realDelay;
    /**
     * 3分钟或以上延误百万车公里次数-合同指标
     */
    private Double contractDelay;
    /**
     * 3分钟或以上延误百万车公里次数-是否达标
     */
    private Integer isDelayCompliance;
    /**
     * 不适合继续服务/未能出车百万车公里次数-实际指标
     */
    private Double realNot;
    /**
     * 不适合继续服务/未能出车百万车公里次数-合同指标
     */
    private Double contractNot;
    /**
     * 不适合继续服务/未能出车百万车公里次数-是否达标
     */
    private Integer isNotCompliance;
    /**
     * 碎修及列检故障百万车公里次数-实际指标
     */
    private Double realFault;
    /**
     * 碎修及列检故障百万车公里次数-合同指标
     */
    private Double contractFault;
    /**
     * 碎修及列检故障百万车公里次数-是否达标
     */
    private Integer isFaultCompliance;
}