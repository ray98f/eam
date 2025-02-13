package com.wzmtr.eam.controller.overhaul;

import com.wzmtr.eam.dto.req.overhaul.OverhaulMaterialReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulTplDetailReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulTplReqDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulMaterialResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulTplDetailResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulTplResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.service.overhaul.OverhaulTplService;
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
 * 预防性检修管理-检修模板
 * @author  Ray
 * @version 1.0
 * @date 2023/08/09
 */
@Slf4j
@RestController
@RequestMapping("/overhaul/tpl")
@Api(tags = "预防性检修管理-检修模板")
@Validated
public class OverhaulTplController {

    @Resource
    private OverhaulTplService overhaulTplService;

    @GetMapping("/page")
    @ApiOperation(value = "获取检修模板列表")
    public PageResponse<OverhaulTplResDTO> pageOverhaulTpl(@RequestParam(required = false) @ApiParam("模版编码") String templateId,
                                                           @RequestParam(required = false) @ApiParam("模版名称") String templateName,
                                                           @RequestParam(required = false) @ApiParam("线路编号") String lineCode,
                                                           @RequestParam(required = false) @ApiParam("位置一") String position1Code,
                                                           @RequestParam(required = false) @ApiParam("专业编号") String subjectCode,
                                                           @RequestParam(required = false) @ApiParam("系统编号") String systemCode,
                                                           @RequestParam(required = false) @ApiParam("设备分类编号") String equipTypeCode,
                                                           @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(overhaulTplService.pageOverhaulTpl(templateId, templateName, lineCode, position1Code, subjectCode, systemCode, equipTypeCode, pageReqDTO));
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
    public DataResponse<T> submitOverhaulTpl(@RequestBody OverhaulTplReqDTO overhaulTplReqDTO) throws Exception {
        overhaulTplService.submitOverhaulTpl(overhaulTplReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/examine")
    @ApiOperation(value = "审核检修模板")
    public DataResponse<T> examineOverhaulTpl(@RequestBody OverhaulTplReqDTO overhaulTplReqDTO) {
        overhaulTplService.examineOverhaulTpl(overhaulTplReqDTO);
        return DataResponse.success();
    }

    /**
     * 检修模板导入
     * @param file 导入文件
     * @return 错误的导入数据
     */
    @PostMapping("/import")
    @ApiOperation(value = "导入检修模板")
    public DataResponse<List<String>> importOverhaulTpl(@RequestParam MultipartFile file) {
        return DataResponse.of(overhaulTplService.importOverhaulTpl(file));
    }

    @PostMapping("/export")
    @ApiOperation(value = "导出检修模板")
    public void exportOverhaulTpl(@RequestBody BaseIdsEntity baseIdsEntity, HttpServletResponse response) throws IOException {
        if (Objects.isNull(baseIdsEntity) || StringUtils.isEmpty(baseIdsEntity.getIds())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "请先勾选后导出");
        }
        overhaulTplService.exportOverhaulTpl(baseIdsEntity.getIds(), response);
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

    @PostMapping("/detail/export")
    @ApiOperation(value = "导出检修项")
    public void exportOverhaulTplDetail(@RequestBody OverhaulTplDetailReqDTO overhaulTplDetailReqDTO,
                                        HttpServletResponse response) throws IOException {
        overhaulTplService.exportOverhaulTplDetail(overhaulTplDetailReqDTO, response);
    }

    @GetMapping("/material/page")
    @ApiOperation(value = "获取物料列表")
    public PageResponse<OverhaulMaterialResDTO> pageOverhaulMaterial(@RequestParam(required = false) @ApiParam("模版编码") String templateId,
                                                                     @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(overhaulTplService.pageOverhaulMaterial(templateId, pageReqDTO));
    }

    @GetMapping("/material/detail")
    @ApiOperation(value = "获取物料详情")
    public DataResponse<OverhaulMaterialResDTO> getOverhaulMaterialDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(overhaulTplService.getOverhaulMaterialDetail(id));
    }

    @PostMapping("/material/add")
    @ApiOperation(value = "新增物料")
    public DataResponse<T> addOverhaulMaterial(@RequestBody OverhaulMaterialReqDTO overhaulMaterialReqDTO) {
        overhaulTplService.addOverhaulMaterial(overhaulMaterialReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/material/modify")
    @ApiOperation(value = "编辑物料")
    public DataResponse<T> modifyOverhaulMaterial(@RequestBody OverhaulMaterialReqDTO overhaulMaterialReqDTO) {
        overhaulTplService.modifyOverhaulMaterial(overhaulMaterialReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/material/delete")
    @ApiOperation(value = "删除物料")
    public DataResponse<T> deleteOverhaulMaterial(@RequestBody BaseIdsEntity baseIdsEntity) {
        overhaulTplService.deleteOverhaulMaterial(baseIdsEntity);
        return DataResponse.success();
    }

    @GetMapping("/material/export")
    @ApiOperation(value = "导出物料")
    public void exportOverhaulMaterial(@RequestParam(required = false) @ApiParam("模版编码") String templateId,
                                       HttpServletResponse response) throws IOException {
        overhaulTplService.exportOverhaulMaterial(templateId, response);
    }
}
