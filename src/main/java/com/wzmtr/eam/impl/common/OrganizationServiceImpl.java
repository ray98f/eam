package com.wzmtr.eam.impl.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.entity.CompanyStructureTreeDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.utils.tree.CompanyTreeUtils;
import com.wzmtr.eam.dto.res.MemberResDTO;
import com.wzmtr.eam.service.common.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public List<CompanyStructureTreeDTO> listCompanyStructure() {
        CompanyStructureTreeDTO companyStructureTreeDTO = organizationMapper.getRoot();
        if (Objects.isNull(companyStructureTreeDTO)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        List<CompanyStructureTreeDTO> extraRootList = organizationMapper.listExtraRootList(companyStructureTreeDTO.getId());
        List<CompanyStructureTreeDTO> extraBodyList = organizationMapper.listExtraBodyList(companyStructureTreeDTO.getId());
        CompanyTreeUtils extraTree = new CompanyTreeUtils(extraRootList, extraBodyList);
        companyStructureTreeDTO.setChildren(extraTree.getTree());
        List<CompanyStructureTreeDTO> list = new ArrayList<>();
        list.add(companyStructureTreeDTO);
        return list;
    }

    @Override
    public List<CompanyStructureTreeDTO> listCompanyList() {
        return organizationMapper.listCompanyList();
    }

    @Override
    public Page<MemberResDTO> listMember(String id, String name, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        Page<MemberResDTO> page;
        if ("root".equals(id)) {
            page = organizationMapper.listMember(pageReqDTO.of(), id, name);
        } else {
            page = organizationMapper.listMember(pageReqDTO.of(), "," + id, name);
        }
        List<MemberResDTO> list = page.getRecords();
        if (null != list && !list.isEmpty()) {
            for (MemberResDTO memberResDTO : list) {
                StringBuilder names = new StringBuilder();
                List<String> ids = Arrays.asList(memberResDTO.getParentIds().split(","));
                if (!ids.isEmpty()) {
                    for (String officeId : ids) {
                        if ("root".equals(officeId)) {
                            continue;
                        }
                        String officeName = organizationMapper.selectOfficeNameById(officeId);
                        if (officeName != null && !"".equals(officeName)) {
                            names.append(officeName).append("-");
                        }
                    }
                }
                names.deleteCharAt(names.length() - 1);
                memberResDTO.setOrgPath(names.toString());
            }
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public List<MemberResDTO> listMembers(String id) {
        List<MemberResDTO> list;
        if ("root".equals(id)) {
            list = organizationMapper.listMembers(id);
        } else {
            list = organizationMapper.listMembers("," + id);
        }
        if (null != list && !list.isEmpty()) {
            for (MemberResDTO memberResDTO : list) {
                if ("root".equals(memberResDTO.getParentId())) {
                    continue;
                }
                memberResDTO.setOrgPath(organizationMapper.selectOfficeNameById(memberResDTO.getParentId()));
            }
        }
        return list;
    }

}
