package com.wzmtr.eam.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * excel中的动态字段
 * Author: Li.Wang
 * Date: 2023/9/25 11:30
 */
@Data
public class DynamicSource {
    private String id;
    private List<Map<String, String>> dataList;

    public static List<DynamicSource> createList(List<String> id, List<Map<String, String>> dataList) {
        return id.stream()
                .map(a -> {
                    DynamicSource dynamicSource = new DynamicSource();
                    dynamicSource.setId(a);
                    dynamicSource.setDataList(dataList);
                    return dynamicSource;
                })
                .collect(Collectors.toList());
    }
}
