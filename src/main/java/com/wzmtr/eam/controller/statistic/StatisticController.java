package com.wzmtr.eam.controller.statistic;

import com.wzmtr.eam.dto.req.fault.FaultQueryReqDTO;
import com.wzmtr.eam.dto.req.statistic.*;
import com.wzmtr.eam.dto.res.equipment.GearboxChangeOilResDTO;
import com.wzmtr.eam.dto.res.equipment.GeneralSurveyResDTO;
import com.wzmtr.eam.dto.res.equipment.PartReplaceResDTO;
import com.wzmtr.eam.dto.res.equipment.WheelsetLathingResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.TrackQueryResDTO;
import com.wzmtr.eam.dto.res.statistic.*;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.fault.FaultQueryService;
import com.wzmtr.eam.service.statistic.StatisticService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 10:27
 */
@Slf4j
@RestController
@RequestMapping("/statistic")
@Api(tags = "统计分析")
@Validated
public class StatisticController {
    @Autowired
    private StatisticService statisticService;
    @Autowired
    private FaultQueryService faultQueryService;

    @PostMapping("car/fault/query")
    @ApiOperation(value = "获取车辆故障统计列表")
    public DataResponse<CarFaultQueryResDTO> query(@RequestBody CarFaultQueryReqDTO reqDTO) {
        return DataResponse.of(statisticService.query(reqDTO));
    }

    @ApiOperation(value = "故障统计列表")
    @PostMapping("fault/list")
    public DataResponse<List<FaultDetailResDTO>> list(@RequestBody FaultQueryReqDTO reqDTO) {
        return DataResponse.of(faultQueryService.statisticList(reqDTO));
    }

    @PostMapping("material/query")
    @ApiOperation(value = "物料统计")
    public PageResponse<MaterialResDTO> query(@RequestBody MaterialQueryReqDTO reqDTO) {
        return PageResponse.of(statisticService.query(reqDTO));
    }
    @GetMapping("material/query/export")
    @ApiOperation(value = "物料统计导出")
    public void materialExport(MaterialListReqDTO reqDTO, HttpServletResponse response) {
        statisticService.materialExport(reqDTO,response);
    }

    @PostMapping("failure/rate/query")
    @ApiOperation(value = "故障率")
    public DataResponse<FailureRateDetailResDTO> query(@RequestBody FailreRateQueryReqDTO reqDTO) {
        return DataResponse.of(statisticService.query(reqDTO));
    }

    @PostMapping("reliability/query")
    @ApiOperation(value = "可靠度指标")
    public DataResponse<ReliabilityListResDTO> reliabilityQuery(@RequestBody FailreRateQueryReqDTO reqDTO) {
        return DataResponse.of(statisticService.reliabilityQuery(reqDTO));
    }

    @PostMapping("one/car/one/gear/query")
    @ApiOperation(value = "一车一档查询")
    public DataResponse<OneCarOneGearResDTO> oneCarOneGearQuery(@RequestBody OneCarOneGearReqDTO reqDTO) {
        return DataResponse.of(statisticService.oneCarOneGearQuery(reqDTO));
    }

    @PostMapping("one/car/one/gear/job/querydmer3")
    @ApiOperation(value = "一车一档检修作业列表查询(2级修90天包)")
    public PageResponse<InspectionJobListResDTO> querydmer3(@RequestBody OneCarOneGearQueryReqDTO reqDTO) {
        return PageResponse.of(statisticService.querydmer3(reqDTO));
    }

    @PostMapping("one/car/one/gear/job/queryER4")
    @ApiOperation(value = "一车一档检修作业列表查询(二级修(180天)")
    public PageResponse<InspectionJobListResDTO> queryER4(@RequestBody OneCarOneGearQueryReqDTO reqDTO) {
        return PageResponse.of(statisticService.queryER4(reqDTO));
    }

    @PostMapping("one/car/one/gear/job/queryER5")
    @ApiOperation(value = "一车一档检修作业列表查询(二级修(360天)")
    public PageResponse<InspectionJobListResDTO> queryER5(@RequestBody OneCarOneGearQueryReqDTO reqDTO) {
        return PageResponse.of(statisticService.queryER5(reqDTO));
    }

    @PostMapping("one/car/one/gear/job/queryDMFM21")
    @ApiOperation(value = "一车一档故障跟踪列表")
    public PageResponse<TrackQueryResDTO> queryDMFM21(@RequestBody OneCarOneGearQueryReqDTO reqDTO) {
        return PageResponse.of(statisticService.queryDMFM21(reqDTO));
    }

    @PostMapping("one/car/one/gear/job/querydmdm20")
    @ApiOperation(value = "一车一档部件更换记录")
    public PageResponse<PartReplaceResDTO> querydmdm20(@RequestBody OneCarOneGearQueryReqDTO reqDTO) {
        return PageResponse.of(statisticService.querydmdm20(reqDTO));
    }

    @PostMapping("one/car/one/gear/job/queryFMHistory")
    @ApiOperation(value = "一车一档故障列表")
    public PageResponse<FaultDetailResDTO> queryFMHistory(@RequestBody OneCarOneGearQueryReqDTO reqDTO) {
        return PageResponse.of(statisticService.queryFMHistory(reqDTO));
    }

    @PostMapping("one/car/one/gear/job/pageGearboxChangeOil")
    @ApiOperation(value = "一车一档齿轮箱换油台账")
    public PageResponse<GearboxChangeOilResDTO> pageGearboxChangeOil(@RequestBody OneCarOneGearQueryReqDTO reqDTO) {
        return PageResponse.of(statisticService.pageGearboxChangeOil(reqDTO));
    }

    @PostMapping("one/car/one/gear/job/pageWheelsetLathing")
    @ApiOperation(value = "一车一档轮对镟修记录")
    public PageResponse<WheelsetLathingResDTO> pageWheelsetLathing(@RequestBody OneCarOneGearQueryReqDTO reqDTO) {
        return PageResponse.of(statisticService.pageWheelsetLathing(reqDTO));
    }

    @PostMapping("one/car/one/gear/job/pageGeneralSurvey")
    @ApiOperation(value = "一车一档普查与技改")
    public PageResponse<GeneralSurveyResDTO> pageGeneralSurvey(@RequestBody OneCarOneGearQueryReqDTO reqDTO) {
        return PageResponse.of(statisticService.pageGeneralSurvey(reqDTO));
    }

    @PostMapping("one/car/one/gear/job/queryER2")
    @ApiOperation(value = "一车一档(二级修(30天)")
    public PageResponse<InspectionJobListResDTO> queryER2(@RequestBody OneCarOneGearQueryReqDTO reqDTO) {
        return PageResponse.of(statisticService.queryER2(reqDTO));
    }

    @PostMapping("one/car/one/gear/job/queryER1")
    @ApiOperation(value = "一车一档(1级修)")
    public PageResponse<InspectionJobListResDTO> queryER1(@RequestBody OneCarOneGearQueryReqDTO reqDTO) {
        return PageResponse.of(statisticService.queryER1(reqDTO));
    }


    @PostMapping("rams/queryCountFaultType")
    @ApiOperation(value = "各系统故障情况统计")
    public DataResponse<List<FaultConditionResDTO>> queryCountFaultType() {
        return DataResponse.of(statisticService.queryCountFaultType());
    }
    @GetMapping("rams/query4AQYYZB")
    @ApiOperation(value = "车辆可靠性表现")
    public DataResponse<RAMSCarResDTO> query4AQYYZB() {
        return DataResponse.of(statisticService.query4AQYYZB());
    }

    @GetMapping("rams/queryresult3")
    @ApiOperation(value = "系统故障统计趋势")
    public DataResponse<List<SystemFaultsResDTO>> queryresult3(@RequestParam(required = false) @ApiParam("开始时间") String startDate, @RequestParam(required = false) @ApiParam("结束") String endDate, @RequestParam(required = false) @ApiParam("系统分类") String sys) {
        return DataResponse.of(statisticService.queryresult3(startDate, endDate, sys));
    }

    @GetMapping("rams/queryresult2")
    @ApiOperation(value = "故障影响统计")
    public DataResponse<List<RAMSResult2ResDTO>> queryresult2(@RequestParam(required = false) @ApiParam("开始时间") String startDate, @RequestParam(required = false) @ApiParam("结束") String endDate) {
        return DataResponse.of(statisticService.queryresult2(startDate, endDate));
    }

    @GetMapping("rams/querySysPerform")
    @ApiOperation(value = "各系统可靠性统计")
    public DataResponse<List<RAMSSysPerformResDTO>> querySysPerform() {
        return DataResponse.of(statisticService.querySysPerform());
    }
    @PostMapping("rams/queryRAMSFaultList")
    @ApiOperation(value = "RAMS故障列表")
    public PageResponse<FaultRAMSResDTO> queryRAMSFaultList(@RequestBody RAMSTimeReqDTO reqDTO) {
        return PageResponse.of(statisticService.queryRAMSFaultList(reqDTO));
    }

}