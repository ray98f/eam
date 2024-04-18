package com.wzmtr.eam.service.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.equipment.EquipmentRoomRelationReqDTO;
import com.wzmtr.eam.dto.req.equipment.EquipmentRoomReqDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentResDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentRoomResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface EquipmentRoomService {

    Page<EquipmentRoomResDTO> listEquipmentRoom(String equipRoomCode, String equipRoomName, String lineCode, String position1Code,
                                                String position1Name, String subjectCode, PageReqDTO pageReqDTO);

    EquipmentRoomResDTO getEquipmentRoomDetail(String id);

    void addEquipmentRoom(EquipmentRoomReqDTO equipmentRoomReqDTO);

    void modifyEquipmentRoom(EquipmentRoomReqDTO equipmentRoomReqDTO);

    void deleteEquipmentRoom(BaseIdsEntity baseIdsEntity);

    void exportEquipmentRoom(List<String> ids, HttpServletResponse response) throws IOException;

    void addEquipment(EquipmentRoomRelationReqDTO equipmentRoomRelationReqDTO);

    void deleteEquipment(EquipmentRoomRelationReqDTO equipmentRoomRelationReqDTO);

    Page<EquipmentResDTO> pageEquipment(String roomId,String equipCode,String equipName, String majorCode, String systemCode, PageReqDTO pageReqDTO);
}
