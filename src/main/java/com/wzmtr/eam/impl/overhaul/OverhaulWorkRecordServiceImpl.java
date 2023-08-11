package com.wzmtr.eam.impl.overhaul;

import com.wzmtr.eam.dto.req.OverhaulOrderReqDTO;
import com.wzmtr.eam.dto.req.OverhaulWorkRecordReqDTO;
import com.wzmtr.eam.mapper.overhaul.OverhaulWorkRecordMapper;
import com.wzmtr.eam.service.overhaul.OverhaulWorkRecordService;
import com.wzmtr.eam.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * @author frp
 */
@Service
@Slf4j
public class OverhaulWorkRecordServiceImpl implements OverhaulWorkRecordService {

    @Autowired
    private OverhaulWorkRecordMapper overhaulWorkRecordMapper;

    @Override
    public void insertRepair(OverhaulOrderReqDTO overhaulOrderReqDTO) {
        // todo ExamineRepairOrder  insertRepair  DMUtil.overTODO

        String workCode = overhaulOrderReqDTO.getWorkerCode();
        String workName = overhaulOrderReqDTO.getWorkerName();
        overhaulWorkRecordMapper.deleteByOrderCode(overhaulOrderReqDTO);
        if (workCode.length() > 2) {
            String[] workerCodes = workCode.split(",");
            String[] workerNames = workName.split(",");
            if (workerCodes.length > 0) {
                for (int i = 0; i < workerCodes.length; i++) {
                    OverhaulWorkRecordReqDTO dmer24 = new OverhaulWorkRecordReqDTO();
                    dmer24.setRecId(TokenUtil.getUuId());
                    dmer24.setOrderCode(overhaulOrderReqDTO.getOrderCode());
                    dmer24.setPlanCode(overhaulOrderReqDTO.getPlanCode());
                    dmer24.setWorkerGroupCode(overhaulOrderReqDTO.getWorkerGroupCode());
                    dmer24.setWorkerCode(workerCodes[i]);
                    dmer24.setUploadTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                    dmer24.setDownloadTime(overhaulOrderReqDTO.getSendPersonId());
                    dmer24.setExt5(overhaulOrderReqDTO.getSendTime());
                    String[] workerMsgs;
                    workerMsgs = workerNames[i].split("-");
                    dmer24.setWorkerName(workerMsgs[0]);
                    if (workerMsgs.length > 1) {
                        dmer24.setExt1(workerMsgs[1]);
                    }
                    overhaulWorkRecordMapper.insert(dmer24);
                    // todo
//                    try {
//                        if ("2".equals(overhaulOrderReqDTO.getWorkStatus())) {
//                            DMUtil.insertTODO("检修工单流转", overhaulOrderReqDTO.getRecId(), overhaulOrderReqDTO.getOrderCode(), dmer24.getWorkerCode(), "检修工单分配", "DMER0200", TokenUtil.getCurrentPersonId());
//                        } else if ("1".equals(overhaulOrderReqDTO.getWorkStatus())) {
//                            List<Map<String, String>> userList = InterfaceHelper.getUserHelpe().getUserBySubjectAndLineAndGroup(overhaulOrderReqDTO.getSubjectCode(), overhaulOrderReqDTO.getLineNo(), "DM_007");
//                            for (Map<String, String> map : userList) {
//                                DMUtil.insertTODO("检修工单流转", overhaulOrderReqDTO.getRecId(), overhaulOrderReqDTO.getOrderCode(), map.get("userId"), "检修工单下达", "DMER0200", TokenUtil.getCurrentPersonId());
//                            }
//                        }
//                    }
//                    catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }
            }
        }
    }

}
