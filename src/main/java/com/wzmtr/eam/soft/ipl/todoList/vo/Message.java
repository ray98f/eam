package com.wzmtr.eam.soft.ipl.todoList.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Message implements Serializable{

	private static final long serialVersionUID = 3413804472546937169L;

	/**
	 * 系统编码
	 */
	String syscode;
	/**
	 * 待办/已办ID
	 */
	String todo_id;
	/**
	 * 操作类别(1：新增2：更新 3：删除)
	 */
	String oper_id;
	/**
	 * 标题
	 */
	String title;
	/**
	 * 用户ID
	 */
	String user_id;
	/**
	 * 待办/已办状态(1：待办 2：已办)
	 */
	String todo_status;
	/**
	 * EIP待办详情URL
	 */
	String eip_url;
	/**
	 * 移动端待办详情URL
	 */
	String phone_url;
	/**
	 * 办公类别(排序字段)
	 */
	String kind_type;
	/**
	 * 主题词
	 */
	Date todo_date;
}
