package com.wzmtr.eam.dto.req.mea;

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
    /**
     * 记录编号
     */
    @ApiModelProperty(value = "记录编号")
    private String recId;
    /**
     * 计量器具分类代码
     */
    @ApiModelProperty(value = "计量器具分类代码")
    private String equipKindCode;
    /**
     * 计量器具代码
     */
    @ApiModelProperty(value = "计量器具代码")
    private String equipCode;
    /**
     * 计量器具名称
     */
    @ApiModelProperty(value = "计量器具名称")
    private String equipName;
    /**
     * 测量范围
     */
    @ApiModelProperty(value = "测量范围")
    private String measureRange;
    /**
     * 准确度
     */
    @ApiModelProperty(value = "准确度")
    private String measureAccuracy;
    /**
     * 检定校准单位
     */
    @ApiModelProperty(value = "检定校准单位")
    private String verifyDept;
    /**
     * 送检条码号
     */
    @ApiModelProperty(value = "送检条码号")
    private String measureBarcode;
    /**
     * 证书编号
     */
    @ApiModelProperty(value = "证书编号")
    private String certificateNo;
    /**
     * 证书类型 0:检测证书;1:校准证书;2:检测结果通知书;3:检测证书;4:测试报告;5:测试证书;6:无证书7:校验证书;
     */
    @ApiModelProperty(value = "证书类型 0:检测证书;1:校准证书;2:检测结果通知书;3:检测证书;4:测试报告;5:测试证书;6:无证书7:校验证书;")
    private String certificateType;
    /**
     * 检定结果
     */
    @ApiModelProperty(value = "检定结果")
    private String verifyResult;
    /**
     * 检定周期(天)
     */
    @ApiModelProperty(value = "检定周期(天)")
    private Integer verifyPeriod;
    /**
     * 开始使用日期
     */
    @ApiModelProperty(value = "开始使用日期")
    private String useBeginDate;
    /**
     * 上次检定日期
     */
    @ApiModelProperty(value = "上次检定日期")
    private String lastVerifyDate;
    /**
     * 下次检定日期
     */
    @ApiModelProperty(value = "下次检定日期")
    private String nextVerifyDate;
    /**
     * 送检日期
     */
    @ApiModelProperty(value = "送检日期")
    private String sendVerifyDate;
    /**
     * 管理类型:10:A;20:B;30:C
     */
    @ApiModelProperty(value = "管理类型:10:A;20:B;30:C")
    private String manageClass;
    /**
     * 检定依据
     */
    @ApiModelProperty(value = "检定依据")
    private String verifyProof;
    /**
     * 结论
     */
    @ApiModelProperty(value = "结论")
    private String verifyConclusion;
    /**
     * 处理单号
     */
    @ApiModelProperty(value = "处理单号")
    private String handleOrderNo;
    /**
     * 管理方式
     */
    @ApiModelProperty(value = "管理方式")
    private String manageMode;
    /**
     * 合同价
     */
    @ApiModelProperty(value = "合同价")
    private BigDecimal contractPrice = new BigDecimal("0");
    /**
     * 周期方式
     */
    @ApiModelProperty(value = "周期方式")
    private String periodMode;
    /**
     * 计划完成状态
     */
    @ApiModelProperty(value = "计划完成状态")
    private String planStatus;
    /**
     * 报警(天)
     */
    @ApiModelProperty(value = "报警(天)")
    private Integer alarm;
    /**
     * 线别代码
     */
    @ApiModelProperty(value = "线别代码")
    private String lineNo;
    /**
     * 线段代码
     */
    @ApiModelProperty(value = "线段代码")
    private String lineSubNo;
    /**
     * 位置一
     */
    @ApiModelProperty(value = "位置一")
    private String position1Code;
    /**
     * 位置一名称
     */
    @ApiModelProperty(value = "位置一名称")
    private String position1Name;
    /**
     * 位置二
     */
    @ApiModelProperty(value = "位置二")
    private String position2Code;
    /**
     * 位置二名称
     */
    @ApiModelProperty(value = "位置二名称")
    private String position2Name;
    /**
     * 位置三
     */
    @ApiModelProperty(value = "位置三")
    private String position3;
    /**
     * 位置补充说明
     */
    @ApiModelProperty(value = "位置补充说明")
    private String positionRemark;
    /**
     * 10:在用;20:限用;30:维修;40:损坏;50:未查;60:送检;70:封存;80:丢失;90:禁用;
     */
    @ApiModelProperty(value = "10:在用;20:限用;30:维修;40:损坏;50:未查;60:送检;70:封存;80:丢失;90:禁用;")
    private String equipmentState;
    /**
     * 型号规格
     */
    @ApiModelProperty(value = "型号规格")
    private String matSpecifi;
    /**
     * 出厂编号
     */
    @ApiModelProperty(value = "出厂编号")
    private String manufactureNo;
    /**
     * 生产厂家
     */
    @ApiModelProperty(value = "生产厂家")
    private String manufacture;
    /**
     * 生产厂家编号
     */
    @ApiModelProperty(value = "生产厂家编号")
    private String manufactureCode;
    /**
     * 来源10:设备自带;20:非设备自带
     */
    @ApiModelProperty(value = "来源10:设备自带;20:非设备自带")
    private String source;
    /**
     * CMC标识 10:是;20:否
     */
    @ApiModelProperty(value = "CMC标识 10:是;20:否")
    private String cmdFlg;
    /**
     * 10:是;20:否
     */
    @ApiModelProperty(value = "10:是;20:否")
    private String confirmation;
    /**
     * 使用责任人工号
     */
    @ApiModelProperty(value = "使用责任人工号")
    private String useNo;
    /**
     * 使用责任人
     */
    @ApiModelProperty(value = "使用责任人")
    private String useName;
    /**
     * 使用单位代码
     */
    @ApiModelProperty(value = "使用单位代码")
    private String useDeptCode;
    /**
     * 使用单位
     */
    @ApiModelProperty(value = "使用单位")
    private String useDeptCname;
    /**
     * 强制检定计量器具
     */
    @ApiModelProperty(value = "强制检定计量器具")
    private String compulsoryTest;
    /**
     * 送检10:否;20:是
     */
    @ApiModelProperty(value = "送检10:否;20:是")
    private String sendVerifyFlag;
    /**
     * 安装使用地点
     */
    @ApiModelProperty(value = "安装使用地点")
    private String usePlace;
    /**
     * 应用领域
     */
    @ApiModelProperty(value = "应用领域")
    private String useArea;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 工作流实例ID
     */
    @ApiModelProperty(value = "工作流实例ID")
    private String workFlowInstId;
    /**
     * 工作流实例状态
     */
    @ApiModelProperty(value = "工作流实例状态")
    private String workFlowInstStatus;
    /**
     * 公司代码
     */
    @ApiModelProperty(value = "公司代码")
    private String companyCode;
    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称")
    private String companyName;
    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private String recCreator;
    /**
     * 创建者姓名
     */
    @ApiModelProperty(value = "创建者姓名")
    private String recCreatorName;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String recCreateTime;
    /**
     * 修改者
     */
    @ApiModelProperty(value = "修改者")
    private String recRevisor;
    /**
     * 修改者姓名
     */
    @ApiModelProperty(value = "修改者姓名")
    private String recRevisorName;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private String recReviseTime;
    /**
     * 删除者
     */
    @ApiModelProperty(value = "删除者")
    private String recDeletor;
    /**
     * 删除者姓名
     */
    @ApiModelProperty(value = "删除者姓名")
    private String recDeletorName;
    /**
     * 删除时间
     */
    @ApiModelProperty(value = "删除时间")
    private String recDeleteTime;
    /**
     * 删除标志
     */
    @ApiModelProperty(value = "删除标志")
    private String deleteFlag;
    /**
     * 归档标记
     */
    @ApiModelProperty(value = "归档标记")
    private String archiveFlag;
    /**
     * 记录状态
     */
    @ApiModelProperty(value = "记录状态")
    private String recStatus;
    /**
     * 扩展字段1
     */
    @ApiModelProperty(value = "扩展字段1")
    private String ext1;
    /**
     * 扩展字段2
     */
    @ApiModelProperty(value = "扩展字段2")
    private String ext2;
    /**
     * 扩展字段3
     */
    @ApiModelProperty(value = "扩展字段3")
    private String ext3;
    /**
     * 扩展字段4
     */
    @ApiModelProperty(value = "扩展字段4")
    private String ext4;
    /**
     * 扩展字段5
     */
    @ApiModelProperty(value = "扩展字段5")
    private String ext5;
    /**
     * 附件
     */
    @ApiModelProperty(value = "附件")
    private String docId;
    /**
     * 移交日期
     */
    @ApiModelProperty(value = "移交日期")
    private String transferDate;
    /**
     * 到期时间
     */
    @ApiModelProperty(value = "到期时间")
    private String expirationDate;
    /**
     * 使用人手机号码
     */
    @ApiModelProperty(value = "使用人手机号码")
    private String phoneNo;
}
