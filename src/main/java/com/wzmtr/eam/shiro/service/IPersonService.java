package com.wzmtr.eam.shiro.service;

import com.wzmtr.eam.shiro.model.TPerson;

/**
 * description:
 *
 * @author zhangxin
 * @version 1.0
 * @date 2021/8/3 10:55
 */
public interface IPersonService {

    TPerson searchPersonByNo(String no);

}
