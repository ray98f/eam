package com.wzmtr.eam.bo;

import lombok.Builder;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/9/5 15:49
 */
@Data
@Builder
public class FaultTrackBO {
    private String recId;
    /*  20 */   private String companyCode;
    /*  21 */   private String companyName;
    /*  22 */   private String faultTrackNo;
    /*  23 */   private String faultAnalysisNo;
    /*  24 */   private String faultNo;
    /*  25 */   private String faultWorkNo;
    /*  26 */   private String trackReason;
    /*  27 */   private String trackUserId;
    /*  28 */   private String trackTime;
    /*  29 */   private Integer trackPeriod;
    /*  30 */   private Integer trackCycle;
    /*  31 */   private String trackStartDate;
    /*  32 */   private String trackEndDate;
    /*  33 */   private String trackReporterId;
    /*  34 */   private String trackReportTime;
    /*  35 */   private String trackResult;
    /*  36 */   private String trackCloserId;
    /*  37 */   private String trackCloseTime;
    /*  38 */   private String workFlowInstId;
    /*  39 */   private String workFlowInstStatus;
    /*  40 */   private String docId;
    /*  41 */   private String remark;
    /*  42 */   private String recCreator;
    /*  43 */   private String recCreateTime;
    /*  44 */   private String recRevisor;
    /*  45 */   private String recReviseTime;
    /*  46 */   private String recDeletor;
    /*  47 */   private String recDeleteTime;
    /*  48 */   private String deleteFlag;
    /*  49 */   private String archiveFlag;
    /*  50 */   private String recStatus;
    /*  51 */   private String ext1;
    /*  52 */   private String ext2;
    /*  53 */   private String ext3;
    /*  54 */   private String ext4;
    /*  55 */   private String ext5;
}
