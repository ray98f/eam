package com.wzmtr.eam.controller.equipment;

import com.wzmtr.eam.dto.req.equipment.GeneralSurveyReqDTO;
import com.wzmtr.eam.dto.res.equipment.GeneralSurveyResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.equipment.GeneralSurveyService;
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

@Slf4j
@RestController
@RequestMapping("/train/maintenance/generalSurvey")
@Api(tags = "设备管理-车辆维保台账-普查与技改台账")
@Validated
public class GeneralSurveyController {

    @Resource
    private GeneralSurveyService generalSurveyService;

    @GetMapping("/page")
    @ApiOperation(value = "获取普查与技改台账列表")
    public PageResponse<GeneralSurveyResDTO> pageGeneralSurvey(@RequestParam(required = false) @ApiParam("列车号") String trainNo,
                                                               @RequestParam(required = false) @ApiParam("技术通知单编号") String recNotifyNo,
                                                               @RequestParam(required = false) @ApiParam("项目内容") String recDetail,
                                                               @RequestParam(required = false) @ApiParam("作业单位") String orgType,
                                                               @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(generalSurveyService.pageGeneralSurvey(trainNo, recNotifyNo, recDetail, orgType, pageReqDTO));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取普查与技改台账详情")
    public DataResponse<GeneralSurveyResDTO> getGeneralSurveyDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(generalSurveyService.getGeneralSurveyDetail(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增普查与技改台账")
    public DataResponse<T> addGeneralSurvey(@RequestBody GeneralSurveyReqDTO generalSurveyReqDTO) {
        generalSurveyService.addGeneralSurvey(generalSurveyReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/modify")
    @ApiOperation(value = "编辑普查与技改台账")
    public DataResponse<T> modifyGeneralSurvey(@RequestBody GeneralSurveyReqDTO generalSurveyReqDTO) {
        generalSurveyService.modifyGeneralSurvey(generalSurveyReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除普查与技改台账")
    public DataResponse<T> deleteGeneralSurvey(@RequestBody BaseIdsEntity baseIdsEntity) {
        generalSurveyService.deleteGeneralSurvey(baseIdsEntity);
        return DataResponse.success();
    }

    @PostMapping("/import")
    @ApiOperation(value = "导入普查与技改台账")
    public DataResponse<T> importGeneralSurvey(@RequestParam MultipartFile file) {
        generalSurveyService.importGeneralSurvey(file);
        return DataResponse.success();
    }

    @GetMapping("/export")
    @ApiOperation(value = "导出普查与技改台账")
    public void exportGeneralSurvey(@RequestParam(required = false) @ApiParam("列车号") String trainNo,
                                    @RequestParam(required = false) @ApiParam("技术通知单编号") String recNotifyNo,
                                    @RequestParam(required = false) @ApiParam("项目内容") String recDetail,
                                    @RequestParam(required = false) @ApiParam("作业单位") String orgType,
                                    HttpServletResponse response) throws IOException {
        generalSurveyService.exportGeneralSurvey(trainNo, recNotifyNo, recDetail, orgType, response);
    }

}
