package com.wzmtr.eam.impl.fault;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.bo.FaultInfoBO;
import com.wzmtr.eam.bo.FaultOrderBO;
import com.wzmtr.eam.dto.req.fault.FaultDetailReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultQueryReqDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.entity.SidEntity;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.fault.FaultQueryMapper;
import com.wzmtr.eam.mapper.fault.FaultReportMapper;
import com.wzmtr.eam.service.fault.FaultQueryService;
import com.wzmtr.eam.utils.*;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Author: Li.Wang
 * Date: 2023/8/17 17:02
 */
@Service
public class FaultQueryServiceImpl implements FaultQueryService {
    @Autowired
    FaultQueryMapper faultQueryMapper;
    @Autowired
    FaultReportMapper reportMapper;
    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public Page<FaultDetailResDTO> list(FaultQueryReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<FaultDetailResDTO> list = faultQueryMapper.list(reqDTO.of(), reqDTO);
        List<FaultDetailResDTO> records = list.getRecords();
        if (CollectionUtil.isEmpty(records)) {
            return new Page<>();
        }
        records.forEach(a -> {
            String repair = organizationMapper.getExtraOrgByAreaId(a.getRepairDeptCode());
            a.setRepairDeptName(StringUtils.isEmpty(repair) ? a.getRepairDeptCode() : repair);
        });
        return list;
    }

    @Override
    public String queryOrderStatus(SidEntity reqDTO) {
        // faultWorkNo
        List<String> status = faultQueryMapper.queryOrderStatus(reqDTO);
        return CollectionUtil.isEmpty(status) ? null : status.get(0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void issue(FaultDetailReqDTO reqDTO) {
        FaultQueryServiceImpl aop = (FaultQueryServiceImpl) AopContext.currentProxy();
        String status = aop.queryOrderStatus(SidEntity.builder().id(reqDTO.getFaultWorkNo()).build());
        FaultOrderBO faultOrderBO = __BeanUtil.convert(reqDTO,FaultOrderBO.class);
        switch (status) {
            case "40":
                faultOrderBO.setReportStartUserId(TokenUtil.getCurrentPersonId());
                faultOrderBO.setReportStartTime(DateUtil.current(DateUtil.YYYY_MM_DD_HH_MM_SS));
                break;
            case "50":
                faultOrderBO.setReportFinishUserId(TokenUtil.getCurrentPersonId());
                faultOrderBO.setReportFinishTime(DateUtil.current(DateUtil.YYYY_MM_DD_HH_MM_SS));
                break;
            case "60":
                faultOrderBO.setConfirmUserId(TokenUtil.getCurrentPersonId());
                faultOrderBO.setConfirmTime(DateUtil.current(DateUtil.YYYY_MM_DD_HH_MM_SS));
                break;
            case "55":
                faultOrderBO.setCheckUserId(TokenUtil.getCurrentPersonId());
                faultOrderBO.setCheckTime(DateUtil.current(DateUtil.YYYY_MM_DD_HH_MM_SS));
                break;
        }
        faultOrderBO.setRecRevisor(TokenUtil.getCurrentPersonId());
        faultOrderBO.setRecReviseTime(DateUtil.current(DateUtil.YYYY_MM_DD_HH_MM_SS));
        reportMapper.updateFaultOrder(faultOrderBO);
        FaultInfoBO faultInfoBO = __BeanUtil.convert(reqDTO,FaultInfoBO.class);
        faultInfoBO.setRecRevisor(TokenUtil.getCurrentPersonId());
        faultInfoBO.setRecReviseTime(DateUtil.current(DateUtil.YYYY_MM_DD_HH_MM_SS));
        reportMapper.updateFaultInfo(faultInfoBO);
    }

    @Override
    public void export(FaultQueryReqDTO reqDTO, HttpServletResponse response) {
        List<String> listName = Arrays.asList("故障编号", "故障现象", "故障详情", "对象名称", "部件名称", "故障工单编号", "对象编码", "故障状态", "维修部门", "提报部门", "提报人员", "联系电话", "提报时间", "发现人", "发现时间", "故障紧急程度", "故障影响", "线路", "车底号/车厢号", "位置一", "位置二", "专业", "系统", "设备分类", "模块", "更换部件", "旧配件编号", "新配件编号", "部件更换时间", "故障处理人员", "故障处理人数");
        List<FaultDetailResDTO> faultDetailResDTOS = faultQueryMapper.exportList(reqDTO);
        List<Map<String, String>> list = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(faultDetailResDTOS)) {
            for (FaultDetailResDTO resDTO : faultDetailResDTOS) {
                String repairDept = organizationMapper.getOrgById(resDTO.getRepairDeptCode());
                String fillinDept = organizationMapper.getOrgById(resDTO.getFillinDeptCode());
                Map<String, String> map = new HashMap<>();
                map.put("故障编号", resDTO.getFaultNo());
                map.put("故障现象", resDTO.getFaultDisplayDetail());
                map.put("故障详情", resDTO.getFaultDetail());
                map.put("对象名称", resDTO.getObjectName());
                map.put("部件名称", resDTO.getPartName());
                map.put("故障工单编号", resDTO.getFaultWorkNo());
                map.put("对象编码", resDTO.getObjectCode());
                map.put("故障状态", resDTO.getRecStatus());
                map.put("维修部门", StringUtils.isEmpty(repairDept) ? resDTO.getRepairDeptCode() : repairDept);
                map.put("提报部门", StringUtils.isEmpty(fillinDept) ? resDTO.getFillinDeptCode() : fillinDept);
                map.put("提报人员", resDTO.getFillinUserName());
                map.put("联系电话", resDTO.getDiscovererPhone());
                map.put("提报时间", resDTO.getFillinTime());
                map.put("发现人", resDTO.getDiscovererName());
                map.put("发现时间", resDTO.getDiscoveryTime());
                map.put("故障紧急程度", resDTO.getFaultLevel());
                map.put("故障影响", resDTO.getFaultAffect());
                map.put("线路", resDTO.getLineCode());
                map.put("车底号/车厢号", resDTO.getTrainTrunk());
                map.put("位置一", resDTO.getPositionName());
                map.put("位置二", resDTO.getPosition2Name());
                map.put("专业", resDTO.getMajorName());
                map.put("系统", resDTO.getSystemName());
                map.put("设备分类", resDTO.getEquipTypeName());
                map.put("模块", resDTO.getFaultModule());
                map.put("故障处理人员", resDTO.getDealerUnit());
                map.put("故障处理人数", resDTO.getDealerNum());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("xxxx", listName, list, null, response);
    }
}
