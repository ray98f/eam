package com.wzmtr.eam.dto.res.fault;

import lombok.Data;

/**
 * 故障完工返回类
 * @author  Ray
 * @version 1.0
 * @date 2024/01/25
 */
@Data
public class FaultOrderResDTO {
    /**
     * 记录编号
     */
    private String recId;
    /**
     * 公司代码
     */
    private String companyCode;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 故障编号
     */
    private String faultNo;
    /**
     * 故障工单号
     */
    private String faultWorkNo;
    /**
     * 报告人工号
     */
    private String reportUserId;
    /**
     * 报告人名
     */
    private String reportUserName;
    /**
     * 报告时间
     */
    private String reportTime;
    /**
     * 工班
     */
    private String workClass;
    /**
     * 派工人工号
     */
    private String dispatchUserId;
    /**
     * 派工时间
     */
    private String dispatchTime;
    /**
     * 维修负责人工号
     */
    private String repairRespUserId;
    /**
     * 工作区域
     */
    private String workArea;
    /**
     * 工单状态
     */
    private String orderStatus;
    /**
     * 维调号
     */
    private String repairDispatchNo;
    /**
     * 抢修号
     */
    private String rushRepairNo;
    /**
     * 到达现场时间
     */
    private String arrivalTime;
    /**
     * 维修开始时间
     */
    private String repairStartTime;
    /**
     * 维修结束时间
     */
    private String repairEndTime;
    /**
     * 耗时（单位：小时）
     */
    private Float repairTime;
    /**
     * 离开现场时间
     */
    private String leaveTime;
    /**
     * 故障原因码
     */
    private String faultReasonCode;
    /**
     * 故障原因详情
     */
    private String faultReasonDetail;
    /**
     * 故障行动码
     */
    private String faultActionCode;
    /**
     * 故障处理详情
     */
    private String faultActionDetail;
    /**
     * 故障处理结果（10-已处理；20-未处理）
     */
    private String faultProcessResult;
    /**
     * 开工人工号
     */
    private String reportStartUserId;
    /**
     * 开工人
     */
    private String reportStartUserName;
    /**
     * 开工时间
     */
    private String reportStartTime;
    /**
     * 完工人工号
     */
    private String reportFinishUserId;
    /**
     * 完工时间
     */
    private String reportFinishTime;
    /**
     * 验收人
     */
    private String checkUserId;
    /**
     * 验收时间
     */
    private String checkTime;
    /**
     * 完工确认人工号
     */
    private String confirmUserId;
    /**
     * 完工确认时间
     */
    private String confirmTime;
    /**
     * 关闭人工号
     */
    private String closeUserId;
    /**
     * 关闭时间
     */
    private String closeTime;
    /**
     * 作废人工号
     */
    private String cancelUserId;
    /**
     * 作废时间
     */
    private String cancelTime;
    /**
     * 派工人
     */
    private String dispatchUserName;
    /**
     * 维修负责人
     */
    private String repairRespUserName;
    /**
     * 维修负责人手机号
     */
    private String repairRespUserMobile;
    /**
     * 验收人
     */
    private String checkUserName;
    /**
     * 关闭人
     */
    private String closeUserName;
    /**
     * 完工人
     */
    private String reportFinishUserName;
    /**
     * 完工确认人
     */
    private String confirmUserName;
    /**
     * 工作流实例ID
     */
    private String workFlowInstId;
    /**
     * 工作流实例状态
     */
    private String workFlowInstStatus;
    /**
     * 附件编号
     */
    private String docId;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建者
     */
    private String recCreator;
    /**
     * 创建时间
     */
    private String recCreateTime;
    /**
     * 修改者
     */
    private String recRevisor;
    /**
     * 修改时间
     */
    private String recReviseTime;
    /**
     * 删除者
     */
    private String recDeletor;
    /**
     * 删除时间
     */
    private String recDeleteTime;
    /**
     * 删除标志
     */
    private String deleteFlag;
    /**
     * 归档标记
     */
    private String archiveFlag;
    /**
     * 记录状态
     */
    private String recStatus;
    /**
     * 扩展字段1
     */
    private String ext1;
    /**
     * 扩展字段2
     */
    private String ext2;
    /**
     * 扩展字段3
     */
    private String ext3;
    /**
     * 扩展字段4
     */
    private String ext4;
    /**
     * 扩展字段5
     */
    private String ext5;
    /**
     * 恢复时间
     */
    private String planRecoveryTime;
    /**
     * 故障影响
     */
    private String faultAffect;
    /**
     * 是否扣修
     */
    private String isDetainingRepair;
    /**
     * 是否故障
     */
    private String isFault;
    /**
     * 处理人员
     */
    private String dealerUnit;
    /**
     * 处理人数
     */
    private String dealerNum;

}
