package com.wzmtr.eam.impl.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.entity.CurrentLoginUser;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.shiro.model.TPerson;
import com.wzmtr.eam.shiro.service.IPersonService;
import com.wzmtr.eam.mapper.common.UserAccountMapper;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.SysUserAccount;
import com.wzmtr.eam.dto.res.common.UserAccountListResDTO;
import com.wzmtr.eam.dto.res.common.UserCenterInfoResDTO;
import com.wzmtr.eam.service.common.UserAccountService;
import com.wzmtr.eam.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author frp
 */
@Service
@Slf4j
public class UserAccountServiceImpl implements UserAccountService {

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private IPersonService personService;

    @Override
    public Page<UserAccountListResDTO> listUserAccount(String searchKey, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return userAccountMapper.listUserAccount(pageReqDTO.of(), searchKey);
    }

    @Override
    public List<UserAccountListResDTO> selectUserAccountById(List<String> ids) {
        return userAccountMapper.selectUserAccountById(ids);
    }

    @Override
    public Page<SysUserAccount> listOutUserAccount(PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return userAccountMapper.listOutUserAccount(pageReqDTO.of());
    }

    @Override
    public String getToken(String userId) {
        CurrentLoginUser person = new CurrentLoginUser();
        // if (CommonConstants.ADMIN.equals(userId)) {
        //     person.setPersonId(CommonConstants.ADMIN);
        //     person.setPersonNo(CommonConstants.ADMIN);
        //     person.setPersonName("系统管理员");
        //     person.setCompanyId("A");
        //     person.setCompanyName("集团本级");
        //     person.setOfficeId("A02");
        //     person.setOfficeName("办公室");
        // } else {
            TPerson p = personService.searchPersonByNo(userId);
            if (p != null) {
                person.setPersonId(p.getId());
                person.setPersonNo(p.getNo());
                person.setPersonName(p.getName());
                person.setMobile(p.getMobile());
                person.setPhone(p.getPhone());
                person.setEmail(p.getEmail());
                person.setCompanyId(p.getCompanyId());
                person.setCompanyName(p.getCompanyName());
                person.setCompanyAreaId(p.getCompanyAreaId());
                person.setOfficeId(p.getOfficeId());
                person.setOfficeName(p.getOfficeName());
                person.setOfficeAreaId(p.getOfficeAreaId());
                person.setNames(p.getNames());
            } else {
                throw new CommonException(ErrorCode.USER_NOT_EXIST);
            }
        return TokenUtil.createSimpleToken(person);
    }

    @Override
    public UserCenterInfoResDTO getUserDetail() {
        UserCenterInfoResDTO res = userAccountMapper.userCenterInfo(TokenUtil.getCurrentPersonId());
        // todo 获取登录用户角色权限
//        res.setUserRoles(userAccountMapper.getUserRoles(res.getId()));
        return res;
    }
}
