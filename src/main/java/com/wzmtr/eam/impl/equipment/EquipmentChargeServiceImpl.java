package com.wzmtr.eam.impl.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.equipment.EquipmentChargeReqDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentChargeResDTO;
import com.wzmtr.eam.dto.res.equipment.excel.ExcelEquipChargeResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.equipment.EquipmentChargeMapper;
import com.wzmtr.eam.service.equipment.EquipmentChargeService;
import com.wzmtr.eam.utils.EasyExcelUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author frp
 */
@Service
@Slf4j
public class EquipmentChargeServiceImpl implements EquipmentChargeService {

    @Autowired
    private EquipmentChargeMapper equipmentChargeMapper;

    @Override
    public Page<EquipmentChargeResDTO> listEquipmentCharge(String equipCode, String equipName, String chargeDate, String position1Code,
                                                           String subjectCode, String systemCode, String equipTypeCode, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return equipmentChargeMapper.pageEquipmentCharge(pageReqDTO.of(), equipCode, equipName, chargeDate, position1Code, subjectCode, systemCode, equipTypeCode);
    }

    @Override
    public EquipmentChargeResDTO getEquipmentChargeDetail(String id) {
        return equipmentChargeMapper.getEquipmentChargeDetail(id);
    }

    @Override
    public void addEquipmentCharge(EquipmentChargeReqDTO equipmentChargeReqDTO) {
        String equipName = equipmentChargeMapper.selectEquipmentNameByCode(equipmentChargeReqDTO.getEquipCode());
        if (StringUtils.isEmpty(equipName)) {
            throw new CommonException(ErrorCode.EQUIP_CODE_ERROR);
        }
        equipmentChargeReqDTO.setRecId(TokenUtil.getUuId());
        equipmentChargeReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        equipmentChargeReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        equipmentChargeMapper.addEquipmentCharge(equipmentChargeReqDTO);
    }

    @Override
    public void modifyEquipmentCharge(EquipmentChargeReqDTO equipmentChargeReqDTO) {
        equipmentChargeReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        equipmentChargeReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        equipmentChargeMapper.modifyEquipmentCharge(equipmentChargeReqDTO);
    }

    @Override
    public void deleteEquipmentCharge(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            equipmentChargeMapper.deleteEquipmentCharge(baseIdsEntity.getIds(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void exportEquipmentCharge(String equipCode, String equipName, String chargeDate, String position1Name,
                                      String subjectCode, String systemCode, String equipTypeCode, HttpServletResponse response) throws IOException {
        List<EquipmentChargeResDTO> equipmentChargeResDTOList = equipmentChargeMapper.listEquipmentCharge(equipCode, equipName, chargeDate, position1Name, subjectCode, systemCode, equipTypeCode);
        if (equipmentChargeResDTOList != null && !equipmentChargeResDTOList.isEmpty()) {
            List<ExcelEquipChargeResDTO> list = new ArrayList<>();
            for (EquipmentChargeResDTO resDTO : equipmentChargeResDTOList) {
                ExcelEquipChargeResDTO res = new ExcelEquipChargeResDTO();
                BeanUtils.copyProperties(resDTO, res);
                res.setChargeDuration(String.valueOf(resDTO.getChargeDuration()));
                list.add(res);
            }
            EasyExcelUtils.export(response, "设备充电信息", list);
        }
    }

}
