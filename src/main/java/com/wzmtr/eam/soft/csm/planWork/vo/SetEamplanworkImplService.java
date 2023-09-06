package com.wzmtr.eam.soft.csm.planWork.vo;

import com.wzmtr.eam.soft.csm.planWork.service.impl.ISetEamplanwork;

import javax.xml.namespace.QName;
import javax.xml.ws.*;
import java.net.MalformedURLException;
import java.net.URL;

@WebServiceClient(name = "SetEamplanworkImplService", targetNamespace = "http://impl.service.planwork.eam.soft.com/", wsdlLocation = "http://192.168.1.123:8080/service/EamplanworkService?wsdl")
public class SetEamplanworkImplService extends Service {

    private static final URL SETEAMPLANWORKIMPLSERVICE_WSDL_LOCATION;
    private static final WebServiceException SETEAMPLANWORKIMPLSERVICE_EXCEPTION;
    private static final QName SETEAMPLANWORKIMPLSERVICE_QNAME = new QName("http://impl.service.planwork.eam.soft.com/", "SetEamplanworkImplService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://192.168.1.123:8080/service/EamplanworkService?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        SETEAMPLANWORKIMPLSERVICE_WSDL_LOCATION = url;
        SETEAMPLANWORKIMPLSERVICE_EXCEPTION = e;
    }

    public SetEamplanworkImplService() {
        super(__getWsdlLocation(), SETEAMPLANWORKIMPLSERVICE_QNAME);
    }

    public SetEamplanworkImplService(WebServiceFeature... features) {
        super(__getWsdlLocation(), SETEAMPLANWORKIMPLSERVICE_QNAME, features);
    }

    public SetEamplanworkImplService(URL wsdlLocation) {
        super(wsdlLocation, SETEAMPLANWORKIMPLSERVICE_QNAME);
    }

    public SetEamplanworkImplService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, SETEAMPLANWORKIMPLSERVICE_QNAME, features);
    }

    public SetEamplanworkImplService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SetEamplanworkImplService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    @WebEndpoint(name = "SetEamplanworkImplPort")
    public ISetEamplanwork getSetEamplanworkImplPort() {
        return getPort(new QName("http://impl.service.planwork.eam.soft.com/", "SetEamplanworkImplPort"), ISetEamplanwork.class);
    }

    @WebEndpoint(name = "SetEamplanworkImplPort")
    public ISetEamplanwork getSetEamplanworkImplPort(WebServiceFeature... features) {
        return getPort(new QName("http://impl.service.planwork.eam.soft.com/", "SetEamplanworkImplPort"), ISetEamplanwork.class, features);
    }

    private static URL __getWsdlLocation() {
        if (SETEAMPLANWORKIMPLSERVICE_EXCEPTION != null) {
            throw SETEAMPLANWORKIMPLSERVICE_EXCEPTION;
        }
        return SETEAMPLANWORKIMPLSERVICE_WSDL_LOCATION;
    }
}