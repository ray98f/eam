package com.wzmtr.eam.utils.task;

import com.wzmtr.eam.service.MdmSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrgDataSyncTask {

    @Autowired
    private MdmSyncService mdmSyncService;

    @Value("${local.base-url}")
    private String localUrl;

    private String SYNC_PERSON_URL = "/mdmSync/syncAllPerson";
    private String SYNC_PERSON_PLUS_URL = "/mdmSync/syncPersonPlus";
    private String SYNC_ORG_URL = "/mdmSync/syncAllOrg";
    private String SYNC_SUPP_ORG_URL = "/mdmSync/syncAllSuppOrg";
    private String SYNC_EXTRA_ORG_URL = "/mdmSync/syncAllExtraOrg";
    private String SYNC_EMP_JOB_URL = "/mdmSync/syncAllEmpJob";

    @Autowired
    private RestTemplate restTemplate;

    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void syncPerson() {
        System.out.println("人员信息同步：" + localUrl + SYNC_PERSON_URL);
        mdmSyncService.syncAllPerson();
    }

    @Scheduled(cron = "0 30 1 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void syncPersonPlus() {
        System.out.println("人员附加信息同步：" + localUrl + SYNC_PERSON_PLUS_URL);
        mdmSyncService.syncPersonPlus();
    }

    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void syncOrg() {
        System.out.println("部门信息同步：" + localUrl + SYNC_ORG_URL);
        mdmSyncService.syncAllOrg();
    }

    @Scheduled(cron = "0 10 2 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void syncSuppOrg() {
        System.out.println("供应商信息同步：" + localUrl + SYNC_SUPP_ORG_URL);
        mdmSyncService.syncSuppOrg();
    }

    @Scheduled(cron = "0 20 2 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void syncExtraOrg() {
        System.out.println("外部部门信息同步：" + localUrl + SYNC_EXTRA_ORG_URL);
        mdmSyncService.syncAllExtraOrg();
    }

    @Scheduled(cron = "0 30 2 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void syncEmpJobInfo() {
        System.out.println("人员岗位信息同步：" + localUrl + SYNC_EMP_JOB_URL);
        mdmSyncService.syncAllEmpJob();
    }

}
