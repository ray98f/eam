package com.wzmtr.eam.impl.common;

import com.wzmtr.eam.entity.OrganMajorLineType;
import com.wzmtr.eam.service.common.UserGroupMemberService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/8/11 15:28
 */
@Service
public class UserGroupMemberServiceImpl implements UserGroupMemberService {
    @Override
    public List<OrganMajorLineType> getDepartmentUserByGroupName(String groupCode) {
        //todo
        // SELECT LOGIN_NAME AS "loginName",USER_NAME AS "userName" FROM (
        //         SELECT U.LOGIN_NAME,U.USER_NAME FROM IPLAT60.XS_USER U WHERE U.USER_ID IN
        //                 (SELECT MEMBER_ID FROM IPLAT60.XS_USER_GROUP_MEMBER WHERE PARENT_ID=
        //                         (SELECT ID FROM IPLAT60.XS_USER_GROUP WHERE GROUP_ENAME='DM_012'))
		// )
        // WHERE LOGIN_NAME IN
        //         (SELECT DISTINCT V_CO_ORG_USER.USER_CODE FROM RPLAT60.V_CO_ORG_USER WHERE V_CO_ORG_USER.ORG_CODE = '010360104')
        return null;
    }
}
