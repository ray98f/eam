package com.wzmtr.eam.impl.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.res.common.MemberResDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentResDTO;
import com.wzmtr.eam.entity.CompanyStructureTree;
import com.wzmtr.eam.entity.OrganMajorLineType;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.basic.OrgMajorMapper;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.equipment.EquipmentMapper;
import com.wzmtr.eam.service.common.OrganizationService;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.tree.CompanyTreeUtils;
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
    public List<CompanyStructureTree> listCompanyStructure() {
        CompanyStructureTree companyStructureTree = organizationMapper.getRoot();
        if (Objects.isNull(companyStructureTree)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        List<CompanyStructureTree> extraRootList = organizationMapper.listExtraRootList(companyStructureTree.getId());
        List<CompanyStructureTree> extraBodyList = organizationMapper.listExtraBodyList(companyStructureTree.getId());
        CompanyTreeUtils extraTree = new CompanyTreeUtils(extraRootList, extraBodyList);
        companyStructureTree.setChildren(extraTree.getTree());
        List<CompanyStructureTree> list = new ArrayList<>();
        list.add(companyStructureTree);
        return list;
    }

    @Override
    public List<CompanyStructureTree> listCompanyList() {
        return organizationMapper.listCompanyList();
    }

    @Override
    public Page<MemberResDTO> pageMember(String id, String name, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        Page<MemberResDTO> page;
        String newId = organizationMapper.getIdByAreaId(id);
        if (!Objects.isNull(newId)) {
            return organizationMapper.pageUserByOffice(pageReqDTO.of(), newId);
        }
        if (CommonConstants.ROOT.equals(id)) {
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
                        if (StringUtils.isNotEmpty(officeName)) {
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
        if (StringUtils.isNotEmpty(newId)) {
            return organizationMapper.listUserByOffice(newId);
        }
        if (CommonConstants.ROOT.equals(id)) {
            list = organizationMapper.listMember(id);
        } else {
            list = organizationMapper.listMember("," + id);
        }
        if (null != list && !list.isEmpty()) {
            for (MemberResDTO memberResDTO : list) {
                if (CommonConstants.ROOT.equals(memberResDTO.getParentId())) {
                    continue;
                }
                memberResDTO.setOrgPath(organizationMapper.selectOfficeNameById(memberResDTO.getParentId()));
            }
        }
        return list;
    }

    @Override
    public List<OrganMajorLineType> getWorkerGroupBySubjectAndLine(String equipName) {
        List<EquipmentResDTO> equipmentRes = equipmentMapper.queryMajor(equipName);
        if (StringUtils.isNotEmpty(equipmentRes)) {
            EquipmentResDTO equipmentResDTO = equipmentRes.get(0);
            return orgMajorMapper.getWorkerGroupBySubjectAndLine(equipmentResDTO.getMajorCode(),
                    equipmentResDTO.getUseLineNo(),"10");
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<CompanyStructureTree> listZttCompanyStructure() {
        CompanyStructureTree companyStructureTree = organizationMapper.getZttRoot();
        if (Objects.isNull(companyStructureTree)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        List<CompanyStructureTree> extraRootList = new ArrayList<>();
        extraRootList.add(companyStructureTree);
        List<String> ids = organizationMapper.downRecursionId(companyStructureTree.getId());
        ids.remove(companyStructureTree.getId());
        List<CompanyStructureTree> extraBodyList = organizationMapper.listExternalUnitsBodyList(ids);
        CompanyTreeUtils extraTree = new CompanyTreeUtils(extraRootList, extraBodyList);
        return extraTree.getTree();
    }

    @Override
    public List<CompanyStructureTree> listDispatchCompanyStructure(String majorCode) {
        if (!CommonConstants.ZC_LIST.contains(majorCode)) {
            return listZttCompanyStructure();
        } else {
            CompanyStructureTree companyStructureTree = organizationMapper.getZcRoot();
            if (Objects.isNull(companyStructureTree)) {
                throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
            }
            List<CompanyStructureTree> extraRootList = new ArrayList<>();
            extraRootList.add(companyStructureTree);
            List<String> ids = organizationMapper.downRecursionId(companyStructureTree.getId());
            ids.remove(companyStructureTree.getId());
            List<CompanyStructureTree> extraBodyList = organizationMapper.listExternalUnitsBodyList(ids);
            CompanyTreeUtils extraTree = new CompanyTreeUtils(extraRootList, extraBodyList);
            return extraTree.getTree();
        }
    }

}
