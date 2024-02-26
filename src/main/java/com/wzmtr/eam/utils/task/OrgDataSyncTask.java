package com.wzmtr.eam.utils.task;

import com.wzmtr.eam.service.common.MdmSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrgDataSyncTask {

    @Autowired
    private MdmSyncService mdmSyncService;

    @Value("${local.base-url}")
    private String localUrl;

    @Scheduled(cron = "0 30 1 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void syncPerson() {
        String syncPersonUrl = "/mdmSync/syncAllPerson";
        log.info("人员信息同步：" + localUrl + syncPersonUrl);
        mdmSyncService.syncAllPerson();
    }

    @Scheduled(cron = "0 50 1 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void syncAllSuppContact() {
        String syncSuppContactUrl = "/mdmSync/syncAllSuppContact";
        log.info("人员信息同步：" + localUrl + syncSuppContactUrl);
        mdmSyncService.syncAllSuppContact();
    }

    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void syncPersonPlus() {
        String syncPersonPlusUrl = "/mdmSync/syncPersonPlus";
        log.info("人员附加信息同步：" + localUrl + syncPersonPlusUrl);
        mdmSyncService.syncPersonPlus();
    }

    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void syncOrg() {
        String syncOrgUrl = "/mdmSync/syncAllOrg";
        log.info("部门信息同步：" + localUrl + syncOrgUrl);
        mdmSyncService.syncAllOrg();
    }

    @Scheduled(cron = "0 10 1 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void syncSuppOrg() {
        String syncSuppOrgUrl = "/mdmSync/syncSuppOrg";
        log.info("供应商信息同步：" + localUrl + syncSuppOrgUrl);
        mdmSyncService.syncSuppOrg();
    }

    @Scheduled(cron = "0 20 1 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void syncExtraOrg() {
        String syncExtraOrgUrl = "/mdmSync/syncAllExtraOrg";
        log.info("外部部门信息同步：" + localUrl + syncExtraOrgUrl);
        mdmSyncService.syncAllExtraOrg();
    }

    @Scheduled(cron = "0 30 2 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void syncEmpJobInfo() {
        String syncEmpJobUrl = "/mdmSync/syncAllEmpJob";
        log.info("人员岗位信息同步：" + localUrl + syncEmpJobUrl);
        mdmSyncService.syncAllEmpJob();
    }

}
