package com.wzmtr.eam.dataobject;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Author: Li.Wang
 * String: 2023/9/5 15:49
 */
@Data
@TableName("T_FAULT_TRACK_DETAIL")
public class FaultTrackWorkDO {
    @TableId(value = "REC_ID")
    private String recId;
    private String companyCode;
    private String companyName;
    private String faultTrackNo;
    private String faultTrackWorkNo;
    private String workerGroupCode;
    private String workGroupName;
    private String dispatchUserId;
    private String dispatchTime;
    private String trackReporterId;
    private String trackReportTime;
    private String trackResult;
    private String trackCloserId;
    private String trackCloseTime;
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
}
