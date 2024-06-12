package com.wzmtr.eam.impl.dict;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.config.MinioConfig;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.entity.Dictionaries;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.dict.DictionariesMapper;
import com.wzmtr.eam.service.dict.IDictionariesService;
import com.wzmtr.eam.utils.DateUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictionariesServiceImpl implements IDictionariesService {

    @Autowired
    private DictionariesMapper dictionariesMapper;
    @Autowired
    private MinioConfig minioConfig;

    @Override
    public Page<Dictionaries> page(String itemName, String itemCode, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return dictionariesMapper.page(pageReqDTO.of(), itemName, itemCode);
    }

    @Override
    public List<Dictionaries> list(String codesetCode, String itemCode, String status) {
        return dictionariesMapper.list(codesetCode, itemCode, status);
    }

    @Override
    public Dictionaries detail(String itemCode) {
        return dictionariesMapper.detail(itemCode);
    }
    @Override
    public Dictionaries queryOneByItemCodeAndCodesetCode(String codesetCode, String itemCode) {
        return dictionariesMapper.queryOneByItemCodeAndCodesetCode(codesetCode, itemCode);
    }

    @Override
    public void add(Dictionaries dictionaries) {
        dictionaries.setRecCreator(TokenUtils.getCurrentPersonId());
        dictionaries.setRecCreateTime(DateUtils.getCurrentTime());
        dictionariesMapper.add(dictionaries);
    }

    @Override
    public void modify(Dictionaries dictionaries) {
        dictionaries.setRecRevisor(TokenUtils.getCurrentPersonId());
        dictionaries.setRecReviseTime(DateUtils.getCurrentTime());
        dictionariesMapper.modify(dictionaries);
    }

    @Override
    public void delete(List<String> ids) {
        if (ids != null && !ids.isEmpty()) {
            dictionariesMapper.delete(ids, TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
        }
    }

    @Override
    public String getImportTemplate(String code) {
        Dictionaries dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_IMPORT_TEMPLATE, code);
        if (StringUtils.isNull(dictionaries)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        return minioConfig.getImgPath() + dictionaries.getItemEname();
    }

}
