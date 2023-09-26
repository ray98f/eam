package com.wzmtr.eam.service.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.equipment.PartFaultReqDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentQrResDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentResDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentTreeResDTO;
import com.wzmtr.eam.dto.res.basic.RegionResDTO;
import com.wzmtr.eam.dto.res.equipment.PartReplaceResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulOrderDetailResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;

public interface EquipmentService {

    List<RegionResDTO> listTrainRegion();

    EquipmentTreeResDTO listEquipmentTree(String lineCode, String regionCode, String recId, String parentNodeRecId, String equipmentCategoryCode);

    Page<EquipmentResDTO> pageEquipment(String equipCode, String equipName, String useLineNo, String useSegNo, String position1Code, String majorCode,
                                        String systemCode, String equipTypeCode, String brand, String startTime, String endTime, String manufacture, PageReqDTO pageReqDTO);

    EquipmentResDTO getEquipmentDetail(String id);

    void importEquipment(MultipartFile file);

    void exportEquipment(List<String> ids, HttpServletResponse response);

    List<EquipmentQrResDTO> generateQr(BaseIdsEntity baseIdsEntity) throws ParseException;

    Page<OverhaulOrderDetailResDTO> listOverhaul(String equipCode, PageReqDTO pageReqDTO);

    Page<FaultDetailResDTO> listFault(String equipCode, PageReqDTO pageReqDTO);

    Page<PartReplaceResDTO> listPartReplace(String equipCode, PageReqDTO pageReqDTO);
}
