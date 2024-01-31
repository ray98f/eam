package com.wzmtr.eam.controller.special.equip;

import com.wzmtr.eam.dto.req.special.equip.SpecialEquipTypeReqDTO;
import com.wzmtr.eam.dto.res.special.equip.SpecialEquipTypeResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.service.special.equip.SpecialEquipTypeService;
import com.wzmtr.eam.utils.StringUtils;
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
import java.util.List;
import java.util.Objects;

/**
 * 特种设备管理-特种设备分类管理
 * @author  Ray
 * @version 1.0
 * @date 2024/01/30
 */
@Slf4j
@RestController
@RequestMapping("/special/equip/type")
@Api(tags = "特种设备管理-特种设备分类管理")
@Validated
public class SpecialEquipTypeController {

    @Resource
    private SpecialEquipTypeService specialEquipTypeService;

    /**
     * 分页获取特种设备分类
     * @param typeCode 分类编号
     * @param typeName 分类名称
     * @param pageReqDTO 分页参数
     * @return 特种设备分类列表
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页获取特种设备分类")
    public PageResponse<SpecialEquipTypeResDTO> pageSpecialEquipType(@RequestParam(required = false) String typeCode,
                                                                     @RequestParam(required = false) String typeName,
                                                                     @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(specialEquipTypeService.pageSpecialEquipType(typeCode, typeName, pageReqDTO));
    }

    /**
     * 获取特种设备分类详情
     * @param id id
     * @return 特种设备分类详情
     */
    @GetMapping("/detail")
    @ApiOperation(value = "获取特种设备分类详情")
    public DataResponse<SpecialEquipTypeResDTO> getSpecialEquipTypeDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(specialEquipTypeService.getSpecialEquipTypeDetail(id));
    }

    /**
     * 导入特种设备分类
     * @param file 文件
     * @return 成功状态
     */
    @PostMapping("/import")
    @ApiOperation(value = "导入特种设备分类")
    public DataResponse<T> importSpecialEquipType(@RequestParam MultipartFile file) {
        specialEquipTypeService.importSpecialEquipType(file);
        return DataResponse.success();
    }

    /**
     * 新增特种设备分类
     * @param specialEquipTypeReqDTO 特种设备分类信息
     * @return 成功状态
     */
    @PostMapping("/add")
    @ApiOperation(value = "新增特种设备分类")
    public DataResponse<T> addSpecialEquipType(@RequestBody SpecialEquipTypeReqDTO specialEquipTypeReqDTO) {
        specialEquipTypeService.addSpecialEquipType(specialEquipTypeReqDTO);
        return DataResponse.success();
    }

    /**
     * 修改特种设备分类
     * @param specialEquipTypeReqDTO 特种设备分类信息
     * @return 成功状态
     */
    @PostMapping("/modify")
    @ApiOperation(value = "编辑特种设备台账")
    public DataResponse<T> modifySpecialEquipType(@RequestBody SpecialEquipTypeReqDTO specialEquipTypeReqDTO) {
        specialEquipTypeService.modifySpecialEquipType(specialEquipTypeReqDTO);
        return DataResponse.success();
    }

    /**
     * 删除特种设备分类
     * @param baseIdsEntity ids
     * @return 成功状态
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除特种设备分类")
    public DataResponse<T> deleteSpecialEquipType(@RequestBody BaseIdsEntity baseIdsEntity) {
        if (Objects.isNull(baseIdsEntity) || StringUtils.isEmpty(baseIdsEntity.getIds())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "请先勾选后删除");
        }
        specialEquipTypeService.deleteSpecialEquipType(baseIdsEntity.getIds());
        return DataResponse.success();
    }

    /**
     * 导出特种设备分类
     * @param baseIdsEntity ids
     * @param response response
     * @throws IOException 流异常
     */
    @PostMapping("/export")
    @ApiOperation(value = "导出特种设备分类")
    public void exportSpecialEquipType(@RequestBody BaseIdsEntity baseIdsEntity, HttpServletResponse response) throws IOException {
        if (Objects.isNull(baseIdsEntity) || StringUtils.isEmpty(baseIdsEntity.getIds())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "请先勾选后导出");
        }
        specialEquipTypeService.exportSpecialEquipType(baseIdsEntity.getIds(), response);
    }

    /**
     * 获取特种设备分类列表
     * @param typeCode 分类编号
     * @param typeName 分类名称
     * @return 特种设备分类列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取特种设备分类列表")
    public DataResponse<List<SpecialEquipTypeResDTO>> listSpecialEquipType(@RequestParam(required = false) String typeCode,
                                                                          @RequestParam(required = false) String typeName) {
        return DataResponse.of(specialEquipTypeService.listSpecialEquipType(typeCode, typeName));
    }
}
