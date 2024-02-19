package com.wzmtr.eam.impl.dict;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.entity.DictionariesType;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.mapper.dict.DictionariesTypeMapper;
import com.wzmtr.eam.service.dict.IDictionariesTypeService;
import com.wzmtr.eam.utils.DateUtils;
import com.wzmtr.eam.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictionariesTypeServiceImpl implements IDictionariesTypeService {

    @Autowired
    private DictionariesTypeMapper dictionariesTypeMapper;

    @Override
    public Page<DictionariesType> page(String name, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return dictionariesTypeMapper.page(pageReqDTO.of(), name);
    }

    @Override
    public DictionariesType detail(Integer id) {
        return dictionariesTypeMapper.detail(id);
    }

    @Override
    public void add(DictionariesType dictionariesType) {
        dictionariesType.setRecCreator(TokenUtils.getCurrentPersonId());
        dictionariesType.setRecCreateTime(DateUtils.getCurrentTime());
        dictionariesTypeMapper.add(dictionariesType);
    }

    @Override
    public void modify(DictionariesType dictionariesType) {
        dictionariesType.setRecRevisor(TokenUtils.getCurrentPersonId());
        dictionariesType.setRecReviseTime(DateUtils.getCurrentTime());
        dictionariesTypeMapper.modify(dictionariesType);
    }

    @Override
    public void delete(List<String> ids) {
        if (ids != null && !ids.isEmpty()) {
            dictionariesTypeMapper.delete(ids, TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
        }
    }

}
