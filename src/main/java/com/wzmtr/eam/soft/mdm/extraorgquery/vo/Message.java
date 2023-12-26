package com.wzmtr.eam.soft.mdm.extraorgquery.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Message implements Serializable {

	private static final long serialVersionUID = -6936988529248640521L;

	/**
	 * 系统编码
	 */
	private String syscode;

	/**
	 * 操作类型
	 */
	private Integer operType;

	/**
	 * 组织机构ID
	 */
	private String orgId;

	/**
	 * 组织机构ID
	 */
	private String id;

}
