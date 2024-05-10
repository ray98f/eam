package com.wzmtr.eam.mapper.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.fault.FaultFollowExportReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultFollowReportReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultFollowReqDTO;
import com.wzmtr.eam.dto.res.fault.FaultFollowReportResDTO;
import com.wzmtr.eam.dto.res.fault.FaultFollowResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 故障管理-故障跟踪(新)
 * @author  Ray
 * @version 1.0
 * @date 2024/05/09
 */
@Mapper
@Repository
public interface FaultFollowMapper {

    /**
     * 分页查询故障跟踪工单列表
     * @param page 分页参数
     * @param followNo 跟踪编号
     * @param faultWorkNo 故障单号
     * @param followStatus 跟踪状态
     * @param userId 用户id
     * @return 故障跟踪工单列表
     */
    Page<FaultFollowResDTO> page(Page<FaultFollowResDTO> page, String followNo, String faultWorkNo,
                                 String followStatus, String userId);

    /**
     * 故障跟踪工单详情查询
     * @param id id
     * @param followNo 跟踪编号
     * @return 故障跟踪工单详情
     */
    FaultFollowResDTO detail(String id, String followNo);

    /**
     * 查询故障跟踪工单的跟踪报告列表
     * @param followNo 跟踪编号
     * @return 跟踪报告列表
     */
    List<FaultFollowReportResDTO> getReport(String followNo);

    /**
     * 获取最新的故障跟踪编号
     * @return 故障跟踪编号
     */
    String selectMaxCode();

    /**
     * 新增故障跟踪工单
     * @param req 故障跟踪工单参数
     */
    void add(FaultFollowReqDTO req);

    /**
     * 编辑故障跟踪工单
     * @param req 故障跟踪工单参数
     */
    void modify(FaultFollowReqDTO req);

    /**
     * 强制关闭故障跟踪工单
     * @param req 故障跟踪工单参数
     */
    void close(FaultFollowReqDTO req);

    /**
     * 导出故障跟踪工单
     * @param req 导出查询参数
     * @return 故障跟踪工单列表
     */
    List<FaultFollowResDTO> export(FaultFollowExportReqDTO req);

    /**
     * 查询故障跟踪工单报告详情
     * @param id id
     * @return 跟踪工单报告详情
     */
    FaultFollowReportResDTO getReportDetail(String id);

    /**
     * 根据跟踪工单编号查询故障跟踪工单最新报告
     * @param followNo 跟踪编号
     * @return 报告
     */
    FaultFollowReportResDTO getLastReportByFollowNo(String followNo);

    /**
     * 根据跟踪工单编号查询故障跟踪工单最新报告
     * @param followNo 跟踪编号
     * @param step 跟踪报告阶段
     * @return 报告
     */
    Integer checkReportStep(String followNo, long step);

    /**
     * 新增故障跟踪工单报告
     * @param req 故障跟踪工单报告参数
     */
    void addReport(FaultFollowReportReqDTO req);

    /**
     * 编辑故障跟踪工单报告
     * @param req 故障跟踪工单报告参数
     */
    void modifyReport(FaultFollowReportReqDTO req);

    /**
     * 新增故障跟踪工单报告
     * @param req 故障跟踪工单报告参数
     */
    void examineReport(FaultFollowReportReqDTO req);
}
