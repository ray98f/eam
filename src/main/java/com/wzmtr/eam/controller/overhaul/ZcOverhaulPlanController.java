package com.wzmtr.eam.controller.overhaul;

import com.wzmtr.eam.dto.req.overhaul.OverhaulObjectReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulPlanListReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulPlanReqDTO;
import com.wzmtr.eam.dto.res.basic.FaultRepairDeptResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulObjectResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulPlanResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulTplDetailResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.overhaul.OverhaulPlanService;
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

/**
 * 预防性检修管理-检修计划（中车）
 * @author  Ray
 * @version 1.0
 * @date 2023/08/11
 */
@Slf4j
@RestController
@RequestMapping("/overhaul/zc")
@Api(tags = "预防性检修管理-检修计划（中车）")
@Validated
public class ZcOverhaulPlanController {

    @Resource
    private OverhaulPlanService overhaulPlanService;

    @GetMapping("/plan/page")
    @ApiOperation(value = "获取检修计划（中车）列表")
    public PageResponse<OverhaulPlanResDTO> pageOverhaulPlan(OverhaulPlanListReqDTO overhaulPlanListReqDTO,
                                                             @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(overhaulPlanService.pageOverhaulPlan(overhaulPlanListReqDTO, pageReqDTO));
    }

    @GetMapping("/plan/detail")
    @ApiOperation(value = "获取检修计划（中车）详情")
    public DataResponse<OverhaulPlanResDTO> getOverhaulPlanDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(overhaulPlanService.getOverhaulPlanDetail(id));
    }

    @GetMapping("/plan/queryDept")
    @ApiOperation(value = "检修计划（中车）获取作业工班")
    public DataResponse<List<FaultRepairDeptResDTO>> queryDept(@RequestParam String lineNo,
                                                               @RequestParam String subjectCode) {
        return DataResponse.of(overhaulPlanService.queryDept(lineNo, subjectCode));
    }

    @PostMapping("/plan/add")
    @ApiOperation(value = "新增检修计划（中车）")
    public DataResponse<T> addOverhaulPlan(@RequestBody OverhaulPlanReqDTO overhaulPlanReqDTO) throws ParseException {
        overhaulPlanService.addOverhaulPlan(overhaulPlanReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/plan/modify")
    @ApiOperation(value = "编辑检修计划（中车）")
    public DataResponse<T> modifyOverhaulPlan(@RequestBody OverhaulPlanReqDTO overhaulPlanReqDTO) {
        overhaulPlanService.modifyOverhaulPlan(overhaulPlanReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/plan/delete")
    @ApiOperation(value = "删除检修计划（中车）")
    public DataResponse<T> deleteOverhaulPlan(@RequestBody BaseIdsEntity baseIdsEntity) {
        overhaulPlanService.deleteOverhaulPlan(baseIdsEntity);
        return DataResponse.success();
    }

    @PostMapping("/plan/trigger")
    @ApiOperation(value = "触发检修计划（中车）")
    public DataResponse<T> triggerOverhaulPlan(@RequestBody OverhaulPlanReqDTO overhaulPlanReqDTO) {
        overhaulPlanService.triggerOverhaulPlan(overhaulPlanReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/plan/submit")
    @ApiOperation(value = "提交检修计划（中车）")
    public DataResponse<T> submitOverhaulPlan(@RequestBody OverhaulPlanReqDTO overhaulPlanReqDTO) throws Exception {
        overhaulPlanService.submitOverhaulPlan(overhaulPlanReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/plan/examine")
    @ApiOperation(value = "审核检修计划（中车）")
    public DataResponse<T> examineOverhaulPlan(@RequestBody OverhaulPlanReqDTO overhaulPlanReqDTO) {
        overhaulPlanService.examineOverhaulPlan(overhaulPlanReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/plan/relation")
    @ApiOperation(value = "检修计划（中车）关联计划")
    public DataResponse<T> relationOverhaulPlan(@RequestBody List<OverhaulPlanReqDTO> list) {
        overhaulPlanService.relationOverhaulPlan(list);
        return DataResponse.success();
    }

    @PostMapping("/plan/switchs")
    @ApiOperation(value = "启用/禁用检修计划（中车）")
    public DataResponse<T> switchsOverhaulPlan(@RequestBody OverhaulPlanReqDTO overhaulPlanReqDTO) {
        overhaulPlanService.switchsOverhaulPlan(overhaulPlanReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/plan/import")
    @ApiOperation(value = "导入检修计划（中车）")
    public DataResponse<T> importOverhaulPlan(@RequestParam MultipartFile file) {
        overhaulPlanService.importOverhaulPlan(file);
        return DataResponse.success();
    }

    @GetMapping("/plan/export")
    @ApiOperation(value = "导出检修计划（中车）")
    public void exportOverhaulPlan(OverhaulPlanListReqDTO overhaulPlanListReqDTO, HttpServletResponse response) throws IOException {
        overhaulPlanService.exportOverhaulPlan(overhaulPlanListReqDTO, response);
    }

    @GetMapping("/object/getTemplates")
    @ApiOperation(value = "获取检修对象（中车）模板")
    public DataResponse<List<OverhaulTplDetailResDTO>> getTemplates(@RequestParam @ApiParam("计划编号") String planCode) {
        return DataResponse.of(overhaulPlanService.getTemplates(planCode));
    }

    @GetMapping("/object/page")
    @ApiOperation(value = "获取检修对象（中车）列表")
    public PageResponse<OverhaulObjectResDTO> pageOverhaulObject(@RequestParam(required = false) @ApiParam("计划编号") String planCode,
                                                                 @RequestParam(required = false) @ApiParam("计划名称") String planName,
                                                                 @RequestParam(required = false) @ApiParam("对象编号") String objectCode,
                                                                 @RequestParam(required = false) @ApiParam("对象名称") String objectName,
                                                                 @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(overhaulPlanService.pageOverhaulObject(planCode, planName, objectCode, objectName, pageReqDTO));
    }

    @GetMapping("/object/detail")
    @ApiOperation(value = "获取检修对象（中车）详情")
    public DataResponse<OverhaulObjectResDTO> getOverhaulObjectDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(overhaulPlanService.getOverhaulObjectDetail(id));
    }

    @PostMapping("/object/add")
    @ApiOperation(value = "新增检修对象（中车）")
    public DataResponse<T> addOverhaulObject(@RequestBody OverhaulObjectReqDTO overhaulObjectReqDTO) {
        overhaulPlanService.addOverhaulObject(overhaulObjectReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/object/modify")
    @ApiOperation(value = "编辑检修对象（中车）")
    public DataResponse<T> modifyOverhaulObject(@RequestBody OverhaulObjectReqDTO overhaulObjectReqDTO) {
        overhaulPlanService.modifyOverhaulObject(overhaulObjectReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/object/delete")
    @ApiOperation(value = "删除检修对象（中车）")
    public DataResponse<T> deleteOverhaulObject(@RequestBody BaseIdsEntity baseIdsEntity) {
        overhaulPlanService.deleteOverhaulObject(baseIdsEntity);
        return DataResponse.success();
    }

    @GetMapping("/object/export")
    @ApiOperation(value = "导出检修对象（中车）")
    public void exportOverhaulObject(@RequestParam(required = false) @ApiParam("计划编号") String planCode,
                                     @RequestParam(required = false) @ApiParam("计划名称") String planName,
                                     @RequestParam(required = false) @ApiParam("对象编号") String objectCode,
                                     @RequestParam(required = false) @ApiParam("对象名称") String objectName,
                                     HttpServletResponse response) throws IOException {
        overhaulPlanService.exportOverhaulObject(planCode, planName, objectCode, objectName, response);
    }
}
