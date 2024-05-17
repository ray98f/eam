package com.wzmtr.eam.mapper.common;

import com.wzmtr.eam.shiro.model.Person;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PersonMapper {

    Person searchPersonByNo(@Param("no") String no);

    Person searchLeaderByMajorAndPositionAndRole(String major, String position, String roleCode);

    Person searchLeaderByDeptAndRole(String deptCode, String roleCode);

    List<Person> listLeader(String major, String position, String roleCode);

}
