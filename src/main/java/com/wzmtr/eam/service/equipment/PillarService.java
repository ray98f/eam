package com.wzmtr.eam.service.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.res.equipment.PillarResDTO;
import com.wzmtr.eam.entity.PageReqDTO;;

public interface PillarService {
    
    Page<PillarResDTO> pagePillar(String pillarNumber, String powerSupplySection, PageReqDTO pageReqDTO);

}
