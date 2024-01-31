package com.wzmtr.eam.service.special.equip;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.special.equip.SpecialEquipReqDTO;
import com.wzmtr.eam.dto.res.special.equip.SpecialEquipHistoryResDTO;
import com.wzmtr.eam.dto.res.special.equip.SpecialEquipResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface SpecialEquipService {
    
    Page<SpecialEquipResDTO> pageSpecialEquip(String equipCode, String equipName, String specialEquipCode, String factNo,
                                              String useLineNo, String position1Code, String specialEquipType, String equipStatus, PageReqDTO pageReqDTO);

    SpecialEquipResDTO getSpecialEquipDetail(String id);

    void importSpecialEquip(MultipartFile file);

    void modifySpecialEquip(SpecialEquipReqDTO specialEquipReqDTO);

    void exportSpecialEquip(List<String> ids, HttpServletResponse response) throws IOException;

    Page<SpecialEquipHistoryResDTO> pageSpecialEquipHistory(String equipCode, PageReqDTO pageReqDTO);

    SpecialEquipHistoryResDTO getSpecialEquipHistoryDetail(String id);

    void exportSpecialEquipHistory(String equipCode, HttpServletResponse response) throws IOException;
}
