package com.wzmtr.eam.mapper.fault;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wzmtr.eam.dataobject.FaultInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Author: Li.Wang
 * Date: 2023/8/15 20:00
 */
@Mapper
@Repository
public interface FaultInfoMapper extends BaseMapper<FaultInfoDO> {

}
