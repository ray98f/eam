package com.wzmtr.eam.service.mea;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.MeaListReqDTO;
import com.wzmtr.eam.dto.res.*;
import com.wzmtr.eam.entity.PageReqDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface MeaService {

    Page<MeaResDTO> pageMea(MeaListReqDTO meaListReqDTO, PageReqDTO pageReqDTO);

    MeaResDTO getMeaDetail(String id);

    void importMea(MultipartFile file);

    void exportMea(MeaListReqDTO meaListReqDTO, HttpServletResponse response);

    Page<SubmissionRecordDetailResDTO> pageMeaRecord(String equipCode, PageReqDTO pageReqDTO);
    
}
