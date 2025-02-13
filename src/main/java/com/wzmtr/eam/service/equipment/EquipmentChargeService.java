package com.wzmtr.eam.service.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.equipment.EquipmentChargeReqDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentChargeResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface EquipmentChargeService {

    Page<EquipmentChargeResDTO> listEquipmentCharge(String equipCode, String equipName, String chargeDate, String position1Name,
                                                    String subjectCode, String systemCode, String equipTypeCode, PageReqDTO pageReqDTO);

    EquipmentChargeResDTO getEquipmentChargeDetail(String id);

    void addEquipmentCharge(EquipmentChargeReqDTO equipmentChargeReqDTO);

    void modifyEquipmentCharge(EquipmentChargeReqDTO equipmentChargeReqDTO);

    void deleteEquipmentCharge(BaseIdsEntity baseIdsEntity);

    void exportEquipmentCharge(List<String> ids, HttpServletResponse response) throws IOException;
}
