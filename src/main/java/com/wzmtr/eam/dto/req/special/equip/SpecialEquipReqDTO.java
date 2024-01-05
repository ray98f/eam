package com.wzmtr.eam.dto.req.special.equip;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class SpecialEquipReqDTO {

    @ApiModelProperty(value = "记录编号")
    private String recId;

    @ApiModelProperty(value = "使用登记机构")
    private String regOrg;

    @ApiModelProperty(value = "登记证编号")
    private String regNo;

    @ApiModelProperty(value = "设备编码")
    private String equipCode;

    @ApiModelProperty(value = "特种设备类别")
    private String specialEquipType;

    @ApiModelProperty(value = "设备代码")
    private String specialEquipCode;

    @ApiModelProperty(value = "出厂编号")
    private String factNo;

    @ApiModelProperty(value = "设备内部编号")
    private String equipInnerNo;

    @ApiModelProperty(value = "设备所在地点")
    private String equipPosition;

    @ApiModelProperty(value = "设备详细地址")
    private String equipDetailedPosition;

    @ApiModelProperty(value = "设备主要参数")
    private String equipParameter;

    @ApiModelProperty(value = "管理部门")
    private String manageOrg;

    @ApiModelProperty(value = "维管部门")
    private String secOrg;

    @ApiModelProperty(value = "安管人员")
    private String secStaffName;

    @ApiModelProperty(value = "安管人员电话")
    private String secStaffPhone;

    @ApiModelProperty(value = "安管人员手机")
    private String secStaffMobile;

    @ApiModelProperty(value = "联系人")
    private String linkmanName;

    @ApiModelProperty(value = "联系人电话")
    private String linkmanPhone;

    @ApiModelProperty(value = "联系人手机")
    private String linkmanMobile;

    @ApiModelProperty(value = "备注")
    private String remark;

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

    @ApiModelProperty(value = "创建人")
    private String recCreator;

    @ApiModelProperty(value = "创建时间")
    private String recCreateTime;

    @ApiModelProperty(value = "创建人")
    private String recRevisor;

    @ApiModelProperty(value = "创建时间")
    private String recReviseTime;

    @ApiModelProperty(value = "删除者")
    private String recDeletor;

    @ApiModelProperty(value = "删除时间")
    private String recDeleteTime;

    @ApiModelProperty(value = "删除标志")
    private String deleteFlag;

    @ApiModelProperty(value = "记录状态")
    private String recStatus;

    @ApiModelProperty(value = "设备名称")
    private String equipName;

    @ApiModelProperty(value = "应用线别代码")
    private String useLineNo;

    @ApiModelProperty(value = "位置一编号")
    private String position1Code;

    @ApiModelProperty(value = "设备状态")
    private String equipStatus;

    @ApiModelProperty(value = "特种设备标记")
    private String specialEquipFlag;

    @ApiModelProperty(value = "审批状态")
    private String approvalStatus;

    @ApiModelProperty(value = "位置一名称")
    private String position1Name;

    @ApiModelProperty(value = "特种设备检测日期")
    private String verifyDate;

    @ApiModelProperty(value = "特种设备检测有效期")
    private String verifyValidityDate;

    @ApiModelProperty(value = "管理部门名称")
    private String manageOrgName;

    @ApiModelProperty(value = "维管部门名称")
    private String secOrgName;
}
