package com.wzmtr.eam.mapper.statistic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.statistic.CarFaultQueryReqDTO;
import com.wzmtr.eam.dto.res.statistic.CarFaultQueryResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 11:03
 */
@Repository
@Mapper
public interface CarFaultMapper {

    Page<CarFaultQueryResDTO> query(Page<Object> of, CarFaultQueryReqDTO reqDTO);
}
