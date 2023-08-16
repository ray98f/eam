package com.wzmtr.eam.impl.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.fault.ObjectReqDTO;
import com.wzmtr.eam.dto.res.fault.ObjectResDTO;
import com.wzmtr.eam.mapper.fault.ObjectMapper;
import com.wzmtr.eam.service.fault.ObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author: Li.Wang
 * Date: 2023/8/16 14:47
 */
@Service
public class ObjectServiceImpl implements ObjectService {
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public Page<ObjectResDTO> queryObject(ObjectReqDTO reqDTO) {
        return objectMapper.queryObject(reqDTO.of(),reqDTO.getObjectCode(),reqDTO.getMajorCode(),reqDTO.getObjectName(),reqDTO.getBrand(),reqDTO.getManufacture(),reqDTO.getEquipTypeCode(),reqDTO.getSystemCode(),reqDTO.getPosition1Code(),reqDTO.getManufactureDateStart(),reqDTO.getManufactureDateEnd(),reqDTO.getUseLineNo(),reqDTO.getUseSegNo());
    }
}
