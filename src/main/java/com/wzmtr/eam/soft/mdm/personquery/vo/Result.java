package com.wzmtr.eam.soft.mdm.personquery.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 人员信息
 *
 * @author wqf
 *
 */
public class Result implements Serializable {

	private static final long serialVersionUID = 5393448087925727882L;

	private String personId;

	/**
	 *	工号
	 */
	private String personNo;

	/**
	 *	姓名
	 */
	private String personName;

	/**
	 *	姓名全拼
	 */
	private String personFullName;

	/**
	 *	曾用名
	 */
	private String beforeName;

	/**
	 *	性别
	 */
	private String personSex;

	/**
	 *	民族
	 */
	private String nation;

	/**
	 *	籍贯
	 */
	private String nativePlace;

	/**
	 *	国籍
	 */
	private String nationality;

	/**
	 *	婚姻状况
	 */
	private String marital;

	/**
	 *	政治面貌
	 */
	private String political;

	/**
	 *	民主党派
	 */
	private String democraticParty;

	/**
	 *	出生日期
	 */
	private Date birthday;

	/**
	 *	出生地
	 */
	private String bitrdhPlace;

	/**
	 *	年龄
	 */
	private Integer age;

	/**
	 *	身份证号码
	 */
	private String idCardNo;

	/**
	 *	身份证地址
	 */
	private String idCardAddress;

	/**
	 *	身份证生效日期
	 */
	private Date idCardEffectDate;

	/**
	 *	身份证截止日期
	 */
	private Date idCardExpDate;

	/**
	 *	身份证签发机关
	 */
	private String idCardSignOffice;

	/**
	 *	护照号码
	 */
	private String passportNo;

	/**
	 *	户口簿编号
	 */
	private String householdRegNo;

	/**
	 *	户口类型
	 */
	private String householdType;

	/**
	 *	户口所在地
	 */
	private String householdAddress;

	/**
	 *	照片
	 */
	private byte[] photo;

	/**
	 *	开始工作日期
	 */
	private Date startWorkDate;

	/**
	 *	用工日期
	 */
	private Date workingDate;

	/**
	 *	司龄（周年）
	 */
	private Integer serviceAge;

	/**
	 *	司龄（虚年）
	 */
	private Integer serviceAgeXu;

	/**
	 *	用工形式
	 */
	private String workType;

	/**
	 *	员工来源
	 */
	private String personSource;

	/**
	 *	工龄（周年）
	 */
	private Integer workAge;

	/**
	 *	工龄（虚年）
	 */
	private Integer workAgeX;

	/**
	 *	入司前工龄
	 */
	private Integer inCompanyWorkAge;

	/**
	 *	最高学历
	 */
	private String highestEdu;

	/**
	 *	最高学位
	 */
	private String highestDegree;

	/**
	 *	入职来源
	 */
	private String entrySource;

	/**
	 *	加入集团日期
	 */
	private Date joinGroupDate;

	/**
	 *	加入当前公司日期
	 */
	private Date joinCompanyDate;

	/**
	 *	相关工作年限
	 */
	private Integer workYear;

	/**
	 *	相关工作年限（虚）
	 */
	private Integer workYearX;

	/**
	 *	非相关工作年限
	 */
	private Integer noRelWorkYear;

	/**
	 *	员工身份
	 */
	private String personIdentity;

	/**
	 *	最高职称
	 */
	private String highestTitle;

	/**
	 *	转正日期
	 */
	private Date toOffiDate;

	/**
	 *	电子邮件
	 */
	private String email;

	/**
	 *	手机
	 */
	private String phoneNo;

	/**
	 *	家庭电话
	 */
	private String homePhoneNo;

	/**
	 *	办公电话
	 */
	private String officePhoneNo;

	/**
	 *	邮政编码
	 */
	private String zipCode;

	/**
	 *	显示序号
	 */
	private Integer showOrder;

	public String getPersonNo() {
		return personNo;
	}

	public void setPersonNo(String personNo) {
		this.personNo = personNo;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getPersonFullName() {
		return personFullName;
	}

	public void setPersonFullName(String personFullName) {
		this.personFullName = personFullName;
	}

	public String getBeforeName() {
		return beforeName;
	}

	public void setBeforeName(String beforeName) {
		this.beforeName = beforeName;
	}

	public String getPersonSex() {
		return personSex;
	}

	public void setPersonSex(String personSex) {
		this.personSex = personSex;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getNativePlace() {
		return nativePlace;
	}

	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getMarital() {
		return marital;
	}

	public void setMarital(String marital) {
		this.marital = marital;
	}

	public String getPolitical() {
		return political;
	}

	public void setPolitical(String political) {
		this.political = political;
	}

	public String getDemocraticParty() {
		return democraticParty;
	}

	public void setDemocraticParty(String democraticParty) {
		this.democraticParty = democraticParty;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getBitrdhPlace() {
		return bitrdhPlace;
	}

	public void setBitrdhPlace(String bitrdhPlace) {
		this.bitrdhPlace = bitrdhPlace;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getIdCardAddress() {
		return idCardAddress;
	}

	public void setIdCardAddress(String idCardAddress) {
		this.idCardAddress = idCardAddress;
	}

	public Date getIdCardEffectDate() {
		return idCardEffectDate;
	}

	public void setIdCardEffectDate(Date idCardEffectDate) {
		this.idCardEffectDate = idCardEffectDate;
	}

	public Date getIdCardExpDate() {
		return idCardExpDate;
	}

	public void setIdCardExpDate(Date idCardExpDate) {
		this.idCardExpDate = idCardExpDate;
	}

	public String getIdCardSignOffice() {
		return idCardSignOffice;
	}

	public void setIdCardSignOffice(String idCardSignOffice) {
		this.idCardSignOffice = idCardSignOffice;
	}

	public String getPassportNo() {
		return passportNo;
	}

	public void setPassportNo(String passportNo) {
		this.passportNo = passportNo;
	}

	public String getHouseholdRegNo() {
		return householdRegNo;
	}

	public void setHouseholdRegNo(String householdRegNo) {
		this.householdRegNo = householdRegNo;
	}

	public String getHouseholdType() {
		return householdType;
	}

	public void setHouseholdType(String householdType) {
		this.householdType = householdType;
	}

	public String getHouseholdAddress() {
		return householdAddress;
	}

	public void setHouseholdAddress(String householdAddress) {
		this.householdAddress = householdAddress;
	}

	public Date getStartWorkDate() {
		return startWorkDate;
	}

	public void setStartWorkDate(Date startWorkDate) {
		this.startWorkDate = startWorkDate;
	}

	public Date getWorkingDate() {
		return workingDate;
	}

	public void setWorkingDate(Date workingDate) {
		this.workingDate = workingDate;
	}

	public Integer getServiceAge() {
		return serviceAge;
	}

	public void setServiceAge(Integer serviceAge) {
		this.serviceAge = serviceAge;
	}

	public Integer getServiceAgeXu() {
		return serviceAgeXu;
	}

	public void setServiceAgeXu(Integer serviceAgeXu) {
		this.serviceAgeXu = serviceAgeXu;
	}

	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	public String getPersonSource() {
		return personSource;
	}

	public void setPersonSource(String personSource) {
		this.personSource = personSource;
	}

	public Integer getWorkAge() {
		return workAge;
	}

	public void setWorkAge(Integer workAge) {
		this.workAge = workAge;
	}

	public Integer getWorkAgeX() {
		return workAgeX;
	}

	public void setWorkAgeX(Integer workAgeX) {
		this.workAgeX = workAgeX;
	}

	public Integer getInCompanyWorkAge() {
		return inCompanyWorkAge;
	}

	public void setInCompanyWorkAge(Integer inCompanyWorkAge) {
		this.inCompanyWorkAge = inCompanyWorkAge;
	}

	public String getHighestEdu() {
		return highestEdu;
	}

	public void setHighestEdu(String highestEdu) {
		this.highestEdu = highestEdu;
	}

	public String getHighestDegree() {
		return highestDegree;
	}

	public void setHighestDegree(String highestDegree) {
		this.highestDegree = highestDegree;
	}

	public String getEntrySource() {
		return entrySource;
	}

	public void setEntrySource(String entrySource) {
		this.entrySource = entrySource;
	}

	public Date getJoinGroupDate() {
		return joinGroupDate;
	}

	public void setJoinGroupDate(Date joinGroupDate) {
		this.joinGroupDate = joinGroupDate;
	}

	public Date getJoinCompanyDate() {
		return joinCompanyDate;
	}

	public void setJoinCompanyDate(Date joinCompanyDate) {
		this.joinCompanyDate = joinCompanyDate;
	}

	public Integer getWorkYear() {
		return workYear;
	}

	public void setWorkYear(Integer workYear) {
		this.workYear = workYear;
	}

	public Integer getWorkYearX() {
		return workYearX;
	}

	public void setWorkYearX(Integer workYearX) {
		this.workYearX = workYearX;
	}

	public Integer getNoRelWorkYear() {
		return noRelWorkYear;
	}

	public void setNoRelWorkYear(Integer noRelWorkYear) {
		this.noRelWorkYear = noRelWorkYear;
	}

	public String getPersonIdentity() {
		return personIdentity;
	}

	public void setPersonIdentity(String personIdentity) {
		this.personIdentity = personIdentity;
	}

	public String getHighestTitle() {
		return highestTitle;
	}

	public void setHighestTitle(String highestTitle) {
		this.highestTitle = highestTitle;
	}

	public Date getToOffiDate() {
		return toOffiDate;
	}

	public void setToOffiDate(Date toOffiDate) {
		this.toOffiDate = toOffiDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getHomePhoneNo() {
		return homePhoneNo;
	}

	public void setHomePhoneNo(String homePhoneNo) {
		this.homePhoneNo = homePhoneNo;
	}

	public String getOfficePhoneNo() {
		return officePhoneNo;
	}

	public void setOfficePhoneNo(String officePhoneNo) {
		this.officePhoneNo = officePhoneNo;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public Integer getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}

}
