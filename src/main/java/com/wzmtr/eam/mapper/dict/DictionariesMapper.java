package com.wzmtr.eam.mapper.dict;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.entity.Dictionaries;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DictionariesMapper {

    Page<Dictionaries> page(Page<Dictionaries> page, String itemName, String itemCode);

    /**
     * @param codesetCode 必填
     * @param itemCode
     * @param status
     * @return
     */

    List<Dictionaries> list(String codesetCode, @Param("itemCode") String itemCode,@Param("status") String status);

    Dictionaries detail(String itemCode);

    void add(Dictionaries dictionaries);

    void modify(Dictionaries dictionaries);

    void delete(List<String> ids, String userId, String time);

    /**
     * 获取字典值
     * @param codesetCode 字典类型编号
     * @param itemCode 编号
     * @return 字典值
     */
    Dictionaries queryOneByItemCodeAndCodesetCode(String codesetCode,String itemCode);
}
