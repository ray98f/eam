package com.wzmtr.eam.service.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.fault.FaultFollowExportReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultFollowReportReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultFollowReqDTO;
import com.wzmtr.eam.dto.res.fault.FaultFollowReportResDTO;
import com.wzmtr.eam.dto.res.fault.FaultFollowResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.shiro.model.Person;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * 故障管理-故障跟踪(新)
 * @author  Ray
 * @version 1.0
 * @date 2024/05/09
 */
public interface FaultFollowService {

    /**
     * 分页查询故障跟踪工单列表
     * @param followNo 跟踪编号
     * @param faultWorkNo 故障单号
     * @param followStatus 跟踪状态
     * @param pageReqDTO 分页参数
     * @return 故障跟踪工单列表
     */
    Page<FaultFollowResDTO> page(String followNo, String faultWorkNo, String followStatus, PageReqDTO pageReqDTO);

    /**
     * 故障跟踪工单详情查询
     * @param id id
     * @return 故障跟踪工单详情
     */
    FaultFollowResDTO detail(String id);

    /**
     * 获取工班长列表
     * @param majorCode 专业code
     * @param positionCode 位置1
     * @return 工班长列表
     */
    List<Person> listLeader(String majorCode, String positionCode);

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
     * @param response response
     * @throws IOException 异常
     */
    void export(FaultFollowExportReqDTO req, HttpServletResponse response) throws IOException;

    /**
     * 获取故障跟踪工单报告列表
     * @param followNo 故障跟踪工单编号
     * @return 跟踪工单报告列表
     */
    List<FaultFollowReportResDTO> listReport(String followNo);

    /**
     * 查询故障跟踪工单报告详情
     * @param id id
     * @return 跟踪工单报告详情
     */
    FaultFollowReportResDTO getReportDetail(String id);

    /**
     * 新增故障跟踪工单报告
     * @param req 故障跟踪工单报告参数
     * @throws ParseException 异常
     */
    void addReport(FaultFollowReportReqDTO req) throws ParseException;

    /**
     * 编辑故障跟踪工单报告
     * @param req 故障跟踪工单报告参数
     */
    void modifyReport(FaultFollowReportReqDTO req);

    /**
     * 审核故障跟踪工单报告
     * @param req 故障跟踪工单报告参数
     */
    void examineReport(FaultFollowReportReqDTO req);
}
