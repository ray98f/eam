package com.wzmtr.eam.dataobject;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/10/26 16:12
 */
@TableName("T_RISK_RECORD")
@Data
public class SecureHazardDO {
    @TableId(value = "REC_ID")
    private String recId;
    private String riskId;
    private String inspectDate;
    private String riskRank;
    private String riskDetail;
    private String inspectDeptCode;
    private String inspectorCode;
    private String positionDesc;
    private String position1Code;
    private String position2Code;
    private String position3;
    private String positionRemark;
    private String riskPic;
    private String notifyDeptCode;
    private String restoreDetail;
    private String planDate;
    private String restoreDeptCode;
    private String restorePic;
    private String restoreDesc;
    private String examinerCode;
    private String examDate;
    private String completionDetail;
    private String undoneDesc;
    private String lastPlanDate;
    private String lastCheckerId;
    private String lastCheckDate;
    private String isRestored;
    private String planNote;
    private String workFlowInstId;
    private String workFlowInstStatus;
    private String recStatus;
    private String recCreator;
    private String recCreateTime;
    private String recRevisor;
    private String recReviseTime;
    private String recDeletor;
    private String recDeleteTime;
    private String deleteFlag;
    private String archiveFlag;
    private String ext1;
    private String ext2;
    private String ext3;
    private String ext4;
    private String ext5;

    // Getters and Setters
}
