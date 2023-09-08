package com.wzmtr.eam.controller.specialEquip;

import com.wzmtr.eam.dto.req.specialEquip.SpecialEquipReqDTO;
import com.wzmtr.eam.dto.res.specialEquip.SpecialEquipHistoryResDTO;
import com.wzmtr.eam.dto.res.specialEquip.SpecialEquipResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.specialEquip.SpecialEquipService;
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

@Slf4j
@RestController
@RequestMapping("/specialEquip")
@Api(tags = "特种设备管理-特种设备台账")
@Validated
public class SpecialEquipController {

    @Resource
    private SpecialEquipService specialEquipService;

    @GetMapping("/page")
    @ApiOperation(value = "获取特种设备台账列表")
    public PageResponse<SpecialEquipResDTO> listSpecialEquip(@RequestParam(required = false) @ApiParam("设备编码") String equipCode,
                                                             @RequestParam(required = false) @ApiParam("设备名称") String equipName,
                                                             @RequestParam(required = false) @ApiParam("特种设备代码") String specialEquipCode,
                                                             @RequestParam(required = false) @ApiParam("出厂编号") String factNo,
                                                             @RequestParam(required = false) @ApiParam("线路") String useLineNo,
                                                             @RequestParam(required = false) @ApiParam("位置一") String position1Code,
                                                             @RequestParam(required = false) @ApiParam("特种设备类别") String specialEquipType,
                                                             @RequestParam(required = false) @ApiParam("设备状态") String equipStatus,
                                                             @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(specialEquipService.pageSpecialEquip(equipCode, equipName, specialEquipCode, factNo, useLineNo,
                position1Code, specialEquipType, equipStatus, pageReqDTO));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取特种设备台账详情")
    public DataResponse<SpecialEquipResDTO> getSpecialEquipDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(specialEquipService.getSpecialEquipDetail(id));
    }

    @PostMapping("/import")
    @ApiOperation(value = "导入特种设备台账")
    public DataResponse<T> importSpecialEquip(@RequestParam MultipartFile file) {
        specialEquipService.importSpecialEquip(file);
        return DataResponse.success();
    }

    @PostMapping("/modify")
    @ApiOperation(value = "编辑特种设备台账")
    public DataResponse<T> modifySpecialEquip(@RequestBody SpecialEquipReqDTO specialEquipReqDTO) {
        specialEquipService.modifySpecialEquip(specialEquipReqDTO);
        return DataResponse.success();
    }

    @GetMapping("/export")
    @ApiOperation(value = "导出特种设备台账")
    public void exportSpecialEquip(@RequestParam(required = false) @ApiParam("设备编码") String equipCode,
                                   @RequestParam(required = false) @ApiParam("设备名称") String equipName,
                                   @RequestParam(required = false) @ApiParam("特种设备代码") String specialEquipCode,
                                   @RequestParam(required = false) @ApiParam("出厂编号") String factNo,
                                   @RequestParam(required = false) @ApiParam("线路") String useLineNo,
                                   @RequestParam(required = false) @ApiParam("位置一") String position1Code,
                                   @RequestParam(required = false) @ApiParam("特种设备类别") String specialEquipType,
                                   @RequestParam(required = false) @ApiParam("设备状态") String equipStatus,
                                   HttpServletResponse response) {
        specialEquipService.exportSpecialEquip(equipCode, equipName, specialEquipCode, factNo, useLineNo,
                position1Code, specialEquipType, equipStatus, response);
    }

    @GetMapping("/history/list")
    @ApiOperation(value = "获取特种设备检测历史记录列表")
    public PageResponse<SpecialEquipHistoryResDTO> pageSpecialEquipHistory(@RequestParam @ApiParam("设备编码") String equipCode,
                                                                           @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(specialEquipService.pageSpecialEquipHistory(equipCode, pageReqDTO));
    }

    @GetMapping("/history/detail")
    @ApiOperation(value = "获取特种设备检测历史记录详情")
    public DataResponse<SpecialEquipHistoryResDTO> getSpecialEquipHistoryDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(specialEquipService.getSpecialEquipHistoryDetail(id));
    }

    @GetMapping("/history/export")
    @ApiOperation(value = "导出特种设备检测历史记录详情")
    public void exportSpecialEquipHistory(@RequestParam @ApiParam("设备编码") String equipCode,
                                          HttpServletResponse response) {
        specialEquipService.exportSpecialEquipHistory(equipCode, response);
    }
}
