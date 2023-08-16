package com.wzmtr.eam.mapper.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.res.fault.ObjectResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Author: Li.Wang
 * Date: 2023/8/16 14:50
 */
@Mapper
@Repository
public interface ObjectMapper {


    Page<ObjectResDTO> queryObject(Page<Object> of, String objectCode, String majorCode, String objectName, String brand, String manufacture, String equipTypeCode, String systemCode, String position1Code, String manufactureDateStart, String manufactureDateEnd, String useLineNo, String useSegNo);
}
