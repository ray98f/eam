package com.wzmtr.eam.soft.csm.planWork.vo;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceException", propOrder = {"code", "message", "msg"})
@Data
public class ServiceException {

    String code;

    String message;

    String msg;
}