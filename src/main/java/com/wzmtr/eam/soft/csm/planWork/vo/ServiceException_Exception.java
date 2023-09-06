package com.wzmtr.eam.soft.csm.planWork.vo;
 
import javax.xml.ws.WebFault;

@WebFault(name = "ServiceException", targetNamespace = "http://impl.service.planwork.eam.soft.com/")
public class ServiceException_Exception extends Exception {
    private ServiceException faultInfo;

    public ServiceException_Exception(String message, ServiceException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    public ServiceException_Exception(String message, ServiceException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    public ServiceException getFaultInfo() {
        return this.faultInfo;
    }
}