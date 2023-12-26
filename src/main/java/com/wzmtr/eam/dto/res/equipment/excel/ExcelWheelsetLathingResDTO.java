package com.wzmtr.eam.dto.res.equipment.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 轮对镟修台账导出类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelWheelsetLathingResDTO {
    @ExcelProperty(value = "记录编号")
    private String recId;
    @ExcelProperty(value = "列车号")
    private String trainNo;
    @ExcelProperty(value = "车厢号")
    private String carriageNo;
    @ExcelProperty(value = "镟修轮对车轴")
    private String axleNo;
    @ExcelProperty(value = "镟修轮对号")
    private String wheelNo;
    @ExcelProperty(value = "轮高")
    private String wheelHeight;
    @ExcelProperty(value = "轮厚")
    private String wheelThick;
    @ExcelProperty(value = "轮径")
    private String wheelDiameter;
    @ExcelProperty(value = "镟修详情")
    private String repairDetail;
    @ExcelProperty(value = "开始日期")
    private String startDate;
    @ExcelProperty(value = "完成日期")
    private String completeDate;
    @ExcelProperty(value = "负责人")
    private String respPeople;
    @ExcelProperty(value = "备注")
    private String remark;
    @ExcelProperty(value = "创建者")
    private String recCreator;
    @ExcelProperty(value = "创建时间")
    private String recCreateTime;
}
