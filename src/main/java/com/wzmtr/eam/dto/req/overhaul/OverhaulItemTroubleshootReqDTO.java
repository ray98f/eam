package com.wzmtr.eam.dto.req.overhaul;

import com.wzmtr.eam.dto.res.overhaul.OverhaulItemResDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * 检修项排查请求类
 * @author  Ray
 * @version 1.0
 * @date 2024/01/17
 */
@Data
@ApiModel
public class OverhaulItemTroubleshootReqDTO {

    /**
     * 作业人员ids
     */
    private String workUserId;

    /**
     * 作业人员名称
     */
    private String workUserName;

    /**
     * 对象编号
     */
    private String objectCode;

    /**
     * 工单编号
     */
    private String orderCode;

    /**
     * 检修项列表
     */
    private List<OverhaulItemResDTO> overhaulItemList;

}
