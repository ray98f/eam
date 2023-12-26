package com.wzmtr.eam.dto.res.equipment.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备台账导出类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelEquipmentResDTO {
    @ExcelProperty(value = "记录编号")
    private String recId;
    @ExcelProperty(value = "公司代码")
    private String companyCode;
    @ExcelProperty(value = "公司名称")
    private String companyName;
    @ExcelProperty(value = "部门代码")
    private String deptCode;
    @ExcelProperty(value = "部门名称")
    private String deptName;
    @ExcelProperty(value = "设备编号")
    private String equipCode;
    @ExcelProperty(value = "设备名称")
    private String equipName;
    @ExcelProperty(value = "专业编号")
    private String majorCode;
    @ExcelProperty(value = "专业名称")
    private String majorName;
    @ExcelProperty(value = "系统编号")
    private String systemCode;
    @ExcelProperty(value = "系统名称")
    private String systemName;
    @ExcelProperty(value = "设备类型编号")
    private String equipTypeCode;
    @ExcelProperty(value = "设备类型名称")
    private String equipTypeName;
    @ExcelProperty(value = "特种设备标识")
    private String specialEquipFlag;
    @ExcelProperty(value = "BOM类型")
    private String bomType;
    @ExcelProperty(value = "生产厂家")
    private String manufacture;
    @ExcelProperty(value = "合同号")
    private String orderNo;
    @ExcelProperty(value = "合同名称")
    private String orderName;
    @ExcelProperty(value = "型号规格")
    private String matSpecifi;
    @ExcelProperty(value = "品牌")
    private String brand;
    @ExcelProperty(value = "出厂日期")
    private String manufactureDate;
    @ExcelProperty(value = "出厂编号")
    private String manufactureNo;
    @ExcelProperty(value = "开始使用时间")
    private String startUseDate;
    @ExcelProperty(value = "数量")
    private String quantity;
    @ExcelProperty(value = "进设备台账时间")
    private String inAccountTime;
    @ExcelProperty(value = "设备状态")
    private String equipStatus;
    @ExcelProperty(value = "来源单号")
    private String sourceAppNo;
    @ExcelProperty(value = "来源明细号")
    private String sourceSubNo;
    @ExcelProperty(value = "来源记录编号")
    private String sourceRecId;
    @ExcelProperty(value = "安装单位")
    private String installDealer;
    @ExcelProperty(value = "来源线别代码")
    private String originLineNo;
    @ExcelProperty(value = "来源线别名称")
    private String originLineName;
    @ExcelProperty(value = "来源线段代码")
    private String originSegNo;
    @ExcelProperty(value = "来源线段名称")
    private String originSegName;
    @ExcelProperty(value = "应用线别代码")
    private String useLineNo;
    @ExcelProperty(value = "应用线别名称")
    private String useLineName;
    @ExcelProperty(value = "应用线段代码")
    private String useSegNo;
    @ExcelProperty(value = "应用线段名称")
    private String useSegName;
    @ExcelProperty(value = "位置一")
    private String position1Code;
    @ExcelProperty(value = "位置一名称")
    private String position1Name;
    @ExcelProperty(value = "位置二")
    private String position2Code;
    @ExcelProperty(value = "位置二名称")
    private String position2Name;
    @ExcelProperty(value = "位置三")
    private String position3;
    @ExcelProperty(value = "位置补充说明")
    private String positionRemark;
    @ExcelProperty(value = "行走里程")
    private String totalMiles;
    @ExcelProperty(value = "备注")
    private String remark;
    @ExcelProperty(value = "审批状态")
    private String approvalStatus;
    @ExcelProperty(value = "特种设备检测日期")
    private String verifyDate;
    @ExcelProperty(value = "特种设备检测有效期")
    private String verifyValidityDate;
    @ExcelProperty(value = "创建人")
    private String recCreator;
    @ExcelProperty(value = "创建时间")
    private String recCreateTime;
    @ExcelProperty(value = "修改人")
    private String recRevisor;
    @ExcelProperty(value = "修改时间")
    private String recReviseTime;
    @ExcelProperty(value = "删除者")
    private String recDeletor;
    @ExcelProperty(value = "删除时间")
    private String recDeleteTime;
    @ExcelProperty(value = "删除标志")
    private String deleteFlag;
    @ExcelProperty(value = "记录状态")
    private String recStatus;
}
