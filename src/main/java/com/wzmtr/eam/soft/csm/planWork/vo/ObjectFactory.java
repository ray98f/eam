package com.wzmtr.eam.soft.csm.planWork.vo;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {
    private static final QName _ServiceException_QNAME = new QName("http://impl.service.planwork.eam.soft.com/", "ServiceException");
    private static final QName _SetEamplanwork_QNAME = new QName("http://impl.service.planwork.eam.soft.com/", "setEamplanwork");
    private static final QName _SetEamplanworkResponse_QNAME = new QName("http://impl.service.planwork.eam.soft.com/", "setEamplanworkResponse");

    public ServiceException createServiceException() {
        return new ServiceException();
    }

    public SetEamplanwork createSetEamplanwork() {
        return new SetEamplanwork();
    }

    public SetEamplanworkResponse createSetEamplanworkResponse() {
        return new SetEamplanworkResponse();
    }

    public RequestMessage createRequestMessage() {
        return new RequestMessage();
    }

    public ErrorMessage createErrorMessage() {
        return new ErrorMessage();
    }

    public Message createMessage() {
        return new Message();
    }

    public ResponseMessage createResponseMessage() {
        return new ResponseMessage();
    }

    public User createUser() {
        return new User();
    }

    @XmlElementDecl(namespace = "http://impl.service.planwork.eam.soft.com/", name = "ServiceException")
    public JAXBElement<ServiceException> createServiceException(ServiceException value) {
        return new JAXBElement<>(_ServiceException_QNAME, ServiceException.class, null, value);
    }

    @XmlElementDecl(namespace = "http://impl.service.planwork.eam.soft.com/", name = "setEamplanwork")
    public JAXBElement<SetEamplanwork> createSetEamplanwork(SetEamplanwork value) {
        return new JAXBElement<>(_SetEamplanwork_QNAME, SetEamplanwork.class, null, value);
    }

    @XmlElementDecl(namespace = "http://impl.service.planwork.eam.soft.com/", name = "setEamplanworkResponse")
    public JAXBElement<SetEamplanworkResponse> createSetEamplanworkResponse(SetEamplanworkResponse value) {
        return new JAXBElement<>(_SetEamplanworkResponse_QNAME, SetEamplanworkResponse.class, null, value);
    }
}