package com.wzmtr.eam.soft.ipl.todoList.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SearchMessage implements Serializable{

	private static final long serialVersionUID = 3413804472546937269L;

	String syscode;     // 系统编码
	String user_id;     // 用户ID
	Integer page_no;
	Integer page_size;
	String todo_status; // 待办/已办状态(1：待办 2：已办...)
	Date start_date;    // 时间范围:开始
	Date end_date;      // 时间范围:结束
}
