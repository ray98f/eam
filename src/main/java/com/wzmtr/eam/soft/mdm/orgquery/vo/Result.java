package com.wzmtr.eam.soft.mdm.orgquery.vo;

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

	/**
	 *
	 */
	private static final long serialVersionUID = 7467763313426374308L;

	// 组织ID
	private String orgId;

	// 组织编码
	private String orgCode;

	// 组织名称
	private String orgName;

	// 组织层级
	private String orgLevel;

	// 业务板块
	private String busiSector;

	// 组织简称
	private String orgShortName;

	// 组织职能
	private String orgFunction;

	// 登记注册类型
	private String registerType;

	// 法人代表
	private String legalRepre;

	// 成立日期
	private Date establishDate;

	// 营业有效期
	private Date busiValidDate;

	// 组织机构类型
	private String orgType;

	// 记录状态
	private String status;

	// 上级机构编码
	private String parentOrgCode;

	// 上级机构名称
	private String parentOrgName;

	// 业务单元
	private String busiUnitId;

	// 部门负责人类型
	private String managerType;

	// 部门负责人
	private String managerId;

	// 生效日期
	private Date validDate;

	// 失效日期
	private Date invalidDate;

	// 是否根节点
	private String isRoot;

	// 历史ID
	private String orgHid;

	// 序号
	private Integer orgSequence;

}
