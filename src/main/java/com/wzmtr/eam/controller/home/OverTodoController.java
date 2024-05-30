package com.wzmtr.eam.controller.home;

import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.service.bpmn.OverTodoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/overtodo")
public class OverTodoController {
    @Autowired
    private OverTodoService overTodoService;

    @GetMapping("/update")
    @ApiOperation(value = "更新状态")
    public DataResponse<Void> count(@RequestParam String bizNo) {
        overTodoService.overTodo(bizNo);
        return DataResponse.success();
    }

}
