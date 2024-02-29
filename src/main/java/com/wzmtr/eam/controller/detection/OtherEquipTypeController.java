package com.wzmtr.eam.controller.detection;

import com.wzmtr.eam.dto.req.detection.OtherEquipTypeReqDTO;
import com.wzmtr.eam.dto.res.detection.OtherEquipTypeResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.service.detection.OtherEquipTypeService;
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
 * 其他设备管理-其他设备分类管理
 * @author  Ray
 * @version 1.0
 * @date 2024/02/02
 */
@Slf4j
@RestController
@RequestMapping("/other/equip/type")
@Api(tags = "其他设备管理-其他设备分类管理")
@Validated
public class OtherEquipTypeController {

    @Resource
    private OtherEquipTypeService otherEquipTypeService;

    /**
     * 分页获取其他设备分类
     * @param typeCode 分类编号
     * @param typeName 分类名称
     * @param pageReqDTO 分页参数
     * @return 其他设备分类列表
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页获取其他设备分类")
    public PageResponse<OtherEquipTypeResDTO> pageOtherEquipType(@RequestParam(required = false) String typeCode,
                                                                 @RequestParam(required = false) String typeName,
                                                                 @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(otherEquipTypeService.pageOtherEquipType(typeCode, typeName, pageReqDTO));
    }

    /**
     * 获取其他设备分类详情
     * @param id id
     * @return 其他设备分类详情
     */
    @GetMapping("/detail")
    @ApiOperation(value = "获取其他设备分类详情")
    public DataResponse<OtherEquipTypeResDTO> getOtherEquipTypeDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(otherEquipTypeService.getOtherEquipTypeDetail(id));
    }

    /**
     * 导入其他设备分类
     * @param file 文件
     * @return 成功状态
     */
    @PostMapping("/import")
    @ApiOperation(value = "导入其他设备分类")
    public DataResponse<T> importOtherEquipType(@RequestParam MultipartFile file) {
        otherEquipTypeService.importOtherEquipType(file);
        return DataResponse.success();
    }

    /**
     * 新增其他设备分类
     * @param otherEquipTypeReqDTO 其他设备分类信息
     * @return 成功状态
     */
    @PostMapping("/add")
    @ApiOperation(value = "新增其他设备分类")
    public DataResponse<T> addOtherEquipType(@RequestBody OtherEquipTypeReqDTO otherEquipTypeReqDTO) {
        otherEquipTypeService.addOtherEquipType(otherEquipTypeReqDTO);
        return DataResponse.success();
    }

    /**
     * 修改其他设备分类
     * @param otherEquipTypeReqDTO 其他设备分类信息
     * @return 成功状态
     */
    @PostMapping("/modify")
    @ApiOperation(value = "编辑其他设备台账")
    public DataResponse<T> modifyOtherEquipType(@RequestBody OtherEquipTypeReqDTO otherEquipTypeReqDTO) {
        otherEquipTypeService.modifyOtherEquipType(otherEquipTypeReqDTO);
        return DataResponse.success();
    }

    /**
     * 删除其他设备分类
     * @param baseIdsEntity ids
     * @return 成功状态
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除其他设备分类")
    public DataResponse<T> deleteOtherEquipType(@RequestBody BaseIdsEntity baseIdsEntity) {
        if (Objects.isNull(baseIdsEntity) || StringUtils.isEmpty(baseIdsEntity.getIds())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "请先勾选后删除");
        }
        otherEquipTypeService.deleteOtherEquipType(baseIdsEntity.getIds());
        return DataResponse.success();
    }

    /**
     * 导出其他设备分类
     * @param typeCode 分类编号
     * @param typeName 分类名称
     * @param ids ids
     * @param response response
     * @throws IOException 流异常
     */
    @GetMapping("/export")
    @ApiOperation(value = "导出其他设备分类")
    public void exportOtherEquipType(@RequestParam(required = false) String typeCode,
                                     @RequestParam(required = false) String typeName,
                                     @RequestParam(required = false) String ids,
                                     HttpServletResponse response) throws IOException {
        otherEquipTypeService.exportOtherEquipType(typeCode, typeName, ids, response);
    }

    /**
     * 获取其他设备分类列表
     * @param typeCode 分类编号
     * @param typeName 分类名称
     * @return 其他设备分类列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取其他设备分类列表")
    public DataResponse<List<OtherEquipTypeResDTO>> listOtherEquipType(@RequestParam(required = false) String typeCode,
                                                                       @RequestParam(required = false) String typeName) {
        return DataResponse.of(otherEquipTypeService.listOtherEquipType(typeCode, typeName));
    }
}
