package com.wzmtr.eam.mapper.basic;

import com.wzmtr.eam.bizobject.PartBO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PartMapper {

   PartBO queryPartByFaultWorkNo(String FaultWorkNo);

}
