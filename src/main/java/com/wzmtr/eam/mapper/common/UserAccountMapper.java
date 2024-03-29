package com.wzmtr.eam.mapper.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.res.common.UserAccountListResDTO;
import com.wzmtr.eam.dto.res.common.UserCenterInfoResDTO;
import com.wzmtr.eam.dto.res.common.UserRoleResDTO;
import com.wzmtr.eam.entity.SysOrgUser;
import com.wzmtr.eam.entity.SysUser;
import com.wzmtr.eam.entity.SysUserAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author frp
 */
@Mapper
@Repository
public interface UserAccountMapper {

    /**
     * 用户账户信息列表
     *
     * @param page
     * @param searchKey
     * @return
     */
    Page<UserAccountListResDTO> listUserAccount(Page<UserAccountListResDTO> page, @Param("searchKey") String searchKey);

    /**
     * 根据id获取信息
     *
     * @param ids
     * @return
     */
    List<UserAccountListResDTO> selectUserAccountById(List<String> ids);

    Page<SysUserAccount> listOutUserAccount(Page<SysUserAccount> page);

    /**
     * 清空表(全量同步时使用)
     *
     * @return
     */
    Integer cleanTable();

    Integer cleanSuppCon();

    Integer createPerson(SysUser sysUser);

    /**
     * 清空人员职位信息
     *
     * @return
     */
    Integer cleanEmpJob();

    Integer createEmpJob(SysOrgUser sysOrgUser);

    Integer updateUserCompany(SysUser sysUser);

    Integer updatePersonPlus(Map<String, Object> paramMap);

    UserCenterInfoResDTO userCenterInfo(@Param("userId") String userId);

    List<UserRoleResDTO> getUserRoles(@Param("userId") String userId);

    List<String> getMajor(@Param("userId") String userId);
}
