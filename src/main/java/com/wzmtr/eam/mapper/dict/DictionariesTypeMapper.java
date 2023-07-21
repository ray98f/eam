package com.wzmtr.eam.mapper.dict;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.entity.DictionariesType;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DictionariesTypeMapper {

    Page<DictionariesType> page(Page<DictionariesType> page, String name);

    DictionariesType detail(Integer id);

    void add(DictionariesType dictionariesType);

    void modify(DictionariesType dictionariesType);

    void delete(List<String> ids, String userId, String time);
}
