package com.wzmtr.eam.mapper.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.fault.FaultQueryReqDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.entity.SidEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Author: Li.Wang
 * Date: 2023/8/17 16:53
 */
@Mapper
@Repository
public interface FaultQueryMapper {
    Page<FaultDetailResDTO> list(Page<Object> of, FaultQueryReqDTO reqDTO);

    String queryOrderStatus(SidEntity reqDTO);
}
