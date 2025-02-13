package com.wzmtr.eam.controller.basic;

import com.wzmtr.eam.dto.req.basic.OrgMajorReqDTO;
import com.wzmtr.eam.dto.res.basic.OrgMajorResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.service.basic.OrgMajorService;
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
import java.util.List;
import java.util.Objects;

/**
 * 基础管理-组织机构专业
 * @author  Ray
 * @version 1.0
 * @date 2023/07/20
 */
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

    @GetMapping("/listUse")
    @ApiOperation(value = "根据专业获取启用的组织机构列表")
    public DataResponse<List<OrgMajorResDTO>> listUseOrgMajor(@RequestParam @ApiParam("专业 1-15 17 70") String majorCode) {
        return DataResponse.of(orgMajorService.listUseOrgMajor(majorCode));
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

    @PostMapping("/export")
    @ApiOperation(value = "导出组织机构专业")
    public void exportOrgMajor(@RequestBody BaseIdsEntity baseIdsEntity, HttpServletResponse response) throws IOException {
        if (Objects.isNull(baseIdsEntity) || StringUtils.isEmpty(baseIdsEntity.getIds())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "请先勾选后导出");
        }
        orgMajorService.exportOrgMajor(baseIdsEntity.getIds(), response);
    }
}
