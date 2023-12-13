package com.wzmtr.eam.impl.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.equipment.EquipmentRoomReqDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentRoomResDTO;
import com.wzmtr.eam.dto.res.equipment.excel.ExcelEquipRoomResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.equipment.EquipmentRoomMapper;
import com.wzmtr.eam.service.equipment.EquipmentRoomService;
import com.wzmtr.eam.utils.CodeUtils;
import com.wzmtr.eam.utils.EasyExcelUtils;
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
public class EquipmentRoomServiceImpl implements EquipmentRoomService {

    @Autowired
    private EquipmentRoomMapper equipmentRoomMapper;

    @Override
    public Page<EquipmentRoomResDTO> listEquipmentRoom(String equipRoomCode, String equipRoomName, String lineCode, String position1Code,
                                                       String position1Name, String subjectCode, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return equipmentRoomMapper.pageEquipmentRoom(pageReqDTO.of(), equipRoomCode, equipRoomName, lineCode, position1Code, position1Name, subjectCode);
    }

    @Override
    public EquipmentRoomResDTO getEquipmentRoomDetail(String id) {
        return equipmentRoomMapper.getEquipmentRoomDetail(id);
    }

    @Override
    public void addEquipmentRoom(EquipmentRoomReqDTO equipmentRoomReqDTO) {
        equipmentRoomReqDTO.setRecId(TokenUtil.getUuId());
        equipmentRoomReqDTO.setEquipRoomCode(CodeUtils.getNextCode(equipmentRoomMapper.selectMaxEquipmentRoomCode(), 1));
        equipmentRoomReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        equipmentRoomReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        equipmentRoomMapper.addEquipmentRoom(equipmentRoomReqDTO);
    }

    @Override
    public void modifyEquipmentRoom(EquipmentRoomReqDTO equipmentRoomReqDTO) {
        equipmentRoomReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        equipmentRoomReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        equipmentRoomMapper.modifyEquipmentRoom(equipmentRoomReqDTO);
    }

    @Override
    public void deleteEquipmentRoom(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            equipmentRoomMapper.deleteEquipmentRoom(baseIdsEntity.getIds(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void exportEquipmentRoom(String equipRoomCode, String equipRoomName, String lineCode, String position1Code,
                                    String position1Name, String subjectCode, HttpServletResponse response) throws IOException {
        List<EquipmentRoomResDTO> equipmentRoomResDTOList = equipmentRoomMapper.listEquipmentRoom(equipRoomCode, equipRoomName, lineCode, position1Code, position1Name, subjectCode);
        if (equipmentRoomResDTOList != null && !equipmentRoomResDTOList.isEmpty()) {
            List<ExcelEquipRoomResDTO> list = new ArrayList<>();
            for (EquipmentRoomResDTO resDTO : equipmentRoomResDTOList) {
                ExcelEquipRoomResDTO res = new ExcelEquipRoomResDTO();
                BeanUtils.copyProperties(resDTO, res);
                res.setLineCode(CommonConstants.LINE_CODE_ONE.equals(resDTO.getLineCode()) ? "S1线" : "S2线");
                list.add(res);
            }
            EasyExcelUtils.export(response, "设备房信息", list);
        }
    }

}
