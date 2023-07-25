package com.wzmtr.eam.service.impl.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.entity.CurrentLoginUser;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.shiro.model.TPerson;
import com.wzmtr.eam.shiro.service.IPersonService;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.common.UserAccountMapper;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.SysUserAccount;
import com.wzmtr.eam.dto.req.UserStatusReqDTO;
import com.wzmtr.eam.dto.res.UserAccountListResDTO;
import com.wzmtr.eam.dto.res.UserCenterInfoResDTO;
import com.wzmtr.eam.dto.res.UserRoleResDTO;
import com.wzmtr.eam.service.common.UserAccountService;
import com.wzmtr.eam.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author frp
 */
@Service
@Slf4j
public class UserAccountServiceImpl implements UserAccountService {

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private IPersonService personService;

    @Override
    public Page<UserAccountListResDTO> listUserAccount(String searchKey, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return userAccountMapper.listUserAccount(pageReqDTO.of(), searchKey);
    }

    /**
     * 根据id获取信息
     *
     * @param ids
     * @return
     */
    @Override
    public List<UserAccountListResDTO> selectUserAccountById(List<String> ids) {
        return userAccountMapper.selectUserAccountById(ids);
    }

    /**
     * 用户详情
     *
     * @param id
     * @return
     */
    @Override
    public HashMap<String, Object> getUserAccountDetail(String id) {
        HashMap<String,Object> hashMap= new HashMap<>();
        hashMap.put("id","123");
        hashMap.put("no","123");
        hashMap.put("name","测试账号");
        hashMap.put("loginName","测试账号");
        hashMap.put("phone","1565656565");
        hashMap.put("disabled",0);
        hashMap.put("orgPath","测试部门-测试组");
        hashMap.put("email","123@qq.com");
        hashMap.put("mobile","1565656565");
        hashMap.put("room","铁投");
        List<UserRoleResDTO> roleResDTOS = new ArrayList<>();
        roleResDTOS.add(new UserRoleResDTO(){{
            setId("123");
            setRoleCode("123");
            setRoleName("测试角色");
        }});
        hashMap.put("roles",roleResDTOS);
        hashMap.put("eipRoles",roleResDTOS);
        return hashMap;
    }

    /**
     * 用户账号锁定/解锁
     *
     * @param userStatusReqDTO
     */
    @Override
    public void ableUserRole(UserStatusReqDTO userStatusReqDTO) {
        if (Objects.isNull(userStatusReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        userStatusReqDTO.setCreatedBy("");
        Integer result = userAccountMapper.ableUserRole(userStatusReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public Page<SysUserAccount> listOutUserAccount(PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return userAccountMapper.listOutUserAccount(pageReqDTO.of());
    }

    @Override
    public String getToken(String userId) {
        CurrentLoginUser person = new CurrentLoginUser();
        if ("admin".equals(userId)) {
            person.setPersonId("admin");
            person.setPersonNo("admin");
            person.setPersonName("系统管理员");
            person.setCompanyId("A");
            person.setCompanyName("集团本级");
            person.setOfficeId("A02");
            person.setOfficeName("办公室");
        } else {
            TPerson p = personService.searchPersonByNo(userId);
            if (p != null) {
                person.setPersonId(p.getId());
                person.setPersonNo(p.getNo());
                person.setPersonName(p.getName());
                person.setCompanyId(p.getCompanyId());
                person.setCompanyName(p.getCompanyName());
                person.setCompanyAreaId(p.getCompanyAreaId());
                person.setOfficeId(p.getOfficeId());
                person.setOfficeName(p.getOfficeName());
                person.setOfficeAreaId(p.getOfficeAreaId());
            } else {
                throw new CommonException(ErrorCode.USER_NOT_EXIST);
            }
        }
        return TokenUtil.createSimpleToken(person);
    }

    @Override
    public UserCenterInfoResDTO getUserDetail() {
        UserCenterInfoResDTO res = userAccountMapper.userCenterInfo(TokenUtil.getCurrentPersonId());
        res.setUserRoles(userAccountMapper.getUserRoles(res.getId()));
        res.setOperationManual("http://10.11.82.91:9000/rights/2022/09/21/温州市铁投集团公权力大数据监督应用平台用户手册.pdf");
        return res;
    }
}
