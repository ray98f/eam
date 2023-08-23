package com.wzmtr.eam.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author lize
 * @Date 2023/8/22
 */
@Component
public class BpmnConstants {
    @Value("${bpmn.account}")
    private String bpmnAccount;
    @Value("${bpmn.password}")
    private String bpmnPassword;

    public static String BPMN_ACCOUNT;
    public static String BPMN_PASSWORD;

    @Value("${eip.url}")
    private String eipUrl;
    public static String EIP_URL;
    @Value("${bpmn.url}")
    private String bpmnUrl;
    public static String BPMN_URL;

    @PostConstruct
    private void init() {
        BPMN_ACCOUNT = bpmnAccount;
        BPMN_PASSWORD = bpmnPassword;
        EIP_URL = eipUrl;
        BPMN_URL = bpmnUrl;
    }
}
