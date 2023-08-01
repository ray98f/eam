package com.wzmtr.eam.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class WheelsetLathingReqDTO {
    
    @ApiModelProperty(value = "记录编号")
    private String recId = "";

    @ApiModelProperty(value = "列车号")
    private String trainNo = "";

    @ApiModelProperty(value = "车厢号")
    private String carriageNo = "";

    @ApiModelProperty(value = "镟修轮对车轴")
    private String axleNo = "";

    @ApiModelProperty(value = "镟修详情")
    private String repairDetail = "";

    @ApiModelProperty(value = "开始日期")
    private String startDate = "";

    @ApiModelProperty(value = "完成日期")
    private String completeDate = "";

    @ApiModelProperty(value = "负责人")
    private String respPeople = "";
    
    @ApiModelProperty(value = "备注")
    private String remark = "";
    
    @ApiModelProperty(value = "附件编号")
    private String docId = "";

    @ApiModelProperty(value = "创建者")
    private String recCreator = "";

    @ApiModelProperty(value = "创建时间")
    private String recCreateTime = "";

    @ApiModelProperty(value = "修改者")
    private String recRevisor;

    @ApiModelProperty(value = "修改时间")
    private String recReviseTime;

    @ApiModelProperty(value = "删除者")
    private String recDeletor;

    @ApiModelProperty(value = "删除时间")
    private String recDeleteTime;

    @ApiModelProperty(value = "删除标志")
    private String deleteFlag;

    @ApiModelProperty(value = "归档标记")
    private String archiveFlag;

    @ApiModelProperty(value = "记录状态")
    private String recStatus;

    @ApiModelProperty(value = "扩展字段1")
    private String ext1;

    @ApiModelProperty(value = "扩展字段2")
    private String ext2;

    @ApiModelProperty(value = "扩展字段3")
    private String ext3;

    @ApiModelProperty(value = "扩展字段4")
    private String ext4;

    @ApiModelProperty(value = "扩展字段5")
    private String ext5;

    @ApiModelProperty(value = "扩展字段6")
    private String ext6;

    @ApiModelProperty(value = "扩展字段7")
    private String ext7;
}
