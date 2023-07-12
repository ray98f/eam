package com.wzmtr.eam.soft.ipl.newsnotify.service.impl;

import com.wzmtr.eam.soft.ipl.newsnotify.vo.RequestMessage;
import com.wzmtr.eam.soft.ipl.newsnotify.vo.ResponseMessage;

import javax.jws.WebService;

@WebService(targetNamespace = "http://impl.service.newsnotify.ipl.soft.com/")
public interface ISetData {
	
	public ResponseMessage setData(RequestMessage requestMessage);
	
}
