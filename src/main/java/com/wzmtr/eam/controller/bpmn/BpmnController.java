package com.wzmtr.eam.controller.bpmn;

import com.wzmtr.eam.dto.req.ExamineListReq;
import com.wzmtr.eam.dto.req.bpmn.BpmnExamineDTO;
import com.wzmtr.eam.dto.req.bpmn.StartInstanceVO;
import com.wzmtr.eam.dto.res.ExamineListRes;
import com.wzmtr.eam.dto.res.ExaminedListRes;
import com.wzmtr.eam.dto.res.HisListRes;
import com.wzmtr.eam.dto.res.RunningListRes;
import com.wzmtr.eam.dto.res.bpmn.ExamineOpinionRes;
import com.wzmtr.eam.dto.res.bpmn.FlowRes;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.bpmn.BpmnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
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

    @ApiOperation(value = "追踪任务")
    @GetMapping("/taskProgress")
    public DataResponse<String> taskProgress(@RequestParam("instId") String instId) {
        return DataResponse.of(bpmnService.taskProgress(instId));
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

    @GetMapping("/queryFlowList")
    @ApiOperation(value = "根据登录人获取流程列表")
    public DataResponse<List<FlowRes>> queryFlowList(@RequestParam("name") String name,
                                                     @RequestParam("modelKey") String modelKey) throws Exception {
        return DataResponse.of(bpmnService.queryFlowList(name, modelKey));
    }

    @GetMapping("/queryFirstTaskKeyByModelId")
    @ApiOperation(value = "根据modelId查询第一个流程节点")
    public DataResponse<String> queryFirstTaskKeyByModelId(@RequestParam("modelId") String modelId) throws Exception {
        return DataResponse.of(bpmnService.queryFirstTaskKeyByModelId(modelId));
    }

    @ApiOperation(value = "根据modelId和taskKey返回对应的taskName")
    @GetMapping("/queryTaskNameByModelIdAndTaskKey")
    public DataResponse<String> queryTaskNameByModelIdAndTaskKey(@RequestParam("modelId") String modelId,
                                                                 @RequestParam("taskKey") String taskKey) throws Exception {
        return DataResponse.of(bpmnService.queryTaskNameByModelIdAndTaskKey(modelId, taskKey));
    }

    @ApiOperation(value = "待办列表")
    @PostMapping("/examineList")
    public DataResponse<List<ExamineListRes>> examineList(@RequestBody ExamineListReq req) {
        return DataResponse.of(bpmnService.examineList(req));
    }

    @ApiOperation(value = "已办列表")
    @PostMapping("/examinedList")
    public PageResponse<ExaminedListRes> examinedList(@RequestBody ExamineListReq req) {
        return PageResponse.of(bpmnService.examinedList(req));
    }

    @ApiOperation(value = "已发列表")
    @PostMapping("/runningList")
    public PageResponse<RunningListRes> runningList(@RequestBody ExamineListReq req) {
        return PageResponse.of(bpmnService.runningList(req));
    }

    @ApiOperation(value = "办结列表")
    @PostMapping("/hisList")
    public PageResponse<HisListRes> hisList(@RequestBody ExamineListReq req) {
        return PageResponse.of(bpmnService.hisList(req));
    }

    @ApiOperation(value = "流程详情接口 获取存在流程引擎的id 回查详细记录")
    @GetMapping("/getSelfId")
    public DataResponse<String> getSelfId(@RequestParam("procId") String procId) {
        return DataResponse.of(bpmnService.getSelfId(procId));
    }

    @ApiOperation(value = "通用审核同意")
    @GetMapping("/agree")
    public DataResponse<T> agree(@RequestParam("taskId") String taskId,
                                 @RequestParam(value = "opinion", required = false) String opinion,
                                 @RequestParam("fromId") String fromId) {
        bpmnService.agree(taskId, opinion, fromId);
        return DataResponse.success();
    }

    @ApiOperation(value = "通用审核驳回")
    @GetMapping("/reject")
    public DataResponse<T> reject(@RequestParam("taskId") String taskId,
                                  @RequestParam(value = "opinion", required = false) String opinion,
                                  @RequestParam("fromId") String fromId) {
        bpmnService.reject(taskId, opinion, fromId);
        return DataResponse.success();
    }
}
