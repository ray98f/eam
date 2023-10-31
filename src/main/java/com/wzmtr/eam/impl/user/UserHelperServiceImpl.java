package com.wzmtr.eam.impl.user;

import cn.hutool.core.collection.CollectionUtil;
import com.wzmtr.eam.dto.res.common.PersonResDTO;
import com.wzmtr.eam.mapper.user.UserHelperMapper;
import com.wzmtr.eam.service.user.UserHelperService;
import com.wzmtr.eam.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        if (StringUtils.isBlank(groupName) || StringUtils.isBlank(orgCode)) {
            return null;
        }
        List<PersonResDTO> resultList = getUserByChildOrg(groupName, orgCode);
        if (CollectionUtil.isNotEmpty(resultList)) {
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
        if (CollectionUtil.isEmpty(resultList)) {
            List<PersonResDTO> dptList = userHelperMapper.queryChildNo(orgCode);
            if (CollectionUtil.isEmpty(dptList)) {
                return null;
            }
            dptList.forEach(a -> {
                List<PersonResDTO> tempList = getUserByChildOrg(groupName, a.getOrgCode());
                if (CollectionUtil.isNotEmpty(tempList)) {
                    resultList.addAll(tempList);
                }
            });
        }
        return resultList;
    }

    @Override
    public List<PersonResDTO> getUserByParentOrg(String groupName, String orgCode) {
        if (StringUtils.isBlank(groupName) || StringUtils.isBlank(orgCode)) {
            return null;
        }
        List<PersonResDTO> resultList = userHelperMapper.getUserByGroupAndOrg(groupName, orgCode);
        if (CollectionUtil.isEmpty(resultList)) {
            List<PersonResDTO> dptList = userHelperMapper.queryParentNo(orgCode);
            if (CollectionUtil.isEmpty(dptList)) {
                return null;
            }
            resultList = getUserByGroupNameAndOrg(groupName, dptList.get(0).getOrgCode());
        }
        return resultList;
    }
}
