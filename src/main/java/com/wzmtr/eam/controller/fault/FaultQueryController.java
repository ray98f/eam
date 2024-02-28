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
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 故障管理-故障查询
 * @author  Li.Wang
 * @version 1.0
 * @date 2023/08/16
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

    /**
     * 完工
     * @param reqDTO 完工返回数据
     * @return 成功状态
     */
    @ApiOperation(value = "完工")
    @PostMapping("/finish/work")
    public DataResponse<T> finishWork(@RequestBody FaultFinishWorkReqDTO reqDTO) {
        faultQueryService.finishWork(reqDTO);
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
    @PostMapping("/export")
    public void export(@RequestBody FaultExportReqDTO reqDTO,
                       HttpServletResponse response) {
        // faultWorkNo
        faultQueryService.export(reqDTO, response);
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
