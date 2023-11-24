package com.wzmtr.eam.dto.req.fault;

import com.wzmtr.eam.bizobject.FaultTrackBO;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.utils.DateUtil;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtil;
import com.wzmtr.eam.utils.__BeanUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/11/23 17:26
 */
@Data
@ApiModel
public class FaultTrackAddReqDTO {
    @ApiModelProperty(value = "跟踪单号")
    private String faultTrackNo;
    private String faultAnalysisNo;
    private String faultNo;
    private String faultWorkNo;
    @ApiModelProperty(value = "跟踪原因")
    private String trackReason;
    @ApiModelProperty(value = "转跟踪人员工号")
    private String trackUserId;
    @ApiModelProperty(value = "转跟踪时间")
    private String trackTime;
    @ApiModelProperty(value = "跟踪期限")
    private Integer trackPeriod = 0;
    @ApiModelProperty(value = "跟踪周期")
    private Integer trackCycle = 0;
    @ApiModelProperty(value = "跟踪开始时间")
    private String trackStartDate;
    @ApiModelProperty(value = "跟踪截止时间")
    private String trackEndDate;
    @ApiModelProperty(value = "转跟踪人员")
    private String trackReporterId;
    @ApiModelProperty(value = "转跟踪报告时间")
    private String trackReportTime;
    @ApiModelProperty(value = "跟踪结果")
    private String trackResult;
    @ApiModelProperty(value = "跟踪关闭人工号")
    private String trackCloserId;
    @ApiModelProperty(value = "跟踪关闭时间")
    private String trackCloseTime;
    @ApiModelProperty(value = "附件编号")
    private String docId;
    /*  41 */   private String remark;
    /*  50 */   private String recStatus;

    public FaultTrackBO toBO(FaultTrackAddReqDTO req) {
        FaultTrackBO bo = __BeanUtil.convert(req, FaultTrackBO.class);
        bo.setRecStatus("10");
        bo.setRecCreator(TokenUtil.getCurrentPerson().getPersonId());
        bo.setRecCreateTime(DateUtil.getCurrentTime());
        bo.setRecId(TokenUtil.getUuId());
        bo.setDeleteFlag("0");
        if (StringUtils.isEmpty(req.getDocId())){
            bo.setDocId(CommonConstants.BLANK);
        }
        return bo;
    }

}
