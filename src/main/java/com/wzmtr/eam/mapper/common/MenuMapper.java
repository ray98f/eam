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

    List<MenuListResDTO> listLoginCatalog(@Param("personId") String personId);

    List<MenuListResDTO.MenuInfo> listLoginMenu(@Param("personId")String personId, @Param("catalogId")String catalogId);

    List<MenuListResDTO.MenuInfo.ButtonInfo> listLoginButton(@Param("personId")String personId, @Param("menuId")String menuId);

    List<MenuListResDTO> listUseCatalog();

    List<MenuListResDTO.MenuInfo> listUseMenu(@Param("catalogId") String catalogId);

    List<MenuListResDTO.MenuInfo.ButtonInfo> listUseButton(@Param("menuId") String menuId);

    List<MenuListResDTO> listCatalog(@Param("menuIds") List<String> menuIds);

    List<MenuListResDTO.MenuInfo> listMenu(@Param("catalogId") String catalogId, @Param("menuIds") List<String> menuIds);

    List<MenuListResDTO.MenuInfo.ButtonInfo> listButton(@Param("menuId") String menuId, @Param("menuIds") List<String> menuIds);

    MenuDetailResDTO getMenuDetail(@Param("id") String id);

    Integer selectRootRight(@Param("type") Integer type, @Param("parentId") String parentId);

    Integer insertMenu(MenuAddReqDTO menuAddReqDTO);

    Integer selectMenuHadChildren(@Param("id") String id);

    Integer selectMenuHadUseChildren(String id, Integer status, Integer isShow);

    Integer deleteMenu(@Param("userId") String userId, @Param("id") String id);

    Integer modifyMenu(MenuModifyReqDTO menuModifyReqDTO);

    List<SuperMenuResDTO> listSuperCatalog(@Param("type") Integer type);

    List<SuperMenuResDTO.MenuInfo> listSuperMenu(@Param("id") String id);

}
