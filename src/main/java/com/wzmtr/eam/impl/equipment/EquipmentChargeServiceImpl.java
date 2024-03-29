package com.wzmtr.eam.impl.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.equipment.EquipmentChargeReqDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentChargeResDTO;
import com.wzmtr.eam.dto.res.equipment.excel.ExcelEquipChargeResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.equipment.EquipmentChargeMapper;
import com.wzmtr.eam.service.common.UserAccountService;
import com.wzmtr.eam.service.equipment.EquipmentChargeService;
import com.wzmtr.eam.utils.DateUtils;
import com.wzmtr.eam.utils.EasyExcelUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author frp
 */
@Service
@Slf4j
public class EquipmentChargeServiceImpl implements EquipmentChargeService {

    @Resource
    private UserAccountService userAccountService;

    @Autowired
    private EquipmentChargeMapper equipmentChargeMapper;

    @Override
    public Page<EquipmentChargeResDTO> listEquipmentCharge(String equipCode, String equipName, String chargeDate, String position1Code,
                                                           String subjectCode, String systemCode, String equipTypeCode, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());


        // 专业未筛选时，按当前用户专业隔离数据  获取当前用户所属组织专业
        List<String> userMajorList = null;
        if (!CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId()) && StringUtils.isEmpty(subjectCode)) {
            userMajorList = userAccountService.listUserMajor();
        }

        return equipmentChargeMapper.pageEquipmentCharge(pageReqDTO.of(), equipCode, equipName, chargeDate, position1Code, subjectCode, systemCode, equipTypeCode,userMajorList);
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
        equipmentChargeReqDTO.setRecId(TokenUtils.getUuId());
        equipmentChargeReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        equipmentChargeReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        equipmentChargeMapper.addEquipmentCharge(equipmentChargeReqDTO);
    }

    @Override
    public void modifyEquipmentCharge(EquipmentChargeReqDTO equipmentChargeReqDTO) {
        equipmentChargeReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        equipmentChargeReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        equipmentChargeMapper.modifyEquipmentCharge(equipmentChargeReqDTO);
    }

    @Override
    public void deleteEquipmentCharge(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            equipmentChargeMapper.deleteEquipmentCharge(baseIdsEntity.getIds(), TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void exportEquipmentCharge(List<String> ids, HttpServletResponse response) throws IOException {
        List<EquipmentChargeResDTO> equipmentChargeResDTOList = equipmentChargeMapper.listEquipmentCharge(ids);
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
