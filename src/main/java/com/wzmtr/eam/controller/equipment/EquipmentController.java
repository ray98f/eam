package com.wzmtr.eam.controller.equipment;

import com.wzmtr.eam.dto.req.equipment.EquipmentExportReqDTO;
import com.wzmtr.eam.dto.req.equipment.EquipmentReqDTO;
import com.wzmtr.eam.dto.res.basic.RegionResDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentQrResDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentResDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentTreeResDTO;
import com.wzmtr.eam.dto.res.equipment.PartReplaceResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulOrderDetailResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.equipment.EquipmentService;
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
import java.text.ParseException;
import java.util.List;

/**
 * 设备管理-设备台账
 * @author  Ray
 * @version 1.0
 * @date 2023/07/24
 */
@Slf4j
@RestController
@RequestMapping("/equipment")
@Api(tags = "设备管理-设备台账")
@Validated
public class EquipmentController {

    @Resource
    private EquipmentService equipmentService;

    /**
     * 获取列车列表
     * @param lineCode 线路编号
     * @return 列车列表
     */
    @GetMapping("/train")
    @ApiOperation(value = "获取列车号")
    public DataResponse<List<RegionResDTO>> listTrainRegion(@RequestParam(required = false) String lineCode) {
        return DataResponse.of(equipmentService.listTrainRegion(lineCode));
    }

    /**
     * 获取设备树
     * @param lineCode 线路编号
     * @param regionCode 位置编号
     * @param recId 位置id
     * @param parentNodeRecId 父位置id
     * @param equipmentCategoryCode 设备分类编号
     * @return 设备树
     */
    @GetMapping("/listTree")
    @ApiOperation(value = "获取设备树")
    public DataResponse<EquipmentTreeResDTO> listEquipmentTree(@RequestParam(required = false) @ApiParam("线路编号") String lineCode,
                                                               @RequestParam(required = false) @ApiParam("位置编号") String regionCode,
                                                               @RequestParam(required = false) @ApiParam("位置id") String recId,
                                                               @RequestParam(required = false) @ApiParam("父位置id") String parentNodeRecId,
                                                               @RequestParam(required = false) @ApiParam("设备分类编号") String equipmentCategoryCode) {
        return DataResponse.of(equipmentService.listEquipmentTree(lineCode, regionCode, recId, parentNodeRecId, equipmentCategoryCode));
    }

    /**
     * 获取设备台账列表
     * @param equipCode 设备编码
     * @param equipName 设备名称
     * @param useLineNo 线路编号
     * @param useSegNo 线段编号
     * @param position1Code 位置一
     * @param majorCode 专业编号
     * @param systemCode 系统编号
     * @param equipTypeCode 设备分类编号
     * @param brand 品牌
     * @param startTime 出产开始时间
     * @param endTime 出产结束时间
     * @param manufacture 生产厂家
     * @param pageReqDTO 分页参数
     * @return 设备台账列表
     */
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

    @GetMapping("/allList")
    @ApiOperation(value = "获取设备列表")
    public DataResponse<List<EquipmentResDTO>> allList(@RequestParam(required = false) @ApiParam("设备编码") String equipCode,
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
        return DataResponse.of(equipmentService.allList(equipCode, equipName, useLineNo, useSegNo, position1Code, majorCode,
                systemCode, equipTypeCode, brand, startTime, endTime, manufacture));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取设备台账详情")
    public DataResponse<EquipmentResDTO> getEquipmentDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(equipmentService.getEquipmentDetail(id));
    }

    /**
     * 新增设备台账
     * @param equipmentReqDTO 设备台账信息
     * @return 成功
     */
    @PostMapping("/add")
    @ApiOperation(value = "新增设备台账")
    public DataResponse<T> addEquipment(@RequestBody EquipmentReqDTO equipmentReqDTO) {
        equipmentService.addEquipment(equipmentReqDTO);
        return DataResponse.success();
    }

    /**
     * 编辑设备台账
     * @param equipmentReqDTO 设备台账信息
     * @return 成功
     */
    @PostMapping("/modify")
    @ApiOperation(value = "编辑设备台账")
    public DataResponse<T> modifyEquipment(@RequestBody EquipmentReqDTO equipmentReqDTO) {
        equipmentService.modifyEquipment(equipmentReqDTO);
        return DataResponse.success();
    }

    /**
     * 删除设备台账
     * @param baseIdsEntity ids
     * @return 成功
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除设备台账")
    public DataResponse<T> deleteEquipment(@RequestBody BaseIdsEntity baseIdsEntity) {
        equipmentService.deleteEquipment(baseIdsEntity);
        return DataResponse.success();
    }

    /**
     * 导入设备台账
     * @param file 文件
     * @return 成功
     */
    @PostMapping("/import")
    @ApiOperation(value = "导入设备台账")
    public DataResponse<T> importEquipment(@RequestParam MultipartFile file) {
        equipmentService.importEquipment(file);
        return DataResponse.success();
    }

    /**
     * 导出设备台账
     * @param reqDTO 导出传参
     * @param response response
     * @throws IOException 异常
     */
    @PostMapping("/export")
    @ApiOperation(value = "导出设备台账")
    public void exportEquipment(@RequestBody EquipmentExportReqDTO reqDTO,
                                HttpServletResponse response) throws IOException {
        equipmentService.exportEquipment(reqDTO, response);
    }

    /**
     * 生成二维码
     * @param baseIdsEntity ids
     * @return 二维码列表
     * @throws ParseException 异常
     */
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
