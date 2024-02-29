package com.wzmtr.eam.controller.overhaul;

import com.wzmtr.eam.dto.req.overhaul.*;
import com.wzmtr.eam.dto.res.basic.FaultRepairDeptResDTO;
import com.wzmtr.eam.dto.res.fault.ConstructionResDTO;
import com.wzmtr.eam.dto.res.overhaul.*;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.OrganMajorLineType;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.service.overhaul.OverhaulOrderService;
import com.wzmtr.eam.utils.StringUtils;
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
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Objects;

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

    /**
     * 获取检修工单列表-开放接口
     * @param overhaulOrderListReqDTO 检修工单返回信息
     * @param pageReqDTO 分页参数
     * @return 检修工单列表
     */
    @GetMapping("/page/open")
    @ApiOperation(value = "获取检修工单列表-开放接口")
    public PageResponse<OverhaulOrderResDTO> openApiPageOverhaulOrder(OverhaulOrderListReqDTO overhaulOrderListReqDTO, @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(overhaulOrderService.openApiPageOverhaulOrder(overhaulOrderListReqDTO, pageReqDTO));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取检修工单详情")
    public DataResponse<OverhaulOrderResDTO> getOverhaulOrderDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(overhaulOrderService.getOverhaulOrderDetail(id));
    }

    @PostMapping("/export")
    @ApiOperation(value = "导出检修工单")
    public void exportOverhaulOrder(@RequestBody BaseIdsEntity baseIdsEntity, HttpServletResponse response) throws IOException {
        if (Objects.isNull(baseIdsEntity) || StringUtils.isEmpty(baseIdsEntity.getIds())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "请先勾选后导出");
        }
        overhaulOrderService.exportOverhaulOrder(baseIdsEntity.getIds(), response);
    }

    @GetMapping("/queryDept")
    @ApiOperation(value = "获取作业工班")
    public DataResponse<List<FaultRepairDeptResDTO>> queryDept(@RequestParam String id) {
        return DataResponse.of(overhaulOrderService.queryDept(id));
    }

    /**
     * 获取工单派工作业人员
     * @param workStatus 工单状态
     * @param workerGroupCode 作业工班编号
     * @return 用户信息
     */
    @GetMapping("/queryWorker")
    @ApiOperation(value = "获取作业人员")
    public DataResponse<List<OrganMajorLineType>> queryWorker(@RequestParam String workStatus,
                                                              @RequestParam String workerGroupCode) {
        return DataResponse.of(overhaulOrderService.queryWorker(workStatus, workerGroupCode));
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

    @GetMapping("/material/page")
    @ApiOperation(value = "材料列表")
    public DataResponse<T> pageMaterial() {
        overhaulOrderService.pageMaterial();
        return DataResponse.success();
    }

    @PostMapping("/material/receive")
    @ApiOperation(value = "领用材料")
    public void receiveMaterial(HttpServletResponse response) throws IOException {
        overhaulOrderService.receiveMaterial(response);
    }

    @PostMapping("/material/return")
    @ApiOperation(value = "退回材料")
    public DataResponse<T> returnMaterial() {
        overhaulOrderService.returnMaterial();
        return DataResponse.success();
    }

    @GetMapping("/construction")
    @ApiOperation(value = "施工计划")
    public PageResponse<ConstructionResDTO> construction(@RequestParam @ApiParam("工单编号") String orderCode,
                                                         @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(overhaulOrderService.construction(orderCode, pageReqDTO));
    }

    @GetMapping("/cancellation")
    @ApiOperation(value = "请销点")
    public PageResponse<ConstructionResDTO> cancellation(@RequestParam @ApiParam("工单编号") String orderCode,
                                                         @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(overhaulOrderService.cancellation(orderCode, pageReqDTO));
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
                                     HttpServletResponse response) throws IOException {
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

    /**
     * 获取检修项检修模块列表
     * @param objectCode 对象编号
     * @param orderCode 工单编号
     * @return 检修模块列表
     */
    @GetMapping("/item/listModel")
    @ApiOperation(value = "获取检修项检修模块列表")
    public DataResponse<List<OverhaulItemResDTO>> listOverhaulItemModel(@RequestParam @ApiParam("对象编号") String objectCode,
                                                                        @RequestParam @ApiParam("工单编号") String orderCode) {
        return DataResponse.of(overhaulOrderService.listOverhaulItemModel(objectCode, orderCode));
    }

    /**
     * 根据检修模块获取检修项列表
     * @param objectCode 对象编号
     * @param orderCode 工单编号
     * @param modelName 模块名称
     * @return 检修项列表
     */
    @GetMapping("/item/list")
    @ApiOperation(value = "根据检修模块获取检修项列表")
    public DataResponse<List<OverhaulItemResDTO>> listOverhaulItem(@RequestParam @ApiParam("对象编号") String objectCode,
                                                                   @RequestParam @ApiParam("工单编号") String orderCode,
                                                                   @RequestParam @ApiParam("模块名称") String modelName) {
        return DataResponse.of(overhaulOrderService.listOverhaulItem(objectCode, orderCode, modelName));
    }

    /**
     * 获取检修项检修模块与检修项列表
     * @param objectCode 对象编号
     * @param orderCode 工单编号
     * @return 检修项列表
     */
    @GetMapping("/item/listTree")
    @ApiOperation(value = "获取检修项检修模块与检修项列表")
    public DataResponse<List<OverhaulItemTreeResDTO>> listOverhaulItemTree(@RequestParam @ApiParam("对象编号") String objectCode,
                                                                           @RequestParam @ApiParam("工单编号") String orderCode) {
        return DataResponse.of(overhaulOrderService.listOverhaulItemTree(objectCode, orderCode));
    }

    @GetMapping("/item/detail")
    @ApiOperation(value = "获取检修项详情")
    public DataResponse<OverhaulItemResDTO> getOverhaulItemDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(overhaulOrderService.getOverhaulItemDetail(id));
    }

    @GetMapping("/item/export")
    @ApiOperation(value = "导出检修项")
    public void exportOverhaulItem(OverhaulItemListReqDTO overhaulItemListReqDTO,
                                   HttpServletResponse response) throws IOException {
        overhaulOrderService.exportOverhaulItem(overhaulItemListReqDTO, response);
    }

    /**
     * 排查检修项
     * @param troubleshootReqDTO 排查检修项信息
     * @return 操作成功/失败
     */
    @PostMapping("/item/troubleshoot")
    @ApiOperation(value = "排查检修项")
    public DataResponse<T> troubleshootOverhaulItem(@RequestBody OverhaulItemTroubleshootReqDTO troubleshootReqDTO) {
        overhaulOrderService.troubleshootOverhaulItem(troubleshootReqDTO);
        return DataResponse.success();
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
                                    HttpServletResponse response) throws IOException {
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
