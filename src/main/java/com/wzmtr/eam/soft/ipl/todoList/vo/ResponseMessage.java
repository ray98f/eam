package com.wzmtr.eam.soft.ipl.todoList.vo;

import lombok.Data;

import java.util.List;

@Data
public class ResponseMessage {
	String state;
	String content;
	ErrorMessage errorMessage;
	List<Result> result;
}
