package com.wzmtr.eam.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.res.UserCenterInfoResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.SysUserAccount;
import com.wzmtr.eam.dto.req.UserStatusReqDTO;
import com.wzmtr.eam.dto.res.UserAccountListResDTO;
import java.util.HashMap;
import java.util.List;

/**
 * @author frp
 */
public interface UserAccountService {

    /**
     * 用户账户信息列表
     *
     * @param searchKey
     * @param pageReqDTO
     * @return
     */
    Page<UserAccountListResDTO> listUserAccount(String searchKey, PageReqDTO pageReqDTO);

    /**
     * 根据id获取信息
     *
     * @param ids
     * @return
     */
    List<UserAccountListResDTO> selectUserAccountById(List<String> ids);

    /**
     * 用户详情
     *
     * @param id
     * @return
     */
    HashMap<String, Object> getUserAccountDetail(String id);

    /**
     * 用户账号锁定/解锁
     *
     * @param userStatusReqDTO
     */
    void ableUserRole(UserStatusReqDTO userStatusReqDTO);

    Page<SysUserAccount> listOutUserAccount(PageReqDTO pageReqDTO);

    String getToken(String userId);

    UserCenterInfoResDTO getUserDetail();

}
