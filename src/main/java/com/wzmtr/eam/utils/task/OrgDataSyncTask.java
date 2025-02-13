package com.wzmtr.eam.utils.task;

import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.service.common.MdmSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 定时任务-基础数据同步
 * @author  Ray
 * @version 1.0
 * @date 2023/11/20
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrgDataSyncTask {

    @Autowired
    private MdmSyncService mdmSyncService;

    @Value("${local.base-url}")
    private String localUrl;

    @Value("${local.data-sync}")
    private String dataSync;

    /**
     * 部门信息同步
     */
    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void syncOrg() {
        if (CommonConstants.OFF.equals(dataSync)) {
            return;
        }
        String syncOrgUrl = "/mdmSync/syncAllOrg";
        log.info("部门信息同步：" + localUrl + syncOrgUrl);
        mdmSyncService.syncAllOrg();
    }

    /**
     * 供应商信息同步
     */
    @Scheduled(cron = "0 10 1 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void syncSuppOrg() {
        if (CommonConstants.OFF.equals(dataSync)) {
            return;
        }
        String syncSuppOrgUrl = "/mdmSync/syncSuppOrg";
        log.info("供应商信息同步：" + localUrl + syncSuppOrgUrl);
        mdmSyncService.syncSuppOrg();
    }

    /**
     * 外部部门信息同步
     */
    @Scheduled(cron = "0 20 1 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void syncExtraOrg() {
        if (CommonConstants.OFF.equals(dataSync)) {
            return;
        }
        String syncExtraOrgUrl = "/mdmSync/syncAllExtraOrg";
        log.info("外部部门信息同步：" + localUrl + syncExtraOrgUrl);
        mdmSyncService.syncAllExtraOrg();
    }

    /**
     * 基础数据部门名称同步
     */
    @Scheduled(cron = "0 30 1 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void syncSysOrgName() {
        if (CommonConstants.OFF.equals(dataSync)) {
            return;
        }
        mdmSyncService.syncSysOrgName();
    }

    /**
     * 人员信息同步
     */
    @Scheduled(cron = "0 40 1 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void syncPerson() {
        if (CommonConstants.OFF.equals(dataSync)) {
            return;
        }
        String syncPersonUrl = "/mdmSync/syncAllPerson";
        log.info("人员信息同步：" + localUrl + syncPersonUrl);
        mdmSyncService.syncAllPerson();
    }

    /**
     * 外部单位人员信息同步
     */
    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void syncAllSuppContact() {
        if (CommonConstants.OFF.equals(dataSync)) {
            return;
        }
        String syncSuppContactUrl = "/mdmSync/syncAllSuppContact";
        log.info("外部单位人员信息同步：" + localUrl + syncSuppContactUrl);
        mdmSyncService.syncAllSuppContact();
    }

    /**
     * 人员附加信息同步
     */
    @Scheduled(cron = "0 10 2 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void syncPersonPlus() {
        if (CommonConstants.OFF.equals(dataSync)) {
            return;
        }
        String syncPersonPlusUrl = "/mdmSync/syncPersonPlus";
        log.info("人员附加信息同步：" + localUrl + syncPersonPlusUrl);
        mdmSyncService.syncPersonPlus();
    }

    /**
     * 岗位信息同步
     */
    @Scheduled(cron = "0 20 2 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void syncEmpJobInfo() {
        if (CommonConstants.OFF.equals(dataSync)) {
            return;
        }
        String syncEmpJobUrl = "/mdmSync/syncAllEmpJob";
        log.info("岗位信息同步：" + localUrl + syncEmpJobUrl);
        mdmSyncService.syncAllEmpJob();
    }

}
