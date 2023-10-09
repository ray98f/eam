package com.wzmtr.eam.controller.equipment;

import com.wzmtr.eam.dto.req.equipment.PartFaultReqDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentQrResDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentResDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentTreeResDTO;
import com.wzmtr.eam.dto.res.basic.RegionResDTO;
import com.wzmtr.eam.dto.res.equipment.PartReplaceResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulOrderDetailResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.service.equipment.EquipmentService;
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
import java.text.ParseException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/equipment")
@Api(tags = "设备管理-设备台账")
@Validated
public class EquipmentController {

    @Resource
    private EquipmentService equipmentService;

    @GetMapping("/train")
    @ApiOperation(value = "获取列车号")
    public DataResponse<List<RegionResDTO>> listTrainRegion() {
        return DataResponse.of(equipmentService.listTrainRegion());
    }

    @GetMapping("/listTree")
    @ApiOperation(value = "获取设备树")
    public DataResponse<EquipmentTreeResDTO> listEquipmentTree(@RequestParam(required = false) @ApiParam("线路编号") String lineCode,
                                                               @RequestParam(required = false) @ApiParam("位置编号") String regionCode,
                                                               @RequestParam(required = false) @ApiParam("位置id") String recId,
                                                               @RequestParam(required = false) @ApiParam("父位置id") String parentNodeRecId,
                                                               @RequestParam(required = false) @ApiParam("设备分类编号") String equipmentCategoryCode) {
        return DataResponse.of(equipmentService.listEquipmentTree(lineCode, regionCode, recId, parentNodeRecId, equipmentCategoryCode));
    }

    @GetMapping("/page")
    @ApiOperation(value = "获取设备台账列表")
    public PageResponse<EquipmentResDTO> listEquipment(@RequestParam(required = false) @ApiParam("设备编码") String equipCode,
                                                       @RequestParam(required = false) @ApiParam("设备名称") String equipName,
                                                       @RequestParam(required = false) @ApiParam("线路编号") String useLineNo,
                                                       @RequestParam(required = false) @ApiParam("线段编号") String useSegNo,
                                                       @RequestParam(required = false) @ApiParam("位置一") String position1Code,
                                                       @RequestParam(required = false) @ApiParam("专业编号") String majorCode,
                                                       @RequestParam(required = false) @ApiParam("系统编号") String systemCode,
                                                       @RequestParam(required = false) @ApiParam("设备分类编号") String equipTypeCode,
                                                       @RequestParam(required = false) @ApiParam("品牌") String brand,
                                                       @RequestParam(required = false) @ApiParam("出产开始时间") String startTime,
                                                       @RequestParam(required = false) @ApiParam("出产结束时间") String endTime,
                                                       @RequestParam(required = false) @ApiParam("生产厂家") String manufacture,
                                                       @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(equipmentService.pageEquipment(equipCode, equipName, useLineNo, useSegNo, position1Code, majorCode,
                systemCode, equipTypeCode, brand, startTime, endTime, manufacture, pageReqDTO));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取设备台账详情")
    public DataResponse<EquipmentResDTO> getEquipmentDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(equipmentService.getEquipmentDetail(id));
    }

    @PostMapping("/import")
    @ApiOperation(value = "导入设备台账")
    public DataResponse<T> importEquipment(@RequestParam MultipartFile file) {
        equipmentService.importEquipment(file);
        return DataResponse.success();
    }

    @PostMapping("/export")
    @ApiOperation(value = "导出设备台账")
    public void exportEquipment(@RequestBody BaseIdsEntity baseIdsEntity, HttpServletResponse response) {
        if (baseIdsEntity == null || baseIdsEntity.getIds().isEmpty()) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "请先勾选后导出");
        }
        equipmentService.exportEquipment(baseIdsEntity.getIds(), response);
    }

    @PostMapping("/qr")
    @ApiOperation(value = "生成二维码")
    public DataResponse<List<EquipmentQrResDTO>> generateQr(@RequestBody BaseIdsEntity baseIdsEntity) throws ParseException {
        return DataResponse.of(equipmentService.generateQr(baseIdsEntity));
    }

    @GetMapping("/overhaul/list")
    @ApiOperation(value = "检修列表")
    public PageResponse<OverhaulOrderDetailResDTO> listOverhaul(@RequestParam @ApiParam("设备编码") String equipCode,
                                                                @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(equipmentService.listOverhaul(equipCode, pageReqDTO));
    }

    @GetMapping("/fault/list")
    @ApiOperation(value = "故障列表")
    public PageResponse<FaultDetailResDTO> listFault(@RequestParam @ApiParam("设备编码") String equipCode,
                                                     @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(equipmentService.listFault(equipCode, pageReqDTO));
    }

    @GetMapping("/partReplace/list")
    @ApiOperation(value = "部件更换列表")
    public PageResponse<PartReplaceResDTO> listPartReplace(@RequestParam @ApiParam("设备编码") String equipCode,
                                                           @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(equipmentService.listPartReplace(equipCode, pageReqDTO));
    }
}
