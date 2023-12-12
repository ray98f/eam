package com.wzmtr.eam.soft.mdm.empjobinfoquery.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 任职岗位信息
 *
 * @author wqf
 *
 */
public class Result implements Serializable {

	private static final long serialVersionUID = 5393448087925727882L;

	/**
	 * 任职信息ID
	 */
	private String empJobInfoId;

	/**
	 * 员工ID
	 */
	private String personId;

	/**
	 * 员工编号
	 */
	private String personNo;

	/**
	 * 所属部门
	 */
	private String departCode;

	/**
	 * 所属部门名称
	 */
	private String departName;

	/**
	 * 所属公司
	 */
	private String companyCode;

	/**
	 * 所属公司名称
	 */
	private String companyName;

	/**
	 * 岗位编号
	 */
	private String postCode;

	/**
	 * 岗位名称
	 */
	private String postName;

	/**
	 * 最新到岗日期
	 */
	private Date latestArriDate;

	/**
	 * 离职日期
	 */
	private Date leaveDate;

	/**
	 * 状态
	 */
	private String leaveStatus;

	/**
	 * 所在组织路径
	 */
	private String departFullPath;

	/**
	 * 岗位序列
	 */
	private String jobCategory;

	/**
	 * 职等
	 */
	private String jobGrade;

	/**
	 * 在职状态
	 */
	private String activeStatus;

	/**
	 * 职级
	 */
	private String jobLevel;

	/**
	 * 职位名称
	 */
	private String jobName;

	/**
	 * 岗位序列名称
	 */
	private String jobCategoryName;

	/**
	 * 职级名称
	 */
	private String jobLevelName;

	/**
	 * 职等名称
	 */
	private String jobGradeName;

	/**
	 * 职位层级
	 */
	private String positionLevel;

	/**
	 * 职级开始日期
	 */
	private Date jobLevelDate;

	/**
	 * 变动前信息ID
	 */
	private String oldInfoId;

	/**
	 * 审批状态
	 */
	private String procResult;

	/**
	 * 范围类型
	 */
	private String scopeType;

	/**
	 * 期限类型
	 */
	private String termType;

	public String getEmpJobInfoId() {
		return empJobInfoId;
	}

	public void setEmpJobInfoId(String empJobInfoId) {
		this.empJobInfoId = empJobInfoId;
	}

	public String getPersonNo() {
		return personNo;
	}

	public void setPersonNo(String personNo) {
		this.personNo = personNo;
	}

	public String getDepartCode() {
		return departCode;
	}

	public void setDepartCode(String departCode) {
		this.departCode = departCode;
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public Date getLatestArriDate() {
		return latestArriDate;
	}

	public void setLatestArriDate(Date latestArriDate) {
		this.latestArriDate = latestArriDate;
	}

	public Date getLeaveDate() {
		return leaveDate;
	}

	public void setLeaveDate(Date leaveDate) {
		this.leaveDate = leaveDate;
	}

	public String getLeaveStatus() {
		return leaveStatus;
	}

	public void setLeaveStatus(String leaveStatus) {
		this.leaveStatus = leaveStatus;
	}

	public String getDepartFullPath() {
		return departFullPath;
	}

	public void setDepartFullPath(String departFullPath) {
		this.departFullPath = departFullPath;
	}

	public String getJobCategory() {
		return jobCategory;
	}

	public void setJobCategory(String jobCategory) {
		this.jobCategory = jobCategory;
	}

	public String getJobGrade() {
		return jobGrade;
	}

	public void setJobGrade(String jobGrade) {
		this.jobGrade = jobGrade;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getJobLevel() {
		return jobLevel;
	}

	public void setJobLevel(String jobLevel) {
		this.jobLevel = jobLevel;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobCategoryName() {
		return jobCategoryName;
	}

	public void setJobCategoryName(String jobCategoryName) {
		this.jobCategoryName = jobCategoryName;
	}

	public String getJobLevelName() {
		return jobLevelName;
	}

	public void setJobLevelName(String jobLevelName) {
		this.jobLevelName = jobLevelName;
	}

	public String getJobGradeName() {
		return jobGradeName;
	}

	public void setJobGradeName(String jobGradeName) {
		this.jobGradeName = jobGradeName;
	}

	public String getPositionLevel() {
		return positionLevel;
	}

	public void setPositionLevel(String positionLevel) {
		this.positionLevel = positionLevel;
	}

	public Date getJobLevelDate() {
		return jobLevelDate;
	}

	public void setJobLevelDate(Date jobLevelDate) {
		this.jobLevelDate = jobLevelDate;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getOldInfoId() {
		return oldInfoId;
	}

	public void setOldInfoId(String oldInfoId) {
		this.oldInfoId = oldInfoId;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getProcResult() {
		return procResult;
	}

	public void setProcResult(String procResult) {
		this.procResult = procResult;
	}

	public String getScopeType() {
		return scopeType;
	}

	public void setScopeType(String scopeType) {
		this.scopeType = scopeType;
	}

	public String getTermType() {
		return termType;
	}

	public void setTermType(String termType) {
		this.termType = termType;
	}

}
