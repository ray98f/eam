package com.wzmtr.eam.soft.mdm.supplierquery.service.impl;

import com.wzmtr.eam.soft.mdm.supplierquery.vo.RequestMessage;
import com.wzmtr.eam.soft.mdm.supplierquery.vo.ResponseMessage;

import javax.jws.WebService;

@WebService(targetNamespace = "http://impl.service.supplierquery.mdm.soft.com/")
public interface IGetData {

	public ResponseMessage getData(RequestMessage requestMessage);
}
