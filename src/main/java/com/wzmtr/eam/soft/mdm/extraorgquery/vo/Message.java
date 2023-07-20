package com.wzmtr.eam.soft.mdm.extraorgquery.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Message implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -6936988529248640521L;

	private String syscode;// 系统编码

	private Integer operType;// 操作类型

	private String orgId;// 组织机构ID

	private String id;// 组织机构ID

}
