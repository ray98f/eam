package com.wzmtr.eam.impl.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.FaultReqDTO;
import com.wzmtr.eam.dto.res.FaultResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.basic.FaultMapper;
import com.wzmtr.eam.service.basic.FaultService;
import com.wzmtr.eam.utils.ExcelPortUtil;
import com.wzmtr.eam.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author frp
 */
@Service
@Slf4j
public class FaultServiceImpl implements FaultService {

    @Autowired
    private FaultMapper faultMapper;

    @Override
    public Page<FaultResDTO> listFault(String code, Integer type, String lineCode, String equipmentCategoryCode, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return faultMapper.pageFault(pageReqDTO.of(), code, type, lineCode, equipmentCategoryCode);
    }

    @Override
    public FaultResDTO getFaultDetail(String id) {
        return faultMapper.getFaultDetail(id);
    }

    @Override
    public void addFault(FaultReqDTO faultReqDTO) {
        Integer result = faultMapper.selectFaultIsExist(faultReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        faultReqDTO.setRecId(TokenUtil.getUuId());
        faultReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        faultReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        faultMapper.addFault(faultReqDTO);
    }

    @Override
    public void modifyFault(FaultReqDTO faultReqDTO) {
        Integer result = faultMapper.selectFaultIsExist(faultReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        faultReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        faultReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        faultMapper.modifyFault(faultReqDTO);
    }

    @Override
    public void deleteFault(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            faultMapper.deleteFault(baseIdsEntity.getIds(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void exportFault(String code, Integer type, String lineCode, String equipmentCategoryCode, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "对象编码", "对象名称", "线路编号", "码值类型", "码值编号", "码值描述", "关联码值", "记录状态", "备注", "创建者", "创建时间");
        List<FaultResDTO> faultResDTOList = faultMapper.listFault(code, type, lineCode, equipmentCategoryCode);
        List<Map<String, String>> list = new ArrayList<>();
        if (faultResDTOList != null && !faultResDTOList.isEmpty()) {
            for (FaultResDTO fault : faultResDTOList) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", fault.getRecId());
                map.put("对象编码", fault.getEquipmentTypeCode());
                map.put("对象名称", fault.getEquipmentTypeName());
                map.put("线路编号", "01".equals(fault.getLineCode()) ? "S1线" : "S2线");
                map.put("码值类型", "1".equals(fault.getFaultCodeType()) ? "现象码" : "2".equals(fault.getFaultCodeType()) ? "原因码" : "行动码");
                map.put("码值编号", fault.getFaultCode());
                map.put("码值描述", fault.getFaultDescr());
                map.put("关联码值", fault.getRelatedCodes());
                map.put("记录状态", "10".equals(fault.getRecStatus()) ? "启用" : "禁用");
                map.put("备注", fault.getRemark());
                map.put("创建者", fault.getRecCreator());
                map.put("创建时间", fault.getRecCreateTime());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("故障码信息", listName, list, null, response);
    }

}
