package com.wzmtr.eam.mapper.detection;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.detection.SpecialEquipTypeReqDTO;
import com.wzmtr.eam.dto.res.detection.SpecialEquipTypeResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 特种设备管理-特种设备分类管理
 * @author  Ray
 * @version 1.0
 * @date 2024/01/30
 */
@Mapper
@Repository
public interface SpecialEquipTypeMapper {

    /**
     * 分页获取特种设备分类
     * @param page 分页参数
     * @param typeCode 分类编号
     * @param typeName 分类名称
     * @return 特种设备分类列表
     */
    Page<SpecialEquipTypeResDTO> pageSpecialEquipType(Page<SpecialEquipTypeResDTO> page, String typeCode, String typeName);

    /**
     * 获取特种设备分类详情
     * @param id id
     * @return 特种设备分类详情
     */
    SpecialEquipTypeResDTO getSpecialEquipTypeDetail(String id);

    /**
     * 导入特种设备分类
     * @param list 列表信息
     */
    void importSpecialEquipType(List<SpecialEquipTypeReqDTO> list);

    /**
     * 获取当前最大的分类编号
     * @return 最大分类编号
     */
    String getMaxTypeCode();

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
     * @param userId 用户id
     * @param time 时间
     */
    void deleteSpecialEquipType(List<String> ids, String userId, String time);

    /**
     * 删除特种设备分类
     * @param ids ids
     * @return 特种设备分类列表
     */
    List<SpecialEquipTypeResDTO> exportSpecialEquipType(List<String> ids);

    /**
     * 获取特种设备分类列表
     * @param typeCode 分类编号
     * @param typeName 分类名称
     * @return 特种设备分类列表
     */
    List<SpecialEquipTypeResDTO> listSpecialEquipType(String typeCode, String typeName);

    /**
     * 根据特种设备分类参数获取特种设备分类详情
     * @param code 编号
     * @param name 名称
     * @return 特种设备分类详情
     */
    SpecialEquipTypeResDTO getSpecialEquipTypeDetailByType(String code, String name);
}
