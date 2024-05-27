package com.wzmtr.eam.impl.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.equipment.EquipmentRoomRelationReqDTO;
import com.wzmtr.eam.dto.req.equipment.EquipmentRoomReqDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentResDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentRoomResDTO;
import com.wzmtr.eam.dto.res.equipment.excel.ExcelEquipRoomResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.equipment.EquipmentMapper;
import com.wzmtr.eam.mapper.equipment.EquipmentRoomMapper;
import com.wzmtr.eam.service.common.UserAccountService;
import com.wzmtr.eam.service.equipment.EquipmentRoomService;
import com.wzmtr.eam.utils.*;
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
public class EquipmentRoomServiceImpl implements EquipmentRoomService {

    @Resource
    private UserAccountService userAccountService;

    @Autowired
    private EquipmentRoomMapper equipmentRoomMapper;

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Override
    public Page<EquipmentRoomResDTO> listEquipmentRoom(String equipRoomCode, String equipRoomName, String lineCode, String position1Code,
                                                       String position1Name, String subjectCode, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());

        // 专业未筛选时，按当前用户专业隔离数据  获取当前用户所属组织专业
        List<String> userMajorList = null;
        if (!CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId()) && StringUtils.isEmpty(subjectCode)) {
            userMajorList = userAccountService.listUserMajor();
        }

        return equipmentRoomMapper.pageEquipmentRoom(pageReqDTO.of(), equipRoomCode, equipRoomName, lineCode, position1Code, position1Name, subjectCode, userMajorList);
    }

    @Override
    public EquipmentRoomResDTO getEquipmentRoomDetail(String id) {
        return equipmentRoomMapper.getEquipmentRoomDetail(id);
    }

    @Override
    public void addEquipmentRoom(EquipmentRoomReqDTO equipmentRoomReqDTO) {
        //轨道专业
        if (CommonConstants.FOURTEEN_STRING.equals(equipmentRoomReqDTO.getSubjectCode())) {
            if (StringUtils.isEmpty(equipmentRoomReqDTO.getEquipRoomCode())) {
                throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
            }
        } else {
            String equipRoomCode = equipmentRoomMapper.getEquipRoomCodeMaxCode();
            if (StringUtils.isEmpty(equipRoomCode)) {
                equipRoomCode = CommonConstants.EQUIPMENT_ROOM_CODE_0;
            }
            String nextCode = CodeUtils.getNextCode(equipRoomCode, CommonConstants.ONE);
            equipmentRoomReqDTO.setEquipRoomCode(nextCode);
        }
        Integer result = equipmentRoomMapper.selectEquipmentRoomIsExist(equipmentRoomReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }

        equipmentRoomReqDTO.setRecId(TokenUtils.getUuId());
        equipmentRoomReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        equipmentRoomReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        equipmentRoomMapper.addEquipmentRoom(equipmentRoomReqDTO);
    }

    @Override
    public void modifyEquipmentRoom(EquipmentRoomReqDTO equipmentRoomReqDTO) {
        Integer result = equipmentRoomMapper.selectEquipmentRoomIsExist(equipmentRoomReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
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

    @Override
    public void addEquipment(EquipmentRoomRelationReqDTO req) {
        if (StringUtils.isNotEmpty(req.getRoomId()) && StringUtils.isNotEmpty(req.getIds())) {
            for (String id : req.getIds()) {
                equipmentRoomMapper.insertRelationBatch(req.getRoomId(), id);
            }
        } else {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }

    }

    @Override
    public void deleteEquipment(EquipmentRoomRelationReqDTO req) {
        if (StringUtils.isNotEmpty(req.getRoomId()) && StringUtils.isNotEmpty(req.getIds())) {
            equipmentRoomMapper.deleteRelationBatch(req);
        } else {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    public Page<EquipmentResDTO> pageEquipment(String roomId, String equipCode, String equipName, String majorCode, String systemCode, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return equipmentMapper.pageEquipmentByRoom(pageReqDTO.of(), roomId, equipCode, equipName, majorCode, systemCode);
    }

}
