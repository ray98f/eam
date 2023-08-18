package com.wzmtr.eam.impl.statistic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.statistic.CarFaultQueryReqDTO;
import com.wzmtr.eam.dto.res.statistic.CarFaultQueryResDTO;
import com.wzmtr.eam.mapper.statistic.CarFaultMapper;
import com.wzmtr.eam.service.statistic.CarFaultService;
import com.wzmtr.eam.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 11:01
 */
@Service
@Slf4j
public class CarFaultServiceImpl implements CarFaultService {
    @Autowired
    private CarFaultMapper carFaultMapper;

    @Override
    public Page<CarFaultQueryResDTO> query(CarFaultQueryReqDTO reqDTO) {
        if (StringUtils.isEmpty(reqDTO.getStartTime()) || StringUtils.isEmpty(reqDTO.getEndTime())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            String starDate = calendar.get(Calendar.YEAR) + "-01-01";
            Date parse = null;
            try {
                parse = sdf.parse(starDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            calendar.setTime(parse);
            reqDTO.setStartTime(sdf.format(calendar.getTime()));
            reqDTO.setEndTime(sdf.format(calendar.getTime()));
        }
        //todo 不懂 这里可能有字符串处理
        // if (inInfo.get("inqu_status-0-objectCode") != null && !inInfo.get("inqu_status-0-objectCode").toString().trim().equals("")) {
        //     /* 39 */         String objectCode = inInfo.get("inqu_status-0-objectCode").toString().substring(2, inInfo.get("inqu_status-0-objectCode").toString().length() - 2);
        //     /* 40 */         objectCode = ("\"" + objectCode + "\"").replace("\"", "'");
        //     /* 41 */         map.put("objectCode", objectCode);
        //     /*    */       }
        return carFaultMapper.query(reqDTO.of(), reqDTO);
    }
}
