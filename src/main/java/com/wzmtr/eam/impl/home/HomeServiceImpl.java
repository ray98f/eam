package com.wzmtr.eam.impl.home;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wzmtr.eam.bizobject.HomeCountBO;
import com.wzmtr.eam.dto.res.home.EChartResDTO;
import com.wzmtr.eam.dto.res.home.HomeCountResDTO;
import com.wzmtr.eam.dto.res.home.ShowAResDTO;
import com.wzmtr.eam.dto.res.home.ShowBCResDTO;
import com.wzmtr.eam.mapper.home.HomeMapper;
import com.wzmtr.eam.service.home.HomeService;
import com.wzmtr.eam.utils.TokenUtil;
import com.wzmtr.eam.utils.StreamUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Author: Li.Wang
 * Date: 2023/9/12 14:46
 */
@Service
public class HomeServiceImpl implements HomeService {
    @Autowired
    private HomeMapper homeMapper;

    @Override
    public HomeCountResDTO count() {
        HomeCountBO count = HomeCountBO.builder()
                .modelName("DM")
                .state("open")
                .userId(TokenUtil.getCurrentPersonId())
                .todoStatus("1")
                .build();
        Integer todoSize = homeMapper.queryForIndex(count);
        HomeCountBO count2 = HomeCountBO.builder()
                .modelName("DM")
                .state("completed")
                .userId(TokenUtil.getCurrentPersonId())
                .todoStatus("2")
                .build();
        Integer overSize = homeMapper.queryForIndex(count2);

        HomeCountBO count3 = HomeCountBO.builder()
                .receiveUserId(TokenUtil.getCurrentPersonId())
                .status("0")
                .build();
        Integer messageSize = homeMapper.count(count3);
        HomeCountBO count4 = HomeCountBO.builder()
                .receiveUserId(TokenUtil.getCurrentPersonId())
                .status("1")
                .build();
        Integer readSize = homeMapper.count(count4);
        HomeCountResDTO homeCountResDTO = new HomeCountResDTO();
        homeCountResDTO.setOverSize(overSize.toString());
        homeCountResDTO.setTodoSize(todoSize.toString());
        homeCountResDTO.setReadSize(readSize.toString());
        homeCountResDTO.setMessageSize(messageSize.toString());
        return homeCountResDTO;
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
        if (CollectionUtil.isNotEmpty(listC)){
            map = StreamUtil.toMap(listC, ShowBCResDTO::getMajorName);
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
        if (CollectionUtil.isNotEmpty(listA)){
            eChartResDTO.setShowA(listA);
        }
        if (CollectionUtil.isNotEmpty(list)){
            eChartResDTO.setShowCount(list);
        }
        return eChartResDTO;
    }
}
