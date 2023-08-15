package com.wzmtr.eam.impl.fault;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.fault.AnalyzeReqDTO;
import com.wzmtr.eam.dto.res.fault.AnalyzeResDTO;
import com.wzmtr.eam.dto.res.secure.SecureHazardResDTO;
import com.wzmtr.eam.mapper.fault.AnalyzeMapper;
import com.wzmtr.eam.service.fault.AnalyzeService;
import com.wzmtr.eam.utils.ExcelPortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Author: Li.Wang
 * Date: 2023/8/14 16:31
 */
@Service
public class AnalyzeServiceImpl implements AnalyzeService {
    @Autowired
    private AnalyzeMapper mapper;

    @Override
    public Page<AnalyzeResDTO> list(AnalyzeReqDTO reqDTO) {
        return mapper.query(reqDTO.of(), reqDTO.getFaultNo(), reqDTO.getMajorCode(), reqDTO.getRecStatus(),
                reqDTO.getLineCode(), reqDTO.getFrequency(), reqDTO.getPositionCode(), reqDTO.getDiscoveryStartTime(),
                reqDTO.getDiscoveryEndTime(), reqDTO.getRespDeptCode(), reqDTO.getAffectCodes());
    }

    @Override
    public void export(String faultAnalysisNo, String faultNo, String faultWorkNo, HttpServletResponse response) {
        List<AnalyzeResDTO> resList = mapper.list(faultAnalysisNo, faultNo, faultWorkNo);
        List<String> listName = Arrays.asList("故障分析编号", "故障编号", "故障工单编号", "专业", "故障发现时间", "线路", "频次", "位置", "牵头部门", "故障等级", "故障影响", "故障现象","故障原因","故障调查及处置情况","系统","本次故障暴露的问题","整改措施");
        if (CollectionUtil.isEmpty(resList)) {
            return;
        }
        List<Map<String, String>> list = new ArrayList<>();
        for (AnalyzeResDTO resDTO : resList) {
            Map<String, String> map = new HashMap<>();
            map.put("故障分析编号", resDTO.getFaultAnalysisNo());
            map.put("故障编号", resDTO.getFaultNo());
            map.put("故障工单编号", resDTO.getFaultWorkNo());
            map.put("专业", resDTO.getMajorName());
            map.put("故障发现时间", resDTO.getDiscoveryTime());
            map.put("线路", resDTO.getLineCode());
            map.put("频次", resDTO.getFrequency());
            map.put("位置", resDTO.getPosition());
            map.put("牵头部门", resDTO.getRespDeptCode());
            map.put("故障等级", resDTO.getFaultLevel());
            map.put("故障影响", resDTO.getAffectCodes());
            map.put("故障现象", resDTO.getFaultDisplayDetail());
            map.put("故障原因", resDTO.getFaultReasonDetail());
            map.put("故障调查及处置情况", resDTO.getFaultProcessDetail());
            map.put("系统", resDTO.getSystemCode());
            map.put("本次故障暴露的问题", resDTO.getProblemDescr());
            map.put("整改措施", resDTO.getImproveDetail());
            list.add(map);
        }
        ExcelPortUtil.excelPort("故障调查及处置情况", listName, list, null, response);
    }
}
