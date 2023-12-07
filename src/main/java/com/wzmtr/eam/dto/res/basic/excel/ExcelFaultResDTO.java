package com.wzmtr.eam.dto.res.basic.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 故障库导出类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelFaultResDTO {

    @ExcelProperty(value = "记录编号")
    private String recId;
    @ExcelProperty(value = "对象编码")
    private String equipmentTypeCode;
    @ExcelProperty(value = "对象名称")
    private String equipmentTypeName;
    @ExcelProperty(value = "线路编号")
    private String lineCode;
    @ExcelProperty(value = "码值类型")
    private String faultCodeType;
    @ExcelProperty(value = "码值编号")
    private String faultCode;
    @ExcelProperty(value = "码值描述")
    private String faultDescr;
    @ExcelProperty(value = "关联码值")
    private String relatedCodes;
    @ExcelProperty(value = "记录状态")
    private String recStatus;
    @ExcelProperty(value = "备注")
    private String remark;
    @ExcelProperty(value = "创建者")
    private String recCreator;
    @ExcelProperty(value = "创建时间")
    private String recCreateTime;
}
