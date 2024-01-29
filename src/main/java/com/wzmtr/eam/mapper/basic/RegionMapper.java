package com.wzmtr.eam.mapper.basic;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dataobject.RegionDO;
import com.wzmtr.eam.dto.req.basic.RegionReqDTO;
import com.wzmtr.eam.dto.res.basic.RegionResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author frp
 */
@Mapper
@Repository
public interface RegionMapper extends BaseMapper<RegionDO> {

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
     * @param ids ids
     * @return 位置分类列表
     */
    List<RegionResDTO> listRegion(List<String> ids);

    List<RegionResDTO> selectByNodeCodes(Set<String> nodeCodes);
}
