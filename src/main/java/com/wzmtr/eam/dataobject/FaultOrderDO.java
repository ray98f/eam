package com.wzmtr.eam.dataobject;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Author: Li.Wang
 * String: 2023/8/17 10:15
 */
@Data
@TableName("T_FAULT_ORDER")
public class FaultOrderDO {
    /**
     * notnull
     */
    @TableId(value = "REC_ID")
    private String recId;
    /**
     * notnull
     */
    private String companyCode;
    /**
     * notnull
     */
    private String companyName;
    /**
     * notnull
     */
    private String faultNo;
    /**
     * notnull
     */
    private String faultWorkNo;
    private String dispatchUserId;
    private String dispatchTime;
    private String workClass;
    private String repairRespUserId;
    private String workArea;
    /**
     * notnull 如果为空则默认提报状态
     */
    private String orderStatus;
    private String planRecoveryTime;
    private String faultAffect;
    private String repairDispatchNo;
    private String rushRepairNo;
    private String arrivalTime;
    private String repairStartTime;
    private String repairEndTime;
    private String repairTime;
    private String leaveTime;
    private String faultReasonCode;
    private String faultReasonDetail;
    private String faultActionCode;
    private String faultActionDetail;
    private String faultProcessResult;
    private String reportUserId;
    private String reportTime;
    private String reportStartUserId;
    private String reportStartTime;
    private String reportFinishUserId;
    private String reportFinishTime;
    private String checkUserId;
    private String checkTime;
    private String confirmUserId;
    private String confirmTime;
    private String closeUserId;
    private String closeTime;
    private String cancelUserId;
    private String cancelTime;
    private String workFlowInstId;
    private String workFlowInstStatus;
    /**
     * notnull
     */
    private String docId;
    private String remark;
    /**
     * notnull
     */
    private String recCreator;
    /**
     * notnull
     */
    private String recCreateTime;
    private String recRevisor;
    private String recReviseTime;
    private String recDeletor;
    private String recDeleteTime;
    private String deleteFlag;
    private String archiveFlag;
    /**
     * notnull
     */
    private String recStatus;
    private String ext1;
    private String ext2;
    private String ext3;
    private String ext4;
    private String ext5;
    private String isDetainingRepair;
    private String isFault;
    private String dealerUnit;
    private String dealerNum;
    @TableField(exist = false)
    private String reportUserName;
    @TableField(exist = false)
    private String dispatchUserName;
    @TableField(exist = false)
    private String repairRespUserName;
    @TableField(exist = false)
    private String checkUserName;
    @TableField(exist = false)
    private String closeUserName;
    @TableField(exist = false)
    private String reportFinishUserName;
    @TableField(exist = false)
    private String confirmUserName;
    private String levelFault;
}
