package com.wzmtr.eam.dto.req.basic.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @Author: Li.Wang
 * Date: 2024/2/2 14:11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegionQuery {
    private Set<String> nodeCodes;
    private String nodeName;
}
