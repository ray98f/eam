package com.wzmtr.eam.service.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.basic.OrgLineReqDTO;
import com.wzmtr.eam.dto.res.basic.OrgLineResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;

import javax.servlet.http.HttpServletResponse;

public interface OrgLineService {

    Page<OrgLineResDTO> listOrgLine(String orgCode, String lineCode, PageReqDTO pageReqDTO);

    OrgLineResDTO getOrgLineDetail(String id);

    void addOrgLine(OrgLineReqDTO orgLineReqDTO);

    void modifyOrgLine(OrgLineReqDTO orgLineReqDTO);

    void deleteOrgLine(BaseIdsEntity baseIdsEntity);

    void exportOrgLine(String orgCode, String lineCode, HttpServletResponse response);
}
