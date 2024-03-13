package com.wzmtr.eam.mapper.detection;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.detection.OtherEquipTypeReqDTO;
import com.wzmtr.eam.dto.res.detection.OtherEquipTypeResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 其他设备管理-其他设备分类管理
 * @author  Ray
 * @version 1.0
 * @date 2024/02/02
 */
@Mapper
@Repository
public interface OtherEquipTypeMapper {

    /**
     * 分页获取其他设备分类
     * @param page 分页参数
     * @param typeCode 分类编号
     * @param typeName 分类名称
     * @return 其他设备分类列表
     */
    Page<OtherEquipTypeResDTO> pageOtherEquipType(Page<OtherEquipTypeResDTO> page, String typeCode, String typeName);

    /**
     * 获取其他设备分类详情
     * @param id id
     * @return 其他设备分类详情
     */
    OtherEquipTypeResDTO getOtherEquipTypeDetail(String id);

    /**
     * 导入其他设备分类
     * @param list 列表信息
     */
    void importOtherEquipType(List<OtherEquipTypeReqDTO> list);

    /**
     * 获取当前最大的分类编号
     * @return 最大分类编号
     */
    String getMaxTypeCode();

    /**
     * 新增其他设备分类
     * @param otherEquipTypeReqDTO 其他设备分类信息
     */
    void addOtherEquipType(OtherEquipTypeReqDTO otherEquipTypeReqDTO);

    /**
     * 修改其他设备分类
     * @param otherEquipTypeReqDTO 其他设备分类信息
     */
    void modifyOtherEquipType(OtherEquipTypeReqDTO otherEquipTypeReqDTO);

    /**
     * 删除其他设备分类
     * @param ids ids
     * @param userId 用户id
     * @param time 时间
     */
    void deleteOtherEquipType(List<String> ids, String userId, String time);

    /**
     * 删除其他设备分类
     * @param typeCode 分类编号
     * @param typeName 分类名称
     * @param ids ids
     * @return 其他设备分类列表
     */
    List<OtherEquipTypeResDTO> exportOtherEquipType(String typeCode, String typeName, List<String> ids);

    /**
     * 获取其他设备分类列表
     * @param typeCode 分类编号
     * @param typeName 分类名称
     * @return 其他设备分类列表
     */
    List<OtherEquipTypeResDTO> listOtherEquipType(String typeCode, String typeName);
}
