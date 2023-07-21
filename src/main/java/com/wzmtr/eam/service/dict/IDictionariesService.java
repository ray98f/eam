package com.wzmtr.eam.service.dict;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.entity.Dictionaries;
import com.wzmtr.eam.entity.PageReqDTO;

import java.util.List;


public interface IDictionariesService {
    
    Page<Dictionaries> page(String itemName, String itemCode, PageReqDTO pageReqDTO);

    List<Dictionaries> list(String codesetCode);

    Dictionaries detail(String itemCode);

    void add(Dictionaries dictionaries);

    void modify(Dictionaries dictionaries);

    void delete(List<String> ids);
}
