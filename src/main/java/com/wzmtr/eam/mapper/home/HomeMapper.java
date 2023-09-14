package com.wzmtr.eam.mapper.home;

import com.wzmtr.eam.bizobject.HomeCountBO;
import com.wzmtr.eam.dto.res.home.ShowAResDTO;
import com.wzmtr.eam.dto.res.home.ShowBCResDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/9/12 15:03
 */
@Mapper
public interface HomeMapper {
    Integer count(HomeCountBO countBO);

    Integer queryForIndex(HomeCountBO countBO);
    List<ShowBCResDTO> queryC();

    List<ShowBCResDTO> queryB();
    List<ShowAResDTO> queryA();
}
