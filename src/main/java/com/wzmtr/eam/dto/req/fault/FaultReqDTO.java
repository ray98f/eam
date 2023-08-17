package com.wzmtr.eam.dto.req.fault;

import com.wzmtr.eam.bo.FaultInfoBO;
import com.wzmtr.eam.bo.FaultOrderBO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.utils.DateUtil;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtil;
import com.wzmtr.eam.utils.__BeanUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Author: Li.Wang
 * Date: 2023/8/15 17:18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class FaultReqDTO extends PageReqDTO {
    @ApiModelProperty(value = "故障编号")
    private String faultNo;
    @ApiModelProperty(value = "故障工单编号")
    private String faultWorkNo;
    @ApiModelProperty(value = "对象编码")
    private String objectCode;
    @ApiModelProperty(value = "对象名称")
    private String objectName;
    @ApiModelProperty(value = "位置1")
    private String positionCode;
    @ApiModelProperty(value = "线别")
    private String lineCode;
    @ApiModelProperty(value = "专业")
    private String majorName;
    @ApiModelProperty(value = "系统")
    private String systemName;
    @ApiModelProperty(value = "专业code")
    private String majorCode;
    @ApiModelProperty(value = "系统code")
    private String systemCode;
    @ApiModelProperty(value = "设备分类代码")
    private String equipTypeCode;
    @ApiModelProperty(value = "故障分类（10-运营故障；20-自检故障；30-新线调试；40-正线故障；50-出库故障）")
    private String faultType;
    @ApiModelProperty(value = "提报时间开始")
    private String fillinTimeStart;
    @ApiModelProperty(value = "提报时间结束")
    private String fillinTimeEnd;
    @ApiModelProperty(value = "故障详情")
    private String faultDetail;
    @ApiModelProperty(value = "是否包含作废 作废(-99) 包含不传，不包含则传99")
    private String recStatus;



}
