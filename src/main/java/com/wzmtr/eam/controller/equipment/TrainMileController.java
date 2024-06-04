package com.wzmtr.eam.controller.equipment;

import com.wzmtr.eam.dto.req.equipment.TrainMileDailyReqDTO;
import com.wzmtr.eam.dto.req.equipment.TrainMileReqDTO;
import com.wzmtr.eam.dto.res.equipment.TrainMileDailyResDTO;
import com.wzmtr.eam.dto.res.equipment.TrainMileResDTO;
import com.wzmtr.eam.dto.res.equipment.TrainMileageResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.service.equipment.TrainMileService;
import com.wzmtr.eam.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Objects;

/**
 * 设备管理-车辆行走里程台账
 * @author  Ray
 * @version 1.0
 * @date 2023/07/27
 */
@Slf4j
@RestController
@RequestMapping("/train")
@Api(tags = "设备管理-车辆行走里程台账")
@Validated
public class TrainMileController {

    @Resource
    private TrainMileService trainMileService;

    @GetMapping("/mile/page")
    @ApiOperation(value = "获取车辆行走里程台账列表")
    public PageResponse<TrainMileResDTO> pageTrainMile(@RequestParam(required = false) @ApiParam("设备编码") String equipCode,
                                                       @RequestParam(required = false) @ApiParam("设备名称") String equipName,
                                                       @RequestParam(required = false) @ApiParam("线路编号") String originLineNo,
                                                       @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(trainMileService.pageTrainMile(equipCode, equipName, originLineNo, pageReqDTO));
    }

    @GetMapping("/mile/detail")
    @ApiOperation(value = "获取车辆行走里程台账详情")
    public DataResponse<TrainMileResDTO> getTrainMileDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(trainMileService.getTrainMileDetail(id));
    }

    @GetMapping("/mile/export")
    @ApiOperation(value = "导出车辆行走里程台账")
    public void exportTrainMile(@RequestParam(required = false) @ApiParam("设备编码") String equipCode,
                                @RequestParam(required = false) @ApiParam("设备名称") String equipName,
                                @RequestParam(required = false) @ApiParam("线路编号") String originLineNo,
                                HttpServletResponse response) throws IOException {
        trainMileService.exportTrainMile(equipCode, equipName, originLineNo, response);
    }

    @PostMapping("/mile/modify")
    @ApiOperation(value = "更新车辆行走里程台账")
    public DataResponse<T> modifyTrainMile(@RequestBody TrainMileReqDTO trainMileReqDTO) {
        trainMileService.modifyTrainMile(trainMileReqDTO);
        return DataResponse.success();
    }

    @GetMapping("/mileage/page")
    @ApiOperation(value = "获取车辆行走里程历史列表")
    public PageResponse<TrainMileageResDTO> pageTrainMileage(@RequestParam(required = false) @ApiParam("开始时间") String startTime,
                                                             @RequestParam(required = false) @ApiParam("结束时间") String endTime,
                                                             @RequestParam(required = false) @ApiParam("设备编号") String equipCode,
                                                             @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(trainMileService.pageTrainMileage(startTime, endTime, equipCode, pageReqDTO));
    }

    @GetMapping("/mileage/detail")
    @ApiOperation(value = "获取车辆行走里程历史详情")
    public DataResponse<TrainMileageResDTO> getTrainMileageDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(trainMileService.getTrainMileageDetail(id));
    }

    @GetMapping("/mileage/export")
    @ApiOperation(value = "导出车辆行走里程历史列表")
    public void exportTrainMileage(@RequestParam(required = false) @ApiParam("开始时间") String startTime,
                                   @RequestParam(required = false) @ApiParam("结束时间") String endTime,
                                   @RequestParam(required = false) @ApiParam("设备编号") String equipCode,
                                   HttpServletResponse response) throws IOException {
        trainMileService.exportTrainMileage(startTime, endTime, equipCode, response);
    }

    /**
     * 获取每日列车里程及能耗列表
     * @param day 时间
     * @param equipCode 设备编号
     * @param equipName 车号
     * @param pageReqDTO 分页参数
     * @return 每日列车里程及能耗列表
     */
    @GetMapping("/mile/daily/page")
    @ApiOperation(value = "获取每日列车里程及能耗列表")
    public PageResponse<TrainMileDailyResDTO> pageTrainDailyMile(@RequestParam(required = false) @ApiParam("时间") String day,
                                                                 @RequestParam(required = false) @ApiParam("设备编号") String equipCode,
                                                                 @RequestParam(required = false) @ApiParam("车号") String equipName,
                                                                 @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(trainMileService.pageTrainDailyMile(day, equipCode, equipName, pageReqDTO));
    }

    /**
     * 获取每日列车里程及能耗详情
     * @param id 主键id
     * @return 每日列车里程及能耗详情
     */
    @GetMapping("/mile/daily/detail")
    @ApiOperation(value = "获取每日列车里程及能耗详情")
    public DataResponse<TrainMileDailyResDTO> getTrainDailyMileDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(trainMileService.getTrainDailyMileDetail(id));
    }

    /**
     * 新增当日列车里程及能耗
     * @param trainMileDailyReqDTO 当日列车里程及能耗返回类
     * @return 执行状态
     */
    @PostMapping("/mile/daily/add")
    @ApiOperation(value = "新增当日列车里程及能耗")
    public DataResponse<T> addTrainDailyMile(@RequestBody TrainMileDailyReqDTO trainMileDailyReqDTO) {
        trainMileService.addTrainDailyMile(trainMileDailyReqDTO);
        return DataResponse.success();
    }

    /**
     * 修改当日列车里程及能耗
     * @param trainMileDailyReqDTO 当日列车里程及能耗返回类
     * @return 执行状态
     */
    @PostMapping("/mile/daily/modify")
    @ApiOperation(value = "修改当日列车里程及能耗")
    public DataResponse<T> modifyTrainDailyMile(@RequestBody TrainMileDailyReqDTO trainMileDailyReqDTO) {
        trainMileService.modifyTrainDailyMile(trainMileDailyReqDTO);
        return DataResponse.success();
    }

    /**
     * 删除当日列车里程及能耗
     * @param baseIdsEntity ids
     * @return 执行状态
     */
    @PostMapping("/mile/daily/delete")
    @ApiOperation(value = "删除当日列车里程及能耗")
    public DataResponse<T> deleteTrainDailyMile(@RequestBody BaseIdsEntity baseIdsEntity) {
        trainMileService.deleteTrainDailyMile(baseIdsEntity);
        return DataResponse.success();
    }

    /**
     * 导出每日列车里程及能耗列表
     * @param baseIdsEntity ids
     * @param response res
     * @throws IOException 流异常
     */
    @PostMapping("/mile/daily/export")
    @ApiOperation(value = "导出每日列车里程及能耗列表")
    public void exportTrainDailyMile(@RequestBody BaseIdsEntity baseIdsEntity, HttpServletResponse response) throws IOException {
        if (Objects.isNull(baseIdsEntity) || StringUtils.isEmpty(baseIdsEntity.getIds())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "请先勾选后导出");
        }
        trainMileService.exportTrainDailyMile(baseIdsEntity.getIds(), response);
    }

    /**
     * 导入每日列车里程及能耗列表
     * @param file 文件
     * @return 导入成功
     * @throws ParseException 异常
     */
    @PostMapping("/mile/daily/import")
    @ApiOperation(value = "导入每日列车里程及能耗列表")
    public DataResponse<T> importTrainDailyMile(@RequestParam MultipartFile file) throws ParseException {
        trainMileService.importTrainDailyMile(file);
        return DataResponse.success();
    }

    /**
     * 初始化每日列车里程及能耗
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 初始话成功
     */
    @GetMapping("/mile/daily/init")
    @ApiOperation(value = "初始化每日列车里程及能耗")
    public DataResponse<T> initTrainDailyMile(@RequestParam String startTime,
                                              @RequestParam String endTime) {
        trainMileService.initTrainDailyMile(startTime, endTime);
        return DataResponse.success();
    }

    /**
     * 根据日期获取当天所有列车的总里程（含非运营）总数-开放接口
     * @param day 日期
     * @return 总数
     */
    @GetMapping("/mile/daily/sum/open")
    @ApiOperation(value = "根据日期获取当天所有列车的总里程（含非运营）总数-开放接口")
    public DataResponse<BigDecimal> getSumDailyMileByDay(@RequestParam String day) {
        return DataResponse.of(trainMileService.getSumDailyMileByDay(day));
    }
}
