package com.wzmtr.eam.utils;

import com.alibaba.fastjson.JSONObject;
import com.wzmtr.eam.dto.res.overTodo.QueryNotWorkFlowResDTO;
import com.wzmtr.eam.dto.req.EipMsgPushReq;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import java.util.Date;

@Slf4j
public class EipMsgPushUtils {
    public static void invokeTodoList(EipMsgPushReq eipMsgPushReq) {
//        com.wzmtr.eam.soft.ipl.todoList.vo.RequestMessage requestMessage = new com.wzmtr.eam.soft.ipl.todoList.vo.RequestMessage();
//        com.wzmtr.eam.soft.ipl.todoList.vo.Message message = new com.wzmtr.eam.soft.ipl.todoList.vo.Message();
//        com.wzmtr.eam.soft.ipl.todoList.vo.ResponseMessage responseMessage;
//        requestMessage.setVerb("CREATE");
//        requestMessage.setNoun("todoList");
//        Date todoDate = new Date(System.currentTimeMillis());
//        message.setOper_id(eipMsgPushReq.getOperId());
//        if (eipMsgPushReq.getTitle() != null && !"".equals(eipMsgPushReq.getTitle())) {
//            message.setTitle(eipMsgPushReq.getTitle());
//        }
//        message.setUser_id(eipMsgPushReq.getUserId());
//        message.setSyscode("EAM");
//        message.setTodo_status(eipMsgPushReq.getTodoStatus());
//        message.setEip_url(eipMsgPushReq.getEipUrl());
//        message.setPhone_url(eipMsgPushReq.getPhoneUrl());
//        message.setKind_type(eipMsgPushReq.getKindType());
//        message.setTodo_date(todoDate);
//        message.setTodo_id(eipMsgPushReq.getTodoId());
//        log.info("EIP推送对象：" + JSONObject.toJSONString(message));
//        requestMessage.setMessage(message);
//        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
//        factory.setAddress("http://10.11.82.82:8083/iplwebservice/CREATEtodoList?wsdl");
////        factory.setAddress("http://esb.wzmtr.com:7003/iplwebservice/ps/CREATEtodoList?wsdl");
//        factory.setServiceClass(com.wzmtr.eam.soft.ipl.todoList.service.impl.ISetData.class);
//        com.wzmtr.eam.soft.ipl.todoList.service.impl.ISetData todoInterface = (com.wzmtr.eam.soft.ipl.todoList.service.impl.ISetData) factory.create();
//        responseMessage = todoInterface.setData(requestMessage);
//        log.info(String.valueOf(responseMessage));
    }
}
