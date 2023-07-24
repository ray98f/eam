package com.wzmtr.eam.shiro.service;

import com.wzmtr.eam.mapper.common.PersonMapper;
import com.wzmtr.eam.shiro.model.TPerson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PersonServiceImpl implements IPersonService {

    @Autowired
    private PersonMapper personDao;

    @Override
    public TPerson searchPersonByNo(String no) {
        return personDao.searchPersonByNo(no);
    }
}
