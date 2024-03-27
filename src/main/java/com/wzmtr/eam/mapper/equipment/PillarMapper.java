package com.wzmtr.eam.mapper.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.equipment.PillarReqDTO;
import com.wzmtr.eam.dto.res.equipment.PillarResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author frp
 */
@Mapper
@Repository
public interface PillarMapper {


    Page<PillarResDTO> pagePillar(Page<PillarResDTO> page, String pillarNumber, String powerSupplySection);

    void add(PillarReqDTO pillarReqDTO);
}
