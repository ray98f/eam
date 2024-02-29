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
@TableName("T_FAULT_DETAIL")
public class FaultAnalyzeDO {
    @TableId(value = "REC_ID")
    private String recId;
    private String companyCode;
    private String companyName;
    private String faultAnalysisNo;
    private String faultNo;
    private String faultWorkNo;
    private String mainEquipCode;
    private String mainEquipName;
    private String majorCode;
    private String recoveryTime;
    private String affectCodes;
    private String frequency;
    private String manufacture;
    private String faultDetail;
    private String faultProcessDetail;
    private String faultReasonDetail;
    private String problemDescr;
    private String improveDetail;
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
    /*  27 */ private String majorName;
    @TableField(exist = false)
    /*  28 */ private String lineCode;
    @TableField(exist = false)
    /*  29 */ private String workClass;
    @TableField(exist = false)
    /*  30 */ private String discoveryTime;
    @TableField(exist = false)
    /*  31 */ private String faultDisplayCode;
    @TableField(exist = false)
    /*  32 */ private String faultDisplayDetail;
    @TableField(exist = false)
    /*  35 */ private String systemName;
    @TableField(exist = false)
    /*  36 */ private String positionName;
    @TableField(exist = false)
    /*  37 */ private String respDeptCode;
    @TableField(exist = false)
    /*  38 */ private String faultReasonCode;
    @TableField(exist = false)
    /*  39 */ private String faultLevel;
    @TableField(exist = false)
    /*  40 */ private String positionCode;
    @TableField(exist = false)
    /*  41 */ private String systemCode;

}
