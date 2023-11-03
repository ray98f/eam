package com.wzmtr.eam.utils.tree;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wzmtr.eam.dto.res.basic.RegionResDTO;
import com.wzmtr.eam.dto.res.common.MenuListResDTO;

import java.util.List;
import java.util.Map;

/**
 * @author frp
 */
public class MenuTreeUtils {

    /**
     * 根节点对象
     */
    private List<MenuListResDTO> rootList;

    /**
     * 其他节点，可以包含根节点
     */
    private List<MenuListResDTO> bodyList;

    public MenuTreeUtils(List<MenuListResDTO> rootList, List<MenuListResDTO> bodyList) {
        this.rootList = rootList;
        this.bodyList = bodyList;
    }

    public List<MenuListResDTO> getTree() {
        if (bodyList != null && !bodyList.isEmpty()) {
            //声明一个map，用来过滤已操作过的数据
            Map<String, String> map = Maps.newHashMapWithExpectedSize(bodyList.size());
            rootList.forEach(beanTree -> getChild(beanTree, map));
        }
        return rootList;
    }

    public void getChild(MenuListResDTO menuListResDTO, Map<String, String> map) {
        List<MenuListResDTO> childList = Lists.newArrayList();
        bodyList.stream()
                .filter(c -> !map.containsKey(c.getId()))
                .filter(c -> c.getParentId().equals(menuListResDTO.getId()))
                .forEach(c -> {
                    map.put(c.getId(), c.getParentId());
                    getChild(c, map);
                    childList.add(c);
                });
        menuListResDTO.setChildren(childList);

    }
}
