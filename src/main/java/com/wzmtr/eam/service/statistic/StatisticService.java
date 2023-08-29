package com.wzmtr.eam.service.statistic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.statistic.*;
import com.wzmtr.eam.dto.res.GearboxChangeOilResDTO;
import com.wzmtr.eam.dto.res.GeneralSurveyResDTO;
import com.wzmtr.eam.dto.res.PartReplaceResDTO;
import com.wzmtr.eam.dto.res.WheelsetLathingResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.TrackQueryResDTO;
import com.wzmtr.eam.dto.res.statistic.*;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 10:48
 */
public interface StatisticService {

    List<FailureRateResDTO> query(FailreRateQueryReqDTO reqDTO);

    Page<MaterialResDTO> query(MaterialQueryReqDTO reqDTO);

    CarFaultQueryResDTO query(CarFaultQueryReqDTO reqDTO);

    ReliabilityListResDTO reliabilityQuery(FailreRateQueryReqDTO reqDTO);

    OneCarOneGearResDTO oneCarOneGearQuery(OneCarOneGearQueryReqDTO reqDTO);

    Page<InspectionJobListResDTO> querydmer3(OneCarOneGearQueryReqDTO reqDTO);

    Page<InspectionJobListResDTO> queryER4(OneCarOneGearQueryReqDTO reqDTO);

    Page<InspectionJobListResDTO> queryER5(OneCarOneGearQueryReqDTO reqDTO);

    /**
     * 故障列表
     * @param reqDTO
     * @return
     */
    Page<FaultDetailResDTO> queryFMHistory(OneCarOneGearQueryReqDTO reqDTO);

    /**
     * 故障跟踪
     * @param reqDTO
     * @return
     */

    Page<TrackQueryResDTO> queryDMFM21(OneCarOneGearQueryReqDTO reqDTO);

    /**
     * 部件跟换
     * @param reqDTO
     * @return
     */

    Page<PartReplaceResDTO> querydmdm20(OneCarOneGearQueryReqDTO reqDTO);

    /**
     * 齿轮箱换油
     * @param reqDTO
     * @return
     */
    Page<GearboxChangeOilResDTO> pageGearboxChangeOil(OneCarOneGearQueryReqDTO reqDTO);

    Page<WheelsetLathingResDTO> pageWheelsetLathing(OneCarOneGearQueryReqDTO reqDTO);

    Page<GeneralSurveyResDTO> pageGeneralSurvey(OneCarOneGearQueryReqDTO reqDTO);

    Page<InspectionJobListResDTO> queryER2(OneCarOneGearQueryReqDTO reqDTO);

    Page<InspectionJobListResDTO> queryER1(OneCarOneGearQueryReqDTO reqDTO);

    RAMSResDTO query4AQYYZB(RAMSTimeReqDTO reqDTO);

    List<SystemFaultsResDTO> queryresult3(String startDate, String endDate,String sys);

    List<RAMSResDTO> queryresult2(String startDate, String endDate);

    List<RAMSResDTO> querySysPerform();
}
