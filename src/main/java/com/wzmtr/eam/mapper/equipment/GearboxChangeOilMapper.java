package com.wzmtr.eam.mapper.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.GearboxChangeOilReqDTO;
import com.wzmtr.eam.dto.res.GearboxChangeOilResDTO;
import com.wzmtr.eam.dto.res.RegionResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface GearboxChangeOilMapper {

    Page<GearboxChangeOilResDTO> pageGearboxChangeOil(Page<GearboxChangeOilResDTO> page, String equipName);

    GearboxChangeOilResDTO getGearboxChangeOilDetail(String id);

    void addGearboxChangeOil(GearboxChangeOilReqDTO gearboxChangeOilReqDTO);

    void deleteGearboxChangeOil(List<String> ids, String userId, String time);

    List<GearboxChangeOilResDTO> listGearboxChangeOil(String equipName);

}
