package com.wzmtr.eam.dto.res.mea;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class SubmissionDetailResDTO {

    @ApiModelProperty(value = "记录编号")
    private String recId;
    
    @ApiModelProperty(value = "检测单号")
    private String sendVerifyNo;
    
    @ApiModelProperty(value = "检测计划明细表REC_ID")
    private String planDetailRecId;
    
    @ApiModelProperty(value = "计量器具代码")
    private String equipCode;
    
    @ApiModelProperty(value = "计量器具名称")
    private String equipName;
    
    @ApiModelProperty(value = "型号规格")
    private String matSpecifi;
    
    @ApiModelProperty(value = "出厂编号")
    private String manufactureNo;
    
    @ApiModelProperty(value = "生产厂家")
    private String manufacture;
    
    @ApiModelProperty(value = "安装单位")
    private String installationUnit;
    
    @ApiModelProperty(value = "检测周期(天)")
    private Integer verifyPeriod;
    
    @ApiModelProperty(value = "计划开始时间")
    private String planBeginDate;
    
    @ApiModelProperty(value = "计划完成时间")
    private String planEndDate;
    
    @ApiModelProperty(value = "计量器具状态（封存、停用、报废和注销）")
    private String equipmentState;
    
    @ApiModelProperty(value = "计量器具使用地点")
    private String useDeptPlace;
    
    @ApiModelProperty(value = "计量器具注册登记代码")
    private String equipmentRegCode;
    
    @ApiModelProperty(value = "安全管理人员")
    private String securityManager;
    
    @ApiModelProperty(value = "安全管理人员联系方式")
    private String securityContact;
    
    @ApiModelProperty(value = "位置一")
    private String position1Code;
    
    @ApiModelProperty(value = "位置一名称")
    private String position1Name;
    
    @ApiModelProperty(value = "位置二")
    private String position2Code;
    
    @ApiModelProperty(value = "位置二名称")
    private String position2Name;
    
    @ApiModelProperty(value = "位置三")
    private String position3;
    
    @ApiModelProperty(value = "位置补充说明")
    private String positionRemark;
    
    @ApiModelProperty(value = "备注")
    private String verifyNote;

    @ApiModelProperty(value = "公司代码")
    private String companyCode;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

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

    @ApiModelProperty(value = "上次检测日期")
    private String lastVerifyDate;

    @ApiModelProperty(value = "下次检测日期")
    private String nextVerifyDate;

    @ApiModelProperty(value = "检定校准单位")
    private String verifyDept;

    @ApiModelProperty(value = "检定有效期")
    private String validDate;
}
