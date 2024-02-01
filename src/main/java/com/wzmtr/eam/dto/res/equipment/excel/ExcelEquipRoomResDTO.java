package com.wzmtr.eam.dto.res.equipment.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备房台账导出类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelEquipRoomResDTO {

    @ExcelProperty(value = "设备房编码")
    private String equipRoomCode;
    @ExcelProperty(value = "设备房名称")
    private String equipRoomName;
    @ExcelProperty(value = "专业名称")
    private String subjectName;
    @ExcelProperty(value = "线别编码")
    private String lineCode;
    @ExcelProperty(value = "位置一名称")
    private String position1Name;
    @ExcelProperty(value = "位置二名称")
    private String position2Name;
    @ExcelProperty(value = "备注")
    private String remark;
    @ExcelProperty(value = "创建者")
    private String recCreator;
    @ExcelProperty(value = "创建时间")
    private String recCreateTime;
}
