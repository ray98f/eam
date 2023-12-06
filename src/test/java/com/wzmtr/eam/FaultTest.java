package com.wzmtr.eam;

import com.wzmtr.eam.bizobject.WorkFlowLogBO;
import com.wzmtr.eam.dataobject.FaultTrackDO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulObjectReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.impl.overhaul.OverhaulWeekPlanServiceImpl;
import com.wzmtr.eam.mapper.fault.FaultTrackWorkMapper;
import com.wzmtr.eam.service.bpmn.IWorkFlowLogService;
import com.wzmtr.eam.utils.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class FaultTest {
    @Autowired
    private FaultTrackWorkMapper faultTrackWorkMapper;

    @Test
    public void test() {
        FaultTrackDO faultTrackDO = new FaultTrackDO();
        faultTrackDO.setCompanyCode("D");
        faultTrackDO.setCompanyName("运营分公司");
        faultTrackDO.setFaultNo("GZ2308250010");
        faultTrackDO.setFaultWorkNo("GD2308280001");
        faultTrackDO.setTrackReason("213");
        faultTrackDO.setTrackUserId("admin");
        faultTrackDO.setTrackTime("2023-11-24 11:22:36");
        faultTrackDO.setTrackStartDate("2023-11-14");
        faultTrackDO.setTrackEndDate("2023-11-24");
        faultTrackDO.setTrackPeriod(0);
        faultTrackDO.setTrackCycle(231);
        faultTrackDO.setDocId("d111b183b48f456ba55ef110446ba455,1b74ec75e9074449a179ed5b10024d9d,4eaca6c8fd8b43d7b967f7274451369e,bd11b03f663840dfa2673c395f16ac4d");
        faultTrackDO.setRecCreator("admin");
        faultTrackDO.setRecCreateTime("2023-11-24 16:49:53");
        faultTrackDO.setDeleteFlag("0");
    }

    @Autowired
    private OverhaulWeekPlanServiceImpl overhaulWeekPlanServiceImpl;

    @Test
    public void testObj() {
        // {"objectCode":"1","templateId":"JM001021","planCode":"JX00142411","planName":"1"}
        OverhaulObjectReqDTO overhaulObjectReqDTO = new OverhaulObjectReqDTO();
        overhaulObjectReqDTO.setObjectCode("1");
        overhaulObjectReqDTO.setTemplateId("JM001021");
        overhaulObjectReqDTO.setPlanCode("JX00142411");
        overhaulObjectReqDTO.setPlanName("1");
        overhaulWeekPlanServiceImpl.detailJudge(overhaulObjectReqDTO);
    }

    @Test
    public void testAssert() {
        Assert.notNull(null, ErrorCode.NORMAL_ERROR, "obj为null");
    }

    @Autowired
    private IWorkFlowLogService workFlowLogService;
    @Test
    public void testWorkFlowLog() {
        WorkFlowLogBO build = WorkFlowLogBO.builder().workFlowInstId("JX00142411").userIds(Collections.singletonList("test")).status("test").creator("test").build();
        workFlowLogService.add(build);
    }

    @Test
    public void testThread(){
        // Executors.newCachedThreadPool()
    }

}