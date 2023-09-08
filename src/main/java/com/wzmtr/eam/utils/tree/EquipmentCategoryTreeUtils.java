package com.wzmtr.eam.utils.tree;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wzmtr.eam.dto.res.basic.EquipmentCategoryResDTO;

import java.util.List;
import java.util.Map;

/**
 * @author frp
 */
public class EquipmentCategoryTreeUtils {

    /**
     * 根节点对象
     */
    private List<EquipmentCategoryResDTO> rootList;

    /**
     * 其他节点，可以包含根节点
     */
    private List<EquipmentCategoryResDTO> bodyList;

    public EquipmentCategoryTreeUtils(List<EquipmentCategoryResDTO> rootList, List<EquipmentCategoryResDTO> bodyList) {
        this.rootList = rootList;
        this.bodyList = bodyList;
    }

    public List<EquipmentCategoryResDTO> getTree() {
        if (bodyList != null && !bodyList.isEmpty()) {
            //声明一个map，用来过滤已操作过的数据
            Map<String, String> map = Maps.newHashMapWithExpectedSize(bodyList.size());
            rootList.forEach(beanTree -> getChild(beanTree, map));
        }
        return rootList;
    }

    public void getChild(EquipmentCategoryResDTO equipmentCategoryResDTO, Map<String, String> map) {
        List<EquipmentCategoryResDTO> childList = Lists.newArrayList();
        bodyList.stream()
                .filter(c -> !map.containsKey(c.getRecId()))
                .filter(c -> c.getParentNodeRecId().equals(equipmentCategoryResDTO.getRecId()))
                .forEach(c -> {
                    map.put(c.getRecId(), c.getParentNodeRecId());
                    getChild(c, map);
                    childList.add(c);
                });
        equipmentCategoryResDTO.setChildren(childList);

    }
}
