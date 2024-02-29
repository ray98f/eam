package com.wzmtr.eam.service.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.entity.CompanyStructureTree;
import com.wzmtr.eam.entity.OrganMajorLineType;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.dto.res.common.MemberResDTO;

import java.util.List;

public interface OrganizationService {

    List<CompanyStructureTree> listCompanyStructure();

    List<CompanyStructureTree> listCompanyList();

    Page<MemberResDTO> pageMember(String id, String name, PageReqDTO pageReqDTO);

    List<MemberResDTO> listMember(String id);

    List<OrganMajorLineType> getWorkerGroupBySubjectAndLine(String equipName);

    /**
     * 获取中铁通组织层级结构
     * @return 中铁通组织层级结构
     */
    List<CompanyStructureTree> listZttCompanyStructure();
}
