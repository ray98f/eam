package com.wzmtr.eam.service.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.basic.EquipmentCategoryReqDTO;
import com.wzmtr.eam.dto.res.basic.EquipmentCategoryModuleResDTO;
import com.wzmtr.eam.dto.res.basic.EquipmentCategoryResDTO;
import com.wzmtr.eam.dto.res.basic.EquipmentCategorySubclassResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * 获取需要使用设备小类的专业列表
     * @return 专业列表
     */
    List<String> getSubclassMajor();

    /**
     * 获取设备分类绑定的部件列表
     * 根节点为模块
     * @param majorCode 专业编码
     * @param systemCode 系统编码
     * @param equipTypeCode 设备分类编码
     * @return 设备分类绑定的部件列表
     */
    List<EquipmentCategoryModuleResDTO> listEquipmentCategoryModulePart(String majorCode,
                                                                        String systemCode,
                                                                        String equipTypeCode);

    /**
     * 获取设备分类绑定的部件列表
     * 根节点为设备小类
     * @param majorCode 专业编码
     * @param systemCode 系统编码
     * @param equipTypeCode 设备分类编码
     * @return 设备分类绑定的部件列表
     */
    List<EquipmentCategorySubclassResDTO> listEquipmentCategorySubclassPart(String majorCode,
                                                                            String systemCode,
                                                                            String equipTypeCode);

    /**
     * 导入设备分类绑定的部件列表
     * @param file 文件
     */
    void importEquipmentCategoryPart(MultipartFile file);
}
