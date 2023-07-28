package com.wzmtr.eam.mapper.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.EquipmentRoomReqDTO;
import com.wzmtr.eam.dto.res.EquipmentRoomResDTO;
import com.wzmtr.eam.dto.res.PillarResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface PillarMapper {


    Page<PillarResDTO> pagePillar(Page<PillarResDTO> page, String pillarNumber, String powerSupplySection);
}
