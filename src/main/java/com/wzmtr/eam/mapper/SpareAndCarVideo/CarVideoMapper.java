package com.wzmtr.eam.mapper.SpareAndCarVideo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.spareAndCarVideo.CarVideoAddReqDTO;
import com.wzmtr.eam.dto.res.spareAndCarVideo.CarVideoResDTO;
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
public interface CarVideoMapper {


    Page<CarVideoResDTO> query(Page<Object> of, String recId, String startApplyTime, String endApplyTime, String recStatus);

    CarVideoResDTO detail(@Param("recId") String recId);

    void deleteByIds(@Param("ids") List<String> ids, @Param("userId") String userId, @Param("time") String time);

    void add(CarVideoAddReqDTO reqDTO);

    void update(CarVideoAddReqDTO reqDTO);
}
