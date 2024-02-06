package com.wzmtr.eam.dto.res.detection.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 其他设备台账导出类
 * @author  Ray
 * @version 1.0
 * @date 2024/02/02
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelOtherEquipResDTO {

    @ExcelProperty(value = "记录编号")
    private String recId;
    @ExcelProperty(value = "设备编码")
    private String equipCode;
    @ExcelProperty(value = "其他设备代码")
    private String otherEquipCode;
    @ExcelProperty(value = "设备名称")
    private String equipName;
    @ExcelProperty(value = "其他设备类别")
    private String otherEquipType;
    @ExcelProperty(value = "应用线别代码")
    private String useLineNo;
    @ExcelProperty(value = "位置一编号")
    private String position1Code;
    @ExcelProperty(value = "位置一名称")
    private String position1Name;
    @ExcelProperty(value = "其他设备检测日期")
    private String verifyDate;
    @ExcelProperty(value = "其他设备检测有效期")
    private String verifyValidityDate;
    @ExcelProperty(value = "使用登记机构")
    private String regOrg;
    @ExcelProperty(value = "登记证编号")
    private String regNo;
    @ExcelProperty(value = "出厂编号")
    private String factNo;
    @ExcelProperty(value = "设备内部编号")
    private String equipInnerNo;
    @ExcelProperty(value = "设备所在地点")
    private String equipPosition;
    @ExcelProperty(value = "设备详细地址")
    private String equipDetailedPosition;
    @ExcelProperty(value = "设备主要参数")
    private String equipParameter;
    @ExcelProperty(value = "管理部门名称")
    private String manageOrgName;
    @ExcelProperty(value = "安管人员")
    private String secStaffName;
    @ExcelProperty(value = "安管人员电话")
    private String secStaffPhone;
    @ExcelProperty(value = "安管人员手机")
    private String secStaffMobile;
    @ExcelProperty(value = "维管部门名称")
    private String secOrgName;
}
