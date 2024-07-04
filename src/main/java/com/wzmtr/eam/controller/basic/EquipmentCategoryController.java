package com.wzmtr.eam.controller.basic;

import com.wzmtr.eam.dto.req.basic.EquipmentCategoryReqDTO;
import com.wzmtr.eam.dto.res.basic.EquipmentCategoryPartResDTO;
import com.wzmtr.eam.dto.res.basic.EquipmentCategoryResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.service.basic.EquipmentCategoryService;
import com.wzmtr.eam.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * 基础管理-设备分类
 * @author  Ray
 * @version 1.0
 * @date 2023/07/20
 */
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

    @PostMapping("/export")
    @ApiOperation(value = "导出设备分类")
    public void exportEquipmentCategory(@RequestBody BaseIdsEntity baseIdsEntity, HttpServletResponse response) throws IOException {
        if (Objects.isNull(baseIdsEntity) || StringUtils.isEmpty(baseIdsEntity.getIds())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "请先勾选后导出");
        }
        equipmentCategoryService.exportEquipmentCategory(baseIdsEntity.getIds(), response);
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

    /**
     * 获取设备分类绑定的部件列表
     * @param majorCode 专业编码
     * @param systemCode 系统编码
     * @param equipTypeCode 设备分类编码
     * @return 设备分类绑定的部件列表
     */
    @GetMapping("/part/list")
    @ApiOperation(value = "获取设备分类绑定的部件列表")
    public DataResponse<List<EquipmentCategoryPartResDTO>> listEquipmentCategoryPart(@RequestParam(required = false) String majorCode,
                                                                                     @RequestParam(required = false) String systemCode,
                                                                                     @RequestParam(required = false) String equipTypeCode) {
        return DataResponse.of(equipmentCategoryService.listEquipmentCategoryPart(majorCode, systemCode, equipTypeCode));
    }

    /**
     * 导入设备分类绑定的部件列表
     * @param file 文件
     * @return 成功
     */
    @PostMapping("/part/import")
    @ApiOperation(value = "导入设备分类绑定的部件列表")
    public DataResponse<T> importEquipmentCategoryPart(@RequestParam MultipartFile file) {
        equipmentCategoryService.importEquipmentCategoryPart(file);
        return DataResponse.success();
    }
}
