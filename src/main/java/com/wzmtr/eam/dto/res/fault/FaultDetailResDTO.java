package com.wzmtr.eam.dto.res.fault;

import com.wzmtr.eam.entity.File;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 故障工单详情结果类
 * @author  Li.Wang
 * @version 1.0
 * @date 2023/08/15
 */
@Data
public class FaultDetailResDTO {
    /**
     * FaultInfo表recId
     */
    @ApiModelProperty(value = "FaultInfo表recId")
    private String faultInfoRecId;
    /**
     * FaultOrder表recId
     */
    @ApiModelProperty(value = "FaultOrder表recId")
    private String faultOrderRecId;
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
     * 报告人工号
     */
    @ApiModelProperty(value = "报告人工号")
    private String reportUserId;
    /**
     * 验收人
     */
    @ApiModelProperty(value = "验收人")
    private String reportUserName;
    /**
     * 报告时间
     */
    @ApiModelProperty(value = "报告时间")
    private String reportTime;
    /**
     * 工班
     */
    @ApiModelProperty(value = "工班")
    private String workClass;
    /**
     * 派工人工号
     */
    @ApiModelProperty(value = "派工人工号")
    private String dispatchUserId;
    /**
     * 派工时间
     */
    @ApiModelProperty(value = "派工时间")
    private String dispatchTime;
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
     * 工单状态
     */
    @ApiModelProperty(value = "工单状态")
    private String orderStatus;
    /**
     * 维调号
     */
    @ApiModelProperty(value = "维调号")
    private String repairDispatchNo;
    /**
     * 抢修号
     */
    @ApiModelProperty(value = "抢修号")
    private String rushRepairNo;
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
     * 耗时（单位：小时）
     */
    @ApiModelProperty(value = "耗时（单位：小时）")
    private Float repairTime = 0.0F;
    /**
     * 离开现场时间
     */
    @ApiModelProperty(value = "离开现场时间")
    private String leaveTime;
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
     * 开工人工号
     */
    @ApiModelProperty(value = "开工人工号")
    private String reportStartUserId;
    /**
     * 开工人
     */
    @ApiModelProperty(value = "开工人")
    private String reportStartUserName;
    /**
     * 开工时间
     */
    @ApiModelProperty(value = "开工时间")
    private String reportStartTime;
    /**
     * 完工人工号
     */
    @ApiModelProperty(value = "完工人工号")
    private String reportFinishUserId;
    /**
     * 完工时间
     */
    @ApiModelProperty(value = "完工时间")
    private String reportFinishTime;
    /**
     * 验收人
     */
    @ApiModelProperty(value = "验收人")
    private String checkUserId;
    /**
     * 验收时间
     */
    @ApiModelProperty(value = "验收时间")
    private String checkTime;
    /**
     * 完工确认人工号
     */
    @ApiModelProperty(value = "完工确认人工号")
    private String confirmUserId;
    /**
     * 完工确认时间
     */
    @ApiModelProperty(value = "完工确认时间")
    private String confirmTime;
    /**
     * 关闭人工号
     */
    @ApiModelProperty(value = "关闭人工号")
    private String closeUserId;
    /**
     * 关闭时间
     */
    @ApiModelProperty(value = "关闭时间")
    private String closeTime;
    /**
     * 作废人工号
     */
    @ApiModelProperty(value = "作废人工号")
    private String cancelUserId;
    /**
     * 作废时间
     */
    @ApiModelProperty(value = "作废时间")
    private String cancelTime;
    /**
     * 派工人
     */
    @ApiModelProperty(value = "派工人")
    private String dispatchUserName;
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
     * 验收人
     */
    @ApiModelProperty(value = "验收人")
    private String checkUserName;
    /**
     * 关闭人
     */
    @ApiModelProperty(value = "关闭人")
    private String closeUserName;
    /**
     * 完工人
     */
    @ApiModelProperty(value = "完工人")
    private String reportFinishUserName;
    /**
     * 完工确认人
     */
    @ApiModelProperty(value = "完工确认人")
    private String confirmUserName;
    /**
     * 恢复时间
     */
    @ApiModelProperty(value = "恢复时间")
    private String planRecoveryTime;
    /**
     * 故障影响
     */
    @ApiModelProperty(value = "故障影响")
    private String faultAffect;
    /**
     * 是否扣修
     */
    @ApiModelProperty(value = "是否扣修")
    private String isDetainingRepair;
    /**
     * 是否故障
     */
    @ApiModelProperty(value = "是否故障")
    private String isFault;
    /**
     * 处理人员
     */
    @ApiModelProperty(value = "处理人员")
    private String dealerUnit;
    /**
     * 处理人数
     */
    @ApiModelProperty(value = "处理人数")
    private String dealerNum;
    /**
     * 检修车/运营车标识
     */
    @ApiModelProperty(value = "检修车/运营车标识")
    private String traintag;
    /**
     * 公司代码
     */
    @ApiModelProperty(value = "公司代码")
    private String companyCode;
    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称")
    private String companyName;
    /**
     * 故障标识（1-标准故障；2-快速故障）
     */
    @ApiModelProperty(value = "故障标识（1-标准故障；2-快速故障）")
    private String faultFlag;
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
     * 线路编码
     */
    @ApiModelProperty(value = "线路编码")
    private String lineCode;
    /**
     * 线路名称
     */
    @ApiModelProperty(value = "线路名称")
    private String lineName;
    /**
     * 位置编码
     */
    @ApiModelProperty(value = "位置编码")
    private String positionCode;
    /**
     * 位置1
     */
    @ApiModelProperty(value = "位置1")
    private String positionName;
    /**
     * 位置2编码
     */
    @ApiModelProperty(value = "位置2编码")
    private String position2Code;
    /**
     * 位置2
     */
    @ApiModelProperty(value = "位置2")
    private String position2Name;
    /**
     * 部件编码
     */
    @ApiModelProperty(value = "部件编码")
    private String partCode;
    /**
     * 部件名称
     */
    @ApiModelProperty(value = "部件名称")
    private String partName;
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
     * 专业代码
     */
    @ApiModelProperty(value = "专业代码")
    private String majorCode;
    /**
     * 系统代码
     */
    @ApiModelProperty(value = "系统代码")
    private String systemCode;
    /**
     * 设备分类代码
     */
    @ApiModelProperty(value = "设备分类代码")
    private String equipTypeCode;
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
    /**
     * 来源编号
     */
    @ApiModelProperty(value = "来源编号")
    private String sourceCode;
    /**
     * 故障现象编码
     */
    @ApiModelProperty(value = "故障现象")
    private String faultDisplayCode;
    /**
     * 故障现象详情
     */
    @ApiModelProperty(value = "故障现象")
    private String faultDisplayDetail;
    /**
     * 故障详情
     */
    @ApiModelProperty(value = "故障详情")
    private String faultDetail;
    /**
     * 发现人工号
     */
    @ApiModelProperty(value = "发现人工号")
    private String discovererId;
    /**
     * 发现人姓名
     */
    @ApiModelProperty(value = "发现人姓名")
    private String discovererName;
    /**
     * 发现时间
     */
    @ApiModelProperty(value = "发现时间")
    private String discoveryTime;
    /**
     * 发现人联系方式
     */
    @ApiModelProperty(value = "发现人联系方式")
    private String discovererPhone;
    /**
     * 提报人工号
     */
    @ApiModelProperty(value = "提报人工号")
    private String fillinUserId;
    /**
     * 提报人
     */
    @ApiModelProperty(value = "提报人")
    private String fillinUserName;
    /**
     * 提报部门
     */
    @ApiModelProperty(value = "提报部门")
    private String fillinDeptCode;
    /**
     * 提报部门名称
     */
    @ApiModelProperty(value = "提报部门名称")
    private String fillinDeptName;
    /**
     * 提报时间
     */
    @ApiModelProperty(value = "提报时间")
    private String fillinTime;
    /**
     * 主责部门
     */
    @ApiModelProperty(value = "主责部门")
    private String respDeptCode;
    /**
     * 主责部门名称
     */
    @ApiModelProperty(value = "主责部门名称")
    private String respDeptName;
    /**
     * 配合部门
     */
    @ApiModelProperty(value = "配合部门")
    private String assistDeptCode;
    /**
     * 维修部门
     */
    @ApiModelProperty(value = "维修部门")
    private String repairDeptCode;
    /**
     * 维修部门名称
     */
    @ApiModelProperty(value = "维修部门名称")
    private String repairDeptName;
    /**
     * 下发人工号
     */
    @ApiModelProperty(value = "下发人工号")
    private String publishUserId;
    /**
     * 下发时间
     */
    @ApiModelProperty(value = "下发时间")
    private String publishTime;
    /**
     * 故障紧急程度
     */
    @ApiModelProperty(value = "故障紧急程度")
    private String faultLevel;
    /**
     * 故障状态
     */
    @ApiModelProperty(value = "故障状态")
    private String faultStatus;
    /**
     * 工作流实例ID
     */
    @ApiModelProperty(value = "工作流实例ID")
    private String workFlowInstId;
    /**
     * 工作流实例状态
     */
    @ApiModelProperty(value = "工作流实例状态")
    private String workFlowInstStatus;
    /**
     * 附件编号
     */
    @ApiModelProperty(value = "附件编号")
    private String docId;
    /**
     * 备注
     */
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
    /**
     *
     */
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
    /**
     * 是否知会OCC
     */
    @ApiModelProperty(value = "是否知会OCC")
    private Boolean maintenance;
    /**
     * 跟踪状态
     */
    @ApiModelProperty(value = "跟踪状态")
    private String trackState;
    @ApiModelProperty(value = "附件文件")
    private List<File> docFile;
    /**
     * 故障流程数据
     */
    @ApiModelProperty(value = "故障流程数据")
    private List<FaultFlowResDTO> flows;
    private String repairLimitTime;
    /**
     * 是否由phm报出 0是 1否
     */
    @ApiModelProperty(value = "是否由phm报出 0是 1否")
    private String ifPhm;
    /**
     * 是否列入列车可靠性统计 0是 1否
     */
    @ApiModelProperty(value = "是否列入列车可靠性统计 0是 1否")
    private String ifReliability;
    /**
     * 是否是外部系统 0否 1是
     */
    @ApiModelProperty(value = "是否是外部系统 0否 1是")
    private String ifOther;
}
