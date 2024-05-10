package com.wzmtr.eam.impl.home;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.dto.req.home.HomeChartReqDTO;
import com.wzmtr.eam.dto.res.home.EChartResDTO;
import com.wzmtr.eam.dto.res.home.HomeCountResDTO;
import com.wzmtr.eam.dto.res.home.ShowAResDTO;
import com.wzmtr.eam.dto.res.home.ShowBCResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.StatusWorkFlowLog;
import com.wzmtr.eam.mapper.home.HomeMapper;
import com.wzmtr.eam.service.dict.IDictionariesService;
import com.wzmtr.eam.service.home.HomeService;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/9/12 14:46
 */
@Service
@Slf4j
public class HomeServiceImpl implements HomeService {
    @Autowired
    private HomeMapper homeMapper;

    @Autowired
    private IDictionariesService dictService;

    @Override
    public Page<StatusWorkFlowLog> todoList(String type, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        //todo 上线后可以删，留个日志方便调试
        log.info("current userID:{}",TokenUtils.getCurrentPersonId());
        return homeMapper.todoList(pageReqDTO.of(), type, TokenUtils.getCurrentPersonId());
    }

    @Override
    public HomeCountResDTO todoCount() {
        return homeMapper.todoCount(TokenUtils.getCurrentPersonId());
    }

    @Override
    public void urgingTodo(String todoId) {
        homeMapper.urgingTodo(todoId);
    }

    @Override
    public EChartResDTO queryChart(HomeChartReqDTO req) {
        // 柱状图数据查询
        List<ShowBCResDTO> listB = homeMapper.queryB(req);
        // 饼图统计数据查询
        List<ShowAResDTO> listA = homeMapper.queryA(req);
        EChartResDTO eChartResDTO = new EChartResDTO();
        if (StringUtils.isNotEmpty(listA)) {
            for (ShowAResDTO a : listA) {
                a.setCname(dictService.queryOneByItemCodeAndCodesetCode("dm.faultStatus", a.getCname()).getItemCname());
            }
            eChartResDTO.setShowA(listA);
        }
        if (StringUtils.isNotEmpty(listB)) {
            eChartResDTO.setShowCount(listB);
        }
        return eChartResDTO;
    }
}
