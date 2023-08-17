package com.wzmtr.eam.impl.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.bo.FaultInfoBO;
import com.wzmtr.eam.bo.FaultOrderBO;
import com.wzmtr.eam.dto.req.fault.FaultReportPageReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultReportReqDTO;
import com.wzmtr.eam.dto.res.fault.FaultReportResDTO;
import com.wzmtr.eam.mapper.fault.FaultReportMapper;
import com.wzmtr.eam.service.fault.FaultReportService;
import com.wzmtr.eam.utils.CodeUtils;
import com.wzmtr.eam.utils.__BeanUtil;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Author: Li.Wang
 * Date: 2023/8/15 19:17
 */
@Service
public class FaultReportServiceImpl implements FaultReportService {
    @Autowired
    private FaultReportMapper faultReportMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addToEquip(FaultReportReqDTO reqDTO) {
        String maxFaultNo = faultReportMapper.getFaultOrderFaultNoMaxCode();
        String maxFaultWorkNo = faultReportMapper.getFaultOrderFaultWorkNoMaxCode();
        // 获取AOP代理对象
        FaultReportServiceImpl aop = (FaultReportServiceImpl) AopContext.currentProxy();
        aop._insertToFaultOrder(reqDTO, maxFaultNo, maxFaultWorkNo);
        aop._insertToFaultInfo(reqDTO, maxFaultNo);
    }

    private void _insertToFaultInfo(FaultReportReqDTO reqDTO, String maxFaultNo) {
        String nextFaultNo = CodeUtils.getNextCode(maxFaultNo, "GZ");
        FaultInfoBO faultInfoBO = reqDTO.toFaultInfoBO(reqDTO);
        faultInfoBO.setFaultNo(nextFaultNo);
        faultReportMapper.addToFaultInfo(faultInfoBO);
    }

    private void _insertToFaultOrder(FaultReportReqDTO reqDTO, String maxFaultNo, String maxFaultWorkNo) {
        String nextFaultNo = CodeUtils.getNextCode(maxFaultNo, "GZ");
        String nextFaultWorkNo = CodeUtils.getNextCode(maxFaultWorkNo, "GD");
        FaultOrderBO faultOrderBO = reqDTO.toFaultOrderBO(reqDTO);
        faultOrderBO.setFaultWorkNo(nextFaultWorkNo);
        faultOrderBO.setFaultNo(nextFaultNo);
        faultReportMapper.addToFaultOrder(faultOrderBO);
    }

    @Override
    public Page<FaultReportResDTO> list(FaultReportPageReqDTO reqDTO) {
        return faultReportMapper.list(reqDTO.of(), reqDTO.getFaultNo(), reqDTO.getObjectCode(), reqDTO.getObjectName(), reqDTO.getFaultModule(), reqDTO.getMajorCode(), reqDTO.getSystemCode(), reqDTO.getEquipTypeCode(), reqDTO.getFillinTimeStart(), reqDTO.getFillinTimeEnd());
    }

    @Override
    public Page<FaultReportResDTO> detail(FaultReportPageReqDTO reqDTO) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addToMajor(FaultReportReqDTO reqDTO) {
        // String faultNo = IMakeSeq.getSeqFromTable("GZ", DateUtils.curDateStr("yyMMdd"), "WBPLAT.SEQ_TDMFM01");
        // String faultWorkNo = IMakeSeq.getSeqFromTable("GD", DateUtils.curDateStr("yyMMdd"), "WBPLAT.SEQ_TDMFM02");
        // SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // /* 126 */
        // inInfo.getBlock("delivery").setCell(i, "recCreator", currentUser);
        // /* 127 */
        // inInfo.getBlock("delivery").setCell(i, "recCreateTime", dateTimeFormat.format(new Date()));
        // /* 128 */
        // inInfo.getBlock("delivery").setCell(i, "fillinTime", dateTimeFormat.format(new Date()));
        // /* 129 */
        // inInfo.getBlock("delivery").setCell(i, "recId", UUID.randomUUID().toString());
        // /* 150 */
        // String recId = UUID.randomUUID().toString();
        // /* 151 */
        // dmfm02.setRecId(recId);
        // /* 152 */
        // dmfm02.setFaultNo(faultNo);
        // if (faultType.equals("30")) {
        //     /* 215 */
        //     dmfm02.setOrderStatus("30");
        //     /*     */
        // }
    }
}
