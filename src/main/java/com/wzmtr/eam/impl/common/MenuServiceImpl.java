package com.wzmtr.eam.impl.common;

import com.wzmtr.eam.dto.req.common.MenuAddReqDTO;
import com.wzmtr.eam.dto.req.common.MenuModifyReqDTO;
import com.wzmtr.eam.dto.res.common.MenuDetailResDTO;
import com.wzmtr.eam.dto.res.common.MenuListResDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.MenuMapper;
import com.wzmtr.eam.service.common.MenuService;
import com.wzmtr.eam.utils.TokenUtil;
import com.wzmtr.eam.utils.tree.MenuTreeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<MenuListResDTO> listLoginMenu() {
        List<MenuListResDTO> extraRootList = menuMapper.listLoginMenuRootList(TokenUtil.getCurrentPersonId());
        List<MenuListResDTO> extraBodyList = menuMapper.listLoginMenuBodyList(TokenUtil.getCurrentPersonId());
        MenuTreeUtils extraTree = new MenuTreeUtils(extraRootList, extraBodyList);
        return extraTree.getTree();
    }

    @Override
    public List<MenuListResDTO> listUseMenu() {
        List<MenuListResDTO> extraRootList = menuMapper.listUseMenuRootList();
        List<MenuListResDTO> extraBodyList = menuMapper.listUseMenuBodyList();
        MenuTreeUtils extraTree = new MenuTreeUtils(extraRootList, extraBodyList);
        return extraTree.getTree();
    }

    @Override
    public List<MenuListResDTO> listMenu() {
        List<MenuListResDTO> extraRootList = menuMapper.listMenuRootList();
        List<MenuListResDTO> extraBodyList = menuMapper.listMenuBodyList();
        MenuTreeUtils extraTree = new MenuTreeUtils(extraRootList, extraBodyList);
        return extraTree.getTree();
    }

    @Override
    public MenuDetailResDTO getMenuDetail(String id) {
        if (Objects.isNull(id)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        return menuMapper.getMenuDetail(id);
    }

    @Override
    public void addMenu(MenuAddReqDTO menuAddReqDTO) {
        if (Objects.isNull(menuAddReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        menuAddReqDTO.setId(TokenUtil.getUuId());
        menuAddReqDTO.setUserId(TokenUtil.getCurrentPersonId());
        Integer result = menuMapper.insertMenu(menuAddReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    public void deleteMenu(String id) {
        if (Objects.isNull(id)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = menuMapper.selectMenuHadChildren(id);
        if (result > 0) {
            throw new CommonException(ErrorCode.RESOURCE_USE);
        }
        result = menuMapper.deleteMenu(TokenUtil.getCurrentPersonId(), id);
        if (result < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

    @Override
    public void modifyMenu(MenuModifyReqDTO menuModifyReqDTO) {
        if (Objects.isNull(menuModifyReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        menuModifyReqDTO.setUserId(TokenUtil.getCurrentPersonId());
        Integer result = menuMapper.modifyMenu(menuModifyReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }
}
