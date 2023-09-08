package com.wzmtr.eam.controller.basic;

import com.wzmtr.eam.dto.req.basic.EquipmentCategoryReqDTO;
import com.wzmtr.eam.dto.res.basic.EquipmentCategoryResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.basic.EquipmentCategoryService;
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
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/basic/equipment-category")
@Api(tags = "基础管理-设备分类")
@Validated
public class EquipmentCategoryController {

    @Resource
    private EquipmentCategoryService equipmentCategoryService;

    @GetMapping("/list")
    @ApiOperation(value = "获取设备分类列表")
    public PageResponse<EquipmentCategoryResDTO> listEquipmentCategory(@RequestParam(required = false) @ApiParam("节点名称") String name,
                                                                       @RequestParam(required = false) @ApiParam("节点编号") String code,
                                                                       @RequestParam(required = false) @ApiParam("设备分类代码") String parentId,
                                                                       @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(equipmentCategoryService.listEquipmentCategory(name, code, parentId, pageReqDTO));
    }

    @GetMapping("/listTree")
    @ApiOperation(value = "获取设备分类列表(树状)")
    public DataResponse<List<EquipmentCategoryResDTO>> listEquipmentCategoryTree() {
        return DataResponse.of(equipmentCategoryService.listEquipmentCategoryTree());
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取设备分类详情")
    public DataResponse<EquipmentCategoryResDTO> getEquipmentCategoryDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(equipmentCategoryService.getEquipmentCategoryDetail(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增设备分类")
    public DataResponse<T> addEquipmentCategory(@RequestBody EquipmentCategoryReqDTO equipmentCategoryReqDTO) {
        equipmentCategoryService.addEquipmentCategory(equipmentCategoryReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/modify")
    @ApiOperation(value = "修改设备分类")
    public DataResponse<T> modifyEquipmentCategory(@RequestBody EquipmentCategoryReqDTO equipmentCategoryReqDTO) {
        equipmentCategoryService.modifyEquipmentCategory(equipmentCategoryReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除设备分类")
    public DataResponse<T> deleteEquipmentCategory(@RequestBody BaseIdsEntity baseIdsEntity) {
        equipmentCategoryService.deleteEquipmentCategory(baseIdsEntity);
        return DataResponse.success();
    }

    @GetMapping("/export")
    @ApiOperation(value = "导出设备分类")
    public void exportEquipmentCategory(@RequestParam(required = false) @ApiParam("节点名称") String name,
                                        @RequestParam(required = false) @ApiParam("节点编号") String code,
                                        @RequestParam(required = false) @ApiParam("设备分类代码") String parentId,
                                        HttpServletResponse response) {
        equipmentCategoryService.exportEquipmentCategory(name, code, parentId, response);
    }

    @GetMapping("/getFirst")
    @ApiOperation(value = "获取设备分类一级分类")
    public DataResponse<List<EquipmentCategoryResDTO>> getFirstEquipmentCategory() {
        return DataResponse.of(equipmentCategoryService.getFirstEquipmentCategory());
    }

    @GetMapping("/getChild")
        @ApiOperation(value = "获取设备分类子集")
    public DataResponse<List<EquipmentCategoryResDTO>> getChildEquipmentCategory(@RequestParam String code) {
        return DataResponse.of(equipmentCategoryService.getChildEquipmentCategory(code));
    }
}
