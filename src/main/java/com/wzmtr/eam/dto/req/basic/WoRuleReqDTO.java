package com.wzmtr.eam.dto.req.basic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class WoRuleReqDTO {

    @ApiModelProperty(value = "id")
    private String recId;

    @ApiModelProperty(value = "规则编号")
    private String ruleCode;

    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    @ApiModelProperty(value = "用途 10 通用 20 点巡检 30 检修")
    private String ruleUseage;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "记录状态 10 无效 20 有效")
    private String recStatus;

    @ApiModelProperty(value = "创建人")
    private String recCreator;

    @ApiModelProperty(value = "创建时间")
    private String recCreateTime;

    @ApiModelProperty(value = "修改人")
    private String recRevisor;

    @ApiModelProperty(value = "修改时间")
    private String recReviseTime;

    @Data
    public static class WoRuleDetail {
        @ApiModelProperty(value = "id")
        private String recId;

        @ApiModelProperty(value = "规则编号")
        private String ruleCode;

        @ApiModelProperty(value = "规则明细名称")
        private String ruleDetalName;

        @ApiModelProperty(value = "起始日期")
        private String startDate;

        @ApiModelProperty(value = "结束日期")
        private String endDate;

        @ApiModelProperty(value = "时间周期（小时）")
        private Long period;

        @ApiModelProperty(value = "里程周期（公里）")
        private String ext1;

        @ApiModelProperty(value = "提前小时数")
        private Long beforeTime;

        @ApiModelProperty(value = "提前公里数")
        private String ext2;

        @ApiModelProperty(value = "规则排序")
        private Integer ruleSort;

        @ApiModelProperty(value = "备注")
        private String remark;

        @ApiModelProperty(value = "创建人")
        private String recCreator;

        @ApiModelProperty(value = "创建时间")
        private String recCreateTime;

        @ApiModelProperty(value = "修改人")
        private String recRevisor;

        @ApiModelProperty(value = "修改时间")
        private String recReviseTime;
    }
}
