package com.wzmtr.eam.service.mea;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.mea.MeaListReqDTO;
import com.wzmtr.eam.dto.req.mea.MeaReqDTO;
import com.wzmtr.eam.dto.res.mea.MeaResDTO;
import com.wzmtr.eam.dto.res.mea.SubmissionRecordDetailResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface MeaService {

    Page<MeaResDTO> pageMea(MeaListReqDTO meaListReqDTO, PageReqDTO pageReqDTO);

    MeaResDTO getMeaDetail(String id);

    void importMea(MultipartFile file);

    /**
     * 新增计量器具
     * @param meaReqDTO 计量器具参数
     */
    void addMea(MeaReqDTO meaReqDTO);

    void exportMea(List<String> ids, HttpServletResponse response) throws IOException;

    Page<SubmissionRecordDetailResDTO> pageMeaRecord(String equipCode, PageReqDTO pageReqDTO);
    
}
