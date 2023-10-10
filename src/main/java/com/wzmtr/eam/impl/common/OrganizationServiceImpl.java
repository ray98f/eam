package com.wzmtr.eam.impl.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.res.equipment.EquipmentResDTO;
import com.wzmtr.eam.entity.OrganMajorLineType;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.mapper.basic.OrgMajorMapper;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.entity.CompanyStructureTreeDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.mapper.equipment.EquipmentMapper;
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
    @Autowired
    private OrgMajorMapper orgMajorMapper;
    @Autowired
    private EquipmentMapper equipmentMapper;

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
    public Page<MemberResDTO> pageMember(String id, String name, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        Page<MemberResDTO> page;
        String newId = organizationMapper.getIdByAreaId(id);
        if (!Objects.isNull(newId)) {
            return organizationMapper.pageUserByOffice(pageReqDTO.of(), newId);
        }
        if ("root".equals(id)) {
            page = organizationMapper.pageMember(pageReqDTO.of(), id, name);
        } else {
            page = organizationMapper.pageMember(pageReqDTO.of(), "," + id, name);
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
    public List<MemberResDTO> listMember(String id) {
        List<MemberResDTO> list;
        String newId = organizationMapper.getIdByAreaId(id);
        if (!Objects.isNull(newId)) {
            return organizationMapper.listUserByOffice(newId);
        }
        if ("root".equals(id)) {
            list = organizationMapper.listMember(id);
        } else {
            list = organizationMapper.listMember("," + id);
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

    @Override
    public List<OrganMajorLineType> getWorkerGroupBySubjectAndLine(String equipName) {
        List<EquipmentResDTO> equipmentResDTOS = equipmentMapper.queryMajor(equipName);
        EquipmentResDTO equipmentResDTO = equipmentResDTOS.get(0);
        return orgMajorMapper.getWorkerGroupBySubjectAndLine(equipmentResDTO.getMajorCode(),equipmentResDTO.getUseLineNo(),"10");
    }

}
