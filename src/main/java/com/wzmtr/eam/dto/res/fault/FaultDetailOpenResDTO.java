package com.wzmtr.eam.dto.res.fault;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 故障工单详情开放结果类
 * @author  Ray
 * @version 1.0
 * @date 2024/06/18
 */
@Data
public class FaultDetailOpenResDTO {
    /**
     * 故障编号
     */
    @ApiModelProperty(value = "故障编号")
    private String faultNo;
    /**
     * 故障工单号
     */
    @ApiModelProperty(value = "故障工单号")
    private String faultWorkNo;
    /**
     * 报告时间
     */
    @ApiModelProperty(value = "报告时间")
    private String reportTime;
    /**
     * 维修负责人工号
     */
    @ApiModelProperty(value = "维修负责人工号")
    private String repairRespUserId;
    /**
     * 工作区域
     */
    @ApiModelProperty(value = "工作区域")
    private String workArea;
    /**
     * 工单状态 0草稿;10提报;20下发;30派工;50完工;55验收;60完工确认;70关闭;99作废
     */
    @ApiModelProperty(value = "工单状态 0草稿;10提报;20下发;30派工;50完工;55验收;60完工确认;70关闭;99作废")
    private String orderStatus;
    /**
     * 到达现场时间
     */
    @ApiModelProperty(value = "到达现场时间")
    private String arrivalTime;
    /**
     * 维修开始时间
     */
    @ApiModelProperty(value = "维修开始时间")
    private String repairStartTime;
    /**
     * 维修结束时间
     */
    @ApiModelProperty(value = "维修结束时间")
    private String repairEndTime;
    /**
     * 故障原因码
     */
    @ApiModelProperty(value = "故障原因码")
    private String faultReasonCode;
    /**
     * 故障原因详情
     */
    @ApiModelProperty(value = "故障原因详情")
    private String faultReasonDetail;
    /**
     * 故障行动码
     */
    @ApiModelProperty(value = "故障行动码")
    private String faultActionCode;
    /**
     * 故障处理详情
     */
    @ApiModelProperty(value = "故障处理详情")
    private String faultActionDetail;
    /**
     * 故障处理结果（10-已处理；20-未处理）
     */
    @ApiModelProperty(value = "故障处理结果（10-已处理；20-未处理）")
    private String faultProcessResult;
    /**
     * 维修负责人
     */
    @ApiModelProperty(value = "维修负责人")
    private String repairRespUserName;
    /**
     * 维修负责人电话
     */
    @ApiModelProperty(value = "维修负责人电话")
    private String repairRespUserMobile;
    /**
     * 对象编码
     */
    @ApiModelProperty(value = "对象编码")
    private String objectCode;
    /**
     * 对象名称
     */
    @ApiModelProperty(value = "对象名称")
    private String objectName;
    /**
     * 车底号/车厢号
     */
    @ApiModelProperty(value = "车底号/车厢号")
    private String trainTrunk;
    /**
     * 线路名称
     */
    @ApiModelProperty(value = "线路名称")
    private String lineName;
    /**
     * 位置1
     */
    @ApiModelProperty(value = "位置1")
    private String positionName;
    /**
     * 位置2
     */
    @ApiModelProperty(value = "位置2")
    private String position2Name;
    /**
     * 专业
     */
    @ApiModelProperty(value = "专业")
    private String majorName;
    /**
     * 系统
     */
    @ApiModelProperty(value = "系统")
    private String systemName;
    /**
     * 设备分类
     */
    @ApiModelProperty(value = "设备分类")
    private String equipTypeName;
    /**
     * 故障模块
     */
    @ApiModelProperty(value = "故障模块")
    private String faultModule;
    /**
     * 故障模块
     */
    @ApiModelProperty(value = "故障模块")
    private String faultModuleId;
    /**
     * 故障分类（10-运营故障；20-自检故障；30-新线调试；40-正线故障；50-出库故障）
     */
    @ApiModelProperty(value = "故障分类（10-运营故障；20-自检故障；30-新线调试；40-正线故障；50-出库故障）")
    private String faultType;
}
