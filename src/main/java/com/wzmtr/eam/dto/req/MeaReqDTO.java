package com.wzmtr.eam.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author frp
 */
@Data
@ApiModel
public class MeaReqDTO {

    @ApiModelProperty(value = "记录编号")
    private String recId;

    @ApiModelProperty(value = "计量器具分类代码")
    private String equipKindCode;

    @ApiModelProperty(value = "计量器具代码")
    private String equipCode;

    @ApiModelProperty(value = "计量器具名称")
    private String equipName;

    @ApiModelProperty(value = "测量范围")
    private String measureRange;

    @ApiModelProperty(value = "准确度")
    private String measureAccuracy;

    @ApiModelProperty(value = "检定校准单位")
    private String verifyDept;

    @ApiModelProperty(value = "送检条码号")
    private String measureBarcode;

    @ApiModelProperty(value = "证书编号")
    private String certificateNo;

    @ApiModelProperty(value = "证书类型 0:检测证书;1:校准证书;2:检测结果通知书;3:检测证书;4:测试报告;5:测试证书;6:无证书7:校验证书;")
    private String certificateType;

    @ApiModelProperty(value = "检定结果")
    private String verifyResult;

    @ApiModelProperty(value = "检定周期(天)")
    private Integer verifyPeriod;

    @ApiModelProperty(value = "开始使用日期")
    private String useBeginDate;

    @ApiModelProperty(value = "上次检定日期")
    private String lastVerifyDate;

    @ApiModelProperty(value = "下次检定日期")
    private String nextVerifyDate;

    @ApiModelProperty(value = "送检日期")
    private String sendVerifyDate;

    @ApiModelProperty(value = "管理类型:10:A;20:B;30:C")
    private String manageClass;

    @ApiModelProperty(value = "检定依据")
    private String verifyProof;

    @ApiModelProperty(value = "结论")
    private String verifyConclusion;

    @ApiModelProperty(value = "处理单号")
    private String handleOrderNo;

    @ApiModelProperty(value = "管理方式")
    private String manageMode;

    @ApiModelProperty(value = "合同价")
    private BigDecimal contractPrice = new BigDecimal("0");

    @ApiModelProperty(value = "周期方式")
    private String periodMode;

    @ApiModelProperty(value = "计划完成状态")
    private String planStatus;

    @ApiModelProperty(value = "报警(天)")
    private Integer alarm;

    @ApiModelProperty(value = "线别代码")
    private String lineNo;

    @ApiModelProperty(value = "线段代码")
    private String lineSubNo;

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

    @ApiModelProperty(value = "10:在用;20:限用;30:维修;40:损坏;50:未查;60:送检;70:封存;80:丢失;90:禁用;")
    private String equipmentState;

    @ApiModelProperty(value = "型号规格")
    private String matSpecifi;

    @ApiModelProperty(value = "出厂编号")
    private String manufactureNo;

    @ApiModelProperty(value = "生产厂家")
    private String manufacture;

    @ApiModelProperty(value = "生产厂家编号")
    private String manufactureCode;

    @ApiModelProperty(value = "来源10:设备自带;20:非设备自带")
    private String source;

    @ApiModelProperty(value = "CMC标识 10:是;20:否")
    private String cmdFlg;

    @ApiModelProperty(value = "10:是;20:否")
    private String confirmation;

    @ApiModelProperty(value = "使用责任人工号")
    private String useNo;

    @ApiModelProperty(value = "使用责任人")
    private String useName;

    @ApiModelProperty(value = "使用单位代码")
    private String useDeptCode;

    @ApiModelProperty(value = "使用单位")
    private String useDeptCname;

    @ApiModelProperty(value = "强制检定计量器具")
    private String compulsoryTest;

    @ApiModelProperty(value = "送检10:否;20:是")
    private String sendVerifyFlag;

    @ApiModelProperty(value = "安装使用地点")
    private String usePlace;

    @ApiModelProperty(value = "应用领域")
    private String useArea;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "工作流实例ID")
    private String workFlowInstId;

    @ApiModelProperty(value = "工作流实例状态")
    private String workFlowInstStatus;

    @ApiModelProperty(value = "公司代码")
    private String companyCode;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "创建者")
    private String recCreator;

    @ApiModelProperty(value = "创建者姓名")
    private String recCreatorName;

    @ApiModelProperty(value = "创建时间")
    private String recCreateTime;

    @ApiModelProperty(value = "修改者")
    private String recRevisor;

    @ApiModelProperty(value = "修改者姓名")
    private String recRevisorName;

    @ApiModelProperty(value = "修改时间")
    private String recReviseTime;

    @ApiModelProperty(value = "删除者")
    private String recDeletor;

    @ApiModelProperty(value = "删除者姓名")
    private String recDeletorName;

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

    @ApiModelProperty(value = "附件")
    private String docId;

    @ApiModelProperty(value = "移交日期")
    private String transferDate;

    @ApiModelProperty(value = "到期时间")
    private String expirationDate;

    @ApiModelProperty(value = "使用人手机号码")
    private String phoneNo;
}
