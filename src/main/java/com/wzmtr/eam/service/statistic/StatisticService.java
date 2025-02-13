package com.wzmtr.eam.service.statistic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.fault.FaultQueryDetailReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultQueryReqDTO;
import com.wzmtr.eam.dto.req.statistic.*;
import com.wzmtr.eam.dto.res.equipment.GearboxChangeOilResDTO;
import com.wzmtr.eam.dto.res.equipment.GeneralSurveyResDTO;
import com.wzmtr.eam.dto.res.equipment.PartReplaceResDTO;
import com.wzmtr.eam.dto.res.equipment.WheelsetLathingResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.TrackQueryResDTO;
import com.wzmtr.eam.dto.res.statistic.*;
import com.wzmtr.eam.entity.PageReqDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 10:48
 */
public interface StatisticService {

    /**
     * 新增站台门故障数据
     * @param req req
     */
    void addDoorFault(DoorFaultReqDTO req);

    /**
     * 编辑站台门故障数据
     * @param req req
     */
    void modifyDoorFault(DoorFaultReqDTO req);

    FailureRateDetailResDTO failureRateQuery(FailreRateQueryReqDTO reqDTO);

    Page<MaterialResDTO> query(MaterialQueryReqDTO reqDTO);

    CarFaultQueryResDTO query(CarFaultQueryReqDTO reqDTO);

    ReliabilityListResDTO reliabilityQuery(FailreRateQueryReqDTO reqDTO);

    OneCarOneGearResDTO oneCarOneGearQuery(OneCarOneGearReqDTO reqDTO);

    Page<InspectionJobListResDTO> querydmer3(OneCarOneGearQueryReqDTO reqDTO);

    Page<InspectionJobListResDTO> queryER4(OneCarOneGearQueryReqDTO reqDTO);

    Page<InspectionJobListResDTO> queryER5(OneCarOneGearQueryReqDTO reqDTO);

    void queryER5Export(String startTime, String endTime, String equipName, HttpServletResponse response) throws IOException;

    /**
     * 故障列表
     *
     * @param reqDTO
     * @return
     */
    Page<FaultDetailResDTO> queryFMHistory(OneCarOneGearQueryReqDTO reqDTO);

    void queryFMHistoryExport(String startTime, String endTime, String equipName, HttpServletResponse response) throws IOException;

    /**
     * 分页查询一车一档故障跟踪列表
     * @param reqDTO 传参
     * @return 故障跟踪列表
     */

    Page<TrackQueryResDTO> queryFaultFollow(OneCarOneGearQueryReqDTO reqDTO);

    /**
     * 部件跟换
     *
     * @param reqDTO
     * @return
     */

    Page<PartReplaceResDTO> querydmdm20(OneCarOneGearQueryReqDTO reqDTO);

    void querydmdm20Export(String equipName, String startTime, String endTime, HttpServletResponse response) throws IOException;

    /**
     * 齿轮箱换油
     *
     * @param reqDTO
     * @return
     */
    Page<GearboxChangeOilResDTO> pageGearboxChangeOil(OneCarOneGearQueryReqDTO reqDTO);

    void pageGearboxChangeOilExport(OneCarOneGearQueryReqDTO reqDTO, HttpServletResponse response) throws IOException;

    Page<WheelsetLathingResDTO> pageWheelsetLathing(OneCarOneGearQueryReqDTO reqDTO);

    void pageWheelsetLathingExport(OneCarOneGearQueryReqDTO reqDTO, HttpServletResponse response) throws IOException;

    Page<GeneralSurveyResDTO> pageGeneralSurvey(OneCarOneGearQueryReqDTO reqDTO);

    void pageGeneralSurveyExport(OneCarOneGearQueryReqDTO reqDTO, HttpServletResponse response) throws IOException;

    Page<InspectionJobListResDTO> queryER2(OneCarOneGearQueryReqDTO reqDTO);

    Page<InspectionJobListResDTO> queryER1(OneCarOneGearQueryReqDTO reqDTO);

    RamsCarResDTO query4AQYYZB();

    List<SystemFaultsResDTO> queryresult3(String startDate, String endDate, String sys);

    List<RamsResult2ResDTO> queryresult2(String startDate, String endDate);

    List<RamsSysPerformResDTO> querySysPerform();

    /**
     * 各系统可靠性统计-导出
     * @param response response
     */
    void exportSysPerform(HttpServletResponse response) throws IOException;

    List<FaultConditionResDTO> queryCountFaultType();

    Page<FaultRamsResDTO> queryRamsFaultList(RamsTimeReqDTO reqDTO);

    void materialExport(MaterialListReqDTO reqDTO, HttpServletResponse response) throws IOException;

    /**
     * 一车一档故障跟踪列表导出
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param equipName 设备名称
     * @param response response
     * @throws IOException 异常
     */
    void queryFaultFollowExport(String startTime, String endTime, String equipName, HttpServletResponse response) throws IOException;

    void querydmer3Export(String startTime, String endTime, String equipName, HttpServletResponse response) throws IOException;

    void queryER4Export(String startTime, String endTime, String equipName, HttpServletResponse response) throws IOException;

    void queryER1Export(String startTime, String endTime, String equipName, HttpServletResponse response) throws IOException;

    /**
     * 故障统计报表列表导出
     * @param reqDTO 入参
     * @param response response
     * @throws IOException 流异常
     */
    void faultListExport(FaultQueryReqDTO reqDTO, HttpServletResponse response) throws IOException;

    /**
     * 故障统计报表导出
     * @param reqDTO 入参
     * @param response response
     */
    void faultExport(FaultQueryDetailReqDTO reqDTO,HttpServletResponse response);

    /**
     * RAMS-列车可靠性统计
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param trainNo 列车号
     * @return 列车可靠性统计
     */
    RamsTrainReliabilityResDTO trainReliability(String startTime, String endTime, String trainNo);

    /**
     * RAMS-列车可靠性统计-故障列表
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param trainNo 列车号
     * @param pageReqDTO 分页参数
     * @return 故障列表
     */
    Page<FaultRamsResDTO> trainReliabilityFaultList(String startTime, String endTime, String trainNo, PageReqDTO pageReqDTO);

    /**
     * 各系统指定时间范围内故障数量统计
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 故障数量统计
     */
    List<SubjectFaultResDTO> getSubjectFaultOpen(String startTime, String endTime);
}
