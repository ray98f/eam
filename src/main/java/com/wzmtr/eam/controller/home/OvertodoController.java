package com.wzmtr.eam.controller.home;

import com.wzmtr.eam.dto.res.home.EChartResDTO;
import com.wzmtr.eam.dto.res.home.HomeCountResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.StatusWorkFlowLog;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.bpmn.OverTodoService;
import com.wzmtr.eam.service.home.HomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 首页管理
 *
 * @author Ray
 * @version 1.0
 * @date 2023/09/12
 */
@RestController
@RequestMapping("/overtodo")
@Api(tags = "首页")
public class OvertodoController {
    @Autowired
    private OverTodoService overTodoService;

    @GetMapping("/update")
    @ApiOperation(value = "更新状态")
    public DataResponse<Void> count(@RequestParam String bizNo) {
        overTodoService.overTodo(bizNo);
        return DataResponse.success();
    }


}
