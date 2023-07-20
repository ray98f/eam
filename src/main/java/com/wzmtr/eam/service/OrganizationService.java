package com.wzmtr.eam.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.entity.CompanyStructureTreeDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.dto.res.MemberResDTO;

import java.util.List;

public interface OrganizationService {

    List<CompanyStructureTreeDTO> listCompanyStructure();

    List<CompanyStructureTreeDTO> listCompanyList();

    Page<MemberResDTO> listMember(String id, String name, PageReqDTO pageReqDTO);

    List<MemberResDTO> listMembers(String id);
}
