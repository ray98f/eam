package com.wzmtr.eam.dataobject;

import lombok.Builder;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/9/5 15:49
 */
@Data
@Builder
public class FaultAnalyzeDO {
    /*  18 */   private String recId;
    /*  19 */   private String companyCode;
    /*  20 */   private String companyName;
    /*  21 */   private String faultAnalysisNo;
    /*  22 */   private String faultNo;
    /*  23 */   private String faultWorkNo;
    /*  24 */   private String mainEquipCode;
    /*  25 */   private String mainEquipName;
    /*  26 */   private String majorCode;
    /*  27 */   private String majorName;
    /*  28 */   private String lineCode;
    /*  29 */   private String workClass;
    /*  30 */   private String discoveryTime;
    /*  31 */   private String faultDisplayCode;
    /*  32 */   private String faultDisplayDetail;
    /*  33 */   private String recoveryTime;
    /*     */
    /*  35 */   private String systemName;
    /*  36 */   private String positionName;
    /*  37 */   private String respDeptCode;
    /*  38 */   private String faultReasonCode;
    /*  39 */   private String faultLevel;
    /*  40 */   private String positionCode;
    /*  41 */   private String systemCode;
    /*     */
    /*  43 */   private String affectCodes;
    /*  44 */   private String frequency;
    /*  45 */   private String manufacture;
    /*  46 */   private String faultDetail;
    /*  47 */   private String faultProcessDetail;
    /*  48 */   private String faultReasonDetail;
    /*  49 */   private String problemDescr;
    /*  50 */   private String improveDetail;
    /*  51 */   private String workFlowInstId;
    /*  52 */   private String workFlowInstStatus;
    /*  53 */   private String docId;
    /*  54 */   private String remark;
    /*  55 */   private String recStatus;
    /*  56 */   private String recCreator;
    /*  57 */   private String recCreateTime;
    /*  58 */   private String recRevisor;
    /*  59 */   private String recReviseTime;
    /*  60 */   private String recDeletor;
    /*  61 */   private String recDeleteTime;
    /*  62 */   private String deleteFlag;
    /*  63 */   private String archiveFlag;
    /*  64 */   private String ext1;
    /*  65 */   private String ext2;
    /*  66 */   private String ext3;
    /*  67 */   private String ext4;
    /*  68 */   private String ext5;
}
