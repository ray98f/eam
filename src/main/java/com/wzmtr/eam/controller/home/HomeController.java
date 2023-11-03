package com.wzmtr.eam.controller.home;

import com.wzmtr.eam.dto.res.home.EChartResDTO;
import com.wzmtr.eam.dto.res.home.HomeCountResDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.service.home.HomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: Li.Wang
 * Date: 2023/9/12 11:22
 */
@RestController
@RequestMapping("/home")
@Api(tags = "首页")
public class HomeController {
    @Autowired
    private HomeService homeService;

    @PostMapping("/count")
    @ApiOperation(value = "数量统计(待办待阅等等。。。)")
    public DataResponse<HomeCountResDTO> count() {
        return DataResponse.of(homeService.count());
    }

    @ApiOperation(value = "图表查询")
    @PostMapping("/queryEChart")
    public DataResponse<EChartResDTO> queryEChart() {
        return DataResponse.of(homeService.queryEChart());
    }
}
