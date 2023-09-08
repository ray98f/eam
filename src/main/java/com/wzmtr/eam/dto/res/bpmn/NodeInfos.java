package com.wzmtr.eam.dto.res.bpmn;

import lombok.Data;

import java.util.List;

/**
 * 麒麟审核人
 *
 * @Author lize
 * @Date 2023/5/30
 */
@Data
public class NodeInfos {
    private List<NodeInfo> list;

    @Override
    public String toString() {
        String str = "[";
        for (NodeInfo info : list) {
            str += info.toString() + ",";
        }
        str = str.substring(0, str.length() - 1);
        str += "]";
        return str;
    }

}
