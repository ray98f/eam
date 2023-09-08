package com.wzmtr.eam.mapper.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.equipment.WheelsetLathingReqDTO;
import com.wzmtr.eam.dto.res.equipment.WheelsetLathingResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface WheelsetLathingMapper {

    Page<WheelsetLathingResDTO> pageWheelsetLathing(Page<WheelsetLathingResDTO> page, String trainNo);

    WheelsetLathingResDTO getWheelsetLathingDetail(String id);

    void addWheelsetLathing(WheelsetLathingReqDTO wheelsetLathingReqDTO);

    void deleteWheelsetLathing(List<String> ids, String userId, String time);

    void importWheelsetLathing(List<WheelsetLathingReqDTO> list);

    List<WheelsetLathingResDTO> listWheelsetLathing(String trainNo);

}
