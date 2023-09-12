package com.wzmtr.eam.controller.home;

import com.wzmtr.eam.dto.req.fault.FaultSubmitReqDTO;
import com.wzmtr.eam.dto.req.mea.CheckPlanListReqDTO;
import com.wzmtr.eam.dto.res.home.HomeCountResDTO;
import com.wzmtr.eam.dto.res.mea.CheckPlanResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.home.HomeService;
import com.wzmtr.eam.service.mea.CheckPlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    // @ApiOperation(value = "故障分析流程提交")
    // @PostMapping("/submit")
    // public DataResponse<String> submit(@RequestBody FaultSubmitReqDTO reqDTO) {
    //     analyzeService.submit(reqDTO);
    //     return DataResponse.success();
    // }
}
