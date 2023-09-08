package com.wzmtr.eam.mapper.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.equipment.GearboxChangeOilReqDTO;
import com.wzmtr.eam.dto.res.equipment.GearboxChangeOilResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface GearboxChangeOilMapper {

    Page<GearboxChangeOilResDTO> pageGearboxChangeOil(Page<GearboxChangeOilResDTO> page, String trainNo);

    GearboxChangeOilResDTO getGearboxChangeOilDetail(String id);

    void addGearboxChangeOil(GearboxChangeOilReqDTO gearboxChangeOilReqDTO);

    void deleteGearboxChangeOil(List<String> ids, String userId, String time);

    void importGearboxChangeOil(List<GearboxChangeOilReqDTO> list);

    List<GearboxChangeOilResDTO> listGearboxChangeOil(String trainNo);

}
