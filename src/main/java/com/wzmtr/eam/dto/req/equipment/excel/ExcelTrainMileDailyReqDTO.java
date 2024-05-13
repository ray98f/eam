package com.wzmtr.eam.dto.req.equipment.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 每日列车里程及能耗导入类
 * @author  Ray
 * @version 1.0
 * @date 2024/01/26
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelTrainMileDailyReqDTO {
    /**
     * 日期
     */
    @ExcelProperty(value = "日期")
    private String day;
    /**
     * 车号
     */
    @ExcelProperty(value = "车号")
    private String equipName;
    /**
     * 当天运营里程
     */
    @ExcelProperty(value = "当天运营里程（公里）")
    private BigDecimal dailyWorkMile;
    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;
}
