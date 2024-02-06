package com.wzmtr.eam.service.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.basic.BomReqDTO;
import com.wzmtr.eam.dto.res.basic.BomResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * Bom结构管理
 * @author  Ray
 * @version 1.0
 * @date 2024/02/04
 */
public interface BomService {

    /**
     * 获取Bom结构列表
     * @param parentId 父级id
     * @param code 编码
     * @param name 名称
     * @param pageReqDTO 分页参数
     * @return Bom结构列表
     */
    Page<BomResDTO> listBom(String parentId, String code, String name, PageReqDTO pageReqDTO);

    /**
     * 获取Bom结构详情
     * @param id id
     * @return Bom结构详情
     */
    BomResDTO getBomDetail(String id);

    /**
     * 新增Bom结构
     * @param bomReqDTO bom结构数据
     */
    void addBom(BomReqDTO bomReqDTO);

    /**
     * 修改Bom结构
     * @param bomReqDTO bom结构数据
     */
    void modifyBom(BomReqDTO bomReqDTO);

    /**
     * 删除Bom结构
     * @param baseIdsEntity ids
     */
    void deleteBom(BaseIdsEntity baseIdsEntity);

    /**
     * 导入Bom结构
     * @param file 文件
     */
    void importBom(MultipartFile file);

    /**
     * 导入车辆与Bom关联关系
     * @param file 文件
     */
    void importBomTrain(MultipartFile file);
}
