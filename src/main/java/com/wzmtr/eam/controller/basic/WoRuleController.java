package com.wzmtr.eam.controller.basic;

import com.wzmtr.eam.dto.req.basic.WoRuleDetailExportReqDTO;
import com.wzmtr.eam.dto.req.basic.WoRuleReqDTO;
import com.wzmtr.eam.dto.res.basic.WoRuleResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.service.basic.WoRuleService;
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
 * 基础管理-工单触发规则
 * @author  Ray
 * @version 1.0
 * @date 2023/07/20
 */
@Slf4j
@RestController
@RequestMapping("/basic/wo-rule")
@Api(tags = "基础管理-工单触发规则")
@Validated
public class WoRuleController {

    @Resource
    private WoRuleService woRuleService;

    @GetMapping("/list")
    @ApiOperation(value = "获取工单触发规则列表")
    public PageResponse<WoRuleResDTO> listWoRule(@RequestParam(required = false) @ApiParam("规则编号") String ruleCode,
                                                 @RequestParam(required = false) @ApiParam("规则名称") String ruleName,
                                                 @RequestParam(required = false) @ApiParam("用途 10 20 30") String ruleUseage,
                                                 @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(woRuleService.listWoRule(ruleCode, ruleName, ruleUseage, pageReqDTO));
    }

    @GetMapping("/get")
    @ApiOperation(value = "获取工单触发规则详情")
    public DataResponse<WoRuleResDTO> getWoRule(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(woRuleService.getWoRule(id));
    }

    @GetMapping("/detail/list")
    @ApiOperation(value = "获取工单触发规则明细列表")
    public PageResponse<WoRuleResDTO.WoRuleDetail> listWoRuleDetail(@RequestParam @ApiParam("规则编号") String ruleCode,
                                                                    @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(woRuleService.listWoRuleDetail(ruleCode, pageReqDTO));
    }

    @GetMapping("/detail/get")
    @ApiOperation(value = "获取工单触发规则明细详情")
    public DataResponse<WoRuleResDTO.WoRuleDetail> getWoRuleDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(woRuleService.getWoRuleDetail(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增工单触发规则")
    public DataResponse<T> addWoRule(@RequestBody WoRuleReqDTO woRuleReqDTO) {
        woRuleService.addWoRule(woRuleReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/detail/add")
    @ApiOperation(value = "新增工单触发规则明细")
    public DataResponse<T> addWoRuleDetail(@RequestBody WoRuleReqDTO.WoRuleDetail woRuleDetail) {
        woRuleService.addWoRuleDetail(woRuleDetail);
        return DataResponse.success();
    }

    @PostMapping("/modify")
    @ApiOperation(value = "修改工单触发规则")
    public DataResponse<T> modifyWoRule(@RequestBody WoRuleReqDTO woRuleReqDTO) {
        woRuleService.modifyWoRule(woRuleReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/detail/modify")
    @ApiOperation(value = "修改工单触发规则明细")
    public DataResponse<T> modifyWoRuleDetail(@RequestBody WoRuleReqDTO.WoRuleDetail woRuleDetail) {
        woRuleService.modifyWoRuleDetail(woRuleDetail);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除工单触发规则")
    public DataResponse<T> deleteWoRule(@RequestBody BaseIdsEntity baseIdsEntity) {
        woRuleService.deleteWoRule(baseIdsEntity);
        return DataResponse.success();
    }

    @PostMapping("/detail/delete")
    @ApiOperation(value = "删除工单触发规则明细")
    public DataResponse<T> deleteWoRuleDetail(@RequestBody BaseIdsEntity baseIdsEntity) {
        woRuleService.deleteWoRuleDetail(baseIdsEntity);
        return DataResponse.success();
    }

    @PostMapping("/export")
    @ApiOperation(value = "导出工单触发规则")
    public void exportWoRule(@RequestBody BaseIdsEntity baseIdsEntity, HttpServletResponse response) throws IOException {
        if (Objects.isNull(baseIdsEntity) || StringUtils.isEmpty(baseIdsEntity.getIds())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "请先勾选后导出");
        }
        woRuleService.exportWoRule(baseIdsEntity.getIds(), response);
    }

    @PostMapping("/detail/export")
    @ApiOperation(value = "导出工单触发规则明细")
    public void exportWoRuleDetail(@RequestBody WoRuleDetailExportReqDTO reqDTO, HttpServletResponse response) throws IOException {
        woRuleService.exportWoRuleDetail(reqDTO, response);
    }
}
