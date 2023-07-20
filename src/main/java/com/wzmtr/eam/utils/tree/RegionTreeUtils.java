package com.wzmtr.eam.utils.tree;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wzmtr.eam.dto.res.RegionResDTO;

import java.util.List;
import java.util.Map;

/**
 * @author frp
 */
public class RegionTreeUtils {

    /**
     * 根节点对象
     */
    private List<RegionResDTO> rootList;

    /**
     * 其他节点，可以包含根节点
     */
    private List<RegionResDTO> bodyList;

    public RegionTreeUtils(List<RegionResDTO> rootList, List<RegionResDTO> bodyList) {
        this.rootList = rootList;
        this.bodyList = bodyList;
    }

    public List<RegionResDTO> getTree() {
        if (bodyList != null && !bodyList.isEmpty()) {
            //声明一个map，用来过滤已操作过的数据
            Map<String, String> map = Maps.newHashMapWithExpectedSize(bodyList.size());
            rootList.forEach(beanTree -> getChild(beanTree, map));
        }
        return rootList;
    }

    public void getChild(RegionResDTO regionResDTO, Map<String, String> map) {
        List<RegionResDTO> childList = Lists.newArrayList();
        bodyList.stream()
                .filter(c -> !map.containsKey(c.getRecId()))
                .filter(c -> c.getParentNodeRecId().equals(regionResDTO.getRecId()))
                .forEach(c -> {
                    map.put(c.getRecId(), c.getParentNodeRecId());
                    getChild(c, map);
                    childList.add(c);
                });
        regionResDTO.setChildren(childList);

    }
}
