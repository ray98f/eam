package com.wzmtr.eam.mapper.bom;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wzmtr.eam.dataobject.BomDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Author: Li.Wang
 * Date: 2023/10/17 15:38
 */
@Mapper
@Repository
public interface BomMapper extends BaseMapper<BomDO> {
}
