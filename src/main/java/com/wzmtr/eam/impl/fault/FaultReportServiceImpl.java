package com.wzmtr.eam.impl.fault;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.bo.FaultInfoBO;
import com.wzmtr.eam.bo.FaultOrderBO;
import com.wzmtr.eam.dto.req.fault.FaultDetailReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultReportPageReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultReportReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultReportToMajorReqDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.FaultReportResDTO;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.fault.FaultReportMapper;
import com.wzmtr.eam.mapper.fault.TrackQueryMapper;
import com.wzmtr.eam.service.fault.FaultReportService;
import com.wzmtr.eam.utils.CodeUtils;
import com.wzmtr.eam.utils.DateUtil;
import com.wzmtr.eam.utils.TokenUtil;
import com.wzmtr.eam.utils.__BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Author: Li.Wang
 * Date: 2023/8/15 19:17
 */
@Service
@Slf4j
public class FaultReportServiceImpl implements FaultReportService {
    @Autowired
    private FaultReportMapper faultReportMapper;
    @Autowired
    private TrackQueryMapper trackQueryMapper;
    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    // @Transactional(rollbackFor = Exception.class)
    public void addToEquip(FaultReportReqDTO reqDTO) {
        String maxFaultNo = faultReportMapper.getFaultInfoFaultNoMaxCode();
        String maxFaultWorkNo = faultReportMapper.getFaultOrderFaultWorkNoMaxCode();
        // 获取AOP代理对象
        // FaultReportServiceImpl aop = (FaultReportServiceImpl) AopContext.currentProxy();
        FaultInfoBO faultInfoBO = reqDTO.toFaultInfoBO(reqDTO);
        String nextFaultNo = CodeUtils.getNextCode(maxFaultNo, "GZ");
        _insertToFaultInfo(faultInfoBO, nextFaultNo);
        FaultOrderBO faultOrderBO = reqDTO.toFaultOrderBO(reqDTO);
        _insertToFaultOrder(faultOrderBO, nextFaultNo, maxFaultWorkNo);
        // TODO: 2023/8/24 知会OCC调度 可能要发通知？
        // if ("Y".equals(maintenance)) {
        //     /* 1756 */             String groupName = "DM_021";
        //     /* 1757 */             String content2 = userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "提报了一条" + majorName + "故障，工单号：" + faultWorkNo + "的故障，请知晓。";
        //     /* 1758 */             EiInfo eiInfo1 = new EiInfo();
        //     /* 1759 */             eiInfo1.set("group", groupName);
        //     /* 1760 */             eiInfo1.set("content", content2);
        //     /* 1761 */             ISendMessage.sendMessageByGroup(eiInfo1);
        //     /* 1762 */             ISendMessage.sendMoblieMessageByGroup(eiInfo1);
        //     /*      */           }
    }

    private void _insertToFaultInfo(FaultInfoBO faultInfoBO, String maxFaultNo) {
        faultInfoBO.setFaultNo(maxFaultNo);
        faultReportMapper.addToFaultInfo(faultInfoBO);
    }

    private void _insertToFaultOrder(FaultOrderBO faultOrderBO, String maxFaultNo, String maxFaultWorkNo) {
        String nextFaultWorkNo = CodeUtils.getNextCode(maxFaultWorkNo, "GD");
        faultOrderBO.setFaultWorkNo(nextFaultWorkNo);
        faultOrderBO.setFaultNo(maxFaultNo);
        faultReportMapper.addToFaultOrder(faultOrderBO);
    }

    @Override
    public Page<FaultReportResDTO> list(FaultReportPageReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<FaultReportResDTO> list = faultReportMapper.list(reqDTO.of(), reqDTO.getFaultNo(), reqDTO.getObjectCode(), reqDTO.getObjectName(), reqDTO.getFaultModule(), reqDTO.getMajorCode(), reqDTO.getSystemCode(), reqDTO.getEquipTypeCode(), reqDTO.getFillinTimeStart(), reqDTO.getFillinTimeEnd());
        if (CollectionUtil.isEmpty(list.getRecords())) {
            return new Page<>();
        }
        list.getRecords().forEach(a -> {
            a.setRepairDeptName(organizationMapper.getExtraOrgByAreaId(a.getRepairDeptCode()));
            a.setFillinDeptName(organizationMapper.getOrgById(a.getFillinDeptCode()));
        });
        return list;
    }


    @Override
    public void addToMajor(FaultReportToMajorReqDTO reqDTO) {
        String maxFaultNo = faultReportMapper.getFaultInfoFaultNoMaxCode();
        String maxFaultWorkNo = faultReportMapper.getFaultOrderFaultWorkNoMaxCode();
        FaultInfoBO faultInfoBO = __BeanUtil.convert(reqDTO, FaultInfoBO.class);
        faultInfoBO.setRecId(TokenUtil.getUuId());
        faultInfoBO.setRecCreator(TokenUtil.getCurrentPerson().getPersonId());
        faultInfoBO.setRecCreateTime(DateUtil.current(DateUtil.YYYY_MM_DD_HH_MM_SS));
        _insertToFaultInfo(faultInfoBO, maxFaultNo);
        FaultOrderBO faultOrderBO = __BeanUtil.convert(reqDTO, FaultOrderBO.class);
        faultOrderBO.setRecId(TokenUtil.getUuId());
        if (reqDTO.getRepairDeptCode() != null) {
            faultOrderBO.setWorkClass(reqDTO.getRepairDeptCode());
        }
        faultOrderBO.setRecCreator(TokenUtil.getCurrentPerson().getPersonId());
        faultOrderBO.setRecCreateTime(DateUtil.current(DateUtil.YYYY_MM_DD_HH_MM_SS));
        _insertToFaultOrder(faultOrderBO, maxFaultNo, maxFaultWorkNo);
    }

    @Override
    public FaultDetailResDTO detail(FaultDetailReqDTO reqDTO) {
        FaultInfoBO faultInfoBO = trackQueryMapper.faultDetail(reqDTO);
        if (faultInfoBO == null) {
            return null;
        }
        return __BeanUtil.convert(faultInfoBO, FaultDetailResDTO.class);
    }

}
