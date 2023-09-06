package com.wzmtr.eam.soft.csm.planWork.vo;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
 
 
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setEamplanworkResponse", propOrder = {"_return"})
@Data
public class SetEamplanworkResponse {
    @XmlElement(name = "return")
    protected ResponseMessage _return;

}

