package com.wzmtr.eam.service.special.equip;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.special.equip.SpecialEquipTypeReqDTO;
import com.wzmtr.eam.dto.res.special.equip.SpecialEquipTypeResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 特种设备管理-特种设备分类管理
 * @author  Ray
 * @version 1.0
 * @date 2024/01/30
 */
public interface SpecialEquipTypeService {

    /**
     * 分页获取特种设备分类
     * @param typeCode 分类编号
     * @param typeName 分类名称
     * @param pageReqDTO 分页参数
     * @return 特种设备分类列表
     */
    Page<SpecialEquipTypeResDTO> pageSpecialEquipType(String typeCode, String typeName, PageReqDTO pageReqDTO);

    /**
     * 获取特种设备分类详情
     * @param id id
     * @return 特种设备分类详情
     */
    SpecialEquipTypeResDTO getSpecialEquipTypeDetail(String id);

    /**
     * 导入特种设备分类
     * @param file 文件
     */
    void importSpecialEquipType(MultipartFile file);

    /**
     * 新增特种设备分类
     * @param specialEquipTypeReqDTO 特种设备分类信息
     */
    void addSpecialEquipType(SpecialEquipTypeReqDTO specialEquipTypeReqDTO);

    /**
     * 修改特种设备分类
     * @param specialEquipTypeReqDTO 特种设备分类信息
     */
    void modifySpecialEquipType(SpecialEquipTypeReqDTO specialEquipTypeReqDTO);

    /**
     * 删除特种设备分类
     * @param ids ids
     */
    void deleteSpecialEquipType(List<String> ids);

    /**
     * 删除特种设备分类
     * @param ids ids
     * @param response response
     * @throws IOException 流异常
     */
    void exportSpecialEquipType(List<String> ids, HttpServletResponse response) throws IOException;

    /**
     * 获取特种设备分类列表
     * @param typeCode 分类编号
     * @param typeName 分类名称
     * @return 特种设备分类列表
     */
    List<SpecialEquipTypeResDTO> listSpecialEquipType(String typeCode, String typeName);

}
