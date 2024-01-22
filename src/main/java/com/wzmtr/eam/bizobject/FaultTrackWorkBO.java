package com.wzmtr.eam.bizobject;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * String: 2023/9/5 15:49
 */
@Data
public class FaultTrackWorkBO {
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
    private String trackStartDate;
    private String trackEndDate;
    private Integer trackPeriod =0;
    private Integer trackCycle =0;
}
