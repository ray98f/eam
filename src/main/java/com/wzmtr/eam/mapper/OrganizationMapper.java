package com.wzmtr.eam.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.BindReqDTO;
import com.wzmtr.eam.dto.res.MemberResDTO;
import com.wzmtr.eam.dto.res.PersonListResDTO;
import com.wzmtr.eam.entity.CompanyStructureTreeDTO;
import com.wzmtr.eam.entity.SysOffice;
import com.wzmtr.eam.dto.res.OrgParentIdsResDTO;
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

    Page<MemberResDTO> listMember(Page<MemberResDTO> page, @Param("id") String id, @Param("name") String name);

    List<MemberResDTO> listMembers(@Param("id") String id);

    String selectOfficeNameById(String id);

    Integer cleanOrg();

    Integer createOrg(SysOffice office);

    Integer cleanSupplier();

    List<OrgParentIdsResDTO> searchParentIds();

    Integer updateParentIds(OrgParentIdsResDTO orgParentIdsResDTO);

}
