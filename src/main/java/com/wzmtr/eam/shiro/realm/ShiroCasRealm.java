package com.wzmtr.eam.shiro.realm;

import com.wzmtr.eam.utils.TokenUtil;
import com.wzmtr.eam.entity.CurrentLoginUser;
import com.wzmtr.eam.shiro.model.TPerson;
import com.wzmtr.eam.shiro.service.IPersonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

/**
 * description:
 *
 * @author zhangxin
 * @version 1.0
 * @date 2021/8/3 10:54
 */
@Slf4j
public class ShiroCasRealm extends CasRealm {


    @Autowired
    private IPersonService personService;

    /**
     * 1、CAS认证 ,验证用户身份
     * 2、将用户基本信息设置到会话中(不用了，随时可以获取的)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {

        AuthenticationInfo authenticationInfo = super.doGetAuthenticationInfo(token);
        if (Objects.isNull(authenticationInfo)) {
            return null;
        }
        String name = (String) authenticationInfo.getPrincipals().getPrimaryPrincipal();
        SecurityUtils.getSubject().getSession().setAttribute("name", name);

        CurrentLoginUser person = new CurrentLoginUser();
        if ("admin".equals(name)) {
            person.setPersonId("admin");
            person.setPersonNo("admin");
            person.setPersonName("系统管理员");
            person.setOfficeId("A02");
        } else {
            TPerson p = personService.searchPersonByNo(name);
            if (p != null) {
                person.setPersonId(p.getId());
                person.setPersonNo(p.getNo());
                person.setPersonName(p.getName());
                person.setOfficeId(p.getOfficeId());
            }
        }
        String jwtToken = TokenUtil.createSimpleToken(person);
        SecurityUtils.getSubject().getSession().setAttribute("jwtToken", jwtToken);
        return authenticationInfo;
    }


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SecurityUtils.getSubject();
        return new SimpleAuthorizationInfo();

    }
}
