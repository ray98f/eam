package com.wzmtr.eam.mapper.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.basic.OrgTypeReqDTO;
import com.wzmtr.eam.dto.res.basic.OrgTypeResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface OrgTypeMapper {

    /**
     * 获取组织机构类别列表-分页
     *
     * @param page
     * @param orgCodes
     * @param orgType
     * @return
     */
    Page<OrgTypeResDTO> pageOrgType(Page<OrgTypeResDTO> page, List<List<String>> orgCodes, String orgType);

    /**
     * 获取组织机构类别详情
     * @param id
     * @return
     */
    OrgTypeResDTO getOrgTypeDetail(String id);

    /**
     * 查询组织机构类别是否已存在
     *
     * @param orgTypeReqDTO
     * @return
     */
    Integer selectOrgTypeIsExist(OrgTypeReqDTO orgTypeReqDTO);

    /**
     * 新增组织机构类别
     *
     * @param orgTypeReqDTO
     */
    void addOrgType(OrgTypeReqDTO orgTypeReqDTO);

    /**
     * 修改组织机构类别
     *
     * @param orgTypeReqDTO
     */
    void modifyOrgType(OrgTypeReqDTO orgTypeReqDTO);

    /**
     * 删除组织机构类别
     *
     * @param ids
     * @param userId
     * @param time
     */
    void deleteOrgType(List<String> ids, String userId, String time);

    /**
     * 获取组织机构类别列表
     * @param ids ids
     * @return 组织机构类别列表
     */
    List<OrgTypeResDTO> listOrgType(List<String> ids);

    /**
     * 基础数据部门名称同步
     */
    void syncSysOrgName();

    /**
     * 删除无效数据
     */
    void deleteNoneOrgCode();


}
