package com.wzmtr.eam.dto.res.basic.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 组织机构位置导出类
 * @author  Ray
 * @version 1.0
 * @date 2024/03/21
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelOrgRegionResDTO {

    @ExcelProperty(value = "记录编号")
    private String recId;
    @ExcelProperty(value = "组织机构代码")
    private String orgCode;
    @ExcelProperty(value = "组织机构名称")
    private String orgName;
    @ExcelProperty(value = "位置编号")
    private String regionCode;
    @ExcelProperty(value = "记录状态")
    private String recStatus;
    @ExcelProperty(value = "备注")
    private String remark;
    @ExcelProperty(value = "创建者")
    private String recCreator;
    @ExcelProperty(value = "创建时间")
    private String recCreateTime;
}
