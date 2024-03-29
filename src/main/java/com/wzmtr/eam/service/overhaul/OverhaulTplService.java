package com.wzmtr.eam.service.overhaul;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.overhaul.OverhaulMaterialReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulTplDetailReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulTplReqDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulMaterialResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulTplDetailResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulTplResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface OverhaulTplService {

    Page<OverhaulTplResDTO> pageOverhaulTpl(String templateId, String templateName, String lineCode, String position1Code,
                                            String subjectCode, String systemCode, String equipTypeCode, PageReqDTO pageReqDTO);

    OverhaulTplResDTO getOverhaulTplDetail(String id);

    void addOverhaulTpl(OverhaulTplReqDTO overhaulTplReqDTO);

    void modifyOverhaulTpl(OverhaulTplReqDTO overhaulTplReqDTO);

    void deleteOverhaulTpl(BaseIdsEntity baseIdsEntity);

    void changeOverhaulTpl(OverhaulTplReqDTO overhaulTplReqDTO);

    void submitOverhaulTpl(OverhaulTplReqDTO overhaulTplReqDTO) throws Exception;

    void examineOverhaulTpl(OverhaulTplReqDTO overhaulTplReqDTO);

    /**
     * 检修模板导入
     * @param file 导入文件
     * @return 错误的导入数据
     */
    List<String> importOverhaulTpl(MultipartFile file);

    void exportOverhaulTpl(List<String> ids, HttpServletResponse response) throws IOException;

    Page<OverhaulTplDetailResDTO> pageOverhaulDetailTpl(String templateId, PageReqDTO pageReqDTO);

    OverhaulTplDetailResDTO getOverhaulTplDetailDetail(String id);

    void addOverhaulTplDetail(OverhaulTplDetailReqDTO overhaulTplDetailReqDTO);

    void modifyOverhaulTplDetail(OverhaulTplDetailReqDTO overhaulTplDetailReqDTO);

    void deleteOverhaulTplDetail(BaseIdsEntity baseIdsEntity);

    void exportOverhaulTplDetail(String templateId, HttpServletResponse response) throws IOException;

    Page<OverhaulMaterialResDTO> pageOverhaulMaterial(String templateId, PageReqDTO pageReqDTO);

    OverhaulMaterialResDTO getOverhaulMaterialDetail(String id);

    void addOverhaulMaterial(OverhaulMaterialReqDTO overhaulMaterialReqDTO);

    void modifyOverhaulMaterial(OverhaulMaterialReqDTO overhaulMaterialReqDTO);

    void deleteOverhaulMaterial(BaseIdsEntity baseIdsEntity);

    void exportOverhaulMaterial(String templateId, HttpServletResponse response) throws IOException;

}
