package com.wzmtr.eam.controller.fault;

import com.wzmtr.eam.dto.req.fault.CompareRowsReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultDetailReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultEqCheckReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultExportReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultFinishWorkReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultNosFaultWorkNosReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultQueryReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultReportReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultSendWorkReqDTO;
import com.wzmtr.eam.dto.res.basic.FaultRepairDeptResDTO;
import com.wzmtr.eam.dto.res.fault.ConstructionResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailOpenResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.FaultListResDTO;
import com.wzmtr.eam.entity.OrganMajorLineType;
import com.wzmtr.eam.entity.SidEntity;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.enums.OrderStatus;
import com.wzmtr.eam.service.fault.FaultQueryService;
import com.wzmtr.eam.service.fault.FaultReportService;
import com.wzmtr.eam.service.fault.TrackQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    private FaultReportService reportService;

    @Autowired
    private FaultQueryService faultQueryService;

    @Autowired
    private TrackQueryService trackQueryService;

    @ApiOperation(value = "列表")
    @PostMapping("/list")
    public PageResponse<FaultListResDTO> list(@RequestBody FaultQueryReqDTO reqDTO) {
        return PageResponse.of(faultQueryService.list(reqDTO));
    }

    /**
     * 根据故障工单号查询故障工单详情-开放接口
     * @param faultNo 故障编号
     * @param faultWorkNo 故障工单号
     * @return 故障工单详情
     */
    @ApiOperation(value = "根据故障工单号查询故障工单详情-开放接口")
    @GetMapping("/detail/open")
    public DataResponse<FaultDetailOpenResDTO> faultDetail(@RequestParam String faultNo,
                                                           @RequestParam String faultWorkNo) {
        return DataResponse.of(faultQueryService.faultDetailOpen(faultNo, faultWorkNo));
    }

    @ApiOperation(value = "当前用户超过限时列表")
    @PostMapping("/queryLimit")
    public DataResponse<List<FaultDetailResDTO>> queryLimit() {
        return DataResponse.of(faultQueryService.queryLimit());
    }

    @ApiOperation(value = "查询订单状态")
    @PostMapping("/queryOrderStatus")
    public DataResponse<String> queryOrderStatus(@RequestBody SidEntity reqDTO) {
        return DataResponse.of(faultQueryService.queryOrderStatus(reqDTO));
    }

    @ApiOperation(value = "转报")
    @PostMapping("/changeReport")
    public DataResponse<String> changeReport(@RequestBody FaultReportReqDTO reqDTO) {
        reportService.changeReport(reqDTO);
        return DataResponse.success();
    }

    @ApiOperation(value = "下发")
    @PostMapping("/issue")
    public DataResponse<String> issue(@RequestBody FaultDetailReqDTO reqDTO) {
        faultQueryService.issue(reqDTO);
        return DataResponse.success();
    }

    @ApiOperation(value = "派工")
    @PostMapping("/send/work")
    public DataResponse<String> sendWork(@RequestBody FaultSendWorkReqDTO reqDTO) {
        faultQueryService.sendWork(reqDTO);
        return DataResponse.success();
    }

    /**
     * 完工报告
     * @param reqDTO 完工返回数据
     * @return 成功状态
     */
    @ApiOperation(value = "完工报告")
    @PostMapping("/finish/work")
    public DataResponse<T> finishWork(@RequestBody FaultFinishWorkReqDTO reqDTO) {
        faultQueryService.finishWork(reqDTO);
        return DataResponse.success();
    }

    @ApiOperation(value = "设调确认")
    @PostMapping("/eqCheck")
    public DataResponse<String> eqCheck(@RequestBody FaultEqCheckReqDTO reqDTO) throws Exception {
        faultQueryService.eqCheck(reqDTO);
        return DataResponse.success();
    }

    @ApiOperation(value = "完工确认")
    @PostMapping("/finishConfirm")
    public DataResponse<String> finishConfirm(@RequestBody FaultNosFaultWorkNosReqDTO reqDTO) {
        reqDTO.setType(OrderStatus.WAN_GONG_QUE_REN);
        faultQueryService.updateHandler(reqDTO);
        return DataResponse.success();
    }

    @ApiOperation(value = "作废")
    @PostMapping("/cancel")
    public DataResponse<String> cancel(@RequestBody FaultNosFaultWorkNosReqDTO reqDTO) {
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
        reqDTO.setType(OrderStatus.YAN_SHOU);
        faultQueryService.updateHandler(reqDTO);
        return DataResponse.success();
    }

    @ApiOperation(value = "导出")
    @PostMapping("/export")
    public void export(@RequestBody FaultExportReqDTO reqDTO, HttpServletResponse response) {
        faultQueryService.export(reqDTO, response);
    }

    @ApiOperation(value = "施工计划")
    @PostMapping("/construction")
    public PageResponse<ConstructionResDTO> construction(@RequestBody FaultQueryReqDTO reqDTO) {
        return PageResponse.of(faultQueryService.construction(reqDTO));
    }

    @ApiOperation(value = "请销点")
    @PostMapping("/cancellation")
    public PageResponse<ConstructionResDTO> cancellation(@RequestBody FaultQueryReqDTO reqDTO) {
        return PageResponse.of(faultQueryService.cancellation(reqDTO));
    }

    @ApiOperation(value = "操作前对选中的作前置校验")
    @PostMapping("/fault/track/compareRows")
    public DataResponse<Boolean> compareRows(@RequestBody CompareRowsReqDTO reqDTO) {
        return DataResponse.of(faultQueryService.compareRows(reqDTO));
    }
    @ApiOperation(value = "获取维修部门")
    @GetMapping("/querydept")
    public DataResponse<List<FaultRepairDeptResDTO>> querydept(@RequestParam String faultNo) {
        return DataResponse.of(faultQueryService.querydept(faultNo));
    }

    @ApiOperation(value = "获取维修部门下的人")
    @GetMapping("/queryWorker")
    public DataResponse<List<OrganMajorLineType>> queryWorker(@RequestParam String workerGroupCode) {
        return DataResponse.of(faultQueryService.queryWorker(workerGroupCode));
    }

    /**
     * 跳转至物资系统领料界面
     * @param orderCode 工单号
     */
    @GetMapping("/material/page")
    @ApiOperation(value = "跳转至物资系统领料界面")
    public DataResponse<String> pageMaterial(@RequestParam String orderCode) {
        return DataResponse.of(faultQueryService.pageMaterial(orderCode));
    }
}
