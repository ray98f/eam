package com.wzmtr.eam.dto.req.specialEquip.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 特种设备台账导入类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/11
 */
@Data
public class ExcelSpecialEquipReqDTO {

    @ExcelProperty(value = "设备编码")
    private String equipCode;
    @ExcelProperty(value = "特种设备代码")
    private String specialEquipCode;
    @ExcelProperty(value = "设备名称")
    private String equipName;
    @ExcelProperty(value = "特种设备类别")
    private String specialEquipType;
    @ExcelProperty(value = "检测日期")
    private String verifyDate;
    @ExcelProperty(value = "检测有效期")
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
    @ExcelProperty(value = "设备参数")
    private String equipParameter;
    @ExcelProperty(value = "管理部门")
    private String manageOrg;
    @ExcelProperty(value = "安管人员")
    private String secStaffName;
    @ExcelProperty(value = "安管人员电话")
    private String secStaffPhone;
    @ExcelProperty(value = "安管人员手机")
    private String secStaffMobile;
    @ExcelProperty(value = "维管部门")
    private String secOrg;
}
