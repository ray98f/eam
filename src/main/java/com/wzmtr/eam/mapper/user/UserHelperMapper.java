package com.wzmtr.eam.mapper.user;

import com.wzmtr.eam.dto.res.common.PersonResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/9/8 16:26
 */
@Repository
@Mapper
public interface UserHelperMapper {

    List<PersonResDTO> getUserByGroup(String groupName);

    List<PersonResDTO> getUserByGroupAndOrg(String groupName, String orgCode);

    List<PersonResDTO> queryChildNo(String orgCode);

    List<PersonResDTO> queryParentNo(String orgCode);
}
