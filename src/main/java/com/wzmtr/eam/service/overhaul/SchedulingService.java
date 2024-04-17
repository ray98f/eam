package com.wzmtr.eam.service.overhaul;

import com.wzmtr.eam.dto.req.overhaul.SchedulingReqDTO;
import com.wzmtr.eam.dto.res.overhaul.SchedulingResDTO;
import com.wzmtr.eam.dto.res.overhaul.SchedulingListResDTO;

import java.text.ParseException;
import java.util.List;

/**
 * 预防性检修管理-检修工单排期
 * @author  Ray
 * @version 1.0
 * @date 2024/04/15
 */
public interface SchedulingService {

    /**
     * 获取工单排期列表
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 工单排期列表
     */
    List<SchedulingListResDTO> listScheduling(String startTime, String endTime);

    /**
     * 获取工单排期详情
     * @param id id
     * @return 工单排期列表
     */
    SchedulingResDTO getSchedulingDetail(String id);

    /**
     * 编辑工单排期
     * @param req 编辑工单排期传参
     */
    void modifyScheduling(SchedulingReqDTO req);

    /**
     * 生成检修工单排期
     * @param equipCodes 车辆设备编号列表
     * @throws ParseException 异常
     */
    void generateOrderScheduling(List<String> equipCodes) throws ParseException;

}
