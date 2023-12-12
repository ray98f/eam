package com.wzmtr.eam.dto.req.equipment.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 设备台账导入类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/11
 */
@Data
public class ExcelEquipmentReqDTO {

    @ExcelProperty(value = "设备名称")
    private String equipName;
    @ExcelProperty(value = "线路")
    private String useLineName;
    @ExcelProperty(value = "线段")
    private String useSegName;
    @ExcelProperty(value = "开始使用日期")
    private String startUseDate;
    @ExcelProperty(value = "专业")
    private String majorName;
    @ExcelProperty(value = "系统")
    private String systemName;
    @ExcelProperty(value = "设备分类")
    private String equipTypeName;
    @ExcelProperty(value = "是否特种设备")
    private String specialEquipFlag;
    @ExcelProperty(value = "生产厂家")
    private String manufacture;
    @ExcelProperty(value = "型号规格")
    private String matSpecifi;
    @ExcelProperty(value = "品牌")
    private String brand;
    @ExcelProperty(value = "出厂日期")
    private String manufactureDate;
    @ExcelProperty(value = "出厂编号")
    private String manufactureNo;
    @ExcelProperty(value = "安装单位")
    private String installDealer;
    @ExcelProperty(value = "位置一")
    private String position1Name;
    @ExcelProperty(value = "位置二")
    private String position2Name;
    @ExcelProperty(value = "位置三")
    private String position3;
    @ExcelProperty(value = "位置补充说明")
    private String positionRemark;
    @ExcelProperty(value = "备注")
    private String remark;
}
