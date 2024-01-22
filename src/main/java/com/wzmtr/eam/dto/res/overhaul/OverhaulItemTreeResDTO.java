package com.wzmtr.eam.dto.res.overhaul;

import lombok.Data;

import java.util.List;

/**
 * 检修模块及检修项树状返回类
 * @author  Ray
 * @version 1.0
 * @date 2024/01/17
 */
@Data
public class OverhaulItemTreeResDTO {

    /**
     * 模块名称
     */
    private String modelName;

    /**
     * 用户工号
     */
    private String workUserId;

    /**
     * 用户姓名
     */
    private String workUserName;

    /**
     * 结果
     */
    private String workResult;

    /**
     * 检修项列表
     */
    private List<OverhaulItemResDTO> itemList;
}
