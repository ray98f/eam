package com.wzmtr.eam.controller.statistic;

import com.wzmtr.eam.dto.req.fault.FaultQueryDetailReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultQueryReqDTO;
import com.wzmtr.eam.dto.req.statistic.*;
import com.wzmtr.eam.dto.res.equipment.GearboxChangeOilResDTO;
import com.wzmtr.eam.dto.res.equipment.GeneralSurveyResDTO;
import com.wzmtr.eam.dto.res.equipment.PartReplaceResDTO;
import com.wzmtr.eam.dto.res.equipment.WheelsetLathingResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.TrackQueryResDTO;
import com.wzmtr.eam.dto.res.statistic.*;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.fault.FaultQueryService;
import com.wzmtr.eam.service.statistic.StatisticService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * 统计分析
 * @author  Li.Wang
 * @version 1.0
 * @date 2023/08/21
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

    @PostMapping("/car/fault/query")
    @ApiOperation(value = "获取车辆故障统计列表")
    public DataResponse<CarFaultQueryResDTO> query(@RequestBody CarFaultQueryReqDTO reqDTO) {
        return DataResponse.of(statisticService.query(reqDTO));
    }

    @ApiOperation(value = "故障统计报表")
    @PostMapping("/fault/list")
    public PageResponse<FaultDetailResDTO> list(@RequestBody FaultQueryReqDTO reqDTO) {
        return PageResponse.of(faultQueryService.statustucList(reqDTO));
    }

    /**
     * 故障统计报表列表导出
     * @param reqDTO 入参
     * @param response response
     */
    @ApiOperation(value = "故障统计报表列表导出")
    @PostMapping("/fault/list/export")
    public void faultListExport(@RequestBody FaultQueryReqDTO reqDTO, HttpServletResponse response) throws IOException {
        statisticService.faultListExport(reqDTO, response);
    }

    @ApiOperation(value = "故障统计报表导出")
    @PostMapping("/fault/export")
    public void faultExport(@RequestBody FaultQueryDetailReqDTO reqDTO, HttpServletResponse response) {
        statisticService.faultExport(reqDTO, response);
    }

    @PostMapping("/material/query")
    @ApiOperation(value = "物料统计")
    public PageResponse<MaterialResDTO> query(@RequestBody MaterialQueryReqDTO reqDTO) {
        return PageResponse.of(statisticService.query(reqDTO));
    }

    @GetMapping("/material/query/export")
    @ApiOperation(value = "物料统计导出")
    public void materialExport(MaterialListReqDTO reqDTO, HttpServletResponse response) throws IOException {
        statisticService.materialExport(reqDTO, response);
    }

    /**
     * 新增站台门故障数据
     * @param req req
     * @return 成功
     */
    @PostMapping("/door/fault/add")
    @ApiOperation(value = "新增站台门故障数据")
    public DataResponse<T> addDoorFault(@RequestBody DoorFaultReqDTO req) {
        statisticService.addDoorFault(req);
        return DataResponse.success();
    }

    /**
     * 编辑站台门故障数据
     * @param req req
     * @return 成功
     */
    @PostMapping("/door/fault/modify")
    @ApiOperation(value = "编辑站台门故障数据")
    public DataResponse<T> modifyDoorFault(@RequestBody DoorFaultReqDTO req) {
        statisticService.modifyDoorFault(req);
        return DataResponse.success();
    }

    /**
     * 故障率指标
     * @param reqDTO req
     * @return 故障率指标
     */
    @PostMapping("/failure/rate/query")
    @ApiOperation(value = "故障率指标")
    public DataResponse<FailureRateDetailResDTO> failureRateQuery(@RequestBody FailreRateQueryReqDTO reqDTO) {
        return DataResponse.of(statisticService.failureRateQuery(reqDTO));
    }

    /**
     * 可靠性指标
     * @param reqDTO req
     * @return 可靠性指标
     */
    @PostMapping("/reliability/query")
    @ApiOperation(value = "可靠性指标")
    public DataResponse<ReliabilityListResDTO> reliabilityQuery(@RequestBody FailreRateQueryReqDTO reqDTO) {
        return DataResponse.of(statisticService.reliabilityQuery(reqDTO));
    }

    /********************************************一车一档*************************************************************/

    @PostMapping("/one/car/one/gear/query")
    @ApiOperation(value = "一车一档查询")
    public DataResponse<OneCarOneGearResDTO> oneCarOneGearQuery(@RequestBody OneCarOneGearReqDTO reqDTO) {
        return DataResponse.of(statisticService.oneCarOneGearQuery(reqDTO));
    }

    @PostMapping("/one/car/one/gear/job/querydmer3")
    @ApiOperation(value = "一车一档检修作业列表查询(2级修90天包)")
    public PageResponse<InspectionJobListResDTO> querydmer3(@RequestBody OneCarOneGearQueryReqDTO reqDTO) {
        return PageResponse.of(statisticService.querydmer3(reqDTO));
    }

    @GetMapping("/one/car/one/gear/job/querydmer3Export")
    @ApiOperation(value = "一车一档检修作业列表查询(2级修90天包)导出")
    public void querydmer3Export(@RequestParam(required = false) @ApiParam("时间开始") String startTime, @RequestParam(required = false) @ApiParam("时间结束") String endTime, @RequestParam(required = false) @ApiParam("列车号") String equipName, HttpServletResponse response) throws IOException {
        statisticService.querydmer3Export(startTime, endTime, equipName, response);
    }

    @PostMapping("/one/car/one/gear/job/queryER4")
    @ApiOperation(value = "一车一档检修作业列表查询(二级修(180天)")
    public PageResponse<InspectionJobListResDTO> queryER4(@RequestBody OneCarOneGearQueryReqDTO reqDTO) {
        return PageResponse.of(statisticService.queryER4(reqDTO));
    }

    @GetMapping("/one/car/one/gear/job/queryER4Export")
    @ApiOperation(value = "一车一档检修作业列表查询(二级修(180天)导出")
    public void queryER4Export(@RequestParam(required = false) @ApiParam("时间开始") String startTime, @RequestParam(required = false) @ApiParam("时间结束") String endTime, @RequestParam(required = false) @ApiParam("列车号") String equipName, HttpServletResponse response) throws IOException {
        statisticService.queryER4Export(startTime, endTime, equipName, response);
    }


    @PostMapping("/one/car/one/gear/job/queryER5")
    @ApiOperation(value = "一车一档检修作业列表查询(二级修(360天)")
    public PageResponse<InspectionJobListResDTO> queryER5(@RequestBody OneCarOneGearQueryReqDTO reqDTO) {
        return PageResponse.of(statisticService.queryER5(reqDTO));
    }

    @GetMapping("/one/car/one/gear/job/queryER5Export")
    @ApiOperation(value = "一车一档检修作业列表查询(二级修(360天)导出")
    public DataResponse<String> queryER5Export(@RequestParam(required = false) @ApiParam("时间开始") String startTime, @RequestParam(required = false) @ApiParam("时间结束") String endTime, @RequestParam(required = false) @ApiParam("列车号") String equipName, HttpServletResponse response) throws IOException {
        statisticService.queryER5Export(startTime, endTime, equipName, response);
        return DataResponse.success();
    }

    @GetMapping("/one/car/one/gear/job/queryDMFM21Export")
    @ApiOperation(value = "一车一档故障跟踪列表导出")
    public DataResponse<String> queryDMFM21Export(@RequestParam(required = false) @ApiParam("时间开始") String startTime, @RequestParam(required = false) @ApiParam("时间结束") String endTime, @RequestParam(required = false) @ApiParam("列车号") String equipName, HttpServletResponse response) throws IOException {
        statisticService.queryDMFM21Export(startTime, endTime, equipName, response);
        return DataResponse.success();
    }

    @PostMapping("/one/car/one/gear/job/queryDMFM21")
    @ApiOperation(value = "一车一档故障跟踪列表")
    public PageResponse<TrackQueryResDTO> queryDMFM21(@RequestBody OneCarOneGearQueryReqDTO reqDTO) {
        return PageResponse.of(statisticService.queryDMFM21(reqDTO));
    }

    @PostMapping("/one/car/one/gear/job/querydmdm20")
    @ApiOperation(value = "一车一档部件更换记录")
    public PageResponse<PartReplaceResDTO> querydmdm20(@RequestBody OneCarOneGearQueryReqDTO reqDTO) {
        return PageResponse.of(statisticService.querydmdm20(reqDTO));
    }

    @GetMapping("/one/car/one/gear/job/querydmdm20Export")
    @ApiOperation(value = "一车一档部件更换记录导出")
    public void querydmdm20Export(@RequestParam(required = false) @ApiParam("时间开始") String startTime, @RequestParam(required = false) @ApiParam("时间结束") String endTime, @RequestParam(required = false) @ApiParam("列车号") String equipName, HttpServletResponse response) throws IOException {
        statisticService.querydmdm20Export(equipName, startTime, endTime, response);
    }

    @PostMapping("/one/car/one/gear/job/queryFMHistory")
    @ApiOperation(value = "一车一档故障列表")
    public PageResponse<FaultDetailResDTO> queryFMHistory(@RequestBody OneCarOneGearQueryReqDTO reqDTO) {
        return PageResponse.of(statisticService.queryFMHistory(reqDTO));
    }

    @GetMapping("/one/car/one/gear/job/queryFMHistoryExport")
    @ApiOperation(value = "一车一档故障列表导出")
    public void queryFMHistoryExport(@RequestParam(required = false) @ApiParam("时间开始") String startTime, @RequestParam(required = false) @ApiParam("时间结束") String endTime, @RequestParam(required = false) @ApiParam("列车号") String equipName, HttpServletResponse response) throws IOException {
        statisticService.queryFMHistoryExport(startTime, endTime, equipName, response);
    }

    @PostMapping("/one/car/one/gear/job/pageGearboxChangeOil")
    @ApiOperation(value = "一车一档齿轮箱换油台账")
    public PageResponse<GearboxChangeOilResDTO> pageGearboxChangeOil(@RequestBody OneCarOneGearQueryReqDTO reqDTO) {
        return PageResponse.of(statisticService.pageGearboxChangeOil(reqDTO));
    }

    @PostMapping("/one/car/one/gear/job/pageGearboxChangeOilExport")
    @ApiOperation(value = "一车一档齿轮箱换油台账导出")
    public void pageGearboxChangeOilExport(@RequestBody OneCarOneGearQueryReqDTO reqDTO, HttpServletResponse response) throws IOException {
        statisticService.pageGearboxChangeOilExport(reqDTO, response);
    }

    @PostMapping("/one/car/one/gear/job/pageWheelsetLathing")
    @ApiOperation(value = "一车一档轮对镟修记录")
    public PageResponse<WheelsetLathingResDTO> pageWheelsetLathing(@RequestBody OneCarOneGearQueryReqDTO reqDTO) {
        return PageResponse.of(statisticService.pageWheelsetLathing(reqDTO));
    }

    @PostMapping("/one/car/one/gear/job/pageWheelsetLathingExport")
    @ApiOperation(value = "一车一档轮对镟修记录导出")
    public void pageWheelsetLathingExport(@RequestBody OneCarOneGearQueryReqDTO reqDTO, HttpServletResponse response) throws IOException {
        statisticService.pageWheelsetLathingExport(reqDTO, response);
    }

    @PostMapping("/one/car/one/gear/job/pageGeneralSurvey")
    @ApiOperation(value = "一车一档普查与技改")
    public PageResponse<GeneralSurveyResDTO> pageGeneralSurvey(@RequestBody OneCarOneGearQueryReqDTO reqDTO) {
        return PageResponse.of(statisticService.pageGeneralSurvey(reqDTO));
    }

    @PostMapping("/one/car/one/gear/job/pageGeneralSurveyExport")
    @ApiOperation(value = "一车一档普查与技改导出")
    public void pageGeneralSurveyExport(@RequestBody OneCarOneGearQueryReqDTO reqDTO, HttpServletResponse response) throws IOException {
        statisticService.pageGeneralSurveyExport(reqDTO, response);
    }

    @PostMapping("/one/car/one/gear/job/queryER2")
    @ApiOperation(value = "一车一档(二级修(30天)")
    public PageResponse<InspectionJobListResDTO> queryER2(@RequestBody OneCarOneGearQueryReqDTO reqDTO) {
        return PageResponse.of(statisticService.queryER2(reqDTO));
    }

    @PostMapping("/one/car/one/gear/job/queryER1")
    @ApiOperation(value = "一车一档(1级修)")
    public PageResponse<InspectionJobListResDTO> queryER1(@RequestBody OneCarOneGearQueryReqDTO reqDTO) {
        return PageResponse.of(statisticService.queryER1(reqDTO));
    }


    @GetMapping("/one/car/one/gear/job/queryER1Export")
    @ApiOperation(value = "一车一档(1级修)导出")
    public void queryER1Export(@RequestParam(required = false) @ApiParam("时间开始") String startTime, @RequestParam(required = false) @ApiParam("时间结束") String endTime, @RequestParam(required = false) @ApiParam("列车号") String equipName, HttpServletResponse response) throws IOException {
        statisticService.queryER1Export(startTime, endTime, equipName, response);
    }

    /*********************************************      RAMS      *****************************************************/

    @PostMapping("/rams/queryCountFaultType")
    @ApiOperation(value = "各系统故障情况统计")
    public DataResponse<List<FaultConditionResDTO>> queryCountFaultType() {
        return DataResponse.of(statisticService.queryCountFaultType());
    }

    @GetMapping("/rams/query4AQYYZB")
    @ApiOperation(value = "车辆可靠性表现")
    public DataResponse<RamsCarResDTO> query4AQYYZB() {
        return DataResponse.of(statisticService.query4AQYYZB());
    }

    @GetMapping("/rams/queryresult3")
    @ApiOperation(value = "系统故障统计趋势")
    public DataResponse<List<SystemFaultsResDTO>> queryresult3(@RequestParam(required = false) @ApiParam("开始时间") String startDate, @RequestParam(required = false) @ApiParam("结束") String endDate, @RequestParam(required = false) @ApiParam("系统分类") String sys) {
        return DataResponse.of(statisticService.queryresult3(startDate, endDate, sys));
    }

    @GetMapping("/rams/queryresult2")
    @ApiOperation(value = "故障影响统计")
    public DataResponse<List<RamsResult2ResDTO>> queryresult2(@RequestParam(required = false) @ApiParam("开始时间") String startDate, @RequestParam(required = false) @ApiParam("结束") String endDate) {
        return DataResponse.of(statisticService.queryresult2(startDate, endDate));
    }

    @GetMapping("/rams/querySysPerform")
    @ApiOperation(value = "各系统可靠性统计")
    public DataResponse<List<RamsSysPerformResDTO>> querySysPerform() {
        return DataResponse.of(statisticService.querySysPerform());
    }

    /**
     * 各系统可靠性统计-导出
     * @param response response
     */
    @PostMapping("/rams/exportSysPerform")
    @ApiOperation(value = "各系统可靠性统计-导出")
    public void exportSysPerform(HttpServletResponse response) throws IOException {
        statisticService.exportSysPerform(response);
    }

    @PostMapping("/rams/queryRAMSFaultList")
    @ApiOperation(value = "RAMS故障列表")
    public PageResponse<FaultRamsResDTO> queryRAMSFaultList(@RequestBody RamsTimeReqDTO reqDTO) {
        return PageResponse.of(statisticService.queryRAMSFaultList(reqDTO));
    }

    /**
     * RAMS-列车可靠性统计
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param trainNo 列车号
     * @return 列车可靠性统计
     */
    @GetMapping("/rams/train/reliability")
    @ApiOperation(value = "列车可靠性统计")
    public DataResponse<RamsTrainReliabilityResDTO> trainReliability(@RequestParam String startTime,
                                                                     @RequestParam String endTime,
                                                                     @RequestParam String trainNo) {
        return DataResponse.of(statisticService.trainReliability(startTime, endTime, trainNo));
    }

    /**
     * RAMS-列车可靠性统计-故障列表
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param trainNo 列车号
     * @return 故障列表
     */
    @GetMapping("/rams/train/reliability/fault")
    @ApiOperation(value = "RAMS-列车可靠性统计-故障列表")
    public PageResponse<FaultRamsResDTO> trainReliabilityFaultList(@RequestParam String startTime,
                                                                   @RequestParam String endTime,
                                                                   @RequestParam String trainNo,
                                                                   @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(statisticService.trainReliabilityFaultList(startTime, endTime, trainNo, pageReqDTO));
    }

    /**
     * 各系统指定时间范围内故障数量统计-开放接口
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 故障数量统计
     */
    @GetMapping("/subject/fault/open")
    @ApiOperation(value = "各系统指定时间范围内故障数量统计-开放接口")
    public DataResponse<List<SubjectFaultResDTO>> getSubjectFaultOpen(@RequestParam String startTime,
                                                                      @RequestParam String endTime) {
        return DataResponse.of(statisticService.getSubjectFaultOpen(startTime, endTime));
    }

}