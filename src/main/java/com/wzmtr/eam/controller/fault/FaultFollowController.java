package com.wzmtr.eam.controller.fault;

import com.wzmtr.eam.dto.req.fault.FaultFollowExportReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultFollowReportReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultFollowReqDTO;
import com.wzmtr.eam.dto.res.fault.FaultFollowDispatchUserResDTO;
import com.wzmtr.eam.dto.res.fault.FaultFollowReportResDTO;
import com.wzmtr.eam.dto.res.fault.FaultFollowResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.SysUser;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.fault.FaultFollowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * 故障管理-故障跟踪(新)
 * @author  Ray
 * @version 1.0
 * @date 2024/05/09
 */
@RestController
@RequestMapping("/fault/follow/order")
@Api(tags = "故障管理-故障跟踪(新)")
public class FaultFollowController {

    @Autowired
    private FaultFollowService faultFollowService;

    /**
     * 分页查询故障跟踪工单列表
     * @param followNo 跟踪编号
     * @param faultWorkNo 故障单号
     * @param followStatus 跟踪状态
     * @param pageReqDTO 分页参数
     * @return 故障跟踪工单列表
     */
    @ApiOperation(value = "分页查询故障跟踪工单列表")
    @GetMapping("/list")
    public PageResponse<FaultFollowResDTO> page(@RequestParam(required = false) String followNo,
                                                @RequestParam(required = false) String faultWorkNo,
                                                @RequestParam(required = false) String followStatus,
                                                @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(faultFollowService.page(followNo, faultWorkNo, followStatus, pageReqDTO));
    }

    /**
     * 查询故障跟踪工单详情
     * @param id id
     * @return 故障跟踪工单详情
     */
    @ApiOperation(value = "查询故障跟踪工单详情")
    @GetMapping("/detail")
    public DataResponse<FaultFollowResDTO> detail(@RequestParam String id) {
        return DataResponse.of(faultFollowService.detail(id));
    }

    /**
     * 获取工班长列表
     * @param faultWorkNo 故障单号
     * @return 工班长列表
     */
    @ApiOperation(value = "获取工班长列表")
    @GetMapping("/leader/list")
    public DataResponse<List<SysUser>> listLeader(@RequestParam String faultWorkNo) {
        return DataResponse.of(faultFollowService.listLeader(faultWorkNo));
    }

    /**
     * 新增故障跟踪工单
     * @param req 故障跟踪工单参数
     * @return 成功
     */
    @ApiOperation(value = "新增故障跟踪工单")
    @PostMapping("/add")
    public DataResponse<T> add(@RequestBody FaultFollowReqDTO req) {
        faultFollowService.add(req);
        return DataResponse.success();
    }

    /**
     * 编辑故障跟踪工单
     * @param req 故障跟踪工单参数
     * @return 成功
     */
    @ApiOperation(value = "编辑故障跟踪工单")
    @PostMapping("/modify")
    public DataResponse<T> modify(@RequestBody FaultFollowReqDTO req) {
        faultFollowService.modify(req);
        return DataResponse.success();
    }

    /**
     * 获取派工人信息
     * @param followNo 跟踪编号
     * @return 派工人信息
     */
    @ApiOperation(value = "获取派工人信息")
    @GetMapping("/dispatch/user")
    public DataResponse<FaultFollowDispatchUserResDTO> listDispatchUser(@RequestParam String followNo) {
        return DataResponse.of(faultFollowService.listDispatchUser(followNo));
    }

    /**
     * 派工故障跟踪工单
     * @param req 故障跟踪工单参数
     * @return 成功
     */
    @ApiOperation(value = "派工故障跟踪工单")
    @PostMapping("/dispatch")
    public DataResponse<T> dispatch(@RequestBody FaultFollowReqDTO req) {
        faultFollowService.dispatch(req);
        return DataResponse.success();
    }

    /**
     * 强制关闭故障跟踪工单
     * @param req 故障跟踪工单参数
     * @return 成功
     */
    @ApiOperation(value = "强制关闭故障跟踪工单")
    @PostMapping("/close")
    public DataResponse<T> close(@RequestBody FaultFollowReqDTO req) {
        faultFollowService.close(req);
        return DataResponse.success();
    }

    /**
     * 导出故障跟踪工单
     * @param req 导出查询参数
     * @param response response
     * @throws IOException 异常
     */
    @ApiOperation(value = "导出故障跟踪工单")
    @PostMapping("/export")
    public void export(@RequestBody FaultFollowExportReqDTO req, HttpServletResponse response) throws IOException {
        faultFollowService.export(req, response);
    }

    /**
     * 获取故障跟踪工单报告列表
     * @param followNo 故障跟踪工单编号
     * @param type 查询类型 1查询全部 2查询未审核的报告
     * @return 跟踪工单报告列表
     */
    @ApiOperation(value = "获取故障跟踪工单报告列表")
    @GetMapping("/report/list")
    public DataResponse<List<FaultFollowReportResDTO>> listReport(@RequestParam String followNo,
                                                                  @RequestParam String type) {
        return DataResponse.of(faultFollowService.listReport(followNo, type));
    }

    /**
     * 查询故障跟踪工单报告详情
     * @param id id
     * @return 跟踪工单报告详情
     */
    @ApiOperation(value = "查询故障跟踪工单报告详情")
    @GetMapping("/report/detail")
    public DataResponse<FaultFollowReportResDTO> getReportDetail(@RequestParam String id) {
        return DataResponse.of(faultFollowService.getReportDetail(id));
    }

    /**
     * 新增故障跟踪工单报告
     * @param req 故障跟踪工单报告参数
     * @return 成功
     * @throws ParseException 异常
     */
    @ApiOperation(value = "新增故障跟踪工单报告")
    @PostMapping("/report/add")
    public DataResponse<T> addReport(@RequestBody FaultFollowReportReqDTO req) throws ParseException {
        faultFollowService.addReport(req);
        return DataResponse.success();
    }

    /**
     * 审核故障跟踪工单报告
     * @param req 故障跟踪工单报告参数
     * @return 成功
     * @throws ParseException 异常
     */
    @ApiOperation(value = "审核故障跟踪工单报告")
    @PostMapping("/report/examine")
    public DataResponse<T> examineReport(@RequestBody FaultFollowReportReqDTO req) throws ParseException {
        faultFollowService.examineReport(req);
        return DataResponse.success();
    }
}
