package com.wzmtr.eam.controller.basic;

import com.wzmtr.eam.dto.req.OrgTypeReqDTO;
import com.wzmtr.eam.dto.res.OrgTypeResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.basic.OrgTypeService;
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
@RequestMapping("/basic/org-type")
@Api(tags = "基础管理-组织机构类别")
@Validated
public class OrgTypeController {

    @Resource
    private OrgTypeService orgTypeService;

    @GetMapping("/list")
    @ApiOperation(value = "获取组织机构类别列表")
    public PageResponse<OrgTypeResDTO> listOrgType(@RequestParam(required = false) @ApiParam("组织机构") String orgCode,
                                                   @RequestParam(required = false) @ApiParam("类别 10 20 30") String orgType,
                                                   @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(orgTypeService.listOrgType(orgCode, orgType, pageReqDTO));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取组织机构类别详情")
    public DataResponse<OrgTypeResDTO> getOrgTypeDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(orgTypeService.getOrgTypeDetail(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增组织机构类别")
    public DataResponse<T> addOrgType(@RequestBody OrgTypeReqDTO orgMajorReqDTO) {
        orgTypeService.addOrgType(orgMajorReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/modify")
    @ApiOperation(value = "修改组织机构类别")
    public DataResponse<T> modifyOrgType(@RequestBody OrgTypeReqDTO orgMajorReqDTO) {
        orgTypeService.modifyOrgType(orgMajorReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除组织机构类别")
    public DataResponse<T> deleteOrgType(@RequestBody BaseIdsEntity baseIdsEntity) {
        orgTypeService.deleteOrgType(baseIdsEntity);
        return DataResponse.success();
    }

    @GetMapping("/export")
    @ApiOperation(value = "导出组织机构类别")
    public void exportOrgType(@RequestParam(required = false) @ApiParam("组织机构") String orgCode,
                              @RequestParam(required = false) @ApiParam("类别") String orgType,
                              HttpServletResponse response) {
        orgTypeService.exportOrgType(orgCode, orgType, response);
    }
}
