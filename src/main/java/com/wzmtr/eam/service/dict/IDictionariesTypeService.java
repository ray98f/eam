package com.wzmtr.eam.service.dict;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.entity.DictionariesType;
import com.wzmtr.eam.entity.PageReqDTO;

import java.util.List;

public interface IDictionariesTypeService {

    Page<DictionariesType> page(String name, PageReqDTO pageReqDTO);

    DictionariesType detail(Integer id);

    void add(DictionariesType dictionariesType);

    void modify(DictionariesType dictionariesType);

    void delete(List<String> ids);
}
