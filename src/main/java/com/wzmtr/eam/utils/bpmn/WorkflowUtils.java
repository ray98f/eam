package com.wzmtr.eam.utils.bpmn;

import com.wzmtr.eam.dto.req.bpmn.StartInstanceVO;
import com.wzmtr.eam.dto.res.NodeInfo;
import com.wzmtr.eam.dto.res.NodeInfos;
import com.wzmtr.eam.dto.res.bpmn.FlowRes;
import com.wzmtr.eam.service.bpmn.BpmnService;
import com.wzmtr.eam.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

public class WorkflowUtils {

    @Autowired
    private BpmnService bpmnService;

    public static WorkflowUtils workflowUtils;

    @PostConstruct
    public void init() {
        workflowUtils = this;
        workflowUtils.bpmnService = this.bpmnService;
    }

    public static String submit(List<FlowRes> list, String id) throws Exception {
        StartInstanceVO startInstanceVO = new StartInstanceVO();
        BeanUtils.copyProperties(list.get(0), startInstanceVO);
        //插入数据
        startInstanceVO.setTypeTitle("eam流程");
        startInstanceVO.setFormData("{\"id\":\"" + id + "\"}");
        //todo:获取审核人是谁填入
        NodeInfos nodeInfos = new NodeInfos();
        NodeInfo nodeInfo = new NodeInfo();
        //获取流程引擎该表单第一个taskKey
        nodeInfo.setNodeId(workflowUtils.bpmnService.queryFirstTaskKeyByModelId(startInstanceVO.getModelId()));
        nodeInfo.setNodeHanderUser("eam");
        nodeInfos.setList(new ArrayList<NodeInfo>() {{
            add(nodeInfo);
        }});
        startInstanceVO.setNodeInfos(nodeInfos.toString());
        return workflowUtils.bpmnService.startInstance(startInstanceVO);
    }
}
