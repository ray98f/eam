package com.wzmtr.eam.mapper.common;

import com.wzmtr.eam.bizobject.StationBO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/9/21 14:05
 */
@Mapper
@Repository
public interface StationMapper {
    List<StationBO> queryStation(String userId, String stationCode);
}
