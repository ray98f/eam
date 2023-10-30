package com.wzmtr.eam.mapper.user;

import com.wzmtr.eam.dto.res.common.PersonResDTO;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/9/8 16:26
 */
public interface UserHelperMapper {

    List<PersonResDTO> getUserByGroup(String groupName);

    List<PersonResDTO> getUserByGroupNameAndOrg(String groupName, String orgCode);
}
