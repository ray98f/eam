package com.wzmtr.eam.dto.req.equipment.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 每日列车里程及能耗导入类
 * @author  Ray
 * @version 1.0
 * @date 2024/01/26
 */
@Data
public class ExcelTrainMileDailyReqDTO {
    /**
     * 车号
     */
    @ExcelProperty(value = "车号")
    private String equipName;
    /**
     * 日期
     */
    @ExcelProperty(value = "日期")
    private String day;
    /**
     * 当天运营里程
     */
    @ExcelProperty(value = "当天运营里程")
    private BigDecimal dailyWorkMile;
    /**
     * 累计运营里程
     */
    @ExcelProperty(value = "累计运营里程")
    private BigDecimal totalWorkMile;
    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;
}
