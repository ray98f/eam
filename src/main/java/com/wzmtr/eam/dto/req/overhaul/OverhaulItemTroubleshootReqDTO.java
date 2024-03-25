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
     * 实际开始时间
     */
    private String actualStartTime;

    /**
     * 实际结束时间
     */
    private String actualEndTime;

    /**
     * 开始时公里数
     */
    private Double startMile;

    /**
     * 结束时公里数
     */
    private Double endMile;

    /**
     * 检修项列表
     */
    private List<OverhaulItemResDTO> overhaulItemList;

}
