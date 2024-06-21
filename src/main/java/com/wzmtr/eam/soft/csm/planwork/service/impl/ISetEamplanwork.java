package com.wzmtr.eam.soft.csm.planwork.service.impl;

import com.wzmtr.eam.soft.csm.planwork.vo.ObjectFactory;
import com.wzmtr.eam.soft.csm.planwork.vo.RequestMessage;
import com.wzmtr.eam.soft.csm.planwork.vo.ResponseMessage;
import com.wzmtr.eam.soft.csm.planwork.vo.ServiceException_Exception;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(name = "ISetEamplanwork", targetNamespace = "http://impl.service.planwork.eam.soft.com/")
@XmlSeeAlso({ObjectFactory.class})
public interface ISetEamplanwork {

  @WebMethod
  @WebResult(targetNamespace = "")
  @RequestWrapper(localName = "setEamplanwork", targetNamespace = "http://impl.service.planwork.eam.soft.com/", className = "com.soft.eam.planwork.service.impl.SetEamplanwork")
  @ResponseWrapper(localName = "setEamplanworkResponse", targetNamespace = "http://impl.service.planwork.eam.soft.com/", className = "com.soft.eam.planwork.service.impl.SetEamplanworkResponse")
  ResponseMessage setEamplanwork(@WebParam(name = "arg0", targetNamespace = "") RequestMessage paramRequestMessage) throws ServiceException_Exception;

}
