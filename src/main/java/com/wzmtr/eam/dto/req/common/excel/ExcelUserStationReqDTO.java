package com.wzmtr.eam.dto.req.common.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 人员车站关联导入类
 * @author  Ray
 * @version 1.0
 * @date 2024/07/23
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelUserStationReqDTO {

    @ExcelProperty(value = "工号")
    private String userNo;
    @ExcelProperty(value = "姓名")
    private String userName;
    @ExcelProperty(value = "车站名称")
    private String stationName;
}
