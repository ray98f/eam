package com.wzmtr.eam.service.impl.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.res.PillarResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.mapper.equipment.PillarMapper;
import com.wzmtr.eam.service.equipment.PillarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author frp
 */
@Service
@Slf4j
public class PillarServiceImpl implements PillarService {

    @Autowired
    private PillarMapper pillarMapper;

    @Override
    public Page<PillarResDTO> pagePillar(String pillarNumber, String powerSupplySection, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return pillarMapper.pagePillar(pageReqDTO.of(), pillarNumber, powerSupplySection);
    }
}
