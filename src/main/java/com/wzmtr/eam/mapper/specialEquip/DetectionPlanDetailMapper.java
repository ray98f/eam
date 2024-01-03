package com.wzmtr.eam.mapper.specialEquip;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wzmtr.eam.dataobject.DetectionPlanDetailDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author: Li.Wang
 * Date: 2023/12/21 14:52
 */
@Mapper
@Repository
public interface DetectionPlanDetailMapper extends BaseMapper<DetectionPlanDetailDO> {
}
