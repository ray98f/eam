package com.wzmtr.eam.soft.mdm.suppcontactsquery.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 外部单位人员
 * 
 * @author wqf
 *
 */
@Data
public class Result implements Serializable {

	private static final long serialVersionUID = -2113245899433887661L;

	/**
	 * 	明细行ID
	 */
	private String perId;

	/**
	 * 	供应商ID
	 */
	private String suppId;

	/**
	 * 	代码
	 */
	private String perCode;

	/**
	 * 	姓名
	 */
	private String perName;

	/**
	 * 	职务
	 */
	private String duty;

	/**
	 * 	电话
	 */
	private String phone;

	/**
	 * 	邮件
	 */
	private String mail;

	private String extraOrg;
	
	private String isUse;
	
}
