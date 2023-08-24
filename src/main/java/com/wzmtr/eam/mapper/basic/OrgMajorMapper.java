package com.wzmtr.eam.mapper.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.OrgMajorReqDTO;
import com.wzmtr.eam.dto.res.OrgMajorResDTO;
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

    /**
     * 获取组织机构专业列表-分页
     *
     * @param page
     * @param orgCodes
     * @param majorCode
     * @return
     */
    Page<OrgMajorResDTO> pageOrgMajor(Page<OrgMajorResDTO> page, List<List<String>> orgCodes, String majorCode);

    /**
     * 获取组织机构专业详情
     *
     * @param id
     * @return
     */
    OrgMajorResDTO getOrgMajorDetail(String id);

    /**
     * 查询组织机构专业是否已存在
     *
     * @param orgMajorReqDTO
     * @return
     */
    Integer selectOrgMajorIsExist(OrgMajorReqDTO orgMajorReqDTO);

    /**
     * 新增组织机构专业
     *
     * @param orgMajorReqDTO
     */
    void addOrgMajor(OrgMajorReqDTO orgMajorReqDTO);

    /**
     * 修改组织机构专业
     *
     * @param orgMajorReqDTO
     */
    void modifyOrgMajor(OrgMajorReqDTO orgMajorReqDTO);

    /**
     * 删除组织机构专业
     *
     * @param ids
     * @param userId
     * @param time
     */
    void deleteOrgMajor(List<String> ids, String userId, String time);

    /**
     * 获取组织机构专业列表
     *
     * @param orgCodes
     * @param majorCode
     * @return
     */
    List<OrgMajorResDTO> listOrgMajor(List<List<String>> orgCodes, String majorCode);
    List<OrgMajorResDTO> queryTypeAndDeptCode(String organType, String majorCode,String lineCode);

    List<OrganMajorLineType> getWorkerGroupBySubjectAndLine(@Param("subjectCode") String subjectCode, @Param("line") String line, @Param("orgType") String orgType);


    List<OrganMajorLineType> getDepartmentUserByGroupName(@Param("dptCode")String dptCode,@Param("groupCname") String groupCname);
}
