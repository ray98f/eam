package com.wzmtr.eam.controller.fault;

import com.wzmtr.eam.dto.req.fault.FaultDetailReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultReportPageReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultReportReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultReportToMajorReqDTO;
import com.wzmtr.eam.dto.res.basic.FaultRespAndRepairDeptResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.FaultReportResDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.basic.OrgMajorService;
import com.wzmtr.eam.service.fault.FaultReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Author: Li.Wang
 * Date: 2023/8/15 17:04
 */
@RestController
@RequestMapping("/fault/report")
@Api(tags = "故障管理-故障提报")
public class FaultReportController {
    @Autowired
    private FaultReportService reportService;
    @Autowired
    private OrgMajorService orgMajorService;

    @ApiOperation(value = "故障提报（到设备）")
    @PostMapping("/insert/epuip")
    public DataResponse<FaultReportResDTO> add(@RequestBody @Valid FaultReportReqDTO reqDTO) {
        reportService.addToEquip(reqDTO);
        return DataResponse.success();
    }

    @ApiOperation(value = "故障提报（到专业）")
    @PostMapping("/insert/major")
    public DataResponse<FaultReportResDTO> addToMajor(@RequestBody @Valid FaultReportToMajorReqDTO reqDTO) {
        reportService.addToMajor(reqDTO);
        return DataResponse.success();
    }

    @ApiOperation(value = "已提报故障")
    @PostMapping("/list")
    public PageResponse<FaultReportResDTO> list(@RequestBody FaultReportPageReqDTO reqDTO) {
        return PageResponse.of(reportService.list(reqDTO));
    }

    @ApiOperation(value = "故障编号详情")
    @PostMapping("/detail")
    public DataResponse<FaultDetailResDTO> detail(@RequestBody FaultDetailReqDTO reqDTO) {
        return DataResponse.of(reportService.detail(reqDTO));
    }
    @GetMapping("/queryTypeAndDeptCode")
    @ApiOperation(value = "获取维修部门和牵头部门")
    public DataResponse<FaultRespAndRepairDeptResDTO> queryTypeAndDeptCode(@RequestParam @ApiParam("线路code")String lineCode, @RequestParam @ApiParam("专业code")String majorCode) {
        return DataResponse.of(orgMajorService.queryTypeAndDeptCode(lineCode,majorCode));
    }

}
