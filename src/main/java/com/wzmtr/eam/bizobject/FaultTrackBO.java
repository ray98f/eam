package com.wzmtr.eam.bizobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: Li.Wang
 * Date: 2023/11/24 8:32
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FaultTrackBO {
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
    private String workClass;
}
