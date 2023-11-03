package com.wzmtr.eam.mapper.common;

import com.wzmtr.eam.dto.req.common.MenuAddReqDTO;
import com.wzmtr.eam.dto.req.common.MenuModifyReqDTO;
import com.wzmtr.eam.dto.res.common.MenuDetailResDTO;
import com.wzmtr.eam.dto.res.common.MenuListResDTO;
import com.wzmtr.eam.dto.res.common.SuperMenuResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MenuMapper {

    List<MenuListResDTO> listLoginMenuRootList(@Param("personId") String personId);

    List<MenuListResDTO> listLoginMenuBodyList(@Param("personId") String personId);

    List<MenuListResDTO> listUseMenuRootList();

    List<MenuListResDTO> listUseMenuBodyList();

    List<MenuListResDTO> listMenuRootList();

    List<MenuListResDTO> listMenuBodyList();

    MenuDetailResDTO getMenuDetail(@Param("id") String id);

    Integer selectRootRight(@Param("type") Integer type, @Param("parentId") String parentId);

    Integer insertMenu(MenuAddReqDTO menuAddReqDTO);

    Integer selectMenuHadChildren(@Param("id") String id);

    Integer selectMenuHadUseChildren(String id, Integer status, Integer isShow);

    Integer deleteMenu(@Param("userId") String userId, @Param("id") String id);

    Integer modifyMenu(MenuModifyReqDTO menuModifyReqDTO);

}
