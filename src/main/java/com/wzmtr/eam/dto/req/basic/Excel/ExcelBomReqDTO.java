package com.wzmtr.eam.dto.req.basic.Excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Bom结构导入类
 * @author  Ray
 * @version 1.0
 * @date 2024/02/05
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelBomReqDTO {
    /**
     * 系统（一级）编码
     */
    @ExcelProperty(value = "系统（一级）编码")
    private String firstSysCode;
    /**
     * 系统（一级）名称
     */
    @ExcelProperty(value = "系统（一级）名称")
    private String firstSysName;
    /**
     * 部件（一级）编码
     */
    @ExcelProperty(value = "部件（一级）编码")
    private String firstComCode;
    /**
     * 部件（一级）名称
     */
    @ExcelProperty(value = "部件（一级）名称")
    private String firstComName;
    /**
     * 系统（二级）编码
     */
    @ExcelProperty(value = "系统（二级）编码")
    private String secondSysCode;
    /**
     * 系统（二级）名称
     */
    @ExcelProperty(value = "系统（二级）名称")
    private String secondSysName;
    /**
     * 部件（二级）编码
     */
    @ExcelProperty(value = "部件（二级）编码")
    private String secondComCode;
    /**
     * 部件（二级）名称
     */
    @ExcelProperty(value = "部件（二级）名称")
    private String secondComName;
    /**
     * 系统（三级）编码
     */
    @ExcelProperty(value = "系统（三级）编码")
    private String thirdSysCode;
    /**
     * 系统（三级）名称
     */
    @ExcelProperty(value = "系统（三级）名称")
    private BigDecimal thirdSysName;
    /**
     * 部件（三级）编码
     */
    @ExcelProperty(value = "部件（三级）编码")
    private BigDecimal thirdComCode;
    /**
     * 部件（三级）名称
     */
    @ExcelProperty(value = "部件（三级）名称")
    private String thirdComName;
}
