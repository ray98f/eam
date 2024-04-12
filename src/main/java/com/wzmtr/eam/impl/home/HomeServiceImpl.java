package com.wzmtr.eam.impl.home;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wzmtr.eam.dto.res.home.EChartResDTO;
import com.wzmtr.eam.dto.res.home.HomeCountResDTO;
import com.wzmtr.eam.dto.res.home.ShowAResDTO;
import com.wzmtr.eam.dto.res.home.ShowBCResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.StatusWorkFlowLog;
import com.wzmtr.eam.mapper.home.HomeMapper;
import com.wzmtr.eam.service.dict.IDictionariesService;
import com.wzmtr.eam.service.home.HomeService;
import com.wzmtr.eam.utils.StreamUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
    public EChartResDTO queryChart() {
        //最近3月
        List<ShowBCResDTO> listB = homeMapper.queryB();
        //最近7天
        List<ShowBCResDTO> listC = homeMapper.queryC();
        //故障状态统计
        List<ShowAResDTO> listA = homeMapper.queryA();
        EChartResDTO eChartResDTO = new EChartResDTO();
        Map<String, ShowBCResDTO> map = Maps.newHashMap();
        if (StringUtils.isNotEmpty(listC)){
            map = StreamUtils.toMap(listC, ShowBCResDTO::getMajorName);
        }
        List<ShowBCResDTO> list = Lists.newArrayList();
        for (ShowBCResDTO a : listB) {
            if (map.containsKey(a.getMajorName())){
                ShowBCResDTO showBcRes = new ShowBCResDTO();
                showBcRes.setMajorName(a.getMajorName());
                showBcRes.setCNT(a.getCNT());
                showBcRes.setCNTSeven(map.get(a.getMajorName()).getCNT());
                list.add(showBcRes);
            }
        }
        if (StringUtils.isNotEmpty(listA)){
            for (ShowAResDTO a : listA) {
                a.setCNAME(dictService.queryOneByItemCodeAndCodesetCode("dm.faultTrackStatus", a.getCNAME()).getItemCname());
            }
            eChartResDTO.setShowA(listA);
        }
        if (StringUtils.isNotEmpty(list)){
            eChartResDTO.setShowCount(list);
        }
        return eChartResDTO;
    }
}
