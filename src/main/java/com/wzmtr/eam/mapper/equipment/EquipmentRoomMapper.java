package com.wzmtr.eam.mapper.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.equipment.EquipmentRoomRelationReqDTO;
import com.wzmtr.eam.dto.req.equipment.EquipmentRoomReqDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentRoomResDTO;
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
                                                String position1Code, String position1Name, String subjectCode,List<String> majors);

    EquipmentRoomResDTO getEquipmentRoomDetail(String id);

    Integer selectEquipmentRoomIsExist(EquipmentRoomReqDTO equipmentRoomReqDTO);

    String getEquipRoomCodeMaxCode();

    String selectMaxEquipmentRoomCode();

    void addEquipmentRoom(EquipmentRoomReqDTO equipmentRoomReqDTO);

    void modifyEquipmentRoom(EquipmentRoomReqDTO equipmentRoomReqDTO);

    void deleteEquipmentRoom(List<String> ids, String userId, String time);

    List<EquipmentRoomResDTO> listEquipmentRoom(String equipRoomCode, String equipRoomName, String lineCode,
                                                String position1Code, String position1Name, String subjectCode);

    /**
     * 导出筛选设备房台账信息
     * @param ids ids
     * @return 设备房台账列表
     */
    List<EquipmentRoomResDTO> exportEquipmentRoom(List<String> ids);

    void insertRelationBatch(EquipmentRoomRelationReqDTO equipmentRoomRelationReqDTO);

    void deleteRelationBatch(EquipmentRoomRelationReqDTO equipmentRoomRelationReqDTO);


}
