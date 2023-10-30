package com.wzmtr.eam.impl;

import com.wzmtr.eam.dto.req.common.MenuAddReqDTO;
import com.wzmtr.eam.dto.req.common.MenuModifyReqDTO;
import com.wzmtr.eam.dto.res.common.MenuDetailResDTO;
import com.wzmtr.eam.dto.res.common.MenuListResDTO;
import com.wzmtr.eam.dto.res.common.SuperMenuResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.MenuMapper;
import com.wzmtr.eam.service.common.MenuService;
import com.wzmtr.eam.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MenuServiceImpl implements MenuService {

    public static final int INT_BUTTON = 3;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<MenuListResDTO> listLoginMenu() {
        List<MenuListResDTO> list;
        List<MenuListResDTO.MenuInfo> menuInfoList;
        List<MenuListResDTO.MenuInfo.ButtonInfo> buttonInfoList;
        list = menuMapper.listLoginCatalog(TokenUtil.getCurrentPersonId());
        list = list.stream().filter(distinctByKey(MenuListResDTO::getId)).collect(Collectors.toList());
        if (!list.isEmpty()) {
            for (MenuListResDTO menuListResDTO : list) {
                menuInfoList = menuMapper.listLoginMenu(TokenUtil.getCurrentPersonId(), menuListResDTO.getId());
                menuInfoList = menuInfoList.stream().filter(distinctByKey(MenuListResDTO.MenuInfo::getId)).collect(Collectors.toList());
                if (!menuInfoList.isEmpty()) {
                    for (MenuListResDTO.MenuInfo menuInfo : menuInfoList) {
                        buttonInfoList = menuMapper.listLoginButton(TokenUtil.getCurrentPersonId(), menuInfo.getId());
                        buttonInfoList = buttonInfoList.stream().filter(distinctByKey(MenuListResDTO.MenuInfo.ButtonInfo::getId)).collect(Collectors.toList());
                        menuInfo.setChildren(buttonInfoList);
                    }
                }
                menuListResDTO.setChildren(menuInfoList);
            }
        }
        return list;
    }

    @Override
    public List<MenuListResDTO> listUseMenu() {
        List<MenuListResDTO> list;
        List<MenuListResDTO.MenuInfo> menuInfoList;
        List<MenuListResDTO.MenuInfo.ButtonInfo> buttonInfoList;
        list = menuMapper.listUseCatalog();
        if (!list.isEmpty()) {
            for (MenuListResDTO menuListResDTO : list) {
                menuInfoList = menuMapper.listUseMenu(menuListResDTO.getId());
                if (!menuInfoList.isEmpty()) {
                    for (MenuListResDTO.MenuInfo menuInfo : menuInfoList) {
                        buttonInfoList = menuMapper.listUseButton(menuInfo.getId());
                        menuInfo.setChildren(buttonInfoList);
                    }
                }
                menuListResDTO.setChildren(menuInfoList);
            }
        }
        return list;
    }

    @Override
    public List<MenuListResDTO> listMenu() {
        List<MenuListResDTO> list;
        List<MenuListResDTO.MenuInfo> menuInfoList;
        List<MenuListResDTO.MenuInfo.ButtonInfo> buttonInfoList;
        list = menuMapper.listCatalog(null);
        if (!list.isEmpty()) {
            for (MenuListResDTO menuListResDTO : list) {
                menuInfoList = menuMapper.listMenu(menuListResDTO.getId(), null);
                if (!menuInfoList.isEmpty()) {
                    for (MenuListResDTO.MenuInfo menuInfo : menuInfoList) {
                        buttonInfoList = menuMapper.listButton(menuInfo.getId(), null);
                        menuInfo.setChildren(buttonInfoList);
                    }
                }
                menuListResDTO.setChildren(menuInfoList);
            }
        }
        return list;
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
        if (menuAddReqDTO.getType() == 1) {
            if ("".equals(menuAddReqDTO.getParentId()) || menuAddReqDTO.getParentId() == null) {
                Integer result = menuMapper.insertMenu(menuAddReqDTO);
                if (result < 0) {
                    throw new CommonException(ErrorCode.INSERT_ERROR);
                }
            } else {
                throw new CommonException(ErrorCode.ROOT_ERROR);
            }
        } else {
            Integer result = menuMapper.selectRootRight(menuAddReqDTO.getType() - 1, menuAddReqDTO.getParentId());
            if (result <= 0) {
                throw new CommonException(ErrorCode.ROOT_ERROR);
            }
            result = menuMapper.insertMenu(menuAddReqDTO);
            if (result < 0) {
                throw new CommonException(ErrorCode.INSERT_ERROR);
            }
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
        if (menuModifyReqDTO.getType() == 1) {
            if ("".equals(menuModifyReqDTO.getParentId()) || menuModifyReqDTO.getParentId() == null) {
                Integer result = menuMapper.modifyMenu(menuModifyReqDTO);
                if (result < 0) {
                    throw new CommonException(ErrorCode.UPDATE_ERROR);
                }
            } else {
                throw new CommonException(ErrorCode.ROOT_ERROR);
            }
        } else {
            Integer result = menuMapper.selectRootRight(menuModifyReqDTO.getType() - 1, menuModifyReqDTO.getParentId());
            if (result <= 0) {
                throw new CommonException(ErrorCode.ROOT_ERROR);
            }
            result = menuMapper.modifyMenu(menuModifyReqDTO);
            if (result < 0) {
                throw new CommonException(ErrorCode.UPDATE_ERROR);
            }
        }
    }

    @Override
    public List<SuperMenuResDTO> listSuper(Integer type) {
        List<SuperMenuResDTO> superMenuResDTOList = menuMapper.listSuperCatalog(type);
        if (null == superMenuResDTOList || superMenuResDTOList.isEmpty()) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        if (type == INT_BUTTON) {
            for (SuperMenuResDTO superMenuResDTO : superMenuResDTOList) {
                List<SuperMenuResDTO.MenuInfo> menuInfoList = menuMapper.listSuperMenu(superMenuResDTO.getId());
                superMenuResDTO.setMenuInfo(menuInfoList);
            }
        }
        return superMenuResDTOList;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
