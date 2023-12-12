package com.wzmtr.eam.soft.mdm.extraorgquery.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 组织机构
 *
 * @author wqf
 *
 */
@Data
public class Result implements Serializable {

	private static final long serialVersionUID = 7467763313426374308L;

	/**
	 * 组织ID
	 */
	private Integer id;

	/**
	 * 组织编码
	 */
	private String code;

	/**
	 * 组织名称
	 */
	private String name;

	/**
	 * 组织机构类型
	 */
	private Integer nodeType;

	/**
	 * 记录状态
	 */
	private Integer status;

	/**
	 * 上级机构编码
	 */
	private Integer parentId;

	/**
	 * 层级
	 */
	private Integer orgLevel;

	private Integer createById;

	private Date createTime;

	private Integer updateById;

	private Date updateTime;

}
