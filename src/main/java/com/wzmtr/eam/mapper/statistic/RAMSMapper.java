package com.wzmtr.eam.mapper.statistic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.res.statistic.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
    List<RAMSCarResDTO> query4AQYYZB();

    List<SystemFaultsResDTO> queryFautTypeByMonthBySys(Set<String> moduleIds, String startDate, String endDate);

    List<RAMSResult2ResDTO> queryresult2(String startDate, String endDate);

    List<FaultConditionResDTO> queryCountFautType4RAMS();

    List<RAMSSysPerformResDTO> querySysPerform();

    List<RAMSResDTO> querytotalMiles();

    Page<FaultRAMSResDTO> queryRAMSFaultList(Page<Object> of, String startDate, String endDate);

    /**
     * 故障列表更换部件查询
     * @return
     */
    FaultRAMSResDTO queryPart(String faultWorkNo);
}
