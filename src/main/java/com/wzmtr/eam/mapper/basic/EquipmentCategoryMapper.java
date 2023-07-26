package com.wzmtr.eam.mapper.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.EquipmentCategoryReqDTO;
import com.wzmtr.eam.dto.res.EquipmentCategoryResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface EquipmentCategoryMapper {

    /**
     * 获取设备分类列表-分页
     *
     * @param page
     * @param name
     * @param code
     * @param parentId
     * @return
     */
    Page<EquipmentCategoryResDTO> pageEquipmentCategory(Page<EquipmentCategoryResDTO> page, String name, String code, String parentId);

    /**
     * 获取设备分类根列表
     *
     * @return
     */
    List<EquipmentCategoryResDTO> listEquipmentCategoryRootList();

    /**
     * 获取设备分类子列表
     *
     * @return
     */
    List<EquipmentCategoryResDTO> listEquipmentCategoryBodyList();

    /**
     * 获取设备分类详情
     *
     * @param id
     * @return
     */
    EquipmentCategoryResDTO getEquipmentCategoryDetail(String id);

    /**
     * 查询设备分类是否已存在
     *
     * @param equipmentCategoryReqDTO
     * @return
     */
    Integer selectEquipmentCategoryIsExist(EquipmentCategoryReqDTO equipmentCategoryReqDTO);

    /**
     * 新增设备分类
     *
     * @param equipmentCategoryReqDTO
     */
    void addEquipmentCategory(EquipmentCategoryReqDTO equipmentCategoryReqDTO);

    /**
     * 修改设备分类
     *
     * @param equipmentCategoryReqDTO
     */
    void modifyEquipmentCategory(EquipmentCategoryReqDTO equipmentCategoryReqDTO);

    /**
     * 删除设备分类
     *
     * @param ids
     * @param userId
     * @param time
     */
    void deleteEquipmentCategory(List<String> ids, String userId, String time);

    /**
     * 获取设备分类列表
     *
     * @param name
     * @param code
     * @param parentId
     * @return
     */
    List<EquipmentCategoryResDTO> listEquipmentCategory(String name, String code, String parentId);

    /**
     * 获取设备分类一级分类
     *
     * @return
     */
    List<EquipmentCategoryResDTO> getFirstEquipmentCategory();

    /**
     * 获取设备分类子级分类
     *
     * @return
     */
    List<EquipmentCategoryResDTO> getChildEquipmentCategory(String code);

    /**
     * 根据元素获取元素
     *
     * @param nodeCode
     * @param nodeName
     * @param nodeLevel
     * @return
     */
    EquipmentCategoryResDTO getIndexByIndex(String nodeCode, String nodeName, Integer nodeLevel);

}
