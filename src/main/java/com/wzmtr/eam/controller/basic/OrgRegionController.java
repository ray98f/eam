package com.wzmtr.eam.controller.basic;

import com.wzmtr.eam.dto.req.basic.OrgRegionReqDTO;
import com.wzmtr.eam.dto.res.basic.OrgRegionResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.service.basic.OrgRegionService;
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
 * 基础管理-组织机构位置
 * @author  Ray
 * @version 1.0
 * @date 2024/03/21
 */
@Slf4j
@RestController
@RequestMapping("/basic/org-region")
@Api(tags = "基础管理-组织机构位置")
@Validated
public class OrgRegionController {

    @Resource
    private OrgRegionService orgMajorService;

    /**
     * 获取组织机构位置列表
     * @param orgCode 组织机构
     * @param regionCode 位置编号
     * @param pageReqDTO 分页参数
     * @return 组织机构位置列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取组织机构位置列表")
    public PageResponse<OrgRegionResDTO> listOrgRegion(@RequestParam(required = false) @ApiParam("组织机构") String orgCode,
                                                       @RequestParam(required = false) @ApiParam("位置编号") String regionCode,
                                                       @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(orgMajorService.listOrgRegion(orgCode, regionCode, pageReqDTO));
    }

    /**
     * 根据专业获取启用的组织机构列表
     * @param regionCode 位置编号
     * @return 启用的组织机构列表
     */
    @GetMapping("/listUse")
    @ApiOperation(value = "根据专业获取启用的组织机构列表")
    public DataResponse<List<OrgRegionResDTO>> listUseOrgRegion(@RequestParam @ApiParam("位置编号") String regionCode) {
        return DataResponse.of(orgMajorService.listUseOrgRegion(regionCode));
    }

    /**
     * 获取组织机构位置详情
     * @param id id
     * @return 组织机构位置详情
     */
    @GetMapping("/detail")
    @ApiOperation(value = "获取组织机构位置详情")
    public DataResponse<OrgRegionResDTO> getOrgRegionDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(orgMajorService.getOrgRegionDetail(id));
    }

    /**
     * 新增组织机构位置
     * @param orgRegionReqDTO 织机构位置参数
     * @return 成功
     */
    @PostMapping("/add")
    @ApiOperation(value = "新增组织机构位置")
    public DataResponse<T> addOrgRegion(@RequestBody OrgRegionReqDTO orgRegionReqDTO) {
        orgMajorService.addOrgRegion(orgRegionReqDTO);
        return DataResponse.success();
    }

    /**
     * 修改组织机构位置
     * @param orgRegionReqDTO 织机构位置参数
     * @return 成功
     */
    @PostMapping("/modify")
    @ApiOperation(value = "修改组织机构位置")
    public DataResponse<T> modifyOrgRegion(@RequestBody OrgRegionReqDTO orgRegionReqDTO) {
        orgMajorService.modifyOrgRegion(orgRegionReqDTO);
        return DataResponse.success();
    }

    /**
     * 删除组织机构位置
     * @param baseIdsEntity ids
     * @return 成功
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除组织机构位置")
    public DataResponse<T> deleteOrgRegion(@RequestBody BaseIdsEntity baseIdsEntity) {
        orgMajorService.deleteOrgRegion(baseIdsEntity);
        return DataResponse.success();
    }

    /**
     * 导出组织机构位置
     * @param baseIdsEntity ids
     * @param response response
     * @throws IOException 流异常
     */
    @PostMapping("/export")
    @ApiOperation(value = "导出组织机构位置")
    public void exportOrgRegion(@RequestBody BaseIdsEntity baseIdsEntity, HttpServletResponse response) throws IOException {
        if (Objects.isNull(baseIdsEntity) || StringUtils.isEmpty(baseIdsEntity.getIds())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "请先勾选后导出");
        }
        orgMajorService.exportOrgRegion(baseIdsEntity.getIds(), response);
    }
}
