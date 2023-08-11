package com.wzmtr.eam.mapper.overhaul;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.OverhaulOrderListReqDTO;
import com.wzmtr.eam.dto.req.OverhaulOrderReqDTO;
import com.wzmtr.eam.dto.req.OverhaulWorkRecordReqDTO;
import com.wzmtr.eam.dto.res.OverhaulOrderResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface OverhaulWorkRecordMapper {

    void deleteByOrderCode(OverhaulOrderReqDTO overhaulOrderReqDTO);

    void insert(OverhaulWorkRecordReqDTO overhaulWorkRecordReqDTO);

}
