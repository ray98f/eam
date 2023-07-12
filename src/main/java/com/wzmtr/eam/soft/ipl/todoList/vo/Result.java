package com.wzmtr.eam.soft.ipl.todoList.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Result implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6513420001582966178L;
	private String syscode;
	private String todo_id;
	private Integer oper_id;
	private String title;
	private String user_id;
	private Integer todo_status;
	private String eip_url;
	private String phone_url;
	private String kind_type;
	private Date todo_date;
	
}
