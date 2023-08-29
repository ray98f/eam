package com.wzmtr.eam.mapper.statistic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.res.PartReplaceResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.TrackQueryResDTO;
import com.wzmtr.eam.dto.res.statistic.InspectionJobListResDTO;
import com.wzmtr.eam.dto.res.statistic.OneCarOneGearResDTO;
import com.wzmtr.eam.dto.res.statistic.RAMSResDTO;
import com.wzmtr.eam.dto.res.statistic.SystemFaultsResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 11:03
 */
@Repository
@Mapper
public interface RAMSMapper {
    List<RAMSResDTO> query4AQYYZB(Page<Object> of, String startTime, String endTime);

    List<SystemFaultsResDTO> queryFautTypeByMonthBySys(Set<String> moduleIds, String startDate, String endDate);

    List<RAMSResDTO> queryresult2(String startDate, String endDate);
    List<RAMSResDTO> querySysPerform();
    List<RAMSResDTO> querytotalMiles();
}
