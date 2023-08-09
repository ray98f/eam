package com.wzmtr.eam.service.overhaul;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.OverhaulTplDetailReqDTO;
import com.wzmtr.eam.dto.req.OverhaulTplReqDTO;
import com.wzmtr.eam.dto.res.OverhaulTplDetailResDTO;
import com.wzmtr.eam.dto.res.OverhaulTplResDTO;
import com.wzmtr.eam.dto.res.RegionResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;

public interface OverhaulTplService {

    Page<OverhaulTplResDTO> pageOverhaulTpl(String templateId, String templateName, String lineNo, String position1Code,
                                            String majorCode, String systemCode, String equipTypeCode, PageReqDTO pageReqDTO);

    OverhaulTplResDTO getOverhaulTplDetail(String id);

    void addOverhaulTpl(OverhaulTplReqDTO overhaulTplReqDTO);

    void modifyOverhaulTpl(OverhaulTplReqDTO overhaulTplReqDTO);

    void deleteOverhaulTpl(BaseIdsEntity baseIdsEntity);

    void changeOverhaulTpl(OverhaulTplReqDTO overhaulTplReqDTO);

    void submitOverhaulTpl(OverhaulTplReqDTO overhaulTplReqDTO);

    void importOverhaulTpl(MultipartFile file);

    void exportOverhaulTpl(String templateId, String templateName, String lineNo, String position1Code,
                           String majorCode, String systemCode, String equipTypeCode, HttpServletResponse response);

    Page<OverhaulTplDetailResDTO> pageOverhaulDetailTpl(String templateId, PageReqDTO pageReqDTO);

    OverhaulTplDetailResDTO getOverhaulTplDetailDetail(String id);

    void addOverhaulTplDetail(OverhaulTplDetailReqDTO overhaulTplDetailReqDTO);

    void modifyOverhaulTplDetail(OverhaulTplDetailReqDTO overhaulTplDetailReqDTO);

    void deleteOverhaulTplDetail(BaseIdsEntity baseIdsEntity);

    void exportOverhaulTplDetail(String templateId, HttpServletResponse response);

}
