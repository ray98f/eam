package com.wzmtr.eam.impl.common;

import com.wzmtr.eam.entity.OrganMajorLineType;
import com.wzmtr.eam.mapper.basic.OrgMajorMapper;
import com.wzmtr.eam.service.common.UserGroupMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/8/11 15:28
 */
@Service
public class UserGroupMemberServiceImpl implements UserGroupMemberService {
    @Autowired
    private OrgMajorMapper orgMajorMapper;
    @Override
    public List<OrganMajorLineType> getDepartmentUserByGroupName(String groupCode) {
        //DM_012 是故障跟踪这边写死的 原来的逻辑 我也看不懂
        return orgMajorMapper.getDepartmentUserByGroupName(groupCode,"DM_012");
    }

    @Override
    public List<OrganMajorLineType> getDepartmentUserByGroupName(String dptCode, String groupCode) {
        return orgMajorMapper.getDepartmentUserByGroupName(dptCode,groupCode);
    }
}
