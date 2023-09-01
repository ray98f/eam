package com.wzmtr.eam.mapper.statistic;

import com.wzmtr.eam.dto.res.statistic.CarFaultQueryResDTO;
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
public interface CarFaultMapper {

    List<CarFaultQueryResDTO> query(Set<String> objectCode, String endTime, String startTime);
}
