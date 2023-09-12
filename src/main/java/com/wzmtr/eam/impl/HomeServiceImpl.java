package com.wzmtr.eam.impl;

import com.wzmtr.eam.bo.HomeCountBO;
import com.wzmtr.eam.dto.res.home.HomeCountResDTO;
import com.wzmtr.eam.dto.res.home.IndexResDTO;
import com.wzmtr.eam.mapper.home.HomeMapper;
import com.wzmtr.eam.service.home.HomeService;
import com.wzmtr.eam.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
        HomeCountBO countBO = HomeCountBO.builder()
                .modelName("DM")
                .state("open")
                .userId(TokenUtil.getCurrentPersonId())
                .todoStatus("1")
                .build();
        Integer todoSize = homeMapper.queryForIndex(countBO);
        HomeCountBO countBO2 = HomeCountBO.builder()
                .modelName("DM")
                .state("completed")
                .userId(TokenUtil.getCurrentPersonId())
                .todoStatus("2")
                .build();
        Integer overSize = homeMapper.queryForIndex(countBO2);

        HomeCountBO countBO3 = HomeCountBO.builder()
                .receiveUserId(TokenUtil.getCurrentPersonId())
                .status("0")
                .build();
        Integer messageSize = homeMapper.count(countBO3);
        HomeCountBO countBO4 = HomeCountBO.builder()
                .receiveUserId(TokenUtil.getCurrentPersonId())
                .status("1")
                .build();
        Integer readSize = homeMapper.count(countBO4);
        HomeCountResDTO homeCountResDTO = new HomeCountResDTO();
        homeCountResDTO.setOverSize(overSize.toString());
        homeCountResDTO.setTodoSize(todoSize.toString());
        homeCountResDTO.setReadSize(readSize.toString());
        homeCountResDTO.setMessageSize(messageSize.toString());
        return homeCountResDTO;
    }
}
