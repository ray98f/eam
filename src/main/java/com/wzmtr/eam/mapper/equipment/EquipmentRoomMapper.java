package com.wzmtr.eam.mapper.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.EquipmentRoomReqDTO;
import com.wzmtr.eam.dto.res.EquipmentRoomResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface EquipmentRoomMapper {


    Page<EquipmentRoomResDTO> pageEquipmentRoom(Page<EquipmentRoomResDTO> page, String equipRoomCode, String equipRoomName, String lineCode,
                                                String position1Code, String position1Name, String subjectCode);

    EquipmentRoomResDTO getEquipmentRoomDetail(String id);

    Integer selectEquipmentRoomIsExist(EquipmentRoomReqDTO equipmentRoomReqDTO);

    String selectMaxEquipmentRoomCode();

    void addEquipmentRoom(EquipmentRoomReqDTO equipmentRoomReqDTO);

    void modifyEquipmentRoom(EquipmentRoomReqDTO equipmentRoomReqDTO);

    void deleteEquipmentRoom(List<String> ids, String userId, String time);

    List<EquipmentRoomResDTO> listEquipmentRoom(String equipRoomCode, String equipRoomName, String lineCode,
                                                String position1Code, String position1Name, String subjectCode);


}
