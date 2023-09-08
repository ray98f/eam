package com.wzmtr.eam.mapper.equipment;

import com.wzmtr.eam.dto.req.equipment.PartFaultReqDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author frp
 */
@Mapper
@Repository
public interface PartFaultMapper {

    void insertPartFault(PartFaultReqDTO partFaultReqDTO);
}
