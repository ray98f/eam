package com.wzmtr.eam.service.common;

import com.wzmtr.eam.dto.res.common.MenuDetailResDTO;
import com.wzmtr.eam.dto.res.common.MenuListResDTO;
import com.wzmtr.eam.dto.res.common.SuperMenuResDTO;
import com.wzmtr.eam.dto.req.common.MenuAddReqDTO;
import com.wzmtr.eam.dto.req.common.MenuModifyReqDTO;

import java.util.List;

public interface MenuService {

    List<MenuListResDTO> listLoginMenu();

    List<MenuListResDTO> listUseMenu();

    List<MenuListResDTO> listMenu();

    MenuDetailResDTO getMenuDetail(String id);

    void addMenu(MenuAddReqDTO menuAddReqDTO);

    void deleteMenu(String id);

    void modifyMenu(MenuModifyReqDTO menuModifyReqDTO);

    List<SuperMenuResDTO> listSuper(Integer type);
}
