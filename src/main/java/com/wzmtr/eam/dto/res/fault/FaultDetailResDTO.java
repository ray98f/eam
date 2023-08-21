package com.wzmtr.eam.dto.res.fault;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/15 9:31
 */
@Data
public class FaultDetailResDTO {
    @ApiModelProperty(value = "故障编号")
    private String faultNo;
    @ApiModelProperty(value = "故障工单号")
    private String faultWorkNo;
    @ApiModelProperty(value = "报告人工号")
    private String reportUserId;
    @ApiModelProperty(value = "验收人")
    private String reportUserName;
    @ApiModelProperty(value = "报告时间")
    private String reportTime;
    @ApiModelProperty(value = "工班")
    private String workClass;
    @ApiModelProperty(value = "派工人工号")
    private String dispatchUserId;
    @ApiModelProperty(value = "派工时间")
    private String dispatchTime;
    @ApiModelProperty(value = "维修负责人工号")
    private String repairRespUserId;
    @ApiModelProperty(value = "工作区域")
    private String workArea;
    @ApiModelProperty(value = "工单状态")
    private String orderStatus;
    @ApiModelProperty(value = "维调号")
    private String repairDispatchNo;
    @ApiModelProperty(value = "抢修号")
    private String rushRepairNo;
    @ApiModelProperty(value = "到达现场时间")
    private String arrivalTime;
    @ApiModelProperty(value = "维修开始时间")
    private String repairStartTime;
    @ApiModelProperty(value = "维修结束时间")
    private String repairEndTime;
    @ApiModelProperty(value = "耗时（单位：小时")
    private Float repairTime = 0.0F;
    @ApiModelProperty(value = "离开现场时间")
    private String leaveTime;
    @ApiModelProperty(value = "故障原因码")
    private String faultReasonCode;
    @ApiModelProperty(value = "故障原因详情")
    private String faultReasonDetail;
    @ApiModelProperty(value = "故障行动码")
    private String faultActionCode;
    @ApiModelProperty(value = "故障处理详情")
    private String faultActionDetail;
    @ApiModelProperty(value = "故障处理结果（10-已处理；20-未处理）")
    private String faultProcessResult;
    @ApiModelProperty(value = "开工人工号")
    private String reportStartUserId;
    @ApiModelProperty(value = "开工时间")
    private String reportStartTime;
    @ApiModelProperty(value = "完工人工号")
    private String reportFinishUserId;
    @ApiModelProperty(value = "完工时间")
    private String reportFinishTime;
    @ApiModelProperty(value = "验收人")
    private String checkUserId;
    @ApiModelProperty(value = "验收时间")
    private String checkTime;
    @ApiModelProperty(value = "完工确认人工号")
    private String confirmUserId;
    @ApiModelProperty(value = "完工确认时间")
    private String confirmTime;
    @ApiModelProperty(value = "关闭人工号")
    private String closeUserId;
    @ApiModelProperty(value = "关闭时间")
    private String closeTime;
    @ApiModelProperty(value = "作废人工号")
    private String cancelUserId;
    @ApiModelProperty(value = "作废时间")
    private String cancelTime;
    @ApiModelProperty(value = "派工人")
    private String dispatchUserName;
    @ApiModelProperty(value = "维修负责人")
    private String repairRespUserName;
    @ApiModelProperty(value = "验收人")
    private String checkUserName;
    @ApiModelProperty(value = "关闭人")
    private String closeUserName;
    @ApiModelProperty(value = "完工人")
    private String reportFinishUserName;
    @ApiModelProperty(value = "完工确认人")
    private String confirmUserName;
    @ApiModelProperty(value = "恢复时间")
    private String planRecoveryTime;
    @ApiModelProperty(value = "故障影响")
    private String faultAffect;
    @ApiModelProperty(value = "是否扣修")
    private String isDetainingRepair;
    @ApiModelProperty(value = "是否故障")
    private String isFault;
    @ApiModelProperty(value = "处理人员")
    private String dealerUnit;
    @ApiModelProperty(value = "处理人数")
    private String dealerNum;
    @ApiModelProperty(value = "检修车/运营车标识")
    private String traintag;
    @ApiModelProperty(value = "公司代码")
    private String companyCode;
    @ApiModelProperty(value = "公司名称")
    private String companyName;
    @ApiModelProperty(value = "故障标识（1-标准故障；2-快速故障）")
    private String faultFlag;
    @ApiModelProperty(value = "对象编码")
    private String objectCode;
    @ApiModelProperty(value = "对象名称")
    private String objectName;
    @ApiModelProperty(value = "车底号/车厢号")
    private String trainTrunk;
    @ApiModelProperty(value = "线路编码")
    private String lineCode;
    @ApiModelProperty(value = "位置编码")
    private String positionCode;
    @ApiModelProperty(value = "位置1")
    private String positionName;
    @ApiModelProperty(value = "位置2编码")
    private String position2Code;
    @ApiModelProperty(value = "位置2")
    private String position2Name;
    @ApiModelProperty(value = "部件编码")
    private String partCode;
    @ApiModelProperty(value = "部件名称")
    private String partName;
    @ApiModelProperty(value = "专业")
    private String majorName;
    @ApiModelProperty(value = "系统")
    private String systemName;
    @ApiModelProperty(value = "设备分类")
    private String equipTypeName;
    @ApiModelProperty(value = "专业代码")
    private String majorCode;
    @ApiModelProperty(value = "系统代码")
    private String systemCode;
    @ApiModelProperty(value = "设备分类代码")
    private String equipTypeCode;
    @ApiModelProperty(value = "故障模块")
    private String faultModule;
    @ApiModelProperty(value = "故障模块")
    private String faultModuleId;
    @ApiModelProperty(value = "故障分类（10-运营故障；20-自检故障；30-新线调试；40-正线故障；50-出库故障）")
    private String faultType;
    @ApiModelProperty(value = "来源编号")
    private String sourceCode;
    @ApiModelProperty(value = "故障现象")
    private String faultDisplayCode;
    @ApiModelProperty(value = "故障现象")
    private String faultDisplayDetail;
    @ApiModelProperty(value = "故障详情")
    private String faultDetail;
    @ApiModelProperty(value = "发现人工号")
    private String discovererId;
    @ApiModelProperty(value = "发现人姓名")
    private String discovererName;
    @ApiModelProperty(value = "发现时间")
    private String discoveryTime;
    @ApiModelProperty(value = "发现人联系方式")
    private String discovererPhone;
    @ApiModelProperty(value = "提报人工号")
    private String fillinUserId;
    @ApiModelProperty(value = "提报人")
    private String fillinUserName;
    @ApiModelProperty(value = "提报部门")
    private String fillinDeptCode;
    @ApiModelProperty(value = "提报时间")
    private String fillinTime;
    @ApiModelProperty(value = "主责部门")
    private String respDeptCode;
    @ApiModelProperty(value = "配合部门")
    private String assistDeptCode;
    @ApiModelProperty(value = "维修部门")
    private String repairDeptCode;
    @ApiModelProperty(value = "维修部门名称")
    private String repairDeptName;
    @ApiModelProperty(value = "下发人工号")
    private String publishUserId;
    @ApiModelProperty(value = "下发时间")
    private String publishTime;
    @ApiModelProperty(value = "故障等级")
    private String faultLevel;
    @ApiModelProperty(value = "故障状态")
    private String faultStatus;
    @ApiModelProperty(value = "工作流实例ID")
    private String workFlowInstId;
    @ApiModelProperty(value = "工作流实例状态")
    private String workFlowInstStatus;
    @ApiModelProperty(value = "附件编号")
    private String docId;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "创建者")
    private String recCreator;
    @ApiModelProperty(value = "创建时间")
    private String recCreateTime;
    @ApiModelProperty(value = "修改者")
    private String recRevisor;
    @ApiModelProperty(value = "修改时间")
    private String recReviseTime;
    @ApiModelProperty(value = "删除者")
    private String recDeletor;
    @ApiModelProperty(value = "删除时间")
    private String recDeleteTime;
    @ApiModelProperty(value = "删除标志")
    private String deleteFlag;
    @ApiModelProperty(value = "归档标记")
    private String archiveFlag;
    @ApiModelProperty(value = "状态")
    private String recStatus;
    @ApiModelProperty(value = "扩展字段1")
    private String ext1;
    @ApiModelProperty(value = "扩展字段2")
    private String ext2;
    @ApiModelProperty(value = "扩展字段3")
    private String ext3;
    @ApiModelProperty(value = "扩展字段4")
    private String ext4;
    @ApiModelProperty(value = "扩展字段5")
    private String ext5;
}
