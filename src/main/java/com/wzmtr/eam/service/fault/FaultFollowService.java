package com.wzmtr.eam.service.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.fault.FaultFollowExportReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultFollowReportReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultFollowReqDTO;
import com.wzmtr.eam.dto.res.fault.FaultFollowDispatchUserResDTO;
import com.wzmtr.eam.dto.res.fault.FaultFollowReportResDTO;
import com.wzmtr.eam.dto.res.fault.FaultFollowResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.SysUser;

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
     * @param faultWorkNo 故障单号
     * @return 工班长列表
     */
    List<SysUser> listLeader(String faultWorkNo);

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
     * 获取派工人信息
     * @param followNo 跟踪编号
     * @return 派工人信息
     */
    FaultFollowDispatchUserResDTO listDispatchUser(String followNo);

    /**
     * 派工故障跟踪工单
     * @param req 故障跟踪工单参数
     */
    void dispatch(FaultFollowReqDTO req);

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
     * @param type 查询类型 1查询全部 2查询未审核的报告
     * @return 跟踪工单报告列表
     */
    List<FaultFollowReportResDTO> listReport(String followNo, String type);

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
     * 审核故障跟踪工单报告
     * @param req 故障跟踪工单报告参数
     * @throws ParseException 异常
     */
    void examineReport(FaultFollowReportReqDTO req) throws ParseException;

    /**
     * 审核故障跟踪工单报告
     * @param req 故障跟踪工单报告参数
     * @throws ParseException 异常
     */
    void engineerExamineReport(FaultFollowReportReqDTO req) throws ParseException;
}
