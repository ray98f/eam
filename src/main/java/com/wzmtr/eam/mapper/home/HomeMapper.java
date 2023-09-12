package com.wzmtr.eam.mapper.home;

import com.wzmtr.eam.bo.HomeCountBO;
import org.apache.ibatis.annotations.Mapper;

/**
 * Author: Li.Wang
 * Date: 2023/9/12 15:03
 */
@Mapper
public interface HomeMapper {
    Integer count(HomeCountBO countBO);

    Integer queryForIndex(HomeCountBO countBO);
}
