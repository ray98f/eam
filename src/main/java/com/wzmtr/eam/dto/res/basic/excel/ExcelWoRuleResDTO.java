package com.wzmtr.eam.dto.res.basic.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 工单触发规则导出类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelWoRuleResDTO {

    @ExcelProperty(value = "记录编号")
    private String recId;
    @ExcelProperty(value = "规则编号")
    private String ruleCode;
    @ExcelProperty(value = "规则名称")
    private String ruleName;
    @ExcelProperty(value = "用途")
    private String ruleUseage;
    @ExcelProperty(value = "记录状态")
    private String recStatus;
    @ExcelProperty(value = "备注")
    private String remark;
    @ExcelProperty(value = "创建者")
    private String recCreator;
    @ExcelProperty(value = "创建时间")
    private String recCreateTime;
}
