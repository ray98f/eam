package com.wzmtr.eam.service.common;

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

    Page<UserAccountListResDTO> listUserAccount(String searchKey, PageReqDTO pageReqDTO);

    List<UserAccountListResDTO> selectUserAccountById(List<String> ids);

    Page<SysUserAccount> listOutUserAccount(PageReqDTO pageReqDTO);

    String getToken(String userId);

    UserCenterInfoResDTO getUserDetail();

}
