package com.wzmtr.eam.controller.statistic;

import com.wzmtr.eam.dto.req.statistic.CarFaultQueryReqDTO;
import com.wzmtr.eam.dto.req.statistic.FailreRateQueryReqDTO;
import com.wzmtr.eam.dto.req.statistic.MaterialQueryReqDTO;
import com.wzmtr.eam.dto.req.statistic.OneCarOneGearQueryReqDTO;
import com.wzmtr.eam.dto.res.statistic.*;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.statistic.StatisticService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("car/fault/query")
    @ApiOperation(value = "获取车辆故障统计列表")
    public PageResponse<CarFaultQueryResDTO> query(@RequestBody CarFaultQueryReqDTO reqDTO) {
        return PageResponse.of(statisticService.query(reqDTO));
    }
    @PostMapping("material/query")
    @ApiOperation(value = "物料统计")
    public PageResponse<MaterialResDTO> query(@RequestBody MaterialQueryReqDTO reqDTO) {
        return PageResponse.of(statisticService.query(reqDTO));
    }
    @PostMapping("failure/rate/query")
    @ApiOperation(value = "故障率")
    public DataResponse<List<FailureRateResDTO>> query(@RequestBody FailreRateQueryReqDTO reqDTO) {
        return DataResponse.of(statisticService.query(reqDTO));
    }
    @PostMapping("reliability/query")
    @ApiOperation(value = "可靠度指标")
    public DataResponse<List<ReliabilityResDTO>> reliabilityQuery(@RequestBody FailreRateQueryReqDTO reqDTO) {
        return DataResponse.of(statisticService.reliabilityQuery(reqDTO));
    }
    @PostMapping("one/car/one/gear/query")
    @ApiOperation(value = "一车一档查询")
    public DataResponse<OneCarOneGearResDTO> oneCarOneGearQuery(@RequestBody OneCarOneGearQueryReqDTO reqDTO) {
        return DataResponse.of(statisticService.oneCarOneGearQuery(reqDTO));
    }
    @PostMapping("one/car/one/gear/job/querydmer3")
    @ApiOperation(value = "一车一档检修作业列表查询(2级修90天包)")
    public DataResponse<InspectionJobListResDTO> querydmer3(@RequestBody OneCarOneGearQueryReqDTO reqDTO) {
        return DataResponse.of(statisticService.querydmer3(reqDTO));
    }
    @PostMapping("one/car/one/gear/job/queryER4")
    @ApiOperation(value = "一车一档检修作业列表查询(二级修(180天)")
    public DataResponse<InspectionJobListResDTO> queryER4(@RequestBody OneCarOneGearQueryReqDTO reqDTO) {
        return DataResponse.of(statisticService.queryER4(reqDTO));
    }
}
