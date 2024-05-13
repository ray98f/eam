package com.wzmtr.eam.mapper.overhaul;

import com.wzmtr.eam.dto.req.overhaul.SchedulingReqDTO;
import com.wzmtr.eam.dto.res.overhaul.SchedulingBuildResDTO;
import com.wzmtr.eam.dto.res.overhaul.SchedulingResDTO;
import com.wzmtr.eam.dto.res.overhaul.SchedulingRuleResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 预防性检修管理-检修工单排期
 * @author  Ray
 * @version 1.0
 * @date 2024/04/15
 */
@Mapper
@Repository
public interface SchedulingMapper {

    /**
     * 获取工单排期列表
     * @param equipCode 车辆设备编号
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 工单排期列表
     */
    List<SchedulingResDTO> listScheduling(String equipCode, String startTime, String endTime);

    /**
     * 获取工单排期详情
     * @param id id
     * @return 工单排期列表
     */
    SchedulingResDTO getSchedulingDetail(String id);

    /**
     * 获取车辆上一次的排期信息
     * @param equipCode 车辆设备编号
     * @param type 类型
     * @param day 日期
     * @return 上一次的排期信息
     */
    SchedulingResDTO getLastSchedulingDetail(String equipCode, String type, String day);

    /**
     * 编辑工单排期
     * @param req 编辑工单排期传参
     */
    void modifyScheduling(SchedulingReqDTO req);

    /**
     * 同步修改工单排期的后续排期
     * @param req 编辑工单排期传参
     * @param days 间隔天数
     */
    void syncModifyScheduling(SchedulingReqDTO req, Long days);

    /**
     * 移除今天之后的检修工单排期
     * @param equipCode 车辆设备编号
     * @param userId 操作人id
     */
    void removeSchedulingAfterNow(String equipCode, String userId);

    /**
     * 获取车辆绑定的工单触发规则
     * @param equipCode 车辆设备编号
     * @param type 类型
     * @return 工单触发规则列表
     */
    List<SchedulingRuleResDTO> getTrainRule(String equipCode, String type);

    /**
     * 获取车辆上一次已触发的排期信息
     * @param equipCode 车辆设备编号
     * @param type 类型
     * @return 已触发的排期信息
     */
    SchedulingResDTO getTrainLastTriggerScheduling(String equipCode, String type);

    /**
     * 判断二级修日期前两天内是否有一级修，有则返回一级修日期
     * @param startTime 二级修两天前的日期
     * @param endTime 二级修日期
     * @return 一级修日期
     */
    String getLastLevelOneRepairDay(String startTime, String endTime);

    /**
     * 新增检修工单排期
     * @param list 排期信息
     * @param userId 操作人id
     */
    void addOverhaulOrderScheduling(List<SchedulingBuildResDTO> list, String userId);

    /**
     * 修改触发标识（排除自动触发执行失败检修计划编号列表）
     * @param list 自动触发执行失败检修计划编号列表
     */
    void triggerScheduling(List<String> list);
}
