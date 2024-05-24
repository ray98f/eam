package com.wzmtr.eam.mapper.home;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.home.CommonFuncReqDTO;
import com.wzmtr.eam.dto.res.home.CommonFuncResDTO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 常用功能管理
 * @author  Ray
 * @version 1.0
 * @date 2024/05/24
 */
@Mapper
public interface CommonFuncMapper {

    /**
     * 获取用户使用的常用功能
     * @param page 分页信息
     * @param userId 用户id
     * @return 常用功能
     */
    Page<CommonFuncResDTO> listUse(Page<CommonFuncResDTO> page, String userId);

    /**
     * 获取全部常用功能
     * @param page 分页信息
     * @param userId 用户id
     * @return 常用功能
     */
    Page<CommonFuncResDTO> listAll(Page<CommonFuncResDTO> page, String userId);

    /**
     * 移除用户所有常用功能
     * @param userId 用户id
     */
    void removeAll(String userId);

    /**
     * 编辑用户常用功能
     * @param req 常用功能编辑传参
     */
    void insert(CommonFuncReqDTO req);
}
