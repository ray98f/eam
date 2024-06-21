package com.wzmtr.eam.mapper.statistic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.statistic.OneCarOneGearQueryReqDTO;
import com.wzmtr.eam.dto.res.equipment.GearboxChangeOilResDTO;
import com.wzmtr.eam.dto.res.equipment.GeneralSurveyResDTO;
import com.wzmtr.eam.dto.res.equipment.PartReplaceResDTO;
import com.wzmtr.eam.dto.res.equipment.WheelsetLathingResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.TrackQueryResDTO;
import com.wzmtr.eam.dto.res.statistic.InspectionJobListResDTO;
import com.wzmtr.eam.dto.res.statistic.OneCarOneGearResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 11:03
 */
@Repository
@Mapper
public interface OneCarOneGearMapper {

    List<OneCarOneGearResDTO> query(String equipName);

    OneCarOneGearResDTO querySummary(String endTime, String startTime, String endTime1, String startTime1, String equipName);

    /**
     * 二级修(90天
     *
     * @param
     * @return
     */
    Page<InspectionJobListResDTO> querydmer3(Page<Object> of, String equipName, String startTime, String endTime);

    List<InspectionJobListResDTO> querydmer3(String equipName, String startTime, String endTime);

    /**
     * 、二级修(180天
     *
     * @param
     * @return
     */
    Page<InspectionJobListResDTO> queryER4(Page<Object> of, String equipName, String startTime, String endTime);

    /**
     * 二级修(360天
     */
    Page<InspectionJobListResDTO> queryER5(Page<Object> of, String equipName, String startTime, String endTime);

    List<InspectionJobListResDTO> queryER5(String equipName, String startTime, String endTime);

    /**
     * 故障列表
     *
     * @return
     */
    Page<FaultDetailResDTO> queryFMHistory(Page<Object> of, String equipName, String startTime, String endTime);

    List<FaultDetailResDTO> queryFMHistory(String equipName, String startTime, String endTime);

    /**
     * 分页查询一车一档故障跟踪列表
     * @param of 分页参数
     * @param equipName 设备名称
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 故障跟踪列表
     */
    Page<TrackQueryResDTO> queryFaultFollow(Page<Object> of, String equipName, String startTime, String endTime);

    /**
     * 查询一车一档故障跟踪列表
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param equipName 设备名称
     * @return 故障跟踪列表
     */
    List<TrackQueryResDTO> queryFaultFollow(String equipName, String startTime, String endTime);

    /**
     * 部件更换列表
     *
     * @return
     */
    Page<PartReplaceResDTO> querydmdm20(Page<Object> of, String equipName, String startTime, String endTime);

    List<PartReplaceResDTO> querydmdm20(String equipName, String startTime, String endTime);

    /**
     * 2机修 30 天
     */
    Page<InspectionJobListResDTO> queryER2(Page<Object> of, String equipName, String startTime, String endTime);


    Page<InspectionJobListResDTO> queryER1(Page<Object> of, String equipName, String startTime, String endTime);
    List<InspectionJobListResDTO> queryER1( String equipName, String startTime, String endTime);

    List<InspectionJobListResDTO> queryER4(String equipName, String startTime, String endTime);

    Page<GearboxChangeOilResDTO> listGearboxChangeOil(Page<GearboxChangeOilResDTO> page, OneCarOneGearQueryReqDTO req);

    List<GearboxChangeOilResDTO> listGearboxChangeOil(OneCarOneGearQueryReqDTO req);

    Page<WheelsetLathingResDTO> listWheelsetLathing(Page<WheelsetLathingResDTO> page, OneCarOneGearQueryReqDTO req);

    List<WheelsetLathingResDTO> listWheelsetLathing(OneCarOneGearQueryReqDTO req);

    /**
     * 一车一档-普查技改分页查询
     * @param page 分页信息
     * @param req 查询条件
     * @return 普查技改分页信息
     */
    Page<GeneralSurveyResDTO> listGeneralSurvey(Page<GeneralSurveyResDTO> page, OneCarOneGearQueryReqDTO req);

    /**
     * 获取普查技改列表
     * @param req 查询条件
     * @return 普查技改列表
     */
    List<GeneralSurveyResDTO> listGeneralSurvey(OneCarOneGearQueryReqDTO req);
}
