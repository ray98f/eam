package com.wzmtr.eam.impl.home;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.home.CommonFuncReqDTO;
import com.wzmtr.eam.dto.res.home.CommonFuncResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.mapper.home.CommonFuncMapper;
import com.wzmtr.eam.service.home.CommonFuncService;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 常用功能管理
 * @author  Ray
 * @version 1.0
 * @date 2024/05/24
 */
@Service
@Slf4j
public class CommonFuncServiceImpl implements CommonFuncService {
    @Autowired
    private CommonFuncMapper commonFuncMapper;

    @Override
    public Page<CommonFuncResDTO> listUse(PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return commonFuncMapper.listUse(pageReqDTO.of(), TokenUtils.getCurrentPersonId());
    }

    @Override
    public Page<CommonFuncResDTO> listAll(PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return commonFuncMapper.listAll(pageReqDTO.of(), TokenUtils.getCurrentPersonId());
    }

    @Override
    public void modify(CommonFuncReqDTO req) {
        if (StringUtils.isNotEmpty(req.getUserCommonFuncList())) {
            commonFuncMapper.removeAll(TokenUtils.getCurrentPersonId());
            req.setUserId(TokenUtils.getCurrentPersonId());
            commonFuncMapper.insert(req);
        }
    }
}
