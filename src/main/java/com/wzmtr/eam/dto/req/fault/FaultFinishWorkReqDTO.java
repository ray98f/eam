package com.wzmtr.eam.dto.req.fault;

import lombok.Data;

import java.util.List;

/**
 * 故障完工返回类
 * @author  Ray
 * @version 1.0
 * @date 2024/01/25
 */
@Data
public class FaultFinishWorkReqDTO {
    /**
     * 故障编号
     */
    private String faultNo;
    /**
     * 工单编号
     */
    private String faultWorkNo;
    /**
     * 对象编码
     */
    private String objectCode;
    /**
     * 对象名称
     */
    private String objectName;
    /**
     * 位置一编码
     */
    private String positionCode;
    /**
     * 位置一名称
     */
    private String positionName;
    /**
     * 专业编码
     */
    private String majorCode;
    /**
     * 专业名称
     */
    private String majorName;
    /**
     * 系统编码
     */
    private String systemCode;
    /**
     * 系统名称
     */
    private String systemName;
    /**
     * 设备分类编码
     */
    private String equipTypeCode;
    /**
     * 设备分类名称
     */
    private String equipTypeName;
    /**
     * 线路编码
     */
    private String lineCode;
    /**
     * 位置二编码
     */
    private String position2Code;
    /**
     * 位置二名称
     */
    private String position2Name;
    /**
     * 模块
     */
    private String faultModule;
    /**
     * 模块id
     */
    private String faultModuleId;
    /**
     * 部件编号
     */
    private String partCode;
    /**
     * 部件名称
     */
    private String partName;
    /**
     * 故障状态
     */
    private String orderStatus;
    /**
     * 到达现场时间
     */
    private String arrivalTime;
    /**
     * 开始时间
     */
    private String repairStartTime;
    /**
     * 完成时间
     */
    private String repairEndTime;
    /**
     * 处理结果 01-04
     */
    private String faultProcessResult;
    /**
     * 故障现象码
     */
    private String faultDisplayCode;
    /**
     * 故障现象详情
     */
    private String faultDisplayDetail;
    /**
     * 故障原因码
     */
    private String faultReasonCode;
    /**
     * 原因详情
     */
    private String faultReasonDetail;
    /**
     * 故障处理码
     */
    private String faultActionCode;
    /**
     * 处理办法
     */
    private String faultActionDetail;
    /**
     * 报告人id
     */
    private String reportUserId;
    /**
     * 报告人名
     */
    private String reportUserName;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否故障 10 是 20 否
     */
    private String isFault;
    /**
     * 是否提交 0 否 1 是
     */
    private String isToSubmit;
    /**
     * 是否扣修 0 否 1 是
     */
    private String isDetainingRepair;
    /**
     * 提交人员列表
     */
    private List<String> userIds;
    /**
     * 完工报告对象编码
     */
    private String finishObjectCode;
    /**
     * 完工报告对象名称
     */
    private String finishObjectName;
    /**
     * 完工报告专业编码
     */
    private String finishMajorCode;
    /**
     * 完工报告系统编码
     */
    private String finishSystemCode;
    /**
     * 完工报告设备分类编码
     */
    private String finishEquipTypeCode;
    /**
     * 完工报告位置一编码
     */
    private String finishPositionCode;
    /**
     * 完工报告位置二编码
     */
    private String finishPosition2Code;
    /**
     * 完工报告部件id
     */
    private String finishPartId;
}
