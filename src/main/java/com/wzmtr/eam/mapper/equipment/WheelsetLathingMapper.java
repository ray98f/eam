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

    Page<WheelsetLathingResDTO> pageWheelsetLathing(Page<WheelsetLathingResDTO> page, String trainNo, String carriageNo, String axleNo, String wheelNo);

    WheelsetLathingResDTO getWheelsetLathingDetail(String id);

    void addWheelsetLathing(WheelsetLathingReqDTO wheelsetLathingReqDTO);

    void deleteWheelsetLathing(List<String> ids, String userId, String time);

    void importWheelsetLathing(List<WheelsetLathingReqDTO> list);

    List<WheelsetLathingResDTO> listWheelsetLathing(String trainNo, String carriageNo, String axleNo, String wheelNo);

    /**
     * 导出轮对镟修台账列表
     * @param ids ids
     * @return 轮对镟修台账列表
     */
    List<WheelsetLathingResDTO> exportWheelsetLathing(List<String> ids);

}
