package com.wzmtr.eam.dto.res.basic.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 组织机构专业导出类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelOrgMajorResDTO {

    @ExcelProperty(value = "记录编号")
    private String recId;
    @ExcelProperty(value = "组织机构代码")
    private String orgCode;
    @ExcelProperty(value = "组织机构名称")
    private String orgName;
    @ExcelProperty(value = "专业")
    private String majorCode;
    @ExcelProperty(value = "记录状态")
    private String recStatus;
    @ExcelProperty(value = "备注")
    private String remark;
    @ExcelProperty(value = "创建者")
    private String recCreator;
    @ExcelProperty(value = "创建时间")
    private String recCreateTime;
}
