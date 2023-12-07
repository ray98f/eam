package com.wzmtr.eam.service.specialEquip;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.specialEquip.SpecialEquipReqDTO;
import com.wzmtr.eam.dto.res.specialEquip.SpecialEquipHistoryResDTO;
import com.wzmtr.eam.dto.res.specialEquip.SpecialEquipResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface SpecialEquipService {
    
    Page<SpecialEquipResDTO> pageSpecialEquip(String equipCode, String equipName, String specialEquipCode, String factNo,
                                              String useLineNo, String position1Code, String specialEquipType, String equipStatus, PageReqDTO pageReqDTO);

    SpecialEquipResDTO getSpecialEquipDetail(String id);

    void importSpecialEquip(MultipartFile file);

    void modifySpecialEquip(SpecialEquipReqDTO specialEquipReqDTO);

    void exportSpecialEquip(String equipCode, String equipName, String specialEquipCode, String factNo,
                            String useLineNo, String position1Code, String specialEquipType, String equipStatus, HttpServletResponse response) throws IOException;

    Page<SpecialEquipHistoryResDTO> pageSpecialEquipHistory(String equipCode, PageReqDTO pageReqDTO);

    SpecialEquipHistoryResDTO getSpecialEquipHistoryDetail(String id);

    void exportSpecialEquipHistory(String equipCode, HttpServletResponse response) throws IOException;
}
