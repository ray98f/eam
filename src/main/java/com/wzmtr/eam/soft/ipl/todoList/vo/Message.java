package com.wzmtr.eam.soft.ipl.todoList.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Message implements Serializable{

	private static final long serialVersionUID = 3413804472546937169L;

	String syscode; // 系统编码
	String todo_id; // 待办/已办ID
	String oper_id; // 操作类别(1：新增2：更新 3：删除)
	String title; // 标题
	String user_id; // 用户ID
	String todo_status; //待办/已办状态(1：待办 2：已办)
	String eip_url; // EIP待办详情URL
	String phone_url;// 移动端待办详情URL
	String kind_type; // 办公类别(排序字段)
	Date todo_date; // 主题词
}
