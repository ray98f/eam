package com.wzmtr.eam.controller.bpmn;

import com.wzmtr.eam.dto.req.bpmn.BpmnExamineDTO;
import com.wzmtr.eam.dto.req.bpmn.StartInstanceVO;
import com.wzmtr.eam.dto.res.bpmn.ExamineOpinionRes;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.service.bpmn.BpmnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author lize
 * @Date 2023/8/16
 */

@Slf4j
@RestController
@RequestMapping("/bpmn")
@Api(tags = "流程引擎接口")
@Validated
public class BpmnController {

    @Autowired
    private BpmnService bpmnService;

    @PostMapping("/startInstance")
    @ApiOperation(value = "提交表单")
    public DataResponse<String> startInstance(@RequestBody StartInstanceVO req) {
        return DataResponse.of(bpmnService.startInstance(req));
    }

    @PostMapping("/agreeInstance")
    @ApiOperation(value = "审核通过")
    public DataResponse<T> agreeInstance(@RequestBody BpmnExamineDTO req) {
        bpmnService.agreeInstance(req);
        return DataResponse.success();
    }

    @PostMapping("/rejectInstance")
    @ApiOperation(value = "审核不通过")
    public DataResponse<T> rejectInstance(@RequestBody BpmnExamineDTO req) {
        bpmnService.rejectInstance(req);
        return DataResponse.success();
    }

    @GetMapping("/login")
    @ApiOperation(value = "登录")
    public DataResponse<String> login(@RequestParam("account") String account, @RequestParam("password") String password) {
        return DataResponse.of(bpmnService.login(account, password));
    }

    @GetMapping("/examineOpinion")
    @ApiOperation(value = "审核意见详情")
    public DataResponse<List<ExamineOpinionRes>> examineOpinion(@RequestParam("instId") String instId) {
        return DataResponse.of(bpmnService.examineOpinion(instId));
    }

    @GetMapping("/nextTaskKey")
    @ApiOperation(value = "该实例下一个节点key")
    public DataResponse<String> nextTaskKey(@RequestParam("procId") String procId) {
        return DataResponse.of(bpmnService.nextTaskKey(procId));
    }

    @GetMapping("/queryTaskIdByProcId")
    @ApiOperation(value = "根据procId获取最新的TaskId")
    public DataResponse<String> queryTaskIdByProcId(@RequestParam("procId") String procId) {
        return DataResponse.of(bpmnService.queryTaskIdByProcId(procId));
    }
}
