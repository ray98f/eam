package com.wzmtr.eam.controller.statistic;

import com.wzmtr.eam.dto.req.statistic.CarFaultQueryReqDTO;
import com.wzmtr.eam.dto.res.statistic.CarFaultQueryResDTO;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.statistic.CarFaultService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 10:27
 */
@Slf4j
@RestController
@RequestMapping("/statistic/car/fault")
@Api(tags = "统计分析-车辆故障")
@Validated
public class CarFaultController {
    @Autowired
    private CarFaultService carFaultService;

    @PostMapping("/query")
    @ApiOperation(value = "获取车辆故障统计分析列表")
    public PageResponse<CarFaultQueryResDTO> pageOverhaulWeekPlan(CarFaultQueryReqDTO reqDTO) {
        return PageResponse.of(carFaultService.query(reqDTO));
    }
}
