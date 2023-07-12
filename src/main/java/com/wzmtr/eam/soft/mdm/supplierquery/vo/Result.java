package com.wzmtr.eam.soft.mdm.supplierquery.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 供应商信息
 * 
 * @author wqf
 *
 */
public class Result implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 672941717071426773L;

	// 供应商代码
	private String suppCode;

	// 结点类型
	private Integer nodeType;

	// 供应商名称
	private String suppName;

	// 名称(外文)
	private String suppNameEn;

	// 所属公司
	private String subCompany;

	// 结点状态
	private Integer nodeStatus;

	// 创建人员
	private String createPerson;

	// 创建时间
	private Date createDate;

	// 修改人员
	private String modifyPerson;

	// 修改时间
	private Date modifyDate;

	// 外文备注
	private String remarksEn;

	// 助记码
	private String mnemonicCode;

	// 供应商类别
	private Integer suppCate;

	// 简称
	private String abbreviation;

	// 企业代码
	private String enterpriseCode;

	// 法人代表
	private String companyRepresent;

	// 税务登记号
	private String taxRegCode;

	// 注册时间
	private Date registerDate;

	// 注册资本
	private BigDecimal registerCapital;

	// 经营范围
	private String operationScope;

	// 总机
	private String officeTel;

	// 传真
	private String fax;

	// 企业邮箱
	private String email;

	// 地址
	private String address;

	// 企业网址
	private String webSite;

	// 备注
	private String remarks;

	// 供应商类型
	private String suppType;

	// 供应商ID
	private String suppId;

	// 申请编码
	private String applyCode;

	// 供应商等级
	private Integer suppLevel;

	// 父节点
	private String parentId;
	
	// 属性
	private String suppAt;

	public String getSuppCode() {
		return suppCode;
	}

	public void setSuppCode(String suppCode) {
		this.suppCode = suppCode;
	}

	public Integer getNodeType() {
		return nodeType;
	}

	public void setNodeType(Integer nodeType) {
		this.nodeType = nodeType;
	}

	public String getSuppName() {
		return suppName;
	}

	public void setSuppName(String suppName) {
		this.suppName = suppName;
	}

	public String getSuppNameEn() {
		return suppNameEn;
	}

	public void setSuppNameEn(String suppNameEn) {
		this.suppNameEn = suppNameEn;
	}

	public String getSubCompany() {
		return subCompany;
	}

	public void setSubCompany(String subCompany) {
		this.subCompany = subCompany;
	}

	public Integer getNodeStatus() {
		return nodeStatus;
	}

	public void setNodeStatus(Integer nodeStatus) {
		this.nodeStatus = nodeStatus;
	}

	public String getCreatePerson() {
		return createPerson;
	}

	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getModifyPerson() {
		return modifyPerson;
	}

	public void setModifyPerson(String modifyPerson) {
		this.modifyPerson = modifyPerson;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getRemarksEn() {
		return remarksEn;
	}

	public void setRemarksEn(String remarksEn) {
		this.remarksEn = remarksEn;
	}

	public String getMnemonicCode() {
		return mnemonicCode;
	}

	public void setMnemonicCode(String mnemonicCode) {
		this.mnemonicCode = mnemonicCode;
	}

	public Integer getSuppCate() {
		return suppCate;
	}

	public void setSuppCate(Integer suppCate) {
		this.suppCate = suppCate;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getEnterpriseCode() {
		return enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	public String getCompanyRepresent() {
		return companyRepresent;
	}

	public void setCompanyRepresent(String companyRepresent) {
		this.companyRepresent = companyRepresent;
	}

	public String getTaxRegCode() {
		return taxRegCode;
	}

	public void setTaxRegCode(String taxRegCode) {
		this.taxRegCode = taxRegCode;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public BigDecimal getRegisterCapital() {
		return registerCapital;
	}

	public void setRegisterCapital(BigDecimal registerCapital) {
		this.registerCapital = registerCapital;
	}

	public String getOperationScope() {
		return operationScope;
	}

	public void setOperationScope(String operationScope) {
		this.operationScope = operationScope;
	}

	public String getOfficeTel() {
		return officeTel;
	}

	public void setOfficeTel(String officeTel) {
		this.officeTel = officeTel;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getSuppType() {
		return suppType;
	}

	public void setSuppType(String suppType) {
		this.suppType = suppType;
	}

	public String getSuppId() {
		return suppId;
	}

	public void setSuppId(String suppId) {
		this.suppId = suppId;
	}

	public String getApplyCode() {
		return applyCode;
	}

	public void setApplyCode(String applyCode) {
		this.applyCode = applyCode;
	}

	public Integer getSuppLevel() {
		return suppLevel;
	}

	public void setSuppLevel(Integer suppLevel) {
		this.suppLevel = suppLevel;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getSuppAt() {
		return suppAt;
	}

	public void setSuppAt(String suppAt) {
		this.suppAt = suppAt;
	}
	
	
}
