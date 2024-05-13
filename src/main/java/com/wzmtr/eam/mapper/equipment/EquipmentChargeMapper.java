package com.wzmtr.eam.mapper.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.equipment.EquipmentChargeReqDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentChargeResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface EquipmentChargeMapper {


    Page<EquipmentChargeResDTO> pageEquipmentCharge(Page<EquipmentChargeResDTO> page, String equipCode, String equipName, String chargeDate,
                                                    String position1Code, String subjectCode, String systemCode, String equipTypeCode,List<String> majors);

    EquipmentChargeResDTO getEquipmentChargeDetail(String id);

    String selectEquipmentNameByCode(String equipCode);

    void addEquipmentCharge(EquipmentChargeReqDTO equipmentChargeReqDTO);

    void modifyEquipmentCharge(EquipmentChargeReqDTO equipmentChargeReqDTO);

    void deleteEquipmentCharge(List<String> ids, String userId, String time);

    List<EquipmentChargeResDTO> listEquipmentCharge(List<String> ids);

}
