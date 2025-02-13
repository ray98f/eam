package com.wzmtr.eam.mapper.secure;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dataobject.SecureHazardDO;
import com.wzmtr.eam.dto.req.secure.SecureHazardAddReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureHazardReqDTO;
import com.wzmtr.eam.dto.res.secure.SecureHazardResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/8/1 11:06
 */
@Mapper
@Repository
public interface SecureHazardMapper extends BaseMapper<SecureHazardDO> {

    Page<SecureHazardResDTO> query(Page<SecureHazardResDTO> of, SecureHazardReqDTO req);

    SecureHazardResDTO detail(String riskId);

    void add(SecureHazardAddReqDTO reqDTO);

    List<SecureHazardResDTO> list(SecureHazardReqDTO reqDTO);

    void deleteByIds(@Param("ids") List<String> ids, @Param("userId") String userId, @Param("time") String time);

    String getMaxCode();
}
