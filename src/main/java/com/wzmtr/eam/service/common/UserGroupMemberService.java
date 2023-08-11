package com.wzmtr.eam.service.common;

import com.wzmtr.eam.entity.OrganMajorLineType;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/8/11 15:27
 */
public interface UserGroupMemberService {

    List<OrganMajorLineType> getDepartmentUserByGroupName(String groupCode);
}
