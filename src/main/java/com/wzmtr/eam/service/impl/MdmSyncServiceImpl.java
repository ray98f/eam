package com.wzmtr.eam.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.wzmtr.eam.config.PersonDefaultConfig;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.mapper.OrganizationMapper;
import com.wzmtr.eam.mapper.UserAccountMapper;
import com.wzmtr.eam.entity.SysOffice;
import com.wzmtr.eam.entity.SysOrgUser;
import com.wzmtr.eam.entity.SysUser;
import com.wzmtr.eam.dto.res.OrgParentIdsResDTO;
import com.wzmtr.eam.service.MdmSyncService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MdmSyncServiceImpl implements MdmSyncService {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private PersonDefaultConfig personDefaultConfig;

    /**
     * 主数据人员信息接口地址
     */
    @Value("${mdm.person.address}")
    private String mdmPersonAddress;

    /**
     * 主数据人员扩展信息接口地址
     */
    @Value("${mdm.person-plus.address}")
    private String mdmPersonPlusAddress;

    /**
     * 主数据组织接口地址
     */
    @Value("${mdm.org.address}")
    private String mdmOrgAddress;

    /**
     * 主数据供应商接口地址
     */
    @Value("${mdm.supp.address}")
    private String mdmSuppOrgAddress;

    /**
     * 主数据外部单位组织接口地址
     */
    @Value("${mdm.extra-org.address}")
    private String mdmExtraOrgAddress;

    /**
     * 主数据员工职位信息接口地址
     */
    @Value("${mdm.emp-job.address}")
    private String mdmEmpJobAddress;

    @Override
    @Transactional
    public void syncAllPerson() {
        com.wzmtr.eam.soft.mdm.personquery.vo.RequestMessage requestMessage = new com.wzmtr.eam.soft.mdm.personquery.vo.RequestMessage();
        com.wzmtr.eam.soft.mdm.personquery.vo.Message message = new com.wzmtr.eam.soft.mdm.personquery.vo.Message();
        com.wzmtr.eam.soft.mdm.personquery.vo.ResponseMessage responseMessage;
        requestMessage.setVerb("Get");
        requestMessage.setNoun("allPersonList");
        message.setOperType(1);
        message.setIfPhoto(0);
        message.setSyscode("EAM");
        requestMessage.setMessage(message);
        List<SysUser> personList = new ArrayList<>();
        try {
            JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
            factory.setAddress(mdmPersonAddress);
            factory.setServiceClass(com.wzmtr.eam.soft.mdm.personquery.service.impl.IGetData.class);
            com.wzmtr.eam.soft.mdm.personquery.service.impl.IGetData personInterface = (com.wzmtr.eam.soft.mdm.personquery.service.impl.IGetData) factory.create();
            responseMessage = personInterface.getData(requestMessage);
            if (CommonConstants.WSDL_SUCCESS.equals(responseMessage.getState())) {
                List<com.wzmtr.eam.soft.mdm.personquery.vo.Result> result = responseMessage.getResult();
                personList = result.stream().map(this::personCopy).collect(Collectors.toList());
            } else {
                log.info("请求失败");
            }
        } catch (Exception e) {
            log.info("错误信息:" + e.getMessage());
        }
        if (personList.size() > 0) {
            doPersonInsertBatch(personList);
        }
    }

    @Override
    public void syncPersonPlus() {
        com.wzmtr.eam.soft.mdm.personplusquery.vo.RequestMessage requestMessage = new com.wzmtr.eam.soft.mdm.personplusquery.vo.RequestMessage();
        com.wzmtr.eam.soft.mdm.personplusquery.vo.Message message = new com.wzmtr.eam.soft.mdm.personplusquery.vo.Message();
        com.wzmtr.eam.soft.mdm.personplusquery.vo.ResponseMessage responseMessage;
        requestMessage.setVerb("Get");
        requestMessage.setNoun("allPersonPlusList");
        message.setOperType(1);
        message.setSyscode("EAM");
        requestMessage.setMessage(message);
        List<Map<String, Object>> plusList = new ArrayList<>();
        try {
            JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
            factory.setAddress(mdmPersonPlusAddress);
            factory.setServiceClass(com.wzmtr.eam.soft.mdm.personplusquery.service.impl.IGetData.class);
            com.wzmtr.eam.soft.mdm.personplusquery.service.impl.IGetData plusInterface = (com.wzmtr.eam.soft.mdm.personplusquery.service.impl.IGetData) factory.create();
            responseMessage = plusInterface.getData(requestMessage);
            if (CommonConstants.WSDL_SUCCESS.equals(responseMessage.getState())) {
                List<com.wzmtr.eam.soft.mdm.personplusquery.vo.Result> result = responseMessage.getResult();
                for (com.wzmtr.eam.soft.mdm.personplusquery.vo.Result item : result) {
                    Map<String, Object> paramMap = new HashMap<>();
                    paramMap.put("id", item.getPersonNo() == null ? "" : item.getPersonNo());
                    paramMap.put("flagPosition", item.getFlagPosition() == null ? "" : item.getFlagPosition());
                    paramMap.put("room", item.getRoom() == null ? "" : item.getRoom());
                    plusList.add(paramMap);
                }
            } else {
                log.info("请求失败");
            }
        } catch (Exception e) {
            log.info("错误信息:" + e.getMessage());
        }
        if (plusList.size() > 0) {
            doPersonPlusUpdateBatch(plusList);
        }
    }

    @Override
    @Transactional
    public void syncAllOrg() {
        com.wzmtr.eam.soft.mdm.orgquery.vo.RequestMessage requestMessage = new com.wzmtr.eam.soft.mdm.orgquery.vo.RequestMessage();
        com.wzmtr.eam.soft.mdm.orgquery.vo.Message message = new com.wzmtr.eam.soft.mdm.orgquery.vo.Message();
        com.wzmtr.eam.soft.mdm.orgquery.vo.ResponseMessage responseMessage;
        requestMessage.setVerb("Get");
        requestMessage.setNoun("allOrgList");
        message.setOperType(1);
        message.setSyscode("EAM");
        requestMessage.setMessage(message);
        List<SysOffice> orgList = new ArrayList<>();
        try {
            JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
            factory.setAddress(mdmOrgAddress);
            factory.setServiceClass(com.wzmtr.eam.soft.mdm.orgquery.service.impl.IGetData.class);
            com.wzmtr.eam.soft.mdm.orgquery.service.impl.IGetData orgInterface = (com.wzmtr.eam.soft.mdm.orgquery.service.impl.IGetData)
                    factory.create();
            responseMessage = orgInterface.getData(requestMessage);
            if (CommonConstants.WSDL_SUCCESS.equals(responseMessage.getState())) {
                List<com.wzmtr.eam.soft.mdm.orgquery.vo.Result> result = responseMessage.getResult();
                orgList = result.stream().map(this::orgCopy).collect(Collectors.toList());
            } else {
                log.info("请求失败");
            }
        } catch (Exception e) {
            log.info("错误信息:" + e.getMessage());
        }
        if (orgList.size() > 0) {
            doOrgInsertBatch(orgList, "org");
        }
    }

    @Override
    public void syncSuppOrg() {
        com.wzmtr.eam.soft.mdm.supplierquery.vo.RequestMessage requestMessage = new com.wzmtr.eam.soft.mdm.supplierquery.vo.RequestMessage();
        com.wzmtr.eam.soft.mdm.supplierquery.vo.Message message = new com.wzmtr.eam.soft.mdm.supplierquery.vo.Message();
        com.wzmtr.eam.soft.mdm.supplierquery.vo.ResponseMessage responseMessage;
        requestMessage.setVerb("Get");
        requestMessage.setNoun("allSuppList");
        message.setOperType(1);
        message.setSyscode("EAM");
        requestMessage.setMessage(message);
        List<SysOffice> orgList = new ArrayList<>();
        try {
            JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
            factory.setAddress(mdmSuppOrgAddress);
            factory.setServiceClass(com.wzmtr.eam.soft.mdm.supplierquery.service.impl.IGetData.class);
            com.wzmtr.eam.soft.mdm.supplierquery.service.impl.IGetData orgInterface = (com.wzmtr.eam.soft.mdm.supplierquery.service.impl.IGetData)
                    factory.create();
            responseMessage = orgInterface.getData(requestMessage);
            if (CommonConstants.WSDL_SUCCESS.equals(responseMessage.getState())) {
                List<com.wzmtr.eam.soft.mdm.supplierquery.vo.Result> result = responseMessage.getResult();
                orgList = result.stream().map(this::orgCopy).collect(Collectors.toList());
            } else {
                log.info("请求失败");
            }
        } catch (Exception e) {
            log.info("错误信息:" + e.getMessage());
        }
        if (orgList.size() > 0) {
            doOrgInsertBatch(orgList, "supp");
        }
    }

    @Override
    @Transactional
    public void syncAllExtraOrg() {
        com.wzmtr.eam.soft.mdm.extraorgquery.vo.RequestMessage requestMessage = new com.wzmtr.eam.soft.mdm.extraorgquery.vo.RequestMessage();
        com.wzmtr.eam.soft.mdm.extraorgquery.vo.Message message = new com.wzmtr.eam.soft.mdm.extraorgquery.vo.Message();
        com.wzmtr.eam.soft.mdm.extraorgquery.vo.ResponseMessage responseMessage;
        requestMessage.setVerb("Get");
        requestMessage.setNoun("allExtraOrgList");
        message.setOperType(1);
        message.setSyscode("EAM");
        requestMessage.setMessage(message);
        List<SysOffice> orgList = new ArrayList<>();
        try {
            JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
            factory.setAddress(mdmExtraOrgAddress);
            factory.setServiceClass(com.wzmtr.eam.soft.mdm.extraorgquery.service.impl.IGetData.class);
            com.wzmtr.eam.soft.mdm.extraorgquery.service.impl.IGetData orgInterface = (com.wzmtr.eam.soft.mdm.extraorgquery.service.impl.IGetData)
                    factory.create();
            responseMessage = orgInterface.getData(requestMessage);
            if (CommonConstants.WSDL_SUCCESS.equals(responseMessage.getState())) {
                List<com.wzmtr.eam.soft.mdm.extraorgquery.vo.Result> result = responseMessage.getResult();
                orgList = result.stream().map(this::orgCopy).collect(Collectors.toList());
            } else {
                log.info("请求失败");
            }
        } catch (Exception e) {
            log.info("错误信息:" + e.getMessage());
        }
        if (orgList.size() > 0) {
            doOrgInsertBatch(orgList, "extra");
        }
    }

    @Override
    @Transactional
    public void syncAllEmpJob() {
        com.wzmtr.eam.soft.mdm.empjobinfoquery.vo.RequestMessage requestMessage = new com.wzmtr.eam.soft.mdm.empjobinfoquery.vo.RequestMessage();
        com.wzmtr.eam.soft.mdm.empjobinfoquery.vo.Message message = new com.wzmtr.eam.soft.mdm.empjobinfoquery.vo.Message();
        com.wzmtr.eam.soft.mdm.empjobinfoquery.vo.ResponseMessage responseMessage = new com.wzmtr.eam.soft.mdm.empjobinfoquery.vo.ResponseMessage();
        requestMessage.setVerb("Get");
        requestMessage.setNoun("allEmpJobInfoList");
        message.setOperType(1);
        message.setSyscode("EAM");
        requestMessage.setMessage(message);
        List<SysOrgUser> empJobList = new ArrayList<>();
        try {
            JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
            factory.setAddress(mdmEmpJobAddress);
            factory.setServiceClass(com.wzmtr.eam.soft.mdm.empjobinfoquery.service.impl.IGetData.class);
            com.wzmtr.eam.soft.mdm.empjobinfoquery.service.impl.IGetData empJobInterface = (com.wzmtr.eam.soft.mdm.empjobinfoquery.service.impl.IGetData)
                    factory.create();
            responseMessage = empJobInterface.getData(requestMessage);
            if (CommonConstants.WSDL_SUCCESS.equals(responseMessage.getState())) {
                log.info("请求成功");
                List<com.wzmtr.eam.soft.mdm.empjobinfoquery.vo.Result> result = responseMessage.getResult();
                empJobList = result.stream().map(this::empJobCopy).collect(Collectors.toList());
            } else {
                log.info("请求失败");
            }
        } catch (Exception e) {
            log.info("错误信息:" + e.getMessage());
        }
        if (empJobList.size() > 0) {
            doEmpJobInsertBatch(empJobList);
        }
    }

    /**
     * 人员属性拷贝
     */
    private <T> SysUser personCopy(T result) {
        SysUser user = new SysUser();
        if (result instanceof com.wzmtr.eam.soft.mdm.personquery.vo.Result) {
            com.wzmtr.eam.soft.mdm.personquery.vo.Result result1 = (com.wzmtr.eam.soft.mdm.personquery.vo.Result) result;
            user.setId(result1.getPersonNo());
            user.setLoginName(result1.getPersonNo());
            user.setPassword(personDefaultConfig.getPassword());
            user.setUserType(personDefaultConfig.getUserType());
            user.setLoginFlag(personDefaultConfig.getLoginFlag());
            user.setNo(result1.getPersonNo());
            user.setPhone(result1.getPhoneNo());
            user.setName(result1.getPersonName());
            user.setMobile(result1.getPhoneNo());
            user.setPhone(result1.getOfficePhoneNo());
            user.setEmail(result1.getEmail());
            user.setBirthDate(result1.getBirthday());
            user.setHireDate(result1.getJoinGroupDate());
            user.setOrderNum(result1.getShowOrder());
            user.setPersonId(result1.getPersonId());
            user.setCreateBy(personDefaultConfig.getCreateBy());
            user.setUpdateBy(personDefaultConfig.getUpdateBy());
        } else {
            com.wzmtr.eam.soft.mdm.suppcontactsquery.vo.Result result2 = (com.wzmtr.eam.soft.mdm.suppcontactsquery.vo.Result) result;
            if (!StringUtils.isEmpty(result2.getPerId()) && !StringUtils.isEmpty(result2.getPhone())) {
                user.setId(result2.getPerId());
                user.setLoginName(result2.getPhone());
                user.setPassword(personDefaultConfig.getPassword());
                user.setUserType("2");
                user.setLoginFlag(personDefaultConfig.getLoginFlag());
                user.setNo(result2.getPerId());
                user.setCompanyId("W");
                user.setOfficeId(result2.getSuppId());
                user.setPhone(result2.getPhone());
                user.setName(result2.getPerName());
                user.setMobile(result2.getPhone());
                user.setPhone(result2.getPhone());
                user.setEmail(result2.getMail());
                user.setCreateBy(personDefaultConfig.getCreateBy());
                user.setUpdateBy(personDefaultConfig.getUpdateBy());
            }
        }

        return user;
    }

    /**
     * 组织机构属性拷贝
     */
    private <T> SysOffice orgCopy(T result) {
        SysOffice org = new SysOffice();
        if (result instanceof com.wzmtr.eam.soft.mdm.orgquery.vo.Result) {
            //内部组织
            com.wzmtr.eam.soft.mdm.orgquery.vo.Result result1 = (com.wzmtr.eam.soft.mdm.orgquery.vo.Result) result;
            if (result1.getOrgCode() != null && !StringUtils.isEmpty(result1.getOrgCode())
                    && result1.getOrgName() != null && !StringUtils.isEmpty(result1.getOrgName())
                    && "1".equals(result1.getStatus())) {
                org.setId(result1.getOrgCode());
                org.setParentId(result1.getParentOrgCode() == null ? "-1" : result1.getParentOrgCode());
                org.setParentIds(result1.getParentOrgCode() == null ? "-1" : "");
                org.setName(result1.getOrgName() == null ? "" : result1.getOrgName());
                org.setType(result1.getOrgType() == null ? "" : result1.getOrgType());
                org.setGrade(result1.getOrgLevel() == null ? "" : result1.getOrgLevel());
                org.setSort(result1.getOrgSequence() == null ? 0 : result1.getOrgSequence());
                org.setUseable("1");
                org.setCreateBy(personDefaultConfig.getCreateBy());
                org.setUpdateBy(personDefaultConfig.getUpdateBy());
            }
        } else if (result instanceof com.wzmtr.eam.soft.mdm.supplierquery.vo.Result) {
            //外单位
            com.wzmtr.eam.soft.mdm.supplierquery.vo.Result result2 = (com.wzmtr.eam.soft.mdm.supplierquery.vo.Result) result;
            if (StringUtils.isNotBlank(result2.getSuppName()) && !"-1".equals(result2.getSuppId()) && result2.getNodeStatus() == 1
                    && !"温州中车四方轨道车辆有限公司".equals(result2.getSuppName()) && !"中铁通轨道运营有限公司".equals(result2.getSuppName())) {
                org.setId(result2.getSuppId());
                org.setParentId("W");
                org.setParentIds("root,W");
                org.setName(result2.getSuppName());
                org.setType("3");
                org.setGrade("7");
                org.setSort(0);
                org.setUseable("1");
                org.setCreateBy(personDefaultConfig.getCreateBy());
                org.setUpdateBy(personDefaultConfig.getUpdateBy());
            }
        } else if (result instanceof com.wzmtr.eam.soft.mdm.extraorgquery.vo.Result) {
            //外单位及部室
            com.wzmtr.eam.soft.mdm.extraorgquery.vo.Result result3 = (com.wzmtr.eam.soft.mdm.extraorgquery.vo.Result) result;
            if (StringUtils.isNotBlank(result3.getName()) && result3.getId() != -1 && result3.getStatus() == 1) {
                org.setId(String.valueOf(result3.getId()));
                org.setParentId(result3.getParentId() == 0 ? "root" : String.valueOf(result3.getParentId()));
                org.setParentIds(result3.getParentId() == 0 ? "root" : "");
                org.setName(result3.getName());
                org.setType("4");
                org.setGrade("7");
                org.setSort(0);
                org.setUseable(String.valueOf(result3.getStatus()));
                org.setCreateBy(personDefaultConfig.getCreateBy());
                org.setUpdateBy(personDefaultConfig.getUpdateBy());
            }
        }
        return org;
    }

    private SysOrgUser empJobCopy(com.wzmtr.eam.soft.mdm.empjobinfoquery.vo.Result result) {

        SysOrgUser empJob = new SysOrgUser();

        empJob.setId(result.getEmpJobInfoId());
        empJob.setCompanyId(result.getCompanyCode());
        empJob.setOfficeId(result.getDepartCode());
        empJob.setUserId(result.getPersonNo());
        empJob.setOfficeName(result.getDepartName());
        empJob.setPostcode(result.getPostCode());
        empJob.setPostname(result.getPostName());
        empJob.setLatestarridate(result.getLatestArriDate());
        empJob.setLeavedate(result.getLeaveDate());
        empJob.setLeavestatus(result.getLeaveStatus());
        empJob.setDepartfullpath(result.getDepartFullPath());
        empJob.setJobcategory(result.getEmpJobInfoId());
        empJob.setJobgrade(result.getJobGrade());
        empJob.setActivestatus(result.getActiveStatus());
        empJob.setJoblevel(result.getJobLevel());
        empJob.setJobname(result.getJobName());
        empJob.setJobcategoryname(result.getJobCategoryName());
        empJob.setJoblevelname(result.getJobLevelName());
        empJob.setJobgradename(result.getJobGradeName());
        empJob.setPositionlevel(result.getPositionLevel());
        empJob.setJobleveldate(result.getJobLevelDate());
        empJob.setMdmUserId(result.getPersonId());
        empJob.setOldinfoid(result.getOldInfoId());
        empJob.setProcresult(result.getProcResult());

        return empJob;
    }

    /**
     * 批量添加人员信息
     */
    private void doPersonInsertBatch(List<SysUser> list) {
        userAccountMapper.cleanTable();
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        try {
            UserAccountMapper mapper = sqlSession.getMapper(UserAccountMapper.class);
            for (SysUser sysUser : list) {
                if (!StringUtils.isEmpty(sysUser.getId())) {
                    mapper.createPerson(sysUser);
                }
            }
            sqlSession.commit();
            List<BatchResult> batchResults = sqlSession.flushStatements();
            sqlSession.clearCache();
            sqlSession.close();
            batchResults.clear();
            System.out.println("————————————————————————————————————同步完成");
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
    }

    /**
     * 批量添加组织机构信息
     */
    private void doOrgInsertBatch(List<SysOffice> list, String type) {
        if ("org".equals(type)) {
            organizationMapper.cleanOrg();
        } else if ("supp".equals(type)) {
            organizationMapper.cleanSupplier();
        } else if ("extra".equals(type)) {
            organizationMapper.cleanExtra();
        }
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        try {
            OrganizationMapper mapper = sqlSession.getMapper(OrganizationMapper.class);
            for (SysOffice office : list) {
                if (office != null && office.getName() != null) {
                    mapper.createOrg(office);
                }
            }
            sqlSession.commit();
            sqlSession.flushStatements();
            sqlSession.clearCache();
            List<OrgParentIdsResDTO> pList = organizationMapper.searchParentIds();
            for (OrgParentIdsResDTO orgParentIds : pList) {
                mapper.updateParentIds(orgParentIds);
            }
            sqlSession.commit();
            sqlSession.flushStatements();
            sqlSession.clearCache();
            sqlSession.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
    }

    /**
     * 批量添加人员职位信息
     */
    private void doEmpJobInsertBatch(List<SysOrgUser> list) {
        userAccountMapper.cleanEmpJob();
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        try {
            UserAccountMapper mapper = sqlSession.getMapper(UserAccountMapper.class);
            List<SysUser> uList = new ArrayList<>();
            Date now = new Date();
            for (SysOrgUser sysOrgUser : list) {
                if (("1".equals(sysOrgUser.getLeavestatus())
                        || "11".equals(sysOrgUser.getLeavestatus())
                        || "12".equals(sysOrgUser.getLeavestatus()))
                        && now.before(sysOrgUser.getLeavedate())
                        && !StringUtils.isEmpty(sysOrgUser.getCompanyId())
                        && !StringUtils.isEmpty(sysOrgUser.getOfficeId())) {
                    SysUser user = new SysUser();
                    user.setId(sysOrgUser.getUserId());
                    user.setCompanyId(sysOrgUser.getCompanyId());
                    user.setOfficeId(sysOrgUser.getOfficeId());
                    uList.add(user);
                }
                mapper.createEmpJob(sysOrgUser);
            }
            sqlSession.commit();
            sqlSession.clearCache();
            if (uList.size() > 0) {
                for (SysUser user : uList) {
                    if (user.getId() != null) {
                        mapper.updateUserCompany(user);
                    }
                }
            }
            sqlSession.commit();
            sqlSession.flushStatements();
            sqlSession.clearCache();
            sqlSession.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }

    }

    /**
     * 人员扩展信息同步
     */
    private void doPersonPlusUpdateBatch(List<Map<String, Object>> plusList) {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        try {
            UserAccountMapper mapper = sqlSession.getMapper(UserAccountMapper.class);
            for (Map<String, Object> item : plusList) {
                if (!StringUtils.isEmpty(item.get("id").toString())) {
                    mapper.updatePersonPlus(item);
                }
            }
            sqlSession.commit();
            List<BatchResult> batchResults = sqlSession.flushStatements();
            sqlSession.clearCache();
            sqlSession.close();
            batchResults.clear();
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
    }
}
