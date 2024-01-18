package com.wzmtr.eam.service.common;

import com.wzmtr.eam.dto.res.bpmn.BpmnExaminePersonRes;
import com.wzmtr.eam.dto.res.common.DispatchResDTO;
import com.wzmtr.eam.entity.OrganMajorLineType;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/8/11 15:27
 */
public interface UserGroupMemberService {

    List<OrganMajorLineType> getDepartmentUserByGroupName(String groupCode);

    /**
     * 根据部门及角色获取人员列表
     * @param groupCname 角色编号
     * @param dptCode 部门编号
     * @return 人员列表
     */
    List<OrganMajorLineType> getDepartmentUserByGroupName(String groupCname, String dptCode);

    List<BpmnExaminePersonRes> getZcOverhaulPlanExamineUser(String subjectCode, String lineCode);

    List<DispatchResDTO> queryDispatch();
}
