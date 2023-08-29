package com.wzmtr.eam.mapper.statistic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.res.statistic.CarFaultQueryResDTO;
import com.wzmtr.eam.dto.res.statistic.ReliabilityResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 11:03
 */
@Repository
@Mapper
public interface ReliabilityMapper {
    List<ReliabilityResDTO> queryTicketFault(String endTime, String startTime);

    List<ReliabilityResDTO> queryGateBrakeFault(String endTime, String startTime);

    List<ReliabilityResDTO> queryEscalatorFault(String endTime, String startTime);

    /**
     * 垂直扶梯可靠度
     * @param endTime
     * @param startTime
     * @return
     */
    List<ReliabilityResDTO> queryVerticalEscalatorFault(String endTime, String startTime);

    List<ReliabilityResDTO> queryTrainPassengerInformationFault(String endTime, String startTime);
    List<ReliabilityResDTO> queryStationPassengerInformationFault(String endTime, String startTime);

    List<ReliabilityResDTO> queryFireFightingEquipmentFault(String endTime, String startTime);
}
