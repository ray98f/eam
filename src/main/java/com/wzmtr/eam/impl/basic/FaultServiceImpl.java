package com.wzmtr.eam.impl.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.basic.FaultReqDTO;
import com.wzmtr.eam.dto.res.basic.FaultResDTO;
import com.wzmtr.eam.dto.res.basic.excel.ExcelFaultResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.basic.FaultMapper;
import com.wzmtr.eam.service.basic.FaultService;
import com.wzmtr.eam.utils.DateUtils;
import com.wzmtr.eam.utils.EasyExcelUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author frp
 */
@Service
@Slf4j
public class FaultServiceImpl implements FaultService {

    @Autowired
    private FaultMapper faultMapper;

    @Override
    public Page<FaultResDTO> listFault(String code, Integer type, String lineCode, String equipmentCategoryCode, String equipmentTypeName, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return faultMapper.pageFault(pageReqDTO.of(), code, type, lineCode, equipmentCategoryCode, equipmentTypeName);
    }

    @Override
    public FaultResDTO getFaultDetail(String id) {
        return faultMapper.getFaultDetail(id);
    }

    @Override
    public void addFault(FaultReqDTO faultReqDTO) {
        Integer result = faultMapper.selectFaultIsExist(faultReqDTO);
        if (result > CommonConstants.ONE) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        faultReqDTO.setRecId(TokenUtils.getUuId());
        faultReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        faultReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        faultMapper.addFault(faultReqDTO);
    }

    @Override
    public void modifyFault(FaultReqDTO faultReqDTO) {
        Integer result = faultMapper.selectFaultIsExist(faultReqDTO);
        if (result > CommonConstants.ONE) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        faultReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        faultReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        faultMapper.modifyFault(faultReqDTO);
    }

    @Override
    public void deleteFault(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            faultMapper.deleteFault(baseIdsEntity.getIds(), TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void exportFault(List<String> ids, HttpServletResponse response) throws IOException {
        List<FaultResDTO> faultResDTOList = faultMapper.exportFault(ids);
        if (faultResDTOList != null && !faultResDTOList.isEmpty()) {
            List<ExcelFaultResDTO> resList = new ArrayList<>();
            for (FaultResDTO fault : faultResDTOList) {
                ExcelFaultResDTO resDTO = ExcelFaultResDTO.builder()
                        .recId(fault.getRecId())
                        .equipmentTypeCode(fault.getEquipmentTypeCode())
                        .equipmentTypeName(fault.getEquipmentTypeName())
                        .lineCode(CommonConstants.LINE_CODE_ONE.equals(fault.getLineCode()) ? "S1线" : "S2线")
                        .faultCodeType(CommonConstants.ONE_STRING.equals(fault.getFaultCodeType()) ? "现象码" : CommonConstants.TWO_STRING.equals(fault.getFaultCodeType()) ? "原因码" : "行动码")
                        .faultCode(fault.getFaultCode())
                        .faultDescr(fault.getFaultDescr())
                        .relatedCodes(fault.getRelatedCodes())
                        .recStatus(CommonConstants.TEN_STRING.equals(fault.getRecStatus()) ? "启用" : "禁用")
                        .remark(fault.getRemark())
                        .recCreator(fault.getRecCreator())
                        .recCreateTime(fault.getRecCreateTime())
                        .build();
                resList.add(resDTO);
            }
            EasyExcelUtils.export(response, "故障码信息", resList);
        }
    }

    /**
     * 故障查询获取码值列表
     * @param code 故障码
     * @param type 故障类型
     * @param lineCode 线路编号
     * @param equipmentCategoryCode 设备类别编号
     * @return 码值列表
     */
    @Override
    public List<FaultResDTO> listQueryFault(String code, Integer type, String lineCode, String equipmentCategoryCode) {
        List<FaultResDTO> list = faultMapper.listFault(code, type, lineCode, equipmentCategoryCode);
        FaultResDTO faultResDTO = new FaultResDTO();
        switch (type) {
            case 1:
                faultResDTO.setFaultCode("E999");
                break;
            case 2:
                faultResDTO.setFaultCode("R999");
                break;
            default:
                faultResDTO.setFaultCode("A999");
                break;
        }
        faultResDTO.setFaultDescr("其他");
        list.add(0, faultResDTO);
        return list;
    }

}
