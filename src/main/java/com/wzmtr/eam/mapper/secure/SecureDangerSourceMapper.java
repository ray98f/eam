package com.wzmtr.eam.mapper.secure;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.secure.SecureDangerSourceAddReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureDangerSourceListReqDTO;
import com.wzmtr.eam.dto.res.secure.SecureDangerSourceResDTO;
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
public interface SecureDangerSourceMapper {

    List<SecureDangerSourceResDTO> list(SecureDangerSourceListReqDTO reqDTO);

    SecureDangerSourceResDTO detail(String dangerRiskId);

    void add(SecureDangerSourceAddReqDTO reqDTO);

    void deleteByIds(@Param("ids") List<String> ids, @Param("userId") String userId, @Param("time") String time);

    void update(SecureDangerSourceAddReqDTO reqDTO);

    Page<SecureDangerSourceResDTO> query(Page<Object> of, SecureDangerSourceListReqDTO req);

    String getMaxCode();
}
