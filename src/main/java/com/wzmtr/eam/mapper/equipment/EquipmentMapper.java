package com.wzmtr.eam.mapper.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.EquipmentReqDTO;
import com.wzmtr.eam.dto.req.EquipmentSiftReqDTO;
import com.wzmtr.eam.dto.req.UnitCodeReqDTO;
import com.wzmtr.eam.dto.res.EquipmentCategoryResDTO;
import com.wzmtr.eam.dto.res.EquipmentResDTO;
import com.wzmtr.eam.dto.res.RegionResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface EquipmentMapper {

    List<RegionResDTO> listTrainRegion();

    List<RegionResDTO> listLine();

    List<RegionResDTO> listRegion(String lineCode, String regionCode, String recId);

    List<RegionResDTO> listCarRegion(String lineCode, String recId);

    List<EquipmentCategoryResDTO> listEquipmentCategory(String equipmentCategoryCode, String lineCode, String recId, String regionCode);

    Page<EquipmentResDTO> pageEquipment(Page<EquipmentResDTO> page, String equipCode, String equipName, String useLineNo, String useSegNo, String position1Code, String majorCode,
                                        String systemCode, String equipTypeCode, String brand, String startTime, String endTime, String manufacture);

    EquipmentResDTO getEquipmentDetail(String id);

    String getMaxCode(Integer type);

    void insertUnitCode(UnitCodeReqDTO unitCodeReqDTO);

    void importEquipment(List<EquipmentReqDTO> list);

    void insertEquipment(EquipmentReqDTO equipmentReqDTO);

    List<EquipmentResDTO> listEquipment(String equipCode, String equipName, String useLineNo, String useSegNo, String position1Code, String majorCode,
                                        String systemCode, String equipTypeCode, String brand, String startTime, String endTime, String manufacture);

    List<EquipmentResDTO> siftEquipment(EquipmentSiftReqDTO equipmentSiftReqDTO);

    void updateEquipment(EquipmentReqDTO equipmentReqDTO);

    List<EquipmentResDTO> selectByEquipName(String equipName);
}
