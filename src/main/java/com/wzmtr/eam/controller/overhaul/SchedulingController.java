package com.wzmtr.eam.controller.overhaul;

import com.wzmtr.eam.dto.req.overhaul.SchedulingReqDTO;
import com.wzmtr.eam.dto.res.overhaul.SchedulingResDTO;
import com.wzmtr.eam.dto.res.overhaul.SchedulingListResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.service.overhaul.SchedulingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;

/**
 * 预防性检修管理-检修工单排期
 * @author  Ray
 * @version 1.0
 * @date 2024/04/15
 */
@Slf4j
@RestController
@RequestMapping("/overhaul/order/scheduling")
@Api(tags = "预防性检修管理-检修工单排期")
@Validated
public class SchedulingController {

    @Resource
    private SchedulingService schedulingService;

    /**
     * 获取工单排期列表
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 工单排期列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取工单排期列表")
    public DataResponse<List<SchedulingListResDTO>> listScheduling(@RequestParam String startTime, @RequestParam String endTime) {
        return DataResponse.of(schedulingService.listScheduling(startTime, endTime));
    }

    /**
     * 获取工单排期详情
     * @param id id
     * @return 工单排期
     */
    @GetMapping("/detail")
    @ApiOperation(value = "获取工单排期详情")
    public DataResponse<SchedulingResDTO> getSchedulingDetail(@RequestParam String id) {
        return DataResponse.of(schedulingService.getSchedulingDetail(id));
    }

    /**
     * 获取上一次工单排期详情
     * @param id id
     * @return 工单排期
     */
    @GetMapping("/last")
    @ApiOperation(value = "获取上一次工单排期详情")
    public DataResponse<SchedulingResDTO> getLastSchedulingDetail(@RequestParam String id) {
        return DataResponse.of(schedulingService.getLastSchedulingDetail(id));
    }

    /**
     * 编辑工单排期
     * @param req 编辑工单排期传参
     * @return 成功
     */
    @PostMapping("/modify")
    @ApiOperation(value = "编辑工单排期")
    public DataResponse<T> modifyScheduling(@RequestBody SchedulingReqDTO req) {
        schedulingService.modifyScheduling(req);
        return DataResponse.success();
    }

    /**
     * 生成检修工单排期
     * @param baseIdsEntity 车辆设备编号列表
     * @return 成功
     * @throws ParseException 异常
     */
    @PostMapping("/generate")
    @ApiOperation(value = "生成检修工单排期")
    public DataResponse<T> generateOrderScheduling(@RequestBody BaseIdsEntity baseIdsEntity) throws ParseException {
        schedulingService.generateOrderScheduling(baseIdsEntity.getIds());
        return DataResponse.success();
    }
}
