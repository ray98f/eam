package com.wzmtr.eam.utils.tree;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wzmtr.eam.entity.CompanyStructureTree;

import java.util.List;
import java.util.Map;

/**
 * @author frp
 */
public class CompanyTreeUtils {
    /**
     * 根节点对象
     */
    private List<CompanyStructureTree> rootList;

    /**
     * 其他节点，可以包含根节点
     */
    private List<CompanyStructureTree> bodyList;

    public CompanyTreeUtils(List<CompanyStructureTree> rootList, List<CompanyStructureTree> bodyList) {
        this.rootList = rootList;
        this.bodyList = bodyList;
    }

    public List<CompanyStructureTree> getTree() {
        if (bodyList != null && !bodyList.isEmpty()) {
            //声明一个map，用来过滤已操作过的数据
            Map<String, String> map = Maps.newHashMapWithExpectedSize(bodyList.size());
            rootList.forEach(beanTree -> getChild(beanTree, map));
        }
        return rootList;
    }

    public void getChild(CompanyStructureTree companyStructureTree, Map<String, String> map) {
        List<CompanyStructureTree> childList = Lists.newArrayList();
        bodyList.stream()
                .filter(c -> !map.containsKey(c.getId()))
                .filter(c -> c.getParentId().equals(companyStructureTree.getId()))
                .forEach(c -> {
                    map.put(c.getId(), c.getParentId());
                    getChild(c, map);
                    childList.add(c);
                });
        companyStructureTree.setChildren(childList);

    }
}
