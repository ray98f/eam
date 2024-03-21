package com.wzmtr.eam.service.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.basic.OrgRegionReqDTO;
import com.wzmtr.eam.dto.res.basic.OrgRegionResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 基础管理-组织机构位置
 * @author  Ray
 * @version 1.0
 * @date 2024/03/21
 */
public interface OrgRegionService {

    /**
     * 获取组织机构位置列表
     * @param orgCode 组织机构
     * @param regionCode 位置编号
     * @param pageReqDTO 分页参数
     * @return 组织机构位置列表
     */
    Page<OrgRegionResDTO> listOrgRegion(String orgCode, String regionCode, PageReqDTO pageReqDTO);

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
     * @param baseIdsEntity ids
     */
    void deleteOrgRegion(BaseIdsEntity baseIdsEntity);

    /**
     * 导出组织机构位置
     * @param ids ids
     * @param response response
     * @throws IOException 流异常
     */
    void exportOrgRegion(List<String> ids, HttpServletResponse response) throws IOException;
}
