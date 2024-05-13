package com.wzmtr.eam.mapper.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.basic.OrgRegionReqDTO;
import com.wzmtr.eam.dto.res.basic.OrgRegionResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 基础管理-组织机构位置
 * @author  Ray
 * @version 1.0
 * @date 2024/03/21
 */
@Mapper
@Repository
public interface OrgRegionMapper {

    /**
     * 获取组织机构位置列表
     * @param page 组织机构
     * @param orgCodes 位置编号
     * @param regionCode 分页参数
     * @return 组织机构位置列表
     */
    Page<OrgRegionResDTO> pageOrgRegion(Page<OrgRegionResDTO> page, List<List<String>> orgCodes, String regionCode);

    /**
     * 根据专业获取启用的组织机构列表
     * @param regionCode 位置编号
     * @return 启用的组织机构列表
     */
    List<OrgRegionResDTO> listUseOrgRegion(String regionCode);

    /**
     * 获取组织机构位置详情
     * @param id id
     * @return 组织机构位置详情
     */
    OrgRegionResDTO getOrgRegionDetail(String id);

    /**
     * 判断织机构位置是否已存在
     * @param orgRegionReqDTO 织机构位置参数
     * @return 是否已存在
     */
    Integer selectOrgRegionIsExist(OrgRegionReqDTO orgRegionReqDTO);

    /**
     * 新增组织机构位置
     * @param orgRegionReqDTO 织机构位置参数
     */
    void addOrgRegion(OrgRegionReqDTO orgRegionReqDTO);

    /**
     * 修改组织机构位置
     * @param orgRegionReqDTO 织机构位置参数
     */
    void modifyOrgRegion(OrgRegionReqDTO orgRegionReqDTO);

    /**
     * 删除组织机构位置
     * @param ids ids
     * @param userId 人员id
     * @param time 时间
     */
    void deleteOrgRegion(List<String> ids, String userId, String time);

    /**
     * 导出组织机构位置
     * @param ids ids
     * @return 组织机构位置列表
     */
    List<OrgRegionResDTO> listOrgRegion(List<String> ids);
}
