package com.wzmtr.eam.mapper.overhaul;

import com.wzmtr.eam.dto.req.overhaul.OverhaulOrderReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulWorkRecordReqDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author frp
 */
@Mapper
@Repository
public interface OverhaulWorkRecordMapper {

    void deleteByOrderCode(OverhaulOrderReqDTO overhaulOrderReqDTO);

    void insert(OverhaulWorkRecordReqDTO overhaulWorkRecordReqDTO);

}
