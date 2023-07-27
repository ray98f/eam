package com.wzmtr.eam.mapper.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.EquipmentChargeReqDTO;
import com.wzmtr.eam.dto.res.EquipmentChargeResDTO;
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
                                                    String position1Name, String subjectCode, String systemCode, String equipTypeCode);

    EquipmentChargeResDTO getEquipmentChargeDetail(String id);

    String selectEquipmentNameByCode(String equipCode);

    void addEquipmentCharge(EquipmentChargeReqDTO equipmentChargeReqDTO);

    void modifyEquipmentCharge(EquipmentChargeReqDTO equipmentChargeReqDTO);

    void deleteEquipmentCharge(List<String> ids, String userId, String time);

    List<EquipmentChargeResDTO> listEquipmentCharge(String equipCode, String equipName, String chargeDate,
                                                    String position1Name, String subjectCode, String systemCode, String equipTypeCode);

}
