package com.wzmtr.eam.impl.user;

import com.wzmtr.eam.dto.res.common.PersonResDTO;
import com.wzmtr.eam.mapper.user.UserHelperMapper;
import com.wzmtr.eam.service.user.UserHelperService;
import com.wzmtr.eam.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/10/31 14:56
 */
@Component
public class UserHelperServiceImpl implements UserHelperService {
    @Autowired
    private UserHelperMapper userHelperMapper;

    @Override
    public List<PersonResDTO> getUserByGroupNameAndOrg(String groupName, String orgCode) {
        if (org.apache.commons.lang3.StringUtils.isBlank(groupName) || org.apache.commons.lang3.StringUtils.isBlank(orgCode)) {
            return Collections.emptyList();
        }
        List<PersonResDTO> resultList = getUserByChildOrg(groupName, orgCode);
        if (StringUtils.isNotEmpty(resultList)) {
            return getUserByParentOrg(groupName, orgCode);
        }
        return resultList;
    }

    @Override
    public List<PersonResDTO> getUserByGroup(String groupName) {
        return userHelperMapper.getUserByGroup(groupName);
    }

    @Override
    public List<PersonResDTO> getUserByChildOrg(String groupName, String orgCode) {
        List<PersonResDTO> resultList = userHelperMapper.getUserByGroupAndOrg(groupName, orgCode);
        if (StringUtils.isEmpty(resultList)) {
            List<PersonResDTO> dptList = userHelperMapper.queryChildNo(orgCode);
            if (StringUtils.isEmpty(dptList)) {
                return Collections.emptyList();
            }
            dptList.forEach(a -> {
                List<PersonResDTO> tempList = getUserByChildOrg(groupName, a.getOrgCode());
                if (StringUtils.isNotEmpty(tempList)) {
                    resultList.addAll(tempList);
                }
            });
        }
        return resultList;
    }

    @Override
    public List<PersonResDTO> getUserByParentOrg(String groupName, String orgCode) {
        if (org.apache.commons.lang3.StringUtils.isBlank(groupName) || org.apache.commons.lang3.StringUtils.isBlank(orgCode)) {
            return Collections.emptyList();
        }
        List<PersonResDTO> resultList = userHelperMapper.getUserByGroupAndOrg(groupName, orgCode);
        if (StringUtils.isEmpty(resultList)) {
            List<PersonResDTO> dptList = userHelperMapper.queryParentNo(orgCode);
            if (StringUtils.isEmpty(dptList)) {
                return Collections.emptyList();
            }
            resultList = getUserByGroupNameAndOrg(groupName, dptList.get(0).getOrgCode());
        }
        return resultList;
    }
}
