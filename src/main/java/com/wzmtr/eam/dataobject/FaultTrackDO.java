package com.wzmtr.eam.dataobject;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Author: Li.Wang
 * String: 2023/9/5 15:49
 */
@Data
@TableName("T_FAULT_TRACK")
public class FaultTrackDO {
    @TableId(value = "REC_ID")
    private String recId;
    private String companyCode;
    private String companyName;
    private String faultTrackNo;
    private String faultNo;
    private String faultWorkNo;
    private String faultAnalysisNo;
    private String trackReason;
    private String trackUserId;
    private String trackTime;
    private String trackStartDate;
    private String trackEndDate;
    private Integer trackPeriod;
    private Integer trackCycle;
    private String trackReporterId;
    private String trackReportTime;
    private String trackResult;
    private String trackCloserId;
    private String trackCloseTime;
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
    @TableField(exist = false)
    private String workClass;
}
