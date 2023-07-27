package com.wzmtr.eam.controller.equipment;

import com.wzmtr.eam.dto.req.EquipmentRoomReqDTO;
import com.wzmtr.eam.dto.res.EquipmentRoomResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.equipment.EquipmentRoomService;
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
@RequestMapping("/equipment/room")
@Api(tags = "设备管理-设备房管理")
@Validated
public class EquipmentRoomController {

    @Resource
    private EquipmentRoomService equipmentRoomService;

    @GetMapping("/list")
    @ApiOperation(value = "获取设备房列表")
    public PageResponse<EquipmentRoomResDTO> listEquipmentRoom(@RequestParam(required = false) @ApiParam("码值编号") String equipRoomCode,
                                                               @RequestParam(required = false) @ApiParam("码值类型") String equipRoomName,
                                                               @RequestParam(required = false) @ApiParam("线路") String lineCode,
                                                               @RequestParam(required = false) @ApiParam("设备分类编号") String position1Code,
                                                               @RequestParam(required = false) @ApiParam("设备分类编号") String position1Name,
                                                               @RequestParam(required = false) @ApiParam("设备分类编号") String subjectCode,
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

    @GetMapping("/export")
    @ApiOperation(value = "导出设备房")
    public void exportEquipmentRoom(@RequestParam(required = false) @ApiParam("码值编号") String equipRoomCode,
                                    @RequestParam(required = false) @ApiParam("码值类型") String equipRoomName,
                                    @RequestParam(required = false) @ApiParam("线路") String lineCode,
                                    @RequestParam(required = false) @ApiParam("设备分类编号") String position1Code,
                                    @RequestParam(required = false) @ApiParam("设备分类编号") String position1Name,
                                    @RequestParam(required = false) @ApiParam("设备分类编号") String subjectCode,
                                    HttpServletResponse response) {
        equipmentRoomService.exportEquipmentRoom(equipRoomCode, equipRoomName, lineCode, position1Code, position1Name, subjectCode, response);
    }
}
