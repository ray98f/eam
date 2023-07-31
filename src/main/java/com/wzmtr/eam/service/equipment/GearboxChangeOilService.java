package com.wzmtr.eam.service.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.GearboxChangeOilReqDTO;
import com.wzmtr.eam.dto.res.GearboxChangeOilResDTO;
import com.wzmtr.eam.dto.res.RegionResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface GearboxChangeOilService {

    Page<GearboxChangeOilResDTO> pageGearboxChangeOil(String trainNo, PageReqDTO pageReqDTO);

    GearboxChangeOilResDTO getGearboxChangeOilDetail(String id);

    void addGearboxChangeOil(GearboxChangeOilReqDTO gearboxChangeOilReqDTO);

    void deleteGearboxChangeOil(BaseIdsEntity baseIdsEntity);

    void importGearboxChangeOil(MultipartFile file);

    void exportGearboxChangeOil(String trainNo, HttpServletResponse response);

}
