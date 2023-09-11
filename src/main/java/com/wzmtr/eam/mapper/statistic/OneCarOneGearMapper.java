package com.wzmtr.eam.mapper.statistic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.res.equipment.PartReplaceResDTO;
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

    OneCarOneGearResDTO querySummary(String endTime, String startTime, String equipName);

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
     * 故障跟踪记录
     *
     * @return
     */
    Page<TrackQueryResDTO> queryDMFM21(Page<Object> of, String equipName, String startTime, String endTime);

    List<TrackQueryResDTO> queryDMFM21(String equipName, String startTime, String endTime);

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
}
