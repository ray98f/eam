package com.wzmtr.eam.soft.ipl.todoList.service.impl;

import com.wzmtr.eam.soft.ipl.todoList.vo.RequestMessage;
import com.wzmtr.eam.soft.ipl.todoList.vo.ResponseMessage;

import javax.jws.WebService;


@WebService(targetNamespace = "http://impl.service.todoList.ipl.soft.com/")
public interface ISetData {
	
	public ResponseMessage setData(RequestMessage requestMessage);
	
}
