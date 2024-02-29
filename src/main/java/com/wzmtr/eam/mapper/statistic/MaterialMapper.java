package com.wzmtr.eam.mapper.statistic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.statistic.MaterialListReqDTO;
import com.wzmtr.eam.dto.res.statistic.MaterialResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 11:03
 */
@Repository
@Mapper
public interface MaterialMapper {

    Page<MaterialResDTO> query(Page<Object> of, List<String> planName, String matName, String startTime, String endTime, String trainNo);

    List<MaterialResDTO> exportList(MaterialListReqDTO reqDTO);
}
