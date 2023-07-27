package com.wzmtr.eam.service.impl.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.EquipmentChargeReqDTO;
import com.wzmtr.eam.dto.res.EquipmentChargeResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.equipment.EquipmentChargeMapper;
import com.wzmtr.eam.service.equipment.EquipmentChargeService;
import com.wzmtr.eam.utils.CodeUtils;
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
public class EquipmentChargeServiceImpl implements EquipmentChargeService {

    @Autowired
    private EquipmentChargeMapper equipmentChargeMapper;

    @Override
    public Page<EquipmentChargeResDTO> listEquipmentCharge(String equipCode, String equipName, String chargeDate, String position1Name,
                                                           String subjectCode, String systemCode, String equipTypeCode, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return equipmentChargeMapper.pageEquipmentCharge(pageReqDTO.of(), equipCode, equipName, chargeDate, position1Name, subjectCode, systemCode, equipTypeCode);
    }

    @Override
    public EquipmentChargeResDTO getEquipmentChargeDetail(String id) {
        return equipmentChargeMapper.getEquipmentChargeDetail(id);
    }

    @Override
    public void addEquipmentCharge(EquipmentChargeReqDTO equipmentChargeReqDTO) {
        String equipName = equipmentChargeMapper.selectEquipmentNameByCode(equipmentChargeReqDTO.getEquipCode());
        if (equipName == null || "".equals(equipName)) {
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
                                      String subjectCode, String systemCode, String equipTypeCode, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "设备编码", "设备名称", "专业", "系统", "设备类别", "位置一", "充电日期", "充电时长", "备注", "创建者", "创建时间");
        List<EquipmentChargeResDTO> equipmentChargeResDTOList = equipmentChargeMapper.listEquipmentCharge(equipCode, equipName, chargeDate, position1Name, subjectCode, systemCode, equipTypeCode);
        List<Map<String, String>> list = new ArrayList<>();
        if (equipmentChargeResDTOList != null && !equipmentChargeResDTOList.isEmpty()) {
            for (EquipmentChargeResDTO resDTO : equipmentChargeResDTOList) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", resDTO.getRecId());
                map.put("设备编码", resDTO.getEquipCode());
                map.put("设备名称", resDTO.getEquipName());
                map.put("专业", resDTO.getMajorName());
                map.put("系统", resDTO.getSystemName());
                map.put("设备类别", resDTO.getEquipTypeName());
                map.put("位置一", resDTO.getPosition1Name());
                map.put("充电日期", resDTO.getChargeDate());
                map.put("充电时长", String.valueOf(resDTO.getChargeDuration()));
                map.put("备注", resDTO.getRemark());
                map.put("创建者", resDTO.getRecCreator());
                map.put("创建时间", resDTO.getRecCreateTime());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("设备充电信息", listName, list, null, response);
    }

}
