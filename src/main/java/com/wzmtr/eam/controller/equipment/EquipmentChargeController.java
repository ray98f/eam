package com.wzmtr.eam.controller.equipment;

import com.wzmtr.eam.dto.req.EquipmentChargeReqDTO;
import com.wzmtr.eam.dto.res.EquipmentChargeResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.equipment.EquipmentChargeService;
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
@RequestMapping("/equipment/charge")
@Api(tags = "设备管理-设备充电管理")
@Validated
public class EquipmentChargeController {

    @Resource
    private EquipmentChargeService equipmentChargeService;

    @GetMapping("/list")
    @ApiOperation(value = "获取设备充电列表")
    public PageResponse<EquipmentChargeResDTO> listEquipmentCharge(@RequestParam(required = false) @ApiParam("设备编码") String equipCode,
                                                                   @RequestParam(required = false) @ApiParam("设备名称") String equipName,
                                                                   @RequestParam(required = false) @ApiParam("充电日期") String chargeDate,
                                                                   @RequestParam(required = false) @ApiParam("位置一") String position1Name,
                                                                   @RequestParam(required = false) @ApiParam("专业") String subjectCode,
                                                                   @RequestParam(required = false) @ApiParam("系统") String systemCode,
                                                                   @RequestParam(required = false) @ApiParam("设备类别") String equipTypeCode,
                                                                   @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(equipmentChargeService.listEquipmentCharge(equipCode, equipName, chargeDate, position1Name, subjectCode, systemCode, equipTypeCode, pageReqDTO));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取设备充电详情")
    public DataResponse<EquipmentChargeResDTO> getEquipmentChargeDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(equipmentChargeService.getEquipmentChargeDetail(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增设备充电")
    public DataResponse<T> addEquipmentCharge(@RequestBody EquipmentChargeReqDTO equipmentChargeReqDTO) {
        equipmentChargeService.addEquipmentCharge(equipmentChargeReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/modify")
    @ApiOperation(value = "修改设备充电")
    public DataResponse<T> modifyEquipmentCharge(@RequestBody EquipmentChargeReqDTO equipmentChargeReqDTO) {
        equipmentChargeService.modifyEquipmentCharge(equipmentChargeReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除设备充电")
    public DataResponse<T> deleteEquipmentCharge(@RequestBody BaseIdsEntity baseIdsEntity) {
        equipmentChargeService.deleteEquipmentCharge(baseIdsEntity);
        return DataResponse.success();
    }

    @GetMapping("/export")
    @ApiOperation(value = "导出设备充电")
    public void exportEquipmentCharge(@RequestParam(required = false) @ApiParam("设备编码") String equipCode,
                                      @RequestParam(required = false) @ApiParam("设备名称") String equipName,
                                      @RequestParam(required = false) @ApiParam("充电日期") String chargeDate,
                                      @RequestParam(required = false) @ApiParam("位置一") String position1Name,
                                      @RequestParam(required = false) @ApiParam("专业") String subjectCode,
                                      @RequestParam(required = false) @ApiParam("系统") String systemCode,
                                      @RequestParam(required = false) @ApiParam("设备类别") String equipTypeCode,
                                      HttpServletResponse response) {
        equipmentChargeService.exportEquipmentCharge(equipCode, equipName, chargeDate, position1Name, subjectCode, systemCode, equipTypeCode, response);
    }
}
