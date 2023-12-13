package com.wzmtr.eam.service.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.basic.RegionReqDTO;
import com.wzmtr.eam.dto.res.basic.RegionResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface RegionService {

    Page<RegionResDTO> listRegion(String name, String code, String parentId, PageReqDTO pageReqDTO);

    List<RegionResDTO> listRegionTree();

    RegionResDTO getRegionDetail(String id);

    void addRegion(RegionReqDTO regionReqDTO);

    void modifyRegion(RegionReqDTO regionReqDTO);

    void deleteRegion(BaseIdsEntity baseIdsEntity);

    void exportRegion(String name, String no, String parentId, HttpServletResponse response) throws IOException;
}
