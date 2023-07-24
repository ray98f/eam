package com.wzmtr.eam.mapper.common;

import com.wzmtr.eam.shiro.model.TPerson;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PersonMapper {

    TPerson searchPersonByNo(@Param("no") String no);

}
