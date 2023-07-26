package com.wzmtr.eam.mapper.equipment;

import com.wzmtr.eam.dto.req.EquipmentPartReqDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author frp
 */
@Mapper
@Repository
public interface EquipmentPartMapper {

    String getMaxPartCode();

    void insertEquipmentPart(EquipmentPartReqDTO equipmentPartReqDTO);
}
