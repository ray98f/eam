package com.wzmtr.eam.controller.equipment;

import com.wzmtr.eam.dto.req.equipment.TransferExportReqDTO;
import com.wzmtr.eam.dto.req.equipment.TransferSplitReqDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentResDTO;
import com.wzmtr.eam.dto.res.equipment.TransferResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.equipment.TransferService;
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

/**
 * 设备管理-设备移交-建设工程
 * @author  Ray
 * @version 1.0
 * @date 2023/07/26
 */
@Slf4j
@RestController
@RequestMapping("/transfer")
@Api(tags = "设备管理-设备移交-建设工程")
@Validated
public class TransferController {

    @Resource
    private TransferService transferService;


    @GetMapping("/page")
    @ApiOperation(value = "获取设备移交列表")
    public PageResponse<TransferResDTO> pageTransfer(@RequestParam(required = false) @ApiParam("移交单号") String transferNo,
                                                     @RequestParam(required = false) @ApiParam("合同清单明细号") String itemCode,
                                                     @RequestParam(required = false) @ApiParam("设备名称") String itemName,
                                                     @RequestParam(required = false) @ApiParam("位置一") String position1Code,
                                                     @RequestParam(required = false) @ApiParam("处理状态") String eamProcessStatus,
                                                     @RequestParam(required = false) @ApiParam("专业编号") String majorCode,
                                                     @RequestParam(required = false) @ApiParam("合同编号") String orderNo,
                                                     @RequestParam(required = false) @ApiParam("合同名称") String orderName,
                                                     @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(transferService.pageTransfer(transferNo, itemCode, itemName, position1Code,
                eamProcessStatus, majorCode, orderNo, orderName, pageReqDTO));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取设备移交详情")
    public DataResponse<TransferResDTO> getTransferDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(transferService.getTransferDetail(id));
    }

    @PostMapping("/export")
    @ApiOperation(value = "导出设备移交")
    public void exportTransfer(@RequestBody TransferExportReqDTO transferExportReqDTO, HttpServletResponse response) throws IOException {
        transferService.exportTransfer(transferExportReqDTO, response);
    }

    @PostMapping("/encoding")
    @ApiOperation(value = "生成设备编码")
    public DataResponse<T> encodingTransfer(@RequestBody BaseIdsEntity baseIdsEntity) {
        transferService.encodingTransfer(baseIdsEntity);
        return DataResponse.success();
    }

    @GetMapping("/split/page")
    @ApiOperation(value = "获取设备拆分列表")
    public PageResponse<EquipmentResDTO> pageSplitTransfer(@RequestParam(required = false) @ApiParam("来源记录编号") String sourceRecId,
                                                           @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(transferService.pageSplitTransfer(sourceRecId, pageReqDTO));
    }

    @GetMapping("/split/detail")
    @ApiOperation(value = "获取设备拆分详情")
    public DataResponse<EquipmentResDTO> getSplitTransferDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(transferService.getSplitTransferDetail(id));
    }

    @PostMapping("/split/save")
    @ApiOperation(value = "保存设备拆分")
    public DataResponse<T> saveSplitTransfer(@RequestBody EquipmentResDTO equipmentResDTO) {
        transferService.saveSplitTransfer(equipmentResDTO);
        return DataResponse.success();
    }

    @PostMapping("/split/submit")
    @ApiOperation(value = "提交设备拆分")
    public DataResponse<T> submitSplitTransfer(@RequestBody TransferSplitReqDTO transferSplitReqDTO) throws Exception {
        transferService.submitSplitTransfer(transferSplitReqDTO);
        return DataResponse.success();
    }

    @GetMapping("/split/export")
    @ApiOperation(value = "导出设备拆分")
    public void exportSplitTransfer(@RequestParam(required = false) @ApiParam("来源记录编号") String sourceRecId,
                                    HttpServletResponse response) throws IOException {
        transferService.exportSplitTransfer(sourceRecId, response);
    }
}
