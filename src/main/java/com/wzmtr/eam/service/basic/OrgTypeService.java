package com.wzmtr.eam.service.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.basic.OrgTypeReqDTO;
import com.wzmtr.eam.dto.res.basic.OrgTypeResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface OrgTypeService {

    Page<OrgTypeResDTO> listOrgType(String orgCode, String orgType, PageReqDTO pageReqDTO);

    OrgTypeResDTO getOrgTypeDetail(String id);

    void addOrgType(OrgTypeReqDTO orgTypeReqDTO);

    void modifyOrgType(OrgTypeReqDTO orgTypeReqDTO);

    void deleteOrgType(BaseIdsEntity baseIdsEntity);

    void exportOrgType(String orgCode, String orgType, HttpServletResponse response) throws IOException;
}
