package com.wzmtr.eam.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author frp
 */
@Data
@ApiModel
public class SubmissionRecordDetailResDTO {

    @ApiModelProperty(value = "记录编号")
    private String recId;
    
    @ApiModelProperty(value = "检测记录表REC_ID")
    private String testRecId;
    
    @ApiModelProperty(value = "检测单明细表REC_ID")
    private String testListDetailRecId;
    
    @ApiModelProperty(value = "检测单号")
    private String sendVerifyNo;
    
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
    
    @ApiModelProperty(value = "送检条码号")
    private String measureBarcode;
    
    @ApiModelProperty(value = "检定日期")
    private String verifyDate;
    
    @ApiModelProperty(value = "检定校准单位")
    private String verifyDept;
    
    @ApiModelProperty(value = "证书编号")
    private String verificationNo;
    
    @ApiModelProperty(value = "证书类型")
    private String verificationType;
    
    @ApiModelProperty(value = "检定依据")
    private String verifyProof;
    
    @ApiModelProperty(value = "检定报告编号")
    private String verifyReportNo;
    
    @ApiModelProperty(value = "检定报告类型 0:检定证书;1:校准证书;2:检定结果通知书;3:检测证书;4:测试报告;5:测试证书;6:无证书7:校验证书;")
    private String verifyReportType;
    
    @ApiModelProperty(value = "检定报告名称")
    private String verifyReportName;
    
    @ApiModelProperty(value = "检定报告日期")
    private String verifyReportDate;
    
    @ApiModelProperty(value = "检定费用")
    private BigDecimal verifyFee;
    
    @ApiModelProperty(value = "检定结果（0:合格;1:不合格）")
    private String verifyResult;
    
    @ApiModelProperty(value = "检定结果说明")
    private String verifyConclusion;
    
    @ApiModelProperty(value = "检定报告附件")
    private String verifyReportFileid;
    
    @ApiModelProperty(value = "上次检定日期")
    private String lastVerifyDate;
    
    @ApiModelProperty(value = "下次检定日期")
    private String nextVerifyDate;
    
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
    
    @ApiModelProperty(value = "故障编号")
    private String faultNo;
    
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

    @ApiModelProperty(value = "检测周期")
    private String verifyPeriod;

    @ApiModelProperty(value = "使用单位")
    private String useDeptCname;

    @ApiModelProperty(value = "计划状态")
    private String planStatus;
}
