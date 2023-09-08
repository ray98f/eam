package com.wzmtr.eam.controller.mea;

import com.wzmtr.eam.dto.req.mea.MeaListReqDTO;
import com.wzmtr.eam.dto.res.mea.MeaResDTO;
import com.wzmtr.eam.dto.res.mea.SubmissionRecordDetailResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.mea.MeaService;
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
@RequestMapping("/mea")
@Api(tags = "计量器具管理-计量器具台账")
@Validated
public class MeaController {

    @Resource
    private MeaService meaService;

    @GetMapping("/page")
    @ApiOperation(value = "获取计量器具台账列表")
    public PageResponse<MeaResDTO> listMea(MeaListReqDTO meaListReqDTO, @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(meaService.pageMea(meaListReqDTO, pageReqDTO));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取计量器具台账详情")
    public DataResponse<MeaResDTO> getMeaDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(meaService.getMeaDetail(id));
    }

    @PostMapping("/import")
    @ApiOperation(value = "导入计量器具台账")
    public DataResponse<T> importMea(@RequestParam MultipartFile file) {
        meaService.importMea(file);
        return DataResponse.success();
    }

    @GetMapping("/export")
    @ApiOperation(value = "导出计量器具台账")
    public void exportMea(MeaListReqDTO meaListReqDTO, HttpServletResponse response) {
        meaService.exportMea(meaListReqDTO, response);
    }

    @GetMapping("/record/page")
    @ApiOperation(value = "获取计量器具检定历史记录列表")
    public PageResponse<SubmissionRecordDetailResDTO> pageMeaRecord(@RequestParam(required = false) @ApiParam("计量器具编号") String equipCode,
                                                                    @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(meaService.pageMeaRecord(equipCode, pageReqDTO));
    }
}
