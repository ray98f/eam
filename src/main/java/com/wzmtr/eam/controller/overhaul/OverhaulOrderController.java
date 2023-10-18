package com.wzmtr.eam.controller.overhaul;

import com.wzmtr.eam.dto.req.overhaul.OverhaulItemListReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulOrderListReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulOrderReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulUpStateReqDTO;
import com.wzmtr.eam.dto.res.basic.FaultRepairDeptResDTO;
import com.wzmtr.eam.dto.res.overhaul.*;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.OrganMajorLineType;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.service.overhaul.OverhaulOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/overhaul/order")
@Api(tags = "预防性检修管理-检修工单")
@Validated
public class OverhaulOrderController {

    @Resource
    private OverhaulOrderService overhaulOrderService;

    @GetMapping("/page")
    @ApiOperation(value = "获取检修工单列表")
    public PageResponse<OverhaulOrderResDTO> pageOverhaulOrder(OverhaulOrderListReqDTO overhaulOrderListReqDTO, @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(overhaulOrderService.pageOverhaulOrder(overhaulOrderListReqDTO, pageReqDTO));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取检修工单详情")
    public DataResponse<OverhaulOrderResDTO> getOverhaulOrderDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(overhaulOrderService.getOverhaulOrderDetail(id));
    }

    @PostMapping("/export")
    @ApiOperation(value = "导出检修工单")
    public void exportOverhaulOrder(@RequestBody BaseIdsEntity baseIdsEntity, HttpServletResponse response) {
        if (baseIdsEntity == null || baseIdsEntity.getIds().isEmpty()) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "请先勾选后导出");
        }
        overhaulOrderService.exportOverhaulOrder(baseIdsEntity.getIds(), response);
    }

    @GetMapping("/queryDept")
    @ApiOperation(value = "获取作业工班")
    public DataResponse<List<FaultRepairDeptResDTO>> queryDept(@RequestParam String id) {
        return DataResponse.of(overhaulOrderService.queryDept(id));
    }

    @GetMapping("/queryWorker")
    @ApiOperation(value = "获取作业人员")
    public DataResponse<List<OrganMajorLineType>> queryWorker(@RequestParam String workerGroupCode) {
        return DataResponse.of(overhaulOrderService.queryWorker(workerGroupCode));
    }

    @PostMapping("/dispatchWorkers")
    @ApiOperation(value = "检修工单派工")
    public DataResponse<T> dispatchWorkers(@RequestBody OverhaulOrderReqDTO overhaulOrderReqDTO) {
        overhaulOrderService.dispatchWorkers(overhaulOrderReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/auditWorkers")
    @ApiOperation(value = "检修工单完工验收")
    public DataResponse<T> auditWorkers(@RequestBody OverhaulOrderReqDTO overhaulOrderReqDTO) {
        overhaulOrderService.auditWorkers(overhaulOrderReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/confirmWorkers")
    @ApiOperation(value = "检修工单完工确认")
    public DataResponse<T> confirmWorkers(@RequestBody OverhaulOrderReqDTO overhaulOrderReqDTO) throws ParseException {
        overhaulOrderService.confirmWorkers(overhaulOrderReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/cancellWorkers")
    @ApiOperation(value = "检修工单作废")
    public DataResponse<T> cancellWorkers(@RequestBody OverhaulOrderReqDTO overhaulOrderReqDTO) {
        overhaulOrderService.cancellWorkers(overhaulOrderReqDTO);
        return DataResponse.success();
    }

    @GetMapping("/object/page")
    @ApiOperation(value = "获取检修对象列表")
    public PageResponse<OverhaulOrderDetailResDTO> pageOverhaulObject(@RequestParam(required = false) @ApiParam("工单编号") String orderCode,
                                                                      @RequestParam(required = false) @ApiParam("计划编号") String planCode,
                                                                      @RequestParam(required = false) @ApiParam("计划名称") String planName,
                                                                      @RequestParam(required = false) @ApiParam("对象编号") String objectCode,
                                                                      @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(overhaulOrderService.pageOverhaulObject(orderCode, planCode, planName, objectCode, pageReqDTO));
    }

    @GetMapping("/object/detail")
    @ApiOperation(value = "获取检修对象详情")
    public DataResponse<OverhaulOrderDetailResDTO> getOverhaulObjectDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(overhaulOrderService.getOverhaulObjectDetail(id));
    }

    @GetMapping("/object/export")
    @ApiOperation(value = "导出检修对象")
    public void exportOverhaulObject(@RequestParam(required = false) @ApiParam("工单编号") String orderCode,
                                     @RequestParam(required = false) @ApiParam("计划编号") String planCode,
                                     @RequestParam(required = false) @ApiParam("计划名称") String planName,
                                     @RequestParam(required = false) @ApiParam("对象编号") String objectCode,
                                     HttpServletResponse response) {
        overhaulOrderService.exportOverhaulObject(orderCode, planCode, planName, objectCode, response);
    }

    @GetMapping("/object/checkjx")
    @ApiOperation(value = "检修对象模块验收")
    public DataResponse<T> checkjx(@RequestParam @ApiParam("工单编号") String orderCode) {
        overhaulOrderService.checkjx(orderCode);
        return DataResponse.success();
    }

    @GetMapping("/item/page")
    @ApiOperation(value = "获取检修项列表")
    public PageResponse<OverhaulItemResDTO> pageOverhaulItem(OverhaulItemListReqDTO overhaulItemListReqDTO,
                                                             @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(overhaulOrderService.pageOverhaulItem(overhaulItemListReqDTO, pageReqDTO));
    }

    @GetMapping("/item/detail")
    @ApiOperation(value = "获取检修项详情")
    public DataResponse<OverhaulItemResDTO> getOverhaulItemDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(overhaulOrderService.getOverhaulItemDetail(id));
    }

    @GetMapping("/item/export")
    @ApiOperation(value = "导出检修项")
    public void exportOverhaulItem(OverhaulItemListReqDTO overhaulItemListReqDTO,
                                   HttpServletResponse response) {
        overhaulOrderService.exportOverhaulItem(overhaulItemListReqDTO, response);
    }

    @GetMapping("/state/page")
    @ApiOperation(value = "获取检修异常列表")
    public PageResponse<OverhaulStateResDTO> pageOverhaulState(@RequestParam(required = false) @ApiParam("对象编号") String objectCode,
                                                               @RequestParam(required = false) @ApiParam("检修项名称") String itemName,
                                                               @RequestParam(required = false) @ApiParam("工单编号") String orderCode,
                                                               @RequestParam(required = false) @ApiParam("检修项id") String tdmer23RecId,
                                                               @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(overhaulOrderService.pageOverhaulState(objectCode, itemName, orderCode, tdmer23RecId, pageReqDTO));
    }

    @GetMapping("/state/detail")
    @ApiOperation(value = "获取检修异常详情")
    public DataResponse<OverhaulStateResDTO> getOverhaulStateDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(overhaulOrderService.getOverhaulStateDetail(id));
    }

    @GetMapping("/state/export")
    @ApiOperation(value = "导出检修异常")
    public void exportOverhaulState(@RequestParam(required = false) @ApiParam("对象编号") String objectCode,
                                    @RequestParam(required = false) @ApiParam("检修项名称") String itemName,
                                    @RequestParam(required = false) @ApiParam("工单编号") String orderCode,
                                    @RequestParam(required = false) @ApiParam("检修项id") String tdmer23RecId,
                                    HttpServletResponse response) {
        overhaulOrderService.exportOverhaulState(objectCode, itemName, orderCode, tdmer23RecId, response);
    }

    @GetMapping("/state/queryOrderInfo")
    @ApiOperation(value = "升级故障查询工单信息")
    public DataResponse<OverhaulStateOrderResDTO> queryOrderInfo(@RequestParam @ApiParam("工单编号") String orderCode) {
        return DataResponse.of(overhaulOrderService.queryOrderInfo(orderCode));
    }

    @PostMapping("/state/up")
    @ApiOperation(value = "检修异常升级故障")
    public DataResponse<T> upState(@RequestBody OverhaulUpStateReqDTO overhaulUpStateReqDTO) {
        overhaulOrderService.upState(overhaulUpStateReqDTO);
        return DataResponse.success();
    }
}
