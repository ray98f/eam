package com.wzmtr.eam.controller.equipment;

import com.wzmtr.eam.dto.req.equipment.EquipmentRoomRelationReqDTO;
import com.wzmtr.eam.dto.req.equipment.EquipmentRoomReqDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentResDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentRoomResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.service.equipment.EquipmentRoomService;
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
import java.util.Objects;

/**
 * 设备管理-设备房管理
 * @author  Ray
 * @version 1.0
 * @date 2023/07/27
 */
@Slf4j
@RestController
@RequestMapping("/equipment/room")
@Api(tags = "设备管理-设备房管理")
@Validated
public class EquipmentRoomController {

    @Resource
    private EquipmentRoomService equipmentRoomService;

    @GetMapping("/list")
    @ApiOperation(value = "获取设备房列表")
    public PageResponse<EquipmentRoomResDTO> listEquipmentRoom(@RequestParam(required = false) @ApiParam("设备房编码") String equipRoomCode,
                                                               @RequestParam(required = false) @ApiParam("设备房名称") String equipRoomName,
                                                               @RequestParam(required = false) @ApiParam("线路") String lineCode,
                                                               @RequestParam(required = false) @ApiParam("位置一编码") String position1Code,
                                                               @RequestParam(required = false) @ApiParam("位置一名称") String position1Name,
                                                               @RequestParam(required = false) @ApiParam("专业") String subjectCode,
                                                               @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(equipmentRoomService.listEquipmentRoom(equipRoomCode, equipRoomName, lineCode, position1Code, position1Name, subjectCode, pageReqDTO));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取设备房详情")
    public DataResponse<EquipmentRoomResDTO> getEquipmentRoomDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(equipmentRoomService.getEquipmentRoomDetail(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增设备房")
    public DataResponse<T> addEquipmentRoom(@RequestBody EquipmentRoomReqDTO equipmentRoomReqDTO) {
        equipmentRoomService.addEquipmentRoom(equipmentRoomReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/modify")
    @ApiOperation(value = "修改设备房")
    public DataResponse<T> modifyEquipmentRoom(@RequestBody EquipmentRoomReqDTO equipmentRoomReqDTO) {
        equipmentRoomService.modifyEquipmentRoom(equipmentRoomReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除设备房")
    public DataResponse<T> deleteEquipmentRoom(@RequestBody BaseIdsEntity baseIdsEntity) {
        equipmentRoomService.deleteEquipmentRoom(baseIdsEntity);
        return DataResponse.success();
    }

    @PostMapping("/export")
    @ApiOperation(value = "导出设备房")
    public void exportEquipmentRoom(@RequestBody BaseIdsEntity baseIdsEntity, HttpServletResponse response) throws IOException {
        if (Objects.isNull(baseIdsEntity) || StringUtils.isEmpty(baseIdsEntity.getIds())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "请先勾选后导出");
        }
        equipmentRoomService.exportEquipmentRoom(baseIdsEntity.getIds(), response);
    }

    @GetMapping("/equipment")
    @ApiOperation(value = "获取设备房设备列表")
    public PageResponse<EquipmentResDTO> listEquipment(@RequestParam @ApiParam("设备房") String roomId,
                                                       @RequestParam(required = false) @ApiParam("设备编码") String equipCode,
                                                       @RequestParam(required = false) @ApiParam("设备名称") String equipName,
                                                       @RequestParam(required = false) @ApiParam("专业编号") String majorCode,
                                                       @RequestParam(required = false) @ApiParam("系统编号") String systemCode,
                                                       @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(equipmentRoomService.pageEquipment(roomId, equipCode, equipName, majorCode, systemCode, pageReqDTO));
    }
    @PostMapping("/deleteEquipment")
    @ApiOperation(value = "取消关联设备")
    public DataResponse<T> deleteEquipment(@RequestBody EquipmentRoomRelationReqDTO req) {
        equipmentRoomService.deleteEquipment(req);
        return DataResponse.success();
    }

    @PostMapping("/addEquipment")
    @ApiOperation(value = "关联设备")
    public DataResponse<T> addEquipment(@RequestBody EquipmentRoomRelationReqDTO req) {
        equipmentRoomService.addEquipment(req);
        return DataResponse.success();
    }
}
