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
    @ExcelProperty(value = "设备编号")
    private String equipCode;
    @ExcelProperty(value = "设备名称")
    private String equipName;
    @ExcelProperty(value = "线路")
    private String useLineName;
    @ExcelProperty(value = "线段")
    private String useSegName;
    @ExcelProperty(value = "位置1")
    private String position1Name;
    @ExcelProperty(value = "专业")
    private String majorName;
    @ExcelProperty(value = "系统")
    private String systemName;
    @ExcelProperty(value = "设备分类")
    private String equipTypeName;
    @ExcelProperty(value = "模块")
    private String ext6;
    @ExcelProperty(value = "型号规格")
    private String matSpecifi;
    @ExcelProperty(value = "位置2")
    private String position2Name;
    @ExcelProperty(value = "位置3")
    private String position3;
    @ExcelProperty(value = "位置补充说明")
    private String positionRemark;
    @ExcelProperty(value = "品牌")
    private String brand;
    @ExcelProperty(value = "生产厂家")
    private String manufacture;
    @ExcelProperty(value = "供应商名称")
    private String ext5;
    @ExcelProperty(value = "出厂日期")
    private String manufactureDate;
    @ExcelProperty(value = "合同号")
    private String orderNo;
    @ExcelProperty(value = "合同名称")
    private String orderName;
    @ExcelProperty(value = "来源单号")
    private String sourceAppNo;
    @ExcelProperty(value = "开始使用时间")
    private String startUseDate;
    @ExcelProperty(value = "特种设备类别")
    private String specialEquipFlag;
    @ExcelProperty(value = "特种设备检测日期")
    private String verifyDate;
    @ExcelProperty(value = "特种设备检测有效期")
    private String verifyValidityDate;
    @ExcelProperty(value = "行走里程")
    private String totalMiles;
    @ExcelProperty(value = "备注")
    private String remark;
}
