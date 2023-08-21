package com.wzmtr.eam.mapper.statistic;

import com.wzmtr.eam.dto.req.statistic.OneCarOneGearQueryReqDTO;
import com.wzmtr.eam.dto.res.statistic.InspectionJobListResDTO;
import com.wzmtr.eam.dto.res.statistic.OneCarOneGearResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 11:03
 */
@Repository
@Mapper
public interface OneCarOneGearMapper {

    OneCarOneGearResDTO query(String endTime, String startTime, String equipName);

    OneCarOneGearResDTO querySummary(String endTime, String startTime, String equipName);

    InspectionJobListResDTO querydmer3(OneCarOneGearQueryReqDTO reqDTO);
}
