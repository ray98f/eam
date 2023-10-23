package com.wzmtr.eam.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/17 10:15
 */
@Data
@TableName("T_FAULT_ORDER")
public class FaultOrderDO {
    @TableId(value = "REC_ID")
    private String recId;
    private String companyCode;
    private String companyName;
    private String faultNo;
    private String faultWorkNo;
    private String reportUserId;
    private String reportUserName;
    private String reportTime;
    private String workClass;
    private String dispatchUserId;
    private String dispatchTime;
    private String repairRespUserId;
    private String workArea;
    private String orderStatus = " ";
    private String repairDispatchNo;
    private String rushRepairNo;
    private String arrivalTime;
    private String repairStartTime;
    private String repairEndTime;
    private Float repairTime = 0.0F;
    private String leaveTime;
    private String faultReasonCode;
    private String faultReasonDetail;
    private String faultActionCode;
    private String faultActionDetail;
    private String faultProcessResult;
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
    private String dispatchUserName;
    private String repairRespUserName;
    private String checkUserName;
    private String closeUserName;
    private String reportFinishUserName;
    private String confirmUserName;
    private String workFlowInstId;
    private String workFlowInstStatus;
    private String docId;
    private String remark;
    private String recCreator;
    private String recCreateTime;
    private String recRevisor;
    private String recReviseTime;
    private String recDeletor;
    private String recDeleteTime;
    private String deleteFlag;
    private String archiveFlag;
    private String recStatus;
    private String ext1;
    private String ext2;
    private String ext3;
    private String ext4;
    private String ext5;
    private String planRecoveryTime;
    private String faultAffect;
    private String isDetainingRepair;
    private String isFault;
    private String dealerUnit;
    private String dealerNum;
}
