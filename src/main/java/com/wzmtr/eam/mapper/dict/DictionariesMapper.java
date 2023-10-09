package com.wzmtr.eam.mapper.dict;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.entity.Dictionaries;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DictionariesMapper {

    Page<Dictionaries> page(Page<Dictionaries> page, String itemName, String itemCode);

    List<Dictionaries> list(String codesetCode, String itemCode, String status);

    Dictionaries detail(String itemCode);

    void add(Dictionaries dictionaries);

    void modify(Dictionaries dictionaries);

    void delete(List<String> ids, String userId, String time);

    Dictionaries queryOneByItemCodeAndCodesetCode(String codesetCode,String itemCode);
}
