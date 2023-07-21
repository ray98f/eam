package com.wzmtr.eam.mapper.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.OrgLineReqDTO;
import com.wzmtr.eam.dto.res.OrgLineResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface OrgLineMapper {

    /**
     * 获取组织机构线别列表-分页
     *
     * @param page
     * @param orgCodes
     * @param lineCode
     * @return
     */
    Page<OrgLineResDTO> pageOrgLine(Page<OrgLineResDTO> page, List<List<String>> orgCodes, String lineCode);

    /**
     * 获取组织机构线别详情
     * @param id
     * @return
     */
    OrgLineResDTO getOrgLineDetail(String id);

    /**
     * 查询组织机构线别是否已存在
     *
     * @param orgLineReqDTO
     * @return
     */
    Integer selectOrgLineIsExist(OrgLineReqDTO orgLineReqDTO);

    /**
     * 新增组织机构线别
     *
     * @param orgLineReqDTO
     */
    void addOrgLine(OrgLineReqDTO orgLineReqDTO);

    /**
     * 修改组织机构线别
     *
     * @param orgLineReqDTO
     */
    void modifyOrgLine(OrgLineReqDTO orgLineReqDTO);

    /**
     * 删除组织机构线别
     *
     * @param ids
     * @param userId
     * @param time
     */
    void deleteOrgLine(List<String> ids, String userId, String time);

    /**
     * 获取组织机构线别列表
     *
     * @param orgCodes
     * @param lineCode
     * @return
     */
    List<OrgLineResDTO> listOrgLine(List<List<String>> orgCodes, String lineCode);


}
