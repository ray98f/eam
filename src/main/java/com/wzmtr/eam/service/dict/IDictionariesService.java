package com.wzmtr.eam.service.dict;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.entity.Dictionaries;
import com.wzmtr.eam.entity.PageReqDTO;

import java.util.List;


public interface IDictionariesService {
    
    Page<Dictionaries> page(String itemName, String itemCode, PageReqDTO pageReqDTO);

    List<Dictionaries> list(String codesetCode, String itemCode, String status);

    Dictionaries detail(String itemCode);

    /**
     * @return State=1的字典
     */
    Dictionaries queryOneByItemCodeAndCodesetCode(String codesetCode, String itemCode);

    void add(Dictionaries dictionaries);

    void modify(Dictionaries dictionaries);

    void delete(List<String> ids);

    /**
     * 根据code获取导入模板链接
     * @param code code
     * @return 导入模板链接
     */
    String getImportTemplate(String code);
}
