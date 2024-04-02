package com.wzmtr.eam.mapper.statistic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.res.statistic.*;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 11:03
 */
@Repository
@Mapper
public interface RamsMapper {
    List<RamsCarResDTO> query4AQYYZB();

    List<SystemFaultsResDTO> queryFautTypeByMonthBySys(Set<String> moduleIds, String startDate, String endDate);

    List<RamsResult2ResDTO> queryresult2(String startDate, String endDate);

    /**
     * 屎山
     * @return
     */
    List<FaultConditionResDTO> queryCountFautType4Rams();
    List<FaultConditionResDTO> queryCountFaut();

    List<RamsSysPerformResDTO> querySysPerform();

    List<RamsResDTO> querytotalMiles();

    /**
     * 分页查询故障列表
     * @param of 分页参数
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param trainNo 车号
     * @return 故障列表
     */
    Page<FaultRamsResDTO> queryRamsFaultList(Page<Object> of, String startDate, String endDate, String trainNo);

    /**
     * 查询故障数量
     *
     * @param startDate   开始时间
     * @param endDate     结束时间
     * @param trainNo     车号
     * @param faultType   故障分类
     * @param faultAffect 故障影响
     * @return 故障数量
     */
    Double countRamsFaultList(String startDate, String endDate, String trainNo, String faultType, String faultAffect);

    /**
     * 获取时间段内累计运营里程相减值
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param trainNo 车号
     * @return 累计运营里程相减值
     */
    Double getMileSubtract(String startDate, String endDate, String trainNo);
    Double getMileByTrainNoEnd(String endDate, String trainNo);
    Double getMileByTrainNoStart(String startDate, String trainNo);

    /**
     * 故障列表更换部件查询
     * @return
     */
    FaultRamsResDTO queryPart(String faultWorkNo);

}
