package com.wzmtr.eam.service.home;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.home.HomeChartReqDTO;
import com.wzmtr.eam.dto.res.home.EChartResDTO;
import com.wzmtr.eam.dto.res.home.HomeCountResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.StatusWorkFlowLog;

/**
 * Author: Li.Wang
 * Date: 2023/9/12 14:23
 */
public interface HomeService {

    /**
     * 获取首页工作台列表
     * @param type 类型 1：待办 2：已办
     * @param pageReqDTO 分页信息
     * @return 工作列表
     */
    Page<StatusWorkFlowLog> todoList(String type, PageReqDTO pageReqDTO);

    /**
     * 返回工作台工作数量
     * @return 工作数量
     */
    HomeCountResDTO todoCount();

    /**
     * 催办
     * @param todoId 代办id
     */
    void urgingTodo(String todoId);

    EChartResDTO queryChart(HomeChartReqDTO req);
}
