package com.wzmtr.eam.impl.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.res.common.UserAccountListResDTO;
import com.wzmtr.eam.dto.res.common.UserCenterInfoResDTO;
import com.wzmtr.eam.dto.res.common.UserRoleResDTO;
import com.wzmtr.eam.entity.CurrentLoginUser;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.SysUserAccount;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.UserAccountMapper;
import com.wzmtr.eam.service.common.UserAccountService;
import com.wzmtr.eam.shiro.model.Person;
import com.wzmtr.eam.shiro.service.IPersonService;
import com.wzmtr.eam.utils.TokenUtils;
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
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return userAccountMapper.listUserAccount(pageReqDTO.of(), searchKey);
    }

    @Override
    public List<UserAccountListResDTO> selectUserAccountById(List<String> ids) {
        return userAccountMapper.selectUserAccountById(ids);
    }

    @Override
    public Page<SysUserAccount> listOutUserAccount(PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return userAccountMapper.listOutUserAccount(pageReqDTO.of());
    }

    @Override
    public String getToken(String userId) {
        CurrentLoginUser person = new CurrentLoginUser();
            Person p = personService.searchPersonByNo(userId);
            if (p != null) {
                person.setPersonId(p.getLoginName());
                person.setPersonNo(p.getLoginName());
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
        return TokenUtils.createSimpleToken(person);
    }

    @Override
    public UserCenterInfoResDTO getUserDetail() {
        UserCenterInfoResDTO res = userAccountMapper.userCenterInfo(TokenUtils.getCurrentPersonId());
        // 获取登录用户角色权限
        res.setUserRoles(userAccountMapper.getUserRoles(TokenUtils.getCurrentPersonId()));
        // 获取登录用户相关专业
        if(res.getUserRoles() != null){

            //如果改用户的角色可以查看全专业
            UserRoleResDTO r =res.getUserRoles().stream().filter(a-> CommonConstants.SYS_ALL_01.equals(a.getRoleCode())).findFirst().orElse(null);
            if(r != null){
                res.setUserMajors( userAccountMapper.getAllMajor());
            }else{
                res.setUserMajors( userAccountMapper.getMajor(TokenUtils.getCurrentPersonId()));
            }
        }

        return res;
    }

    @Override
    public List<UserRoleResDTO> getUserRolesById(String userId) {
        return userAccountMapper.getUserRoles(userId);
    }


    @Override
    public List<String> listUserMajor() {

        List<UserRoleResDTO> res = userAccountMapper.getUserRoles(TokenUtils.getCurrentPersonId());
        List<String> majorList = null;
        // 获取登录用户相关专业
        if(res != null){

            //如果该用户的角色可以查看全专业
            UserRoleResDTO r = res.stream().filter(a-> CommonConstants.SYS_ALL_01.equals(a.getRoleCode())).findFirst().orElse(null);
            if(r != null){
                majorList = ( userAccountMapper.getAllMajor());
            }else{
                majorList = ( userAccountMapper.getMajor(TokenUtils.getCurrentPersonId()));
            }
        }

        return majorList;
    }


}
