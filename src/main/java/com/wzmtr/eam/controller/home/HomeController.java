package com.wzmtr.eam.controller.home;

import com.wzmtr.eam.dto.res.home.EChartResDTO;
import com.wzmtr.eam.dto.res.home.HomeCountResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.StatusWorkFlowLog;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.home.HomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 首页管理
 * @author  Ray
 * @version 1.0
 * @date 2023/09/12
 */
@RestController
@RequestMapping("/home")
@Api(tags = "首页")
public class HomeController {
    @Autowired
    private HomeService homeService;

    /**
     * 获取首页工作台列表
     * @param type 类型 1：待办 2：已办
     * @param pageReqDTO 分页信息
     * @return 工作列表
     */
    @GetMapping("/todoList")
    public PageResponse<StatusWorkFlowLog> todoList(@RequestParam String type, @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(homeService.todoList(type, pageReqDTO));
    }

    /**
     * 返回工作台工作数量
     * @return 工作数量
     */
    @GetMapping("/todoCount")
    public DataResponse<HomeCountResDTO> todoCount() {
        return DataResponse.of(homeService.todoCount());
    }

    /**
     * 催办
     * @param todoId 代办id
     */
    @GetMapping("/urging")
    public DataResponse<T> urgingTodo(@RequestParam String todoId) {
        homeService.urgingTodo(todoId);
        return DataResponse.success();
    }

    @ApiOperation(value = "图表查询")
    @PostMapping("/queryEChart")
    public DataResponse<EChartResDTO> queryChart() {
        return DataResponse.of(homeService.queryChart());
    }
}
