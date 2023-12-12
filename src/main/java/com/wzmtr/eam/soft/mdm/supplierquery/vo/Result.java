package com.wzmtr.eam.soft.mdm.supplierquery.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 供应商信息
 * 
 * @author wqf
 *
 */
@Data
public class Result implements Serializable {

	private static final long serialVersionUID = 672941717071426773L;

	/**
	 *	供应商代码
	 */
	private String suppCode;

	/**
	 *	结点类型
	 */
	private Integer nodeType;

	/**
	 *	供应商名称
	 */
	private String suppName;

	/**
	 *	名称(外文)
	 */
	private String suppNameEn;

	/**
	 *	所属公司
	 */
	private String subCompany;

	/**
	 *	结点状态
	 */
	private Integer nodeStatus;

	/**
	 *	创建人员
	 */
	private String createPerson;

	/**
	 *	创建时间
	 */
	private Date createDate;

	/**
	 *	修改人员
	 */
	private String modifyPerson;

	/**
	 *	修改时间
	 */
	private Date modifyDate;

	/**
	 *	外文备注
	 */
	private String remarksEn;

	/**
	 *	助记码
	 */
	private String mnemonicCode;

	/**
	 *	供应商类别
	 */
	private Integer suppCate;

	/**
	 *	简称
	 */
	private String abbreviation;

	/**
	 *	企业代码
	 */
	private String enterpriseCode;

	/**
	 *	法人代表
	 */
	private String companyRepresent;

	/**
	 *	税务登记号
	 */
	private String taxRegCode;

	/**
	 *	注册时间
	 */
	private Date registerDate;

	/**
	 *	注册资本
	 */
	private BigDecimal registerCapital;

	/**
	 *	经营范围
	 */
	private String operationScope;

	/**
	 *	总机
	 */
	private String officeTel;

	/**
	 *	传真
	 */
	private String fax;

	/**
	 *	企业邮箱
	 */
	private String email;

	/**
	 *	地址
	 */
	private String address;

	/**
	 *	企业网址
	 */
	private String webSite;

	/**
	 *	备注
	 */
	private String remarks;

	/**
	 *	供应商类型
	 */
	private String suppType;

	/**
	 *	供应商ID
	 */
	private String suppId;

	/**
	 *	申请编码
	 */
	private String applyCode;

	/**
	 *	供应商等级
	 */
	private Integer suppLevel;

	/**
	 *	父节点
	 */
	private String parentId;

	/**
	 *	属性
	 */
	private String suppAt;
	
}
