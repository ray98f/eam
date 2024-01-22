package com.wzmtr.eam.controller.fault;

import com.wzmtr.eam.dto.req.fault.*;
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
    public DataResponse<String> add(@RequestBody @Valid FaultReportReqDTO reqDTO) {
        return DataResponse.of(reportService.addToFault(reqDTO));
    }

    @ApiOperation(value = "故障提报（到专业）")
    @PostMapping("/insert/major")
    public DataResponse<String> addToMajor(@RequestBody @Valid FaultReportReqDTO reqDTO) {
        return DataResponse.of(reportService.addToFault(reqDTO));
    }

    @ApiOperation(value = "已提报故障")
    @PostMapping("/list")
    public PageResponse<FaultReportResDTO> list(@RequestBody FaultReportPageReqDTO reqDTO) {
        return PageResponse.of(reportService.list(reqDTO));
    }

    /**
     * 已提报故障-开放接口
     * @param reqDTO 请求参数
     * @return 已提报故障列表
     */
    @ApiOperation(value = "已提报故障-开放接口")
    @PostMapping("/list/open")
    public PageResponse<FaultReportResDTO> openApiList(@RequestBody FaultReportPageReqDTO reqDTO) {
        return PageResponse.of(reportService.openApiList(reqDTO));
    }

    @ApiOperation(value = "已提报故障单作废")
    @PostMapping("/fault/cancel")
    public DataResponse<String> cancel(@RequestBody FaultCancelReqDTO reqDTO) {
        // faultWorkNo
        reportService.cancel(reqDTO);
        return DataResponse.success();
    }

    @ApiOperation(value = "已提报故障单撤销")
    @PostMapping("/fault/delete")
    public DataResponse<String> delete(@RequestBody FaultCancelReqDTO reqDTO) {
        // faultWorkNo
        reportService.delete(reqDTO);
        return DataResponse.success();
    }
    @ApiOperation(value = "已提报故障单修改")
    @PostMapping("/fault/update")
    public DataResponse<String> update(@RequestBody FaultReportReqDTO reqDTO) {
        reportService.update(reqDTO);
        return DataResponse.success();
    }


    @ApiOperation(value = "故障编号详情")
    @PostMapping("/detail")
    public DataResponse<FaultDetailResDTO> detail(@RequestBody FaultDetailReqDTO reqDTO) {
        return DataResponse.of(reportService.detail(reqDTO));
    }

    @GetMapping("/queryTypeAndDeptCode")
    @ApiOperation(value = "获取维修部门和牵头部门")
    public DataResponse<FaultRespAndRepairDeptResDTO> queryTypeAndDeptCode(@RequestParam @ApiParam("线路code") String lineCode, @RequestParam @ApiParam("专业code") String majorCode) {
        return DataResponse.of(orgMajorService.queryTypeAndDeptCode(lineCode, majorCode));
    }

}
