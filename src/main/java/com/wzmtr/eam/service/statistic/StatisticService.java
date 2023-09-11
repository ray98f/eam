package com.wzmtr.eam.service.statistic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.statistic.*;
import com.wzmtr.eam.dto.res.equipment.GearboxChangeOilResDTO;
import com.wzmtr.eam.dto.res.equipment.GeneralSurveyResDTO;
import com.wzmtr.eam.dto.res.equipment.PartReplaceResDTO;
import com.wzmtr.eam.dto.res.equipment.WheelsetLathingResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.TrackQueryResDTO;
import com.wzmtr.eam.dto.res.statistic.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 10:48
 */
public interface StatisticService {

    FailureRateDetailResDTO query(FailreRateQueryReqDTO reqDTO);

    Page<MaterialResDTO> query(MaterialQueryReqDTO reqDTO);

    CarFaultQueryResDTO query(CarFaultQueryReqDTO reqDTO);

    ReliabilityListResDTO reliabilityQuery(FailreRateQueryReqDTO reqDTO);

    OneCarOneGearResDTO oneCarOneGearQuery(OneCarOneGearReqDTO reqDTO);

    Page<InspectionJobListResDTO> querydmer3(OneCarOneGearQueryReqDTO reqDTO);

    Page<InspectionJobListResDTO> queryER4(OneCarOneGearQueryReqDTO reqDTO);

    Page<InspectionJobListResDTO> queryER5(OneCarOneGearQueryReqDTO reqDTO);

    void queryER5Export(String startTime, String endTime, String equipName, HttpServletResponse response);

    /**
     * 故障列表
     *
     * @param reqDTO
     * @return
     */
    Page<FaultDetailResDTO> queryFMHistory(OneCarOneGearQueryReqDTO reqDTO);

    void queryFMHistoryExport(String startTime, String endTime, String equipName, HttpServletResponse response);

    /**
     * 故障跟踪
     * @param reqDTO
     * @return
     */

    Page<TrackQueryResDTO> queryDMFM21(OneCarOneGearQueryReqDTO reqDTO);

    /**
     * 部件跟换
     *
     * @param reqDTO
     * @return
     */

    Page<PartReplaceResDTO> querydmdm20(OneCarOneGearQueryReqDTO reqDTO);

    void querydmdm20Export(String equipName, String startTime, String endTime, HttpServletResponse response);

    /**
     * 齿轮箱换油
     *
     * @param reqDTO
     * @return
     */
    Page<GearboxChangeOilResDTO> pageGearboxChangeOil(OneCarOneGearQueryReqDTO reqDTO);

    Page<WheelsetLathingResDTO> pageWheelsetLathing(OneCarOneGearQueryReqDTO reqDTO);

    Page<GeneralSurveyResDTO> pageGeneralSurvey(OneCarOneGearQueryReqDTO reqDTO);

    void pageGeneralSurveyExport(String equipName, HttpServletResponse response);

    Page<InspectionJobListResDTO> queryER2(OneCarOneGearQueryReqDTO reqDTO);

    Page<InspectionJobListResDTO> queryER1(OneCarOneGearQueryReqDTO reqDTO);

    RAMSCarResDTO query4AQYYZB();

    List<SystemFaultsResDTO> queryresult3(String startDate, String endDate, String sys);

    List<RAMSResult2ResDTO> queryresult2(String startDate, String endDate);

    List<RAMSSysPerformResDTO> querySysPerform();

    List<FaultConditionResDTO> queryCountFaultType();

    Page<FaultRAMSResDTO> queryRAMSFaultList(RAMSTimeReqDTO reqDTO);

    void materialExport(MaterialListReqDTO reqDTO, HttpServletResponse response);

    void queryDMFM21Export(String startTime, String endTime, String equipName, HttpServletResponse response);

    void pageGearboxChangeOilExport(String equipName, HttpServletResponse response);

    void querydmer3Export(String startTime, String endTime, String equipName, HttpServletResponse response);

    void queryER4Export(String startTime, String endTime, String equipName, HttpServletResponse response);

    void queryER1Export(String startTime, String endTime, String equipName, HttpServletResponse response);

    void pageWheelsetLathingExport(String startTime, String endTime, String equipName, HttpServletResponse response);
}
