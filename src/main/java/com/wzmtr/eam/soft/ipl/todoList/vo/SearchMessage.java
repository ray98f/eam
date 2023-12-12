package com.wzmtr.eam.soft.ipl.todoList.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SearchMessage implements Serializable{

	private static final long serialVersionUID = 3413804472546937269L;

	/**
	 * 系统编码
	 */
	String syscode;
	/**
	 * 用户ID
	 */
	String user_id;
	/**
	 * 页码
	 */
	Integer page_no;
	/**
	 * 页数
	 */
	Integer page_size;
	/**
	 * 待办/已办状态(1：待办 2：已办...)
	 */
	String todo_status;
	/**
	 * 时间范围:开始
	 */
	Date start_date;
	/**
	 * 时间范围:结束
	 */
	Date end_date;
}
