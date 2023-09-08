package com.wzmtr.eam.service.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.equipment.PartReplaceReqDTO;
import com.wzmtr.eam.dto.res.equipment.PartReplaceBomResDTO;
import com.wzmtr.eam.dto.res.equipment.PartReplaceResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface PartReplaceService {

    Page<PartReplaceResDTO> pagePartReplace(String equipName, PageReqDTO pageReqDTO);

    PartReplaceResDTO getPartReplaceDetail(String id);

    List<PartReplaceBomResDTO> getBom(String equipCode, String node);

    void addPartReplace(PartReplaceReqDTO partReplaceReqDTO);

    void deletePartReplace(BaseIdsEntity baseIdsEntity);

    void importPartReplace(MultipartFile file);

    void exportPartReplace(String equipName, HttpServletResponse response);

}
