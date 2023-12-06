package com.wzmtr.eam.impl.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.equipment.EquipmentRoomReqDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentRoomResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.equipment.EquipmentRoomMapper;
import com.wzmtr.eam.service.equipment.EquipmentRoomService;
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
                                    String position1Name, String subjectCode, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "设备房编码", "设备房名称", "专业编码", "专业名称", "线别编码",
                "位置一编码", "位置一名称", "位置二编码", "位置二名称", "备注", "创建者", "创建时间");
        List<EquipmentRoomResDTO> equipmentRoomResDTOList = equipmentRoomMapper.listEquipmentRoom(equipRoomCode, equipRoomName, lineCode, position1Code, position1Name, subjectCode);
        List<Map<String, String>> list = new ArrayList<>();
        if (equipmentRoomResDTOList != null && !equipmentRoomResDTOList.isEmpty()) {
            for (EquipmentRoomResDTO resDTO : equipmentRoomResDTOList) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", resDTO.getRecId());
                map.put("设备房编码", resDTO.getEquipRoomCode());
                map.put("设备房名称", resDTO.getEquipRoomName());
                map.put("专业编码", resDTO.getSubjectCode());
                map.put("专业名称", resDTO.getSubjectName());
                map.put("线别编码", CommonConstants.LINE_CODE_ONE.equals(resDTO.getLineCode()) ? "S1线" : "S2线");
                map.put("位置一编码", resDTO.getPosition1Code());
                map.put("位置一名称", resDTO.getPosition1Name());
                map.put("位置二编码", resDTO.getPosition2Code());
                map.put("位置二名称", resDTO.getPosition2Name());
                map.put("备注", resDTO.getRemark());
                map.put("创建者", resDTO.getRecCreator());
                map.put("创建时间", resDTO.getRecCreateTime());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("设备房信息", listName, list, null, response);
    }

}
