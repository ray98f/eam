package com.wzmtr.eam.service.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.res.EquipmentResDTO;
import com.wzmtr.eam.dto.res.EquipmentTreeResDTO;
import com.wzmtr.eam.dto.res.RegionResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestParam;
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

    void exportEquipment(String equipCode, String equipName, String useLineNo, String useSegNo, String position1Code, String majorCode,
                         String systemCode, String equipTypeCode, String brand, String startTime, String endTime, String manufacture, HttpServletResponse response);

    String generateQr(String id) throws ParseException;
}
