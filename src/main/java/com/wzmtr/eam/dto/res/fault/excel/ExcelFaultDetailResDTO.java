package com.wzmtr.eam.dto.res.fault.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 故障详情导出列表
 * @author  Ray
 * @version 1.0
 * @date 2024/02/27
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelFaultDetailResDTO {
    @ExcelProperty(value = "状态")
    private String orderStatus;
    @ExcelProperty(value = "工单编号")
    private String faultWorkNo;
    @ExcelProperty(value = "提报时间")
    private String fillinTime;
    @ExcelProperty(value = "紧急程度")
    private String faultLevel;
    @ExcelProperty(value = "位置")
    private String positionName;
    @ExcelProperty(value = "专业")
    private String systemName;
    @ExcelProperty(value = "故障概况")
    private String faultDisplayDetail;
    @ExcelProperty(value = "故障描述")
    private String faultDetail;
    @ExcelProperty(value = "发现人")
    private String fillinUserName;
    @ExcelProperty(value = "处理人")
    private String reportFinishUserName;
    @ExcelProperty(value = "维修完成时间")
    private String reportFinishTime;
    @ExcelProperty(value = "工单关闭时间")
    private String closeTime;
}
