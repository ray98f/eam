package com.wzmtr.eam.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class TransferResDTO {

    @ApiModelProperty(value = "记录编号")
    private String recId;

    @ApiModelProperty(value = "移交单号")
    private String transferNo;

    @ApiModelProperty(value = "合同清单明细号")
    private String itemCode;

    @ApiModelProperty(value = "合同清单明细名称")
    private String itemName;

    @ApiModelProperty(value = "线别代码")
    private String lineNo;

    @ApiModelProperty(value = "线别名称")
    private String lineName;

    @ApiModelProperty(value = "线段代码")
    private String lineSubNo;

    @ApiModelProperty(value = "线段名称")
    private String lineSubName;

    @ApiModelProperty(value = "专业代码")
    private String majorCode;

    @ApiModelProperty(value = "专业名称")
    private String majorName;

    @ApiModelProperty(value = "项目编号")
    private String proCode;

    @ApiModelProperty(value = "项目名称")
    private String proName;

    @ApiModelProperty(value = "合同编号")
    private String orderNo;

    @ApiModelProperty(value = "合同名称")
    private String orderName;

    @ApiModelProperty(value = "供应商编号")
    private String supplierId;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "型号规格")
    private String matSpecifi;

    @ApiModelProperty(value = "品牌/生产厂家")
    private String brand;

    @ApiModelProperty(value = "其他特征参数")
    private String otherFeature;

    @ApiModelProperty(value = "图号/国标号/厂家零件号")
    private String appendixNo;

    @ApiModelProperty(value = "单位")
    private String stockUnit;

    @ApiModelProperty(value = "数量（默认值0）")
    private Integer quantity;

    @ApiModelProperty(value = "是组合件")
    private String isBom;

    @ApiModelProperty(value = "是隐蔽工程")
    private String isInvisible;

    @ApiModelProperty(value = "税后单价（默认值0）")
    private Double taxPrice;

    @ApiModelProperty(value = "税前单价（默认值0）")
    private Double preTaxPrice;

    @ApiModelProperty(value = "税率（默认值0）")
    private Double taxRate;

    @ApiModelProperty(value = "有技术资料")
    private String isDoc;

    @ApiModelProperty(value = "工程质保期（天）（默认值0）")
    private Integer assurancePeriod;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "使用年限N3（默认值0）")
    private Integer lifeYears;

    @ApiModelProperty(value = "使用次数N8（默认值0）")
    private Integer useCounter;

    @ApiModelProperty(value = "生产厂家")
    private String manufacture;

    @ApiModelProperty(value = "供货商")
    private String vendorCode;

    @ApiModelProperty(value = "位置1代码")
    private String position1Code;

    @ApiModelProperty(value = "位置1")
    private String position1Name;

    @ApiModelProperty(value = "位置2代码")
    private String position2Code;

    @ApiModelProperty(value = "位置2")
    private String position2Name;

    @ApiModelProperty(value = "位置3")
    private String position3;

    @ApiModelProperty(value = "位置补充说明")
    private String positionRemark;

    @ApiModelProperty(value = "合同单价（默认值0）")
    private Double orderAmt;

    @ApiModelProperty(value = "结构形式")
    private String constructureForm;

    @ApiModelProperty(value = "结构净高（默认值0）")
    private String netHeight;

    @ApiModelProperty(value = "是否使用甲供料")
    private String isOwnerSupply;

    @ApiModelProperty(value = "甲供料数量（默认值0）")
    private Integer ownerSupplyQuantity;

    @ApiModelProperty(value = "甲供料单位")
    private String ownerSupplyUnit;

    @ApiModelProperty(value = "甲供料价格（默认值0）")
    private Double ownerSupplyPrice;

    @ApiModelProperty(value = "甲供料含税价格（默认值0）")
    private Double ownerSupplyTaxPrice;

    @ApiModelProperty(value = "甲供料合同")
    private String ownerSupplyOrder;

    @ApiModelProperty(value = "安装费（默认值0）")
    private Double installAmt;

    @ApiModelProperty(value = "审核意见")
    private String auditMsg;

    @ApiModelProperty(value = "状态（新增、监理审核、业主审核、专工审核、监理驳回、业主驳回、专工驳回、完成）")
    private String status;

    @ApiModelProperty(value = "合同清单主键")
    private String recId2;

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

    @ApiModelProperty(value = "归档标记")
    private String archiveFlag;

    @ApiModelProperty(value = "是否进设备台账（0：是；1：否）")
    private String isEquipment;

    @ApiModelProperty(value = "EAM处理状态")
    private String eamProcessStatus;
}
