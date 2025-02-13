package com.wzmtr.eam.dto.req.fault;

import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dataobject.FaultInfoDO;
import com.wzmtr.eam.dataobject.FaultOrderDO;
import com.wzmtr.eam.dto.res.equipment.EquipmentResDTO;
import com.wzmtr.eam.enums.OrderStatus;
import com.wzmtr.eam.utils.BeanUtils;
import com.wzmtr.eam.utils.DateUtils;
import com.wzmtr.eam.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Objects;

/**
 * Author: Li.Wang
 * Date: 2023/8/15 17:18
 */
@Data
@ApiModel
public class FaultReportReqDTO {
    @ApiModelProperty(value = "id")
    private String recId;
    @ApiModelProperty(value = "故障编号")
    private String faultNo;
    @ApiModelProperty(value = "对象编码")
    private String objectCode;
    @ApiModelProperty(value = "公司code")
    private String companyCode;
    @ApiModelProperty(value = "公司Name")
    private String companyName;
    @ApiModelProperty(value = "对象名称")
    private String objectName;
    @ApiModelProperty(value = "车底号/车厢号")
    private String trainTrunk;
    @ApiModelProperty(value = "位置1")
    private String positionCode;
    @ApiModelProperty(value = "对象编码")
    private String objectCodeTextField;
    @ApiModelProperty(value = "线别姓名")
    private String lineName;
    @ApiModelProperty(value = "线别")
    private String lineCode;
    @ApiModelProperty(value = "位置2")
    private String position2Name;
    @ApiModelProperty(value = "位置2编码")
    private String position2Code;
    @ApiModelProperty(value = "专业")
    private String majorName;
    @ApiModelProperty(value = "系统")
    private String systemName;
    @ApiModelProperty(value = "设备分类")
    private String equipTypeName;
    @ApiModelProperty(value = "专业code")
    private String majorCode;
    @ApiModelProperty(value = "系统code")
    private String systemCode;
    @ApiModelProperty(value = "设备分类代码")
    private String equipTypeCode;
    @ApiModelProperty(value = "故障模块")
    private String faultModule;
    @ApiModelProperty(value = "故障模块Id")
    private String faultModuleId;
    @ApiModelProperty(value = "故障分类（10-运营故障；20-自检故障；30-新线调试；40-正线故障；50-出库故障）")
    private String faultType;
    @ApiModelProperty(value = "来源编号")
    private String sourceCode;
    @ApiModelProperty(value = "发现时间")
    private String discoveryTime;
    @ApiModelProperty(value = "发现人")
    private String discovererName;
    @ApiModelProperty(value = "发现人工号")
    private String discovererId;
    @ApiModelProperty(value = "发现人手机号")
    private String discovererPhone;
    @ApiModelProperty(value = "知会OCC调度")
    private Boolean maintenance;
    @ApiModelProperty(value = "故障现象")
    private String faultDisplayDetail;
    @ApiModelProperty(value = "提报时间")
    private String fillinTime;
    @ApiModelProperty(value = "提报人工号")
    private String fillinUserId;
    @ApiModelProperty(value = "提报人")
    private String fillinUserName;
    @ApiModelProperty(value = "提报部门Code")
    private String fillinDeptCode;
    @ApiModelProperty(value = "提报部门")
    private String fillinDeptName;
    @ApiModelProperty(value = "故障状态")
    private String orderStatus;
    @ApiModelProperty(value = "牵头部门")
    private String respDeptCode;
    @ApiModelProperty(value = "配合部门")
    private String assistDeptCode;
    @ApiModelProperty(value = "维修部门")
    private String repairDeptCode;
    @ApiModelProperty(value = "附件编号")
    private String docId;
    @ApiModelProperty(value = "故障详情")
    private String faultDetail;
    @ApiModelProperty(value = "故障造成影响")
    private String faultAffect;
    @ApiModelProperty(value = "检修车/运营车标识")
    private String traintag;
    @ApiModelProperty(value = "记录状态")
    private String recStatus;
    @ApiModelProperty(value = "部件编码")
    private String partCode;
    @ApiModelProperty(value = "部件名称")
    private String partName;
    @ApiModelProperty(value = "故障状态 10 草稿 20 提报")
    private String faultStatus;
    @ApiModelProperty(value = "是否由phm报出 0是 1否")
    private String ifPhm;
    @ApiModelProperty(value = "是否列入列车可靠性统计 0是 1否")
    private String ifReliability;
    @ApiModelProperty(value = "是否是外部系统 0否 1是")
    private String ifOther;
    @ApiModelProperty(value = "")
    private String ext5;

    @ApiModelProperty(value = "维修时限")
    private String repairLimitTime;


    @ApiModelProperty(value = "故障工单号:转报时用")
    private String faultWorkNo;
    /**
     * 数据库非空字段兜底赋值，初始化为空字符串
     */
    public FaultOrderDO toFaultOrderInsertDO(FaultReportReqDTO req) {
        FaultOrderDO convert = BeanUtils.convert(req, FaultOrderDO.class);
        if (StringUtils.isNotEmpty(req.getRepairDeptCode())) {
            convert.setWorkClass(req.getRepairDeptCode());
        }
        if (StringUtils.isEmpty(req.getRecStatus())) {
            convert.setRecStatus(CommonConstants.BLANK);
        }
        if (StringUtils.isEmpty(req.getCompanyName())) {
            convert.setCompanyName(CommonConstants.BLANK);
        }
        if (StringUtils.isEmpty(req.getDocId())) {
            convert.setDocId(CommonConstants.BLANK);
        }
        if (StringUtils.isEmpty(req.getCompanyCode())) {
            convert.setCompanyCode(CommonConstants.BLANK);
        }
        convert.setRecCreator(CommonConstants.BLANK);
        convert.setFaultNo(CommonConstants.BLANK);
        convert.setFaultWorkNo(CommonConstants.BLANK);
        convert.setRecCreateTime(DateUtils.getCurrentTime());
        convert.setDeleteFlag("0");
        if (StringUtils.isEmpty(req.getOrderStatus())) {
            convert.setOrderStatus(OrderStatus.TI_BAO.getCode());
        }
        return convert;
    }

    public FaultOrderDO toFaultOrderChangeDO(FaultReportReqDTO req) {
        FaultOrderDO convert = BeanUtils.convert(req, FaultOrderDO.class);
        if (StringUtils.isNotEmpty(req.getRepairDeptCode())) {
            convert.setWorkClass(req.getRepairDeptCode());
        }
        convert.setDeleteFlag("0");
        convert.setOrderStatus(OrderStatus.TI_BAO.getCode());
        return convert;
    }

    public FaultInfoDO toFaultInfoInsertDO(FaultReportReqDTO req) {
        FaultInfoDO convert = BeanUtils.convert(req, FaultInfoDO.class);
        // 是否知会OCC调度
        if (!Objects.isNull(req.getMaintenance())) {
            convert.setExt4(req.getMaintenance().toString());
        }
        //前端这里 1 是  0 否 我这里统一下把。。
        String reliability = req.getIfReliability();
        if (StringUtils.isNotEmpty(reliability) && CommonConstants.ONE_STRING.equals(reliability)) {
            convert.setIfReliability("0");
        }
        if (StringUtils.isNotEmpty(reliability) && CommonConstants.ZERO_STRING.equals(reliability)) {
            convert.setIfReliability("1");
        }
        convert.setTrainTag(req.getTraintag());
        if (StringUtils.isEmpty(req.getFillinDeptCode())) {
            convert.setFillinDeptCode(CommonConstants.BLANK);
        }
        if (StringUtils.isEmpty(req.getFillinUserName())) {
            convert.setFillinUserName(CommonConstants.BLANK);
        }
        if (StringUtils.isEmpty(req.getFaultDisplayDetail())) {
            convert.setFaultDisplayDetail(CommonConstants.BLANK);
        }
        if (StringUtils.isEmpty(req.getSourceCode())) {
            convert.setSourceCode(CommonConstants.BLANK);
        }
        if (StringUtils.isEmpty(req.getLineCode())) {
            convert.setLineCode(CommonConstants.BLANK);
        }
        if (StringUtils.isEmpty(req.getObjectName())) {
            convert.setObjectName(CommonConstants.BLANK);
        }
        if (StringUtils.isEmpty(req.getObjectCode())) {
            convert.setObjectCode(CommonConstants.BLANK);
        }
        if (StringUtils.isEmpty(req.getDocId())) {
            convert.setDocId(CommonConstants.BLANK);
        }
        if (StringUtils.isEmpty(req.getFaultType())) {
            convert.setFaultType(CommonConstants.BLANK);
        }
        if (StringUtils.isEmpty(req.getPositionCode())){
            convert.setPositionCode(CommonConstants.BLANK);
        }
        if (StringUtils.isEmpty(req.getCompanyCode())){
            convert.setCompanyCode(CommonConstants.BLANK);
        }
        if (StringUtils.isEmpty(req.getCompanyName())){
            convert.setCompanyName(CommonConstants.BLANK);
        }
        if (StringUtils.isEmpty(req.getFaultStatus())){
            convert.setFaultStatus(CommonConstants.BLANK);
        }
        convert.setFaultFlag(CommonConstants.BLANK);
        convert.setFaultLevel(CommonConstants.BLANK);
        convert.setRecStatus(CommonConstants.BLANK);
        convert.setFaultDisplayCode(CommonConstants.BLANK);
        convert.setDeleteFlag("0");
        return convert;
    }

    /**
     * 根据设备编号查询的设备数据填充故障提报入参
     */
    public FaultReportReqDTO toReportReqFromEquipment(EquipmentResDTO req) {
        FaultReportReqDTO convert = new FaultReportReqDTO();
        convert.setObjectCode(req.getEquipCode());
        convert.setObjectName(req.getEquipName());
        convert.setMajorCode(req.getMajorCode());
        convert.setMajorName(req.getMajorName());
        convert.setSystemCode(req.getSystemCode());
        convert.setSystemName(req.getSystemName());
        convert.setEquipTypeCode(req.getEquipTypeCode());
        convert.setEquipTypeName(req.getEquipTypeName());
        convert.setLineCode(req.getUseLineNo());
        convert.setLineName(req.getUseLineName());
        convert.setPositionCode(req.getPosition1Code());
        convert.setPosition2Code(req.getPosition2Code());
        convert.setPosition2Name(req.getPosition2Name());
        return convert;
    }

}