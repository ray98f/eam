package com.wzmtr.eam.mapper.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.WheelsetLathingReqDTO;
import com.wzmtr.eam.dto.res.WheelsetLathingResDTO;
import com.wzmtr.eam.dto.res.RegionResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface WheelsetLathingMapper {

    Page<WheelsetLathingResDTO> pageWheelsetLathing(Page<WheelsetLathingResDTO> page, String equipName);

    WheelsetLathingResDTO getWheelsetLathingDetail(String id);

    void addWheelsetLathing(WheelsetLathingReqDTO wheelsetLathingReqDTO);

    void deleteWheelsetLathing(List<String> ids, String userId, String time);

    List<WheelsetLathingResDTO> listWheelsetLathing(String equipName);

}
