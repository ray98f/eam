package com.wzmtr.eam.service.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.res.common.UserCenterInfoResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.SysUserAccount;
import com.wzmtr.eam.dto.res.common.UserAccountListResDTO;

import java.util.List;

/**
 * @author frp
 */
public interface UserAccountService {

    Page<UserAccountListResDTO> listUserAccount(String searchKey, PageReqDTO pageReqDTO);

    List<UserAccountListResDTO> selectUserAccountById(List<String> ids);

    Page<SysUserAccount> listOutUserAccount(PageReqDTO pageReqDTO);

    String getToken(String userId);

    UserCenterInfoResDTO getUserDetail();

    /**
     * 当前用户专业列表
     * @return
     */
    List<String> listUserMajor();
}
