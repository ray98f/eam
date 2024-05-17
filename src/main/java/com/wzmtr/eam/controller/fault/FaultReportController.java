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
 * 故障管理-故障提报
 * @author  Li.Wang
 * @version 1.0
 * @date 2023/08/15
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
    @PostMapping("/insert/equip")
    public DataResponse<String> addToEquip(@RequestBody @Valid FaultReportReqDTO reqDTO) {
        return DataResponse.of(reportService.addToFault(reqDTO));
    }

    /**
     * 故障提报（到设备）-开放接口
     * @param reqDTO 入参
     * @return 故障编号
     */
    @ApiOperation(value = "故障提报（到设备）-开放接口")
    @PostMapping("/insert/equip/open")
    public DataResponse<String> addToEquipOpen(@RequestBody @Valid FaultReportOpenReqDTO reqDTO) {
        return DataResponse.of(reportService.addToFaultOpen(reqDTO));
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
    public PageResponse<FaultDetailResDTO> openApiList(@RequestBody FaultReportPageReqDTO reqDTO) {
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

    /**
     * 获取维修部门和牵头部门
     * @param lineCode 线路code
     * @param majorCode 专业code
     * @param station 位置一code
     * @return 维修部门和牵头部门
     */
    @GetMapping("/queryTypeAndDeptCode")
    @ApiOperation(value = "获取维修部门和牵头部门")
    public DataResponse<FaultRespAndRepairDeptResDTO> queryTypeAndDeptCode(@RequestParam @ApiParam("线路code") String lineCode,
                                                                           @RequestParam @ApiParam("专业code") String majorCode,
                                                                           @RequestParam @ApiParam("位置一code") String station) {
        return DataResponse.of(orgMajorService.queryTypeAndDeptCode(lineCode, majorCode, station));
    }

}
