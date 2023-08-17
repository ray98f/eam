package com.wzmtr.eam.mapper.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.FaultReqDTO;
import com.wzmtr.eam.dto.res.FaultResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface FaultQueryMapper {

    /**
     * 获取故障库列表-分页
     *
     * @param page
     * @param code
     * @param type
     * @param lineCode
     * @param equipmentCategoryCode
     * @return
     */
    Page<FaultResDTO> pageFault(Page<FaultResDTO> page, String code, Integer type, String lineCode, String equipmentCategoryCode);

    /**
     * 获取故障库详情
     * @param id
     * @return
     */
    FaultResDTO getFaultDetail(String id);

    /**
     * 查询故障库是否已存在
     *
     * @param faultReqDTO
     * @return
     */
    Integer selectFaultIsExist(FaultReqDTO faultReqDTO);

    /**
     * 新增故障库
     *
     * @param faultReqDTO
     */
    void addFault(FaultReqDTO faultReqDTO);

    /**
     * 修改故障库
     *
     * @param faultReqDTO
     */
    void modifyFault(FaultReqDTO faultReqDTO);

    /**
     * 删除故障库
     *
     * @param ids
     * @param userId
     * @param time
     */
    void deleteFault(List<String> ids, String userId, String time);

    /**
     * 获取故障库列表
     *
     * @param code
     * @param type
     * @param lineCode
     * @param equipmentCategoryCode
     * @return
     */
    List<FaultResDTO> listFault(String code, Integer type, String lineCode, String equipmentCategoryCode);


}
