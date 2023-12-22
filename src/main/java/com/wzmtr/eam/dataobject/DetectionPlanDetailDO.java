package com.wzmtr.eam.dataobject;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: Li.Wang
 * Date: 2023/12/21 14:52
 */
@Data
@TableName("T_DETECTION_PLAN_DETAIL")
@Accessors(chain = true)
public class DetectionPlanDetailDO {
    @TableId(value = "REC_ID")
    private String recId;
    private String instrmPlanNo;
    private String equipCode;
    private String equipName;
    private String matSpecifi;
    private String manufactureNo;
    private String manufacture;
    private String installationUnit;
    private String verifyDept;
    private String verifyPeriod;
    private String planBeginDate;
    private String planEndDate;
    private String equipmentState;
    private String useDeptPlace;
    private String equipmentRegCode;
    private String securityManager;
    private String securityContact;
    private String position1Code;
    private String position1Name;
    private String position2Code;
    private String position2Name;
    private String position3;
    private String positionRemark;
    private String verifyNote;
    private String docId;
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

}
