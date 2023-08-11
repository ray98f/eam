package com.wzmtr.eam.mapper.overhaul;

import com.wzmtr.eam.dto.req.OverhaulItemReqDTO;
import com.wzmtr.eam.dto.req.OverhaulOrderReqDTO;
import com.wzmtr.eam.dto.req.OverhaulWorkRecordReqDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author frp
 */
@Mapper
@Repository
public interface OverhaulItemMapper {

    void insert(OverhaulItemReqDTO overhaulItemReqDTO);

}
