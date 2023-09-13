package com.wzmtr.eam.controller.equipment;

import com.wzmtr.eam.dto.req.equipment.PartReplaceReqDTO;
import com.wzmtr.eam.dto.res.equipment.PartReplaceBomResDTO;
import com.wzmtr.eam.dto.res.equipment.PartReplaceResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.equipment.PartReplaceService;
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
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/train/maintenance/partReplace")
@Api(tags = "设备管理-车辆维保台账-部件更换台账")
@Validated
public class PartReplaceController {

    @Resource
    private PartReplaceService partReplaceService;

    @GetMapping("/page")
    @ApiOperation(value = "获取部件更换台账列表")
    public PageResponse<PartReplaceResDTO> pagePartReplace(@RequestParam(required = false) @ApiParam("设备名称") String equipName,
                                                           @RequestParam(required = false) @ApiParam("部件名称") String replacementName,
                                                           @RequestParam(required = false) @ApiParam("故障工单编号") String faultWorkNo,
                                                           @RequestParam(required = false) @ApiParam("作业单位") String orgType,
                                                           @RequestParam(required = false) @ApiParam("更换原因") String replaceReason,
                                                           @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(partReplaceService.pagePartReplace(equipName, replacementName, faultWorkNo, orgType, replaceReason, pageReqDTO));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取部件更换台账详情")
    public DataResponse<PartReplaceResDTO> getPartReplaceDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(partReplaceService.getPartReplaceDetail(id));
    }

    @GetMapping("/bom")
    @ApiOperation(value = "查询部件台账列表")
    public DataResponse<List<PartReplaceBomResDTO>> getBom(@RequestParam(required = false) @ApiParam("equipCode") String equipCode,
                                                           @RequestParam(required = false) @ApiParam("node") String node) {
        return DataResponse.of(partReplaceService.getBom(equipCode, node));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增部件更换台账")
    public DataResponse<T> addPartReplace(@RequestBody PartReplaceReqDTO partReplaceReqDTO) {
        partReplaceService.addPartReplace(partReplaceReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除部件更换台账")
    public DataResponse<T> deletePartReplace(@RequestBody BaseIdsEntity baseIdsEntity) {
        partReplaceService.deletePartReplace(baseIdsEntity);
        return DataResponse.success();
    }

    @PostMapping("/import")
    @ApiOperation(value = "导入部件更换台账")
    public DataResponse<T> importPartReplace(@RequestParam MultipartFile file) {
        partReplaceService.importPartReplace(file);
        return DataResponse.success();
    }

    @GetMapping("/export")
    @ApiOperation(value = "导出部件更换台账")
    public void exportPartReplace(@RequestParam(required = false) @ApiParam("设备名称") String equipName,
                                  @RequestParam(required = false) @ApiParam("部件名称") String replacementName,
                                  @RequestParam(required = false) @ApiParam("故障工单编号") String faultWorkNo,
                                  @RequestParam(required = false) @ApiParam("作业单位") String orgType,
                                  @RequestParam(required = false) @ApiParam("更换原因") String replaceReason,
                                  HttpServletResponse response) {
        partReplaceService.exportPartReplace(equipName, replacementName, faultWorkNo, orgType, replaceReason, response);
    }

}
