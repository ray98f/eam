package com.wzmtr.eam.dto.req.equipment;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
@ApiModel
public class GeneralSurveyExportReqDTO {
    /**
     * 列车号
     */
    private String trainNo;
    /**
     * 技术通知单编号
     */
    private String recNotifyNo;
    /**
     * 项目内容
     */
    private String recDetail;
    /**
     * 作业单位
     */
    private String orgType;
    /**
     * ids
     */
    private List<String> ids;
}
