package com.wzmtr.eam.mapper.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.res.MemberResDTO;
import com.wzmtr.eam.entity.CompanyStructureTreeDTO;
import com.wzmtr.eam.entity.SysOffice;
import com.wzmtr.eam.dto.res.basic.OrgParentResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OrganizationMapper {

    CompanyStructureTreeDTO getRoot();

    List<CompanyStructureTreeDTO> listExtraRootList(@Param("root") String root);

    List<CompanyStructureTreeDTO> listExtraBodyList(@Param("root") String root);

    List<CompanyStructureTreeDTO> listCompanyList();

    Page<MemberResDTO> pageMember(Page<MemberResDTO> page, @Param("id") String id, @Param("name") String name);

    String getIdByAreaId(@Param("id") String id);

    List<MemberResDTO> listMember(@Param("id") String id);

    String selectOfficeNameById(String id);

    String getOrgNameByOrgCode(String id);

    Integer cleanOrg();

    Integer createOrg(SysOffice office);

    Integer cleanSupplier();

    Integer cleanExtra();

    List<OrgParentResDTO> searchParent();

    Integer updateParent(OrgParentResDTO orgParentResDTO);

    List<String> downRecursion(@Param("id") String id);

    String selectCompanyIdByOfficeId(@Param("officeId") String officeId);

    String getExtraOrgByAreaId(@Param("areaId") String areaId);

    String getOrgById(@Param("id") String id);

    SysOffice getByNames(@Param("names") String names);

    String getNamesById(@Param("id") String id);

}
