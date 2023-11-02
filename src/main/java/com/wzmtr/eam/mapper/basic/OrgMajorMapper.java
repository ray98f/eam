package com.wzmtr.eam.mapper.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.basic.OrgMajorReqDTO;
import com.wzmtr.eam.dto.res.basic.OrgMajorResDTO;
import com.wzmtr.eam.entity.OrganMajorLineType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface OrgMajorMapper {

    Page<OrgMajorResDTO> pageOrgMajor(Page<OrgMajorResDTO> page, List<List<String>> orgCodes, String majorCode);

    List<OrgMajorResDTO> listUseOrgMajor(String majorCode);

    OrgMajorResDTO getOrgMajorDetail(String id);

    Integer selectOrgMajorIsExist(OrgMajorReqDTO orgMajorReqDTO);

    void addOrgMajor(OrgMajorReqDTO orgMajorReqDTO);

    void modifyOrgMajor(OrgMajorReqDTO orgMajorReqDTO);

    void deleteOrgMajor(List<String> ids, String userId, String time);

    List<OrgMajorResDTO> listOrgMajor(List<List<String>> orgCodes, String majorCode);

    List<OrgMajorResDTO> queryTypeAndDeptCode(String organType, String majorCode, String lineCode);

    List<OrganMajorLineType> getWorkerGroupBySubjectAndLine(@Param("subjectCode") String subjectCode, @Param("line") String line, @Param("orgType") String orgType);

    List<OrganMajorLineType> getDepartmentUserByGroupName(@Param("dptCode") String dptCode, @Param("groupCname") String groupCname);
}
