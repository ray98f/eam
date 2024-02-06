package com.wzmtr.eam.dto.req.basic.Excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 车辆与Bom关联关系导入类
 * @author  Ray
 * @version 1.0
 * @date 2024/02/06
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelBomTrainReqDTO {
    /**
     * 车号
     */
    @ExcelProperty(value = "车号")
    private String equipName;
    /**
     * 一级Bom名称
     */
    @ExcelProperty(value = "一级Bom名称")
    private String bomParenName;
}
