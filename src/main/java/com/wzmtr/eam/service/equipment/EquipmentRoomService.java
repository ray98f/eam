package com.wzmtr.eam.service.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.equipment.EquipmentRoomReqDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentRoomResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;

import javax.servlet.http.HttpServletResponse;

public interface EquipmentRoomService {

    Page<EquipmentRoomResDTO> listEquipmentRoom(String equipRoomCode, String equipRoomName, String lineCode, String position1Code,
                                                String position1Name, String subjectCode, PageReqDTO pageReqDTO);

    EquipmentRoomResDTO getEquipmentRoomDetail(String id);

    void addEquipmentRoom(EquipmentRoomReqDTO equipmentRoomReqDTO);

    void modifyEquipmentRoom(EquipmentRoomReqDTO equipmentRoomReqDTO);

    void deleteEquipmentRoom(BaseIdsEntity baseIdsEntity);

    void exportEquipmentRoom(String equipRoomCode, String equipRoomName, String lineCode, String position1Code,
                             String position1Name, String subjectCode, HttpServletResponse response);
}
