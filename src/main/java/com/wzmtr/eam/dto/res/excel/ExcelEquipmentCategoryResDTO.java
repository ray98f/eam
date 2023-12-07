package com.wzmtr.eam.dto.res.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备分类导出类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/06
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelEquipmentCategoryResDTO {

    @ExcelProperty(value = "记录编号")
    private String recId;
    @ExcelProperty(value = "节点编号")
    private String nodeCode;
    @ExcelProperty(value = "节点名称")
    private String nodeName;
    @ExcelProperty(value = "记录状态")
    private String recStatus;
    @ExcelProperty(value = "备注")
    private String remark;
    @ExcelProperty(value = "创建者")
    private String recCreator;
    @ExcelProperty(value = "创建时间")
    private String recCreateTime;
}
