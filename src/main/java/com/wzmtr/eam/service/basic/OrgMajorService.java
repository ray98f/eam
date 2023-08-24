package com.wzmtr.eam.service.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.OrgMajorReqDTO;
import com.wzmtr.eam.dto.res.FaultRespAndRepairDeptResDTO;
import com.wzmtr.eam.dto.res.OrgMajorResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;

import javax.servlet.http.HttpServletResponse;

public interface OrgMajorService {

    Page<OrgMajorResDTO> listOrgMajor(String orgCode, String majorCode, PageReqDTO pageReqDTO);

    OrgMajorResDTO getOrgMajorDetail(String id);

    void addOrgMajor(OrgMajorReqDTO orgMajorReqDTO);

    void modifyOrgMajor(OrgMajorReqDTO orgMajorReqDTO);

    void deleteOrgMajor(BaseIdsEntity baseIdsEntity);

    void exportOrgMajor(String orgCode, String majorCode, HttpServletResponse response);

    FaultRespAndRepairDeptResDTO queryTypeAndDeptCode(String lineCode);
}
