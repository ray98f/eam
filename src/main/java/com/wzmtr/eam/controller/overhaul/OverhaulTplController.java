package com.wzmtr.eam.controller.overhaul;

import com.wzmtr.eam.dto.req.OverhaulTplDetailReqDTO;
import com.wzmtr.eam.dto.req.OverhaulTplReqDTO;
import com.wzmtr.eam.dto.res.OverhaulTplDetailResDTO;
import com.wzmtr.eam.dto.res.OverhaulTplResDTO;
import com.wzmtr.eam.dto.res.RegionResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.overhaul.OverhaulTplService;
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
@RequestMapping("/overhaulTpl")
@Api(tags = "预防性检修管理-检修模板")
@Validated
public class OverhaulTplController {

    @Resource
    private OverhaulTplService overhaulTplService;

    @GetMapping("/page")
    @ApiOperation(value = "获取检修模板列表")
    public PageResponse<OverhaulTplResDTO> listOverhaulTpl(@RequestParam(required = false) @ApiParam("模版编码") String templateId,
                                                           @RequestParam(required = false) @ApiParam("模版名称") String templateName,
                                                           @RequestParam(required = false) @ApiParam("线路编号") String lineNo,
                                                           @RequestParam(required = false) @ApiParam("位置一") String position1Code,
                                                           @RequestParam(required = false) @ApiParam("专业编号") String majorCode,
                                                           @RequestParam(required = false) @ApiParam("系统编号") String systemCode,
                                                           @RequestParam(required = false) @ApiParam("设备分类编号") String equipTypeCode,
                                                           @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(overhaulTplService.pageOverhaulTpl(templateId, templateName, lineNo, position1Code, majorCode, systemCode, equipTypeCode, pageReqDTO));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取检修模板详情")
    public DataResponse<OverhaulTplResDTO> getOverhaulTplDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(overhaulTplService.getOverhaulTplDetail(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增检修模板")
    public DataResponse<T> addOverhaulTpl(@RequestBody OverhaulTplReqDTO overhaulTplReqDTO) {
        overhaulTplService.addOverhaulTpl(overhaulTplReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/modify")
    @ApiOperation(value = "编辑检修模板")
    public DataResponse<T> modifyOverhaulTpl(@RequestBody OverhaulTplReqDTO overhaulTplReqDTO) {
        overhaulTplService.modifyOverhaulTpl(overhaulTplReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除检修模板")
    public DataResponse<T> deleteOverhaulTpl(@RequestBody BaseIdsEntity baseIdsEntity) {
        overhaulTplService.deleteOverhaulTpl(baseIdsEntity);
        return DataResponse.success();
    }

    @PostMapping("/change")
    @ApiOperation(value = "变更检修模板")
    public DataResponse<T> changeOverhaulTpl(@RequestBody OverhaulTplReqDTO overhaulTplReqDTO) {
        overhaulTplService.changeOverhaulTpl(overhaulTplReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/submit")
    @ApiOperation(value = "提交检修模板")
    public DataResponse<T> submitOverhaulTpl(@RequestBody OverhaulTplReqDTO overhaulTplReqDTO) {
        overhaulTplService.submitOverhaulTpl(overhaulTplReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/import")
    @ApiOperation(value = "导入检修模板")
    public DataResponse<T> importOverhaulTpl(@RequestParam MultipartFile file) {
        overhaulTplService.importOverhaulTpl(file);
        return DataResponse.success();
    }

    @GetMapping("/export")
    @ApiOperation(value = "导出检修模板")
    public void exportOverhaulTpl(@RequestParam(required = false) @ApiParam("模版编码") String templateId,
                                  @RequestParam(required = false) @ApiParam("模版名称") String templateName,
                                  @RequestParam(required = false) @ApiParam("线路编号") String lineNo,
                                  @RequestParam(required = false) @ApiParam("位置一") String position1Code,
                                  @RequestParam(required = false) @ApiParam("专业编号") String majorCode,
                                  @RequestParam(required = false) @ApiParam("系统编号") String systemCode,
                                  @RequestParam(required = false) @ApiParam("设备分类编号") String equipTypeCode,
                                  HttpServletResponse response) {
        overhaulTplService.exportOverhaulTpl(templateId, templateName, lineNo, position1Code, majorCode, systemCode, equipTypeCode, response);
    }

    @GetMapping("/detail/page")
    @ApiOperation(value = "获取检修项列表")
    public PageResponse<OverhaulTplDetailResDTO> pageOverhaulDetailTpl(@RequestParam(required = false) @ApiParam("模版编码") String templateId,
                                                                       @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(overhaulTplService.pageOverhaulDetailTpl(templateId, pageReqDTO));
    }

    @GetMapping("/detail/detail")
    @ApiOperation(value = "获取检修项详情")
    public DataResponse<OverhaulTplDetailResDTO> getOverhaulTplDetailDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(overhaulTplService.getOverhaulTplDetailDetail(id));
    }

    @PostMapping("/detail/add")
    @ApiOperation(value = "新增检修项")
    public DataResponse<T> addOverhaulTplDetail(@RequestBody OverhaulTplDetailReqDTO overhaulTplDetailReqDTO) {
        overhaulTplService.addOverhaulTplDetail(overhaulTplDetailReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/detail/modify")
    @ApiOperation(value = "编辑检修项")
    public DataResponse<T> modifyOverhaulTplDetail(@RequestBody OverhaulTplDetailReqDTO overhaulTplDetailReqDTO) {
        overhaulTplService.modifyOverhaulTplDetail(overhaulTplDetailReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/detail/delete")
    @ApiOperation(value = "删除检修项")
    public DataResponse<T> deleteOverhaulTplDetail(@RequestBody BaseIdsEntity baseIdsEntity) {
        overhaulTplService.deleteOverhaulTplDetail(baseIdsEntity);
        return DataResponse.success();
    }

    @GetMapping("/detail/export")
    @ApiOperation(value = "导出检修项")
    public void exportOverhaulTplDetail(@RequestParam(required = false) @ApiParam("模版编码") String templateId,
                                        HttpServletResponse response) {
        overhaulTplService.exportOverhaulTplDetail(templateId, response);
    }
}
