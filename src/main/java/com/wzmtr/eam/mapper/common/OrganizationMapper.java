package com.wzmtr.eam.mapper.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.res.basic.OrgParentResDTO;
import com.wzmtr.eam.dto.res.common.MemberResDTO;
import com.wzmtr.eam.entity.CompanyStructureTree;
import com.wzmtr.eam.entity.SysOffice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OrganizationMapper {

    CompanyStructureTree getRoot();

    List<CompanyStructureTree> listExtraRootList(@Param("root") String root);

    List<CompanyStructureTree> listExtraBodyList(@Param("root") String root);

    List<CompanyStructureTree> listCompanyList();

    Page<MemberResDTO> pageMember(Page<MemberResDTO> page, @Param("id") String id, @Param("name") String name);

    String getIdByAreaId(@Param("id") String id);

    List<MemberResDTO> listMember(@Param("id") String id);

    Page<MemberResDTO> pageUserByOffice(Page<MemberResDTO> page, @Param("id") String id);

    List<MemberResDTO> listUserByOffice(@Param("id") String id);

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
