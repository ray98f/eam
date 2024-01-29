package com.wzmtr.eam.controller.basic;

import com.wzmtr.eam.dto.req.basic.RegionReqDTO;
import com.wzmtr.eam.dto.res.basic.RegionResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.service.basic.RegionService;
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

@Slf4j
@RestController
@RequestMapping("/basic/region")
@Api(tags = "基础管理-位置分类")
@Validated
public class RegionController {

    @Resource
    private RegionService regionService;

    @GetMapping("/list")
    @ApiOperation(value = "获取位置分类列表")
    public PageResponse<RegionResDTO> listRegion(@RequestParam(required = false) @ApiParam("节点名称") String name,
                                                 @RequestParam(required = false) @ApiParam("节点编号") String code,
                                                 @RequestParam(required = false) @ApiParam("设备分类代码") String parentId,
                                                 @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(regionService.listRegion(name, code, parentId, pageReqDTO));
    }

    @GetMapping("/listTree")
    @ApiOperation(value = "获取位置分类列表(树状)")
    public DataResponse<List<RegionResDTO>> listRegionTree() {
        return DataResponse.of(regionService.listRegionTree());
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取位位置分类详情")
    public DataResponse<RegionResDTO> getRegionDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(regionService.getRegionDetail(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增位置分类")
    public DataResponse<T> addRegion(@RequestBody RegionReqDTO regionReqDTO) {
        regionService.addRegion(regionReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/modify")
    @ApiOperation(value = "修改位置分类")
    public DataResponse<T> modifyRegion(@RequestBody RegionReqDTO regionReqDTO) {
        regionService.modifyRegion(regionReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除位置分类")
    public DataResponse<T> deleteRegion(@RequestBody BaseIdsEntity baseIdsEntity) {
        regionService.deleteRegion(baseIdsEntity);
        return DataResponse.success();
    }

    @PostMapping("/export")
    @ApiOperation(value = "导出位置分类")
    public void exportRegion(@RequestBody BaseIdsEntity baseIdsEntity, HttpServletResponse response) throws IOException {
        if (baseIdsEntity == null || baseIdsEntity.getIds().isEmpty()) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "请先勾选后导出");
        }
        regionService.exportRegion(baseIdsEntity.getIds(), response);
    }
}
