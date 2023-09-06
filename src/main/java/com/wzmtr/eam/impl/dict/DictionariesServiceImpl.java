package com.wzmtr.eam.impl.dict;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.service.dict.IDictionariesService;
import com.wzmtr.eam.entity.Dictionaries;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.mapper.dict.DictionariesMapper;
import com.wzmtr.eam.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class DictionariesServiceImpl implements IDictionariesService {

    @Autowired
    private DictionariesMapper dictionariesMapper;

    @Override
    public Page<Dictionaries> page(String itemName, String itemCode, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return dictionariesMapper.page(pageReqDTO.of(), itemName, itemCode);
    }

    @Override
    public List<Dictionaries> list(String codesetCode) {
        return dictionariesMapper.list(codesetCode);
    }

    @Override
    public Dictionaries detail(String itemCode) {
        return dictionariesMapper.detail(itemCode);
    }
    @Override
    public Dictionaries queryOneByItemCodeAndCodesetCode(String itemCode,String codesetCode) {
        return dictionariesMapper.queryOneByItemCodeAndCodesetCode(itemCode,codesetCode);
    }

    @Override
    public void add(Dictionaries dictionaries) {
        dictionaries.setRecCreator(TokenUtil.getCurrentPersonId());
        dictionaries.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        dictionariesMapper.add(dictionaries);
    }

    @Override
    public void modify(Dictionaries dictionaries) {
        dictionaries.setRecRevisor(TokenUtil.getCurrentPersonId());
        dictionaries.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        dictionariesMapper.modify(dictionaries);
    }

    @Override
    public void delete(List<String> ids) {
        if (ids != null && !ids.isEmpty()) {
            dictionariesMapper.delete(ids, TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        }
    }


}
