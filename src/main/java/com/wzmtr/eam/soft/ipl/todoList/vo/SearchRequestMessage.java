package com.wzmtr.eam.soft.ipl.todoList.vo;

import lombok.Data;

@Data
public class SearchRequestMessage {
	String verb;
	String noun;
	User user;
	SearchMessage message;
}
