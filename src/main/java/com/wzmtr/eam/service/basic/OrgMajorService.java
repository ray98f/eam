package com.wzmtr.eam.service.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.basic.OrgMajorReqDTO;
import com.wzmtr.eam.dto.res.basic.FaultRespAndRepairDeptResDTO;
import com.wzmtr.eam.dto.res.basic.OrgMajorResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface OrgMajorService {

    Page<OrgMajorResDTO> listOrgMajor(String orgCode, String majorCode, PageReqDTO pageReqDTO);

    List<OrgMajorResDTO> listUseOrgMajor(String majorCode);

    OrgMajorResDTO getOrgMajorDetail(String id);

    void addOrgMajor(OrgMajorReqDTO orgMajorReqDTO);

    void modifyOrgMajor(OrgMajorReqDTO orgMajorReqDTO);

    void deleteOrgMajor(BaseIdsEntity baseIdsEntity);

    void exportOrgMajor(List<String> ids, HttpServletResponse response) throws IOException;

    FaultRespAndRepairDeptResDTO queryTypeAndDeptCode(String lineCode, String majorCode);
}
