package com.wzmtr.eam.controller.basic;

import com.wzmtr.eam.dto.req.basic.OrgLineReqDTO;
import com.wzmtr.eam.dto.res.basic.OrgLineResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.service.basic.OrgLineService;
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

@Slf4j
@RestController
@RequestMapping("/basic/org-line")
@Api(tags = "基础管理-组织机构线别")
@Validated
public class OrgLineController {

    @Resource
    private OrgLineService orgLineService;

    @GetMapping("/list")
    @ApiOperation(value = "获取组织机构线别列表")
    public PageResponse<OrgLineResDTO> listOrgLine(@RequestParam(required = false) @ApiParam("组织机构") String orgCode,
                                                   @RequestParam(required = false) @ApiParam("线路 01 02") String lineCode,
                                                   @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(orgLineService.listOrgLine(orgCode, lineCode, pageReqDTO));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取组织机构线别详情")
    public DataResponse<OrgLineResDTO> getOrgLineDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(orgLineService.getOrgLineDetail(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增组织机构线别")
    public DataResponse<T> addOrgLine(@RequestBody OrgLineReqDTO orgLineReqDTO) {
        orgLineService.addOrgLine(orgLineReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/modify")
    @ApiOperation(value = "修改组织机构线别")
    public DataResponse<T> modifyOrgLine(@RequestBody OrgLineReqDTO orgLineReqDTO) {
        orgLineService.modifyOrgLine(orgLineReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除组织机构线别")
    public DataResponse<T> deleteOrgLine(@RequestBody BaseIdsEntity baseIdsEntity) {
        orgLineService.deleteOrgLine(baseIdsEntity);
        return DataResponse.success();
    }

    @PostMapping("/export")
    @ApiOperation(value = "导出组织机构线别")
    public void exportOrgLine(@RequestBody BaseIdsEntity baseIdsEntity, HttpServletResponse response) throws IOException {
        if (baseIdsEntity == null || baseIdsEntity.getIds().isEmpty()) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "请先勾选后导出");
        }
        orgLineService.exportOrgLine(baseIdsEntity.getIds(), response);
    }
}
