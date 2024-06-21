package com.wzmtr.eam.mapper.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.basic.OrgMajorReqDTO;
import com.wzmtr.eam.dto.res.basic.OrgMajorResDTO;
import com.wzmtr.eam.dto.res.common.DispatchResDTO;
import com.wzmtr.eam.dto.res.common.ZcjxResDTO;
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

    List<OrgMajorResDTO> allListOrgMajor( List<List<String>> orgCodes, String majorCode);

    List<OrgMajorResDTO> listUseOrgMajor(String majorCode);

    OrgMajorResDTO getOrgMajorDetail(String id);

    Integer selectOrgMajorIsExist(OrgMajorReqDTO orgMajorReqDTO);

    void addOrgMajor(OrgMajorReqDTO orgMajorReqDTO);

    void modifyOrgMajor(OrgMajorReqDTO orgMajorReqDTO);

    void deleteOrgMajor(List<String> ids, String userId, String time);

    List<OrgMajorResDTO> listOrgMajor(List<String> ids);

    List<OrgMajorResDTO> queryTypeAndDeptCode(String organType, String majorCode, String lineCode);

    List<OrganMajorLineType> getWorkerGroupBySubjectAndLine(@Param("subjectCode") String subjectCode, @Param("line") String line, @Param("orgType") String orgType);

    /**
     * 根据部门及角色获取人员列表
     * @param groupCname 角色编号
     * @param dptCode 部门编号
     * @return 人员列表
     */
    List<OrganMajorLineType> getDepartmentUserByGroupName(@Param("groupCname") String groupCname, @Param("dptCode") String dptCode);


    List<DispatchResDTO> queryDispatch();
    List<ZcjxResDTO> queryJxWorker(String officeId);
    List<ZcjxResDTO> queryShWorker(String officeId,String roleCode);

    /**
     * 根据位置和专业获取部门列表
     * @param station 位置一code
     * @param majorCode 专业code
     * @return 部门列表
     */
    List<OrgMajorResDTO> listOrganByStationAndMajor(String station, String majorCode);

    /**
     * 根据位置和专业获取部门
     * @param station 位置一code
     * @param majorCode 专业code
     * @return 部门
     */
    OrgMajorResDTO getOrganByStationAndMajor(String station, String majorCode);

    /**
     * 获取组织机构路径
     * @param station 车站
     * @param majorCode 专业编号
     * @return 组织机构路径
     */
    String getOrgNamesByStationAndMajor(String station, String majorCode);

    /**
     * 基础数据部门名称同步
     */
    void syncSysOrgName();

    /**
     * 删除无效数据
     */
    void deleteNoneOrgCode();
}
