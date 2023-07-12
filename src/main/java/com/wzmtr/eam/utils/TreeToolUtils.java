package com.wzmtr.eam.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wzmtr.eam.entity.CompanyStructureTreeDTO;

import java.util.List;
import java.util.Map;

/**
 * @author frp
 */
public class TreeToolUtils {
    /**
     * 根节点对象
     */
    private List<CompanyStructureTreeDTO> rootList;

    /**
     * 其他节点，可以包含根节点
     */
    private List<CompanyStructureTreeDTO> bodyList;

    public TreeToolUtils(List<CompanyStructureTreeDTO> rootList, List<CompanyStructureTreeDTO> bodyList) {
        this.rootList = rootList;
        this.bodyList = bodyList;
    }

    public List<CompanyStructureTreeDTO> getTree() {
        if (bodyList != null && !bodyList.isEmpty()) {
            //声明一个map，用来过滤已操作过的数据
            Map<String, String> map = Maps.newHashMapWithExpectedSize(bodyList.size());
            rootList.forEach(beanTree -> getChild(beanTree, map));
        }
        return rootList;
    }

    public void getChild(CompanyStructureTreeDTO companyStructureTreeDTO, Map<String, String> map) {
        List<CompanyStructureTreeDTO> childList = Lists.newArrayList();
        bodyList.stream()
                .filter(c -> !map.containsKey(c.getId()))
                .filter(c -> c.getParentId().equals(companyStructureTreeDTO.getId()))
                .forEach(c -> {
                    map.put(c.getId(), c.getParentId());
                    getChild(c, map);
                    childList.add(c);
                });
        companyStructureTreeDTO.setChildren(childList);

    }
}
