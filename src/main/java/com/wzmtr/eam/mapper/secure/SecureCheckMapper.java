package com.wzmtr.eam.mapper.secure;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.secure.SecureCheckAddReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureCheckRecordListReqDTO;
import com.wzmtr.eam.dto.res.secure.SecureCheckRecordListResDTO;
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
public interface SecureCheckMapper {

    SecureCheckRecordListResDTO detail(String secRiskId);

    List<SecureCheckRecordListResDTO> list(SecureCheckRecordListReqDTO reqDTO);

    void deleteByIds(@Param("ids") List<String> ids, @Param("userId") String userId, @Param("time") String time);

    void add(SecureCheckAddReqDTO reqDTO);

    void update(SecureCheckAddReqDTO reqDTO);

    Page<SecureCheckRecordListResDTO> query(Page<Object> of, SecureCheckRecordListReqDTO req);

    String getMaxCode();
}
