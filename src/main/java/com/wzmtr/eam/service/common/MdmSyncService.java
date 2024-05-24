package com.wzmtr.eam.service.common;

public interface MdmSyncService {

    void syncAllPerson();

    void syncAllSuppContact();

    void syncPersonPlus();

    void syncAllOrg();

    void syncSuppOrg();

    void syncAllExtraOrg();

    /**
     * 基础数据部门名称同步
     */
    void syncSysOrgName();

    void syncAllEmpJob();

}
