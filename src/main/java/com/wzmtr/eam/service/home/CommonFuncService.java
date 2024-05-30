package com.wzmtr.eam.service.home;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.home.CommonFuncReqDTO;
import com.wzmtr.eam.dto.res.home.CommonFuncResDTO;
import com.wzmtr.eam.entity.PageReqDTO;

/**
 * 常用功能管理
 * @author  Ray
 * @version 1.0
 * @date 2024/05/24
 */
public interface CommonFuncService {

    /**
     * 获取用户使用的常用功能
     * @param pageReqDTO 分页信息
     * @return 常用功能
     */
    Page<CommonFuncResDTO> listUse(PageReqDTO pageReqDTO);

    /**
     * 获取全部常用功能
     * @param pageReqDTO 分页信息
     * @return 常用功能
     */
    Page<CommonFuncResDTO> listAll(PageReqDTO pageReqDTO);

    /**
     * 编辑用户常用功能
     * @param req 常用功能编辑传参
     */
    void modify(CommonFuncReqDTO req);
}
