package com.wzmtr.eam.controller.overhaul;

import com.wzmtr.eam.dto.req.overhaul.*;
import com.wzmtr.eam.dto.res.basic.FaultRepairDeptResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulObjectResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulPlanResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulTplDetailResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulWeekPlanResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.service.overhaul.OverhaulWeekPlanService;
import com.wzmtr.eam.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Objects;

/**
 * 预防性检修管理-检修周计划（中铁通）
 * @author  Ray
 * @version 1.0
 * @date 2023/08/17
 */
@Slf4j
@RestController
@RequestMapping("/overhaul/ztt")
@Api(tags = "预防性检修管理-检修周计划（中铁通）")
@Validated
public class ZttOverhaulWeekPlanController {

    @Resource
    private OverhaulWeekPlanService overhaulWeekPlanService;

    @GetMapping("/weekPlan/page")
    @ApiOperation(value = "获取检修周计划（中铁通）列表")
    public PageResponse<OverhaulWeekPlanResDTO> pageOverhaulWeekPlan(OverhaulWeekPlanListReqDTO overhaulWeekPlanListReqDTO,
                                                                     @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(overhaulWeekPlanService.pageOverhaulWeekPlan(overhaulWeekPlanListReqDTO, pageReqDTO));
    }

    @GetMapping("/weekPlan/detail")
    @ApiOperation(value = "获取检修周计划（中铁通）详情")
    public DataResponse<OverhaulWeekPlanResDTO> getOverhaulWeekPlanDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(overhaulWeekPlanService.getOverhaulWeekPlanDetail(id));
    }

    /**
     * 检修周计划（中铁通）获取作业工班
     * @param lineNo 线路编号
     * @param subjectCode 系统编号
     * @return 作业工班列表
     */
    @GetMapping("/weekPlan/queryDept")
    @ApiOperation(value = "检修周计划（中铁通）获取作业工班")
    public DataResponse<List<FaultRepairDeptResDTO>> queryDept(@RequestParam String lineNo,
                                                               @RequestParam String subjectCode) {
        return DataResponse.of(overhaulWeekPlanService.queryDept(lineNo, subjectCode));
    }

    @PostMapping("/weekPlan/add")
    @ApiOperation(value = "新增检修周计划（中铁通）")
    public DataResponse<T> addOverhaulWeekPlan(@RequestBody OverhaulWeekPlanReqDTO overhaulWeekPlanReqDTO) throws ParseException {
        overhaulWeekPlanService.addOverhaulWeekPlan(overhaulWeekPlanReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/weekPlan/modify")
    @ApiOperation(value = "编辑检修周计划（中铁通）")
    public DataResponse<T> modifyOverhaulWeekPlan(@RequestBody OverhaulWeekPlanReqDTO overhaulWeekPlanReqDTO) {
        overhaulWeekPlanService.modifyOverhaulWeekPlan(overhaulWeekPlanReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/weekPlan/delete")
    @ApiOperation(value = "删除检修周计划（中铁通）")
    public DataResponse<T> deleteOverhaulWeekPlan(@RequestBody BaseIdsEntity baseIdsEntity) {
        overhaulWeekPlanService.deleteOverhaulWeekPlan(baseIdsEntity);
        return DataResponse.success();
    }

    @PostMapping("/weekPlan/trigger")
    @ApiOperation(value = "触发检修周计划（中铁通）")
    public DataResponse<T> triggerOverhaulWeekPlan(@RequestBody OverhaulWeekPlanReqDTO overhaulWeekPlanReqDTO) throws Exception {
        overhaulWeekPlanService.triggerOverhaulWeekPlan(overhaulWeekPlanReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/weekPlan/submit")
    @ApiOperation(value = "提交检修周计划（中铁通）")
    public DataResponse<T> submitOverhaulWeekPlan(@RequestBody OverhaulWeekPlanReqDTO overhaulWeekPlanReqDTO) throws Exception {
        overhaulWeekPlanService.submitOverhaulWeekPlan(overhaulWeekPlanReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/weekPlan/examine")
    @ApiOperation(value = "审核检修周计划（中铁通）")
    public DataResponse<T> examineOverhaulWeekPlan(@RequestBody OverhaulWeekPlanReqDTO overhaulWeekPlanReqDTO) throws Exception {
        overhaulWeekPlanService.examineOverhaulWeekPlan(overhaulWeekPlanReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/weekPlan/export")
    @ApiOperation(value = "导出检修周计划（中铁通）")
    public void exportOverhaulWeekPlan(@RequestBody BaseIdsEntity baseIdsEntity, HttpServletResponse response) throws IOException {
        if (Objects.isNull(baseIdsEntity) || StringUtils.isEmpty(baseIdsEntity.getIds())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "请先勾选后导出");
        }
        overhaulWeekPlanService.exportOverhaulWeekPlan(baseIdsEntity.getIds(), response);
    }

    @GetMapping("/plan/page")
    @ApiOperation(value = "获取检修计划（中铁通）列表")
    public PageResponse<OverhaulPlanResDTO> pageOverhaulPlan(OverhaulPlanListReqDTO overhaulPlanListReqDTO,
                                                             @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(overhaulWeekPlanService.pageOverhaulPlan(overhaulPlanListReqDTO, pageReqDTO));
    }

    @GetMapping("/plan/detail")
    @ApiOperation(value = "获取检修计划（中铁通）详情")
    public DataResponse<OverhaulPlanResDTO> getOverhaulPlanDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(overhaulWeekPlanService.getOverhaulPlanDetail(id));
    }

    @PostMapping("/plan/add")
    @ApiOperation(value = "新增检修计划（中铁通）")
    public DataResponse<T> addOverhaulPlan(@RequestBody OverhaulPlanReqDTO overhaulPlanReqDTO) throws ParseException {
        overhaulWeekPlanService.addOverhaulPlan(overhaulPlanReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/plan/modify")
    @ApiOperation(value = "编辑检修计划（中铁通）")
    public DataResponse<T> modifyOverhaulPlan(@RequestBody OverhaulPlanReqDTO overhaulPlanReqDTO) {
        overhaulWeekPlanService.modifyOverhaulPlan(overhaulPlanReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/plan/delete")
    @ApiOperation(value = "删除检修计划（中铁通）")
    public DataResponse<T> deleteOverhaulPlan(@RequestBody BaseIdsEntity baseIdsEntity) {
        overhaulWeekPlanService.deleteOverhaulPlan(baseIdsEntity);
        return DataResponse.success();
    }

    @GetMapping("/plan/import")
    @ApiOperation(value = "导入检修计划（中铁通）")
    public DataResponse<T> importOverhaulPlan(@RequestParam MultipartFile file) {
        overhaulWeekPlanService.importOverhaulPlan(file);
        return DataResponse.success();
    }

    @PostMapping("/plan/export")
    @ApiOperation(value = "导出检修计划（中铁通）")
    public void exportOverhaulPlan(@RequestBody OverhaulPlanListReqDTO overhaulPlanListReqDTO,
                                   HttpServletResponse response) throws IOException {
        overhaulWeekPlanService.exportOverhaulPlan(overhaulPlanListReqDTO, response);
    }

    @GetMapping("/object/getTemplates")
    @ApiOperation(value = "获取检修对象（中铁通）模板")
    public DataResponse<List<OverhaulTplDetailResDTO>> getTemplates(@RequestParam @ApiParam("计划编号") String planCode) {
        return DataResponse.of(overhaulWeekPlanService.getTemplates(planCode));
    }

    @GetMapping("/object/page")
    @ApiOperation(value = "获取检修对象（中铁通）列表")
    public PageResponse<OverhaulObjectResDTO> pageOverhaulObject(@RequestParam(required = false) @ApiParam("计划编号") String planCode,
                                                                 @RequestParam(required = false) @ApiParam("计划名称") String planName,
                                                                 @RequestParam(required = false) @ApiParam("对象编号") String objectCode,
                                                                 @RequestParam(required = false) @ApiParam("对象名称") String objectName,
                                                                 @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(overhaulWeekPlanService.pageOverhaulObject(planCode, planName, objectCode, objectName, pageReqDTO));
    }

    @GetMapping("/object/detail")
    @ApiOperation(value = "获取检修对象（中铁通）详情")
    public DataResponse<OverhaulObjectResDTO> getOverhaulObjectDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(overhaulWeekPlanService.getOverhaulObjectDetail(id));
    }

    @PostMapping("/object/add")
    @ApiOperation(value = "新增检修对象（中铁通）")
    public DataResponse<T> addOverhaulObject(@RequestBody OverhaulObjectReqDTO overhaulObjectReqDTO) {
        overhaulWeekPlanService.addOverhaulObject(overhaulObjectReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/object/modify")
    @ApiOperation(value = "编辑检修对象（中铁通）")
    public DataResponse<T> modifyOverhaulObject(@RequestBody OverhaulObjectReqDTO overhaulObjectReqDTO) {
        overhaulWeekPlanService.modifyOverhaulObject(overhaulObjectReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/object/delete")
    @ApiOperation(value = "删除检修对象（中铁通）")
    public DataResponse<T> deleteOverhaulObject(@RequestBody BaseIdsEntity baseIdsEntity) {
        overhaulWeekPlanService.deleteOverhaulObject(baseIdsEntity);
        return DataResponse.success();
    }

    @GetMapping("/object/export")
    @ApiOperation(value = "导出检修对象（中铁通）")
    public void exportOverhaulObject(@RequestParam(required = false) @ApiParam("计划编号") String planCode,
                                     @RequestParam(required = false) @ApiParam("计划名称") String planName,
                                     @RequestParam(required = false) @ApiParam("对象编号") String objectCode,
                                     @RequestParam(required = false) @ApiParam("对象名称") String objectName,
                                     HttpServletResponse response) throws IOException {
        overhaulWeekPlanService.exportOverhaulObject(planCode, planName, objectCode, objectName, response);
    }
}
