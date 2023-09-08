package com.wzmtr.eam.controller.basic;

import com.wzmtr.eam.dto.req.basic.OrgMajorReqDTO;
import com.wzmtr.eam.dto.res.basic.OrgMajorResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.basic.OrgMajorService;
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

@Slf4j
@RestController
@RequestMapping("/basic/org-major")
@Api(tags = "基础管理-组织机构专业")
@Validated
public class OrgMajorController {

    @Resource
    private OrgMajorService orgMajorService;

    @GetMapping("/list")
    @ApiOperation(value = "获取组织机构专业列表")
    public PageResponse<OrgMajorResDTO> listOrgMajor(@RequestParam(required = false) @ApiParam("组织机构") String orgCode,
                                                     @RequestParam(required = false) @ApiParam("专业 1-15 17 70") String majorCode,
                                                     @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(orgMajorService.listOrgMajor(orgCode, majorCode, pageReqDTO));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取组织机构专业详情")
    public DataResponse<OrgMajorResDTO> getOrgMajorDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(orgMajorService.getOrgMajorDetail(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增组织机构专业")
    public DataResponse<T> addOrgMajor(@RequestBody OrgMajorReqDTO orgMajorReqDTO) {
        orgMajorService.addOrgMajor(orgMajorReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/modify")
    @ApiOperation(value = "修改组织机构专业")
    public DataResponse<T> modifyOrgMajor(@RequestBody OrgMajorReqDTO orgMajorReqDTO) {
        orgMajorService.modifyOrgMajor(orgMajorReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除组织机构专业")
    public DataResponse<T> deleteOrgMajor(@RequestBody BaseIdsEntity baseIdsEntity) {
        orgMajorService.deleteOrgMajor(baseIdsEntity);
        return DataResponse.success();
    }

    @GetMapping("/export")
    @ApiOperation(value = "导出组织机构专业")
    public void exportOrgMajor(@RequestParam(required = false) @ApiParam("组织机构") String orgCode,
                               @RequestParam(required = false) @ApiParam("专业") String majorCode,
                               HttpServletResponse response) {
        orgMajorService.exportOrgMajor(orgCode, majorCode, response);
    }
}
