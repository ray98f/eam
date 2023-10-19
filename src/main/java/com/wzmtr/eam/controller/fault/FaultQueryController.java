package com.wzmtr.eam.controller.fault;

import com.wzmtr.eam.dto.req.fault.*;
import com.wzmtr.eam.dto.res.basic.FaultRepairDeptResDTO;
import com.wzmtr.eam.dto.res.fault.ConstructionResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.entity.OrganMajorLineType;
import com.wzmtr.eam.entity.SidEntity;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.enums.OrderStatus;
import com.wzmtr.eam.service.fault.FaultQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

/**
 * Author: Li.Wang
 * Date: 2023/8/16 20:36
 */
@RestController
@RequestMapping("/fault/query")
@Api(tags = "故障管理-故障查询")
public class FaultQueryController {
    @Autowired
    private FaultQueryService faultQueryService;

    @ApiOperation(value = "列表")
    @PostMapping("/list")
    public PageResponse<FaultDetailResDTO> list(@RequestBody FaultQueryReqDTO reqDTO) {
        return PageResponse.of(faultQueryService.list(reqDTO));
    }

    @ApiOperation(value = "查询订单状态")
    @PostMapping("/queryOrderStatus")
    public DataResponse<String> queryOrderStatus(@RequestBody SidEntity reqDTO) {
        return DataResponse.of(faultQueryService.queryOrderStatus(reqDTO));
    }

    @ApiOperation(value = "下发")
    @PostMapping("/issue")
    public DataResponse<String> issue(@RequestBody FaultDetailReqDTO reqDTO) {
        // faultWorkNo
        faultQueryService.issue(reqDTO);
        return DataResponse.success();
    }

    @ApiOperation(value = "派工")
    @PostMapping("/send/work")
    public DataResponse<String> sendWork(@RequestBody FaultSendWorkReqDTO reqDTO) {
        // faultWorkNo
        faultQueryService.sendWork(reqDTO);
        return DataResponse.success();
    }

    @ApiOperation(value = "设调确认")
    @PostMapping("/eqCheck")
    public DataResponse<String> eqCheck(@RequestBody FaultEqCheckReqDTO reqDTO) throws Exception {
        // faultWorkNo
        faultQueryService.eqCheck(reqDTO);
        return DataResponse.success();
    }
    @ApiOperation(value = "完工确认")
    @PostMapping("/finishConfirm")
    public DataResponse<String> finishConfirm(@RequestBody FaultNosFaultWorkNosReqDTO reqDTO) {
        // faultWorkNo
        reqDTO.setType(OrderStatus.WAN_GONG_QUE_REN);
        faultQueryService.updateHandler(reqDTO);
        return DataResponse.success();
    }
    @ApiOperation(value = "作废")
    @PostMapping("/cancel")
    public DataResponse<String> cancel(@RequestBody FaultNosFaultWorkNosReqDTO reqDTO) {
        // faultWorkNo
        reqDTO.setType(OrderStatus.ZUO_FEI);
        faultQueryService.updateHandler(reqDTO);
        return DataResponse.success();
    }

    @ApiOperation(value = "故障单关闭")
    @PostMapping("/fault/close")
    public DataResponse<String> close(@RequestBody FaultNosFaultWorkNosReqDTO reqDTO) {
        reqDTO.setType(OrderStatus.GUAN_BI);
        faultQueryService.updateHandler(reqDTO);
        return DataResponse.success();
    }

    @ApiOperation(value = "故障单验收")
    @PostMapping("/fault/check")
    public DataResponse<String> check(@RequestBody FaultNosFaultWorkNosReqDTO reqDTO) {
        // faultWorkNo
        reqDTO.setType(OrderStatus.YAN_SHOU);
        faultQueryService.updateHandler(reqDTO);
        return DataResponse.success();
    }

    @ApiOperation(value = "导出")
    @GetMapping("/export")
    public void export(@RequestParam(required = false) @ApiParam("故障编号s")Set<String> faultNos,
                       @RequestParam(required = false) @ApiParam("故障工单编号s")Set<String> faultWorkNos,
                       HttpServletResponse response) {
        // faultWorkNo
        faultQueryService.export(faultNos,faultWorkNos, response);
    }


    @ApiOperation(value = "施工计划")
    @PostMapping("/construction")
    public PageResponse<ConstructionResDTO> construction(@RequestBody FaultQueryReqDTO reqDTO) {
        // faultWorkNo
        return PageResponse.of(faultQueryService.construction(reqDTO));
    }

    @ApiOperation(value = "请销点")
    @PostMapping("/cancellation")
    public PageResponse<ConstructionResDTO> cancellation(@RequestBody FaultQueryReqDTO reqDTO) {
        // faultWorkNo
        return PageResponse.of(faultQueryService.cancellation(reqDTO));
    }


    @ApiOperation(value = "操作前对选中的作前置校验")
    @PostMapping("/fault/track/compareRows")
    public DataResponse<Boolean> compareRows(@RequestBody CompareRowsReqDTO reqDTO) {
        // faultWorkNo
        return DataResponse.of(faultQueryService.compareRows(reqDTO));
    }


    @ApiOperation(value = "故障工单驳回")
    @PostMapping("/fault/work/reject")
    public DataResponse<String> reject(@RequestBody FaultSubmitReqDTO reqDTO) {
        // faultWorkNo
        // faultQueryService.reject(reqDTO);
        return DataResponse.success();
    }
    // @ApiOperation(value = "故障对象确认")
    // @PostMapping("/fault/track/cancel")
    // public DataResponse<String> cancel(@RequestBody FaultSubmitReqDTO reqDTO) {
    //     // faultWorkNo
    //     faultQueryService.returns(reqDTO);
    //     return DataResponse.success();
    // }
    @ApiOperation(value = "获取维修部门")
    @GetMapping("querydept")
    public DataResponse<List<FaultRepairDeptResDTO>> querydept(@RequestParam String faultNo) {
        return DataResponse.of(faultQueryService.querydept(faultNo));
    }
    @ApiOperation(value = "获取维修部门下的人")
    @GetMapping("queryWorker")
    public DataResponse<List<OrganMajorLineType>> queryWorker(@RequestParam String workerGroupCode) {
        return DataResponse.of(faultQueryService.queryWorker(workerGroupCode));
    }
}
