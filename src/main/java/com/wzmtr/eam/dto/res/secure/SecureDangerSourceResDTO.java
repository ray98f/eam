package com.wzmtr.eam.dto.res.secure;

import com.wzmtr.eam.entity.File;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/8/1 9:48
 */
@Data
@ApiModel
public class SecureDangerSourceResDTO {
    @ApiModelProperty(value = "记录编号")
    private String recId;
    @ApiModelProperty(value = "危险源记录单号")
    private String dangerRiskId;
    @ApiModelProperty(value = "记录部门")
    private String recDept;
    @ApiModelProperty(value = "记录部门名称")
    private String recDeptName;
    @ApiModelProperty(value = "危险源")
    private String dangerRisk;
    @ApiModelProperty(value = "危险源等级")
    private String dangerRiskRank;
    @ApiModelProperty(value = "危险源描述")
    private String dangerRiskDetail;
    @ApiModelProperty(value = "后果/伤害")
    private String consequense;
    @ApiModelProperty(value = "责任部门")
    private String respDeptCode;
    @ApiModelProperty(value = "责任部门名称")
    private String respDeptName;
    @ApiModelProperty(value = "责任人")
    private String respCode;
    @ApiModelProperty(value = "地点")
    private String positionDesc;
    @ApiModelProperty(value = "位置一")
    private String position1Code;
    @ApiModelProperty(value = "位置二")
    private String position2Code;
    @ApiModelProperty(value = "位置三")
    private String position3;
    @ApiModelProperty(value = "位置补充说明")
    private String positionRemark;
    @ApiModelProperty(value = "危险源照片")
    private String dangerRiskPic;
    @ApiModelProperty(value = "控制措施")
    private String controlDetail;
    @ApiModelProperty(value = "发现时间")
    private String discDate;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "创建者")
    private String recCreator;
    @ApiModelProperty(value = "创建时间")
    private String recCreateTime;
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
    @ApiModelProperty(value = "附件文件")
    private List<File> docFile;

}
