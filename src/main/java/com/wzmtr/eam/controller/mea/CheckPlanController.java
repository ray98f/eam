package com.wzmtr.eam.controller.mea;

import com.wzmtr.eam.dto.req.mea.CheckPlanListReqDTO;
import com.wzmtr.eam.dto.req.mea.CheckPlanReqDTO;
import com.wzmtr.eam.dto.req.mea.MeaInfoQueryReqDTO;
import com.wzmtr.eam.dto.req.mea.MeaInfoReqDTO;
import com.wzmtr.eam.dto.res.mea.CheckPlanResDTO;
import com.wzmtr.eam.dto.res.mea.MeaInfoResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.service.mea.CheckPlanService;
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
import java.util.Objects;

/**
 * 预防性检修管理-定检计划
 * @author  Ray
 * @version 1.0
 * @date 2023/08/17
 */
@Slf4j
@RestController
@RequestMapping("/mea/checkPlan")
@Api(tags = "计量器具管理-定检计划")
@Validated
public class CheckPlanController {

    @Resource
    private CheckPlanService checkPlanService;

    @GetMapping("/page")
    @ApiOperation(value = "获取定检计划列表")
    public PageResponse<CheckPlanResDTO> listCheckPlan(CheckPlanListReqDTO checkPlanListReqDTO, @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(checkPlanService.pageCheckPlan(checkPlanListReqDTO, pageReqDTO));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取定检计划详情")
    public DataResponse<CheckPlanResDTO> getCheckPlanDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(checkPlanService.getCheckPlanDetail(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增定检计划")
    public DataResponse<T> addCheckPlan(@RequestBody CheckPlanReqDTO checkPlanReqDTO) {
        checkPlanService.addCheckPlan(checkPlanReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/modify")
    @ApiOperation(value = "编辑定检计划")
    public DataResponse<T> modifyCheckPlan(@RequestBody CheckPlanReqDTO checkPlanReqDTO) {
        checkPlanService.modifyCheckPlan(checkPlanReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除定检计划")
    public DataResponse<T> deleteCheckPlan(@RequestBody BaseIdsEntity baseIdsEntity) {
        checkPlanService.deleteCheckPlan(baseIdsEntity);
        return DataResponse.success();
    }

    @PostMapping("/submit")
    @ApiOperation(value = "提交定检计划")
    public DataResponse<T> submitCheckPlan(@RequestBody CheckPlanReqDTO checkPlanReqDTO) throws Exception {
        checkPlanService.submitCheckPlan(checkPlanReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/examine")
    @ApiOperation(value = "审核定检计划")
    public DataResponse<T> examineCheckPlan(@RequestBody CheckPlanReqDTO checkPlanReqDTO) {
        checkPlanService.examineCheckPlan(checkPlanReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/export")
    @ApiOperation(value = "导出定检计划")
    public void exportCheckPlan(@RequestBody BaseIdsEntity baseIdsEntity, HttpServletResponse response) throws IOException {
        if (Objects.isNull(baseIdsEntity) || StringUtils.isEmpty(baseIdsEntity.getIds())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "请先勾选后导出");
        }
        checkPlanService.exportCheckPlan(baseIdsEntity.getIds(), response);
    }

    @GetMapping("/info/page")
    @ApiOperation(value = "获取定检计划明细列表")
    public PageResponse<MeaInfoResDTO> pageCheckPlanInfo(@RequestParam(required = false) @ApiParam("计量器具代码") String equipCode,
                                                         @RequestParam(required = false) @ApiParam("计量器具检定计划号") String instrmPlanNo,
                                                         @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(checkPlanService.pageCheckPlanInfo(equipCode, instrmPlanNo, pageReqDTO));
    }

    @GetMapping("/info/detail")
    @ApiOperation(value = "获取定检计划明细详情")
    public DataResponse<MeaInfoResDTO> getCheckPlanInfoDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(checkPlanService.getCheckPlanInfoDetail(id));
    }

    @PostMapping("/info/add")
    @ApiOperation(value = "新增定检计划明细")
    public DataResponse<T> addCheckPlanInfo(@RequestBody MeaInfoReqDTO meaInfoReqDTO) {
        checkPlanService.addCheckPlanInfo(meaInfoReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/info/modify")
    @ApiOperation(value = "编辑定检计划明细")
    public DataResponse<T> modifyCheckPlanInfo(@RequestBody MeaInfoReqDTO meaInfoReqDTO) {
        checkPlanService.modifyCheckPlanInfo(meaInfoReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/info/delete")
    @ApiOperation(value = "删除定检计划明细")
    public DataResponse<T> deleteCheckPlanInfo(@RequestBody BaseIdsEntity baseIdsEntity) {
        checkPlanService.deleteCheckPlanInfo(baseIdsEntity);
        return DataResponse.success();
    }

    @PostMapping("/info/export")
    @ApiOperation(value = "导出定检计划明细")
    public void exportCheckPlanInfo(@RequestBody BaseIdsEntity baseIdsEntity, HttpServletResponse response) throws IOException {
        if (Objects.isNull(baseIdsEntity) || StringUtils.isEmpty(baseIdsEntity.getIds())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "请先勾选后导出");
        }
        checkPlanService.exportCheckPlanInfo(baseIdsEntity.getIds(), response);
    }

    @GetMapping("/info/query")
    @ApiOperation(value = "筛选查询定检计划明细列表")
    public PageResponse<MeaInfoResDTO> queryCheckPlanInfo(MeaInfoQueryReqDTO meaInfoQueryReqDTO, @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(checkPlanService.queryCheckPlanInfo(meaInfoQueryReqDTO, pageReqDTO));
    }
}
