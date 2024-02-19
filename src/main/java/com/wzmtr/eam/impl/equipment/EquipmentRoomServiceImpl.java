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
import com.wzmtr.eam.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
        equipmentRoomReqDTO.setRecId(TokenUtils.getUuId());
        equipmentRoomReqDTO.setEquipRoomCode(CodeUtils.getNextCode(equipmentRoomMapper.selectMaxEquipmentRoomCode(), 1));
        equipmentRoomReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        equipmentRoomReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        equipmentRoomMapper.addEquipmentRoom(equipmentRoomReqDTO);
    }

    @Override
    public void modifyEquipmentRoom(EquipmentRoomReqDTO equipmentRoomReqDTO) {
        equipmentRoomReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        equipmentRoomReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        equipmentRoomMapper.modifyEquipmentRoom(equipmentRoomReqDTO);
    }

    @Override
    public void deleteEquipmentRoom(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            equipmentRoomMapper.deleteEquipmentRoom(baseIdsEntity.getIds(), TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void exportEquipmentRoom(List<String> ids, HttpServletResponse response) throws IOException {
        List<EquipmentRoomResDTO> equipmentRoomResDTOList = equipmentRoomMapper.exportEquipmentRoom(ids);
        if (StringUtils.isNotEmpty(equipmentRoomResDTOList)) {
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
