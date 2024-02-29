package com.wzmtr.eam.service.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.basic.EquipmentCategoryReqDTO;
import com.wzmtr.eam.dto.res.basic.EquipmentCategoryResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface EquipmentCategoryService {

    Page<EquipmentCategoryResDTO> listEquipmentCategory(String name, String code, String parentId, PageReqDTO pageReqDTO);

    List<EquipmentCategoryResDTO> listEquipmentCategoryTree();

    EquipmentCategoryResDTO getEquipmentCategoryDetail(String id);

    void addEquipmentCategory(EquipmentCategoryReqDTO equipmentCategoryReqDTO);

    void modifyEquipmentCategory(EquipmentCategoryReqDTO equipmentCategoryReqDTO);

    void deleteEquipmentCategory(BaseIdsEntity baseIdsEntity);

    void exportEquipmentCategory(List<String> ids, HttpServletResponse response) throws IOException;

    List<EquipmentCategoryResDTO> getFirstEquipmentCategory();

    List<EquipmentCategoryResDTO> getChildEquipmentCategory(String code);
}
