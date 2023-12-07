package com.wzmtr.eam.dto.res.equipment.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备移交导出类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelTransferResDTO {
    @ExcelProperty(value = "记录编号")
    private String recId;
    @ExcelProperty(value = "移交单号")
    private String transferNo;
    @ExcelProperty(value = "合同清单明细号")
    private String itemCode;
    @ExcelProperty(value = "合同清单明细名称")
    private String itemName;
    @ExcelProperty(value = "线别代码")
    private String lineNo;
    @ExcelProperty(value = "线别名称")
    private String lineName;
    @ExcelProperty(value = "线段代码")
    private String lineSubNo;
    @ExcelProperty(value = "线段名称")
    private String lineSubName;
    @ExcelProperty(value = "专业代码")
    private String majorCode;
    @ExcelProperty(value = "专业名称")
    private String majorName;
    @ExcelProperty(value = "项目编号")
    private String proCode;
    @ExcelProperty(value = "项目名称")
    private String proName;
    @ExcelProperty(value = "合同编号")
    private String orderNo;
    @ExcelProperty(value = "合同名称")
    private String orderName;
    @ExcelProperty(value = "供应商编号")
    private String supplierId;
    @ExcelProperty(value = "供应商名称")
    private String supplierName;
    @ExcelProperty(value = "型号规格")
    private String matSpecifi;
    @ExcelProperty(value = "品牌/生产厂家")
    private String brand;
    @ExcelProperty(value = "其他特征参数")
    private String otherFeature;
    @ExcelProperty(value = "图号/国标号/厂家零件号")
    private String appendixNo;
    @ExcelProperty(value = "单位")
    private String stockUnit;
    @ExcelProperty(value = "数量（默认值0）")
    private String quantity;
    @ExcelProperty(value = "是组合件")
    private String isBom;
    @ExcelProperty(value = "是隐蔽工程")
    private String isInvisible;
    @ExcelProperty(value = "税后单价（默认值0）")
    private String taxPrice;
    @ExcelProperty(value = "税前单价（默认值0）")
    private String preTaxPrice;
    @ExcelProperty(value = "税率（默认值0）")
    private String taxRate;
    @ExcelProperty(value = "有技术资料")
    private String isDoc;
    @ExcelProperty(value = "工程质保期（天）（默认值0）")
    private String assurancePeriod;
    @ExcelProperty(value = "备注")
    private String remark;
    @ExcelProperty(value = "使用年限N3（默认值0）")
    private String lifeYears;
    @ExcelProperty(value = "使用次数N8（默认值0）")
    private String useCounter;
    @ExcelProperty(value = "生产厂家")
    private String manufacture;
    @ExcelProperty(value = "供货商")
    private String vendorCode;
    @ExcelProperty(value = "位置1代码")
    private String position1Code;
    @ExcelProperty(value = "位置1")
    private String position1Name;
    @ExcelProperty(value = "位置2代码")
    private String position2Code;
    @ExcelProperty(value = "位置2")
    private String position2Name;
    @ExcelProperty(value = "位置3")
    private String position3;
    @ExcelProperty(value = "位置补充说明")
    private String positionRemark;
    @ExcelProperty(value = "合同单价（默认值0）")
    private String orderAmt;
    @ExcelProperty(value = "结构形式")
    private String constructureForm;
    @ExcelProperty(value = "结构净高（默认值0）")
    private String netHeight;
    @ExcelProperty(value = "是否使用甲供料")
    private String isOwnerSupply;
    @ExcelProperty(value = "甲供料数量（默认值0）")
    private String ownerSupplyQuantity;
    @ExcelProperty(value = "甲供料单位")
    private String ownerSupplyUnit;
    @ExcelProperty(value = "甲供料价格（默认值0）")
    private String ownerSupplyPrice;
    @ExcelProperty(value = "甲供料含税价格（默认值0）")
    private String ownerSupplyTaxPrice;
    @ExcelProperty(value = "甲供料合同")
    private String ownerSupplyOrder;
    @ExcelProperty(value = "安装费（默认值0）")
    private String installAmt;
    @ExcelProperty(value = "审核意见")
    private String auditMsg;
    @ExcelProperty(value = "状态（新增、监理审核、业主审核、专工审核、监理驳回、业主驳回、专工驳回、完成）")
    private String status;
    @ExcelProperty(value = "合同清单主键")
    private String recId2;
    @ExcelProperty(value = "扩展字段1")
    private String ext1;
    @ExcelProperty(value = "扩展字段2")
    private String ext2;
    @ExcelProperty(value = "扩展字段3")
    private String ext3;
    @ExcelProperty(value = "扩展字段4")
    private String ext4;
    @ExcelProperty(value = "扩展字段5")
    private String ext5;
    @ExcelProperty(value = "扩展字段6")
    private String ext6;
    @ExcelProperty(value = "创建人")
    private String recCreator;
    @ExcelProperty(value = "创建时间")
    private String recCreateTime;
    @ExcelProperty(value = "创建人")
    private String recRevisor;
    @ExcelProperty(value = "创建时间")
    private String recReviseTime;
    @ExcelProperty(value = "删除者")
    private String recDeletor;
    @ExcelProperty(value = "删除时间")
    private String recDeleteTime;
    @ExcelProperty(value = "删除标志")
    private String deleteFlag;
    @ExcelProperty(value = "归档标记")
    private String archiveFlag;
    @ExcelProperty(value = "是否进设备台账（0：是；1：否）")
    private String isEquipment;
    @ExcelProperty(value = "EAM处理状态")
    private String eamProcessStatus;
}
