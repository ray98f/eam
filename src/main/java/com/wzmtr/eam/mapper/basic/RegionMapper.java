package com.wzmtr.eam.mapper.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.EquipmentCategoryReqDTO;
import com.wzmtr.eam.dto.req.RegionReqDTO;
import com.wzmtr.eam.dto.res.RegionResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface RegionMapper {

    /**
     * 获取位置分类列表-分页
     *
     * @param page
     * @param name
     * @param code
     * @param parentId
     * @return
     */
    Page<RegionResDTO> pageRegion(Page<RegionResDTO> page, String name, String code, String parentId);

    /**
     * 获取位置分类根列表
     *
     * @return
     */
    List<RegionResDTO> listRegionRootList();

    /**
     * 获取位置分类子列表
     *
     * @return
     */
    List<RegionResDTO> listRegionBodyList();

    /**
     * 获取位置分类详情
     * @param id
     * @return
     */
    RegionResDTO getRegionDetail(String id);

    /**
     * 查询位置分类是否已存在
     *
     * @param regionReqDTO
     * @return
     */
    Integer selectRegionIsExist(RegionReqDTO regionReqDTO);

    /**
     * 新增位置分类
     *
     * @param regionReqDTO
     */
    void addRegion(RegionReqDTO regionReqDTO);

    /**
     * 修改位置分类
     *
     * @param regionReqDTO
     */
    void modifyRegion(RegionReqDTO regionReqDTO);

    /**
     * 删除位置分类
     *
     * @param ids
     * @param userId
     * @param time
     */
    void deleteRegion(List<String> ids, String userId, String time);

    /**
     * 获取位置分类列表
     *
     * @param name
     * @param code
     * @param parentId
     * @return
     */
    List<RegionResDTO> listRegion(String name, String code, String parentId);

}
