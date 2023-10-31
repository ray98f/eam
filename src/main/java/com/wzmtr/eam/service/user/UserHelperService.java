package com.wzmtr.eam.service.user;

import com.wzmtr.eam.dto.res.common.PersonResDTO;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/10/31 14:55
 */
public interface UserHelperService {
    List<PersonResDTO> getUserByGroupNameAndOrg(String groupName, String orgCode);

    List<PersonResDTO> getUserByGroup(String groupName);

    List<PersonResDTO> getUserByChildOrg(String groupName, String orgCode);

    List<PersonResDTO> getUserByParentOrg(String groupName, String orgCode);
}
