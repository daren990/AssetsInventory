package com.uhf.vo;

/**
 * 资产信息表
 * @author lbb
 *
 */
public class Asset {
	private int id;// 资产ID
	private String name;// 资产名称
	private String assetType;// 资产类别
	private String assetNo;// 资产编号
	private String address;// 存放地址
	private String custodian;// 保管员
	private String status;// 资产状态
	private String labelId;// 标签ID
	private String cTime;// 盘点的操作时间
	private int pStatus;// 盘点的状态，0：未盘点、1：已盘点
	private long pcFlag;// 某次盘点的标记
	private String deptId;// 部门id
	private String brand;// 资产品牌
	private float price;// 资产价格
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAssetType() {
		return assetType;
	}
	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}
	public String getAssetNo() {
		return assetNo;
	}
	public void setAssetNo(String assetNo) {
		this.assetNo = assetNo;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCustodian() {
		return custodian;
	}
	public void setCustodian(String custodian) {
		this.custodian = custodian;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLabelId() {
		return labelId;
	}
	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}
	public String getcTime() {
		return cTime;
	}
	public void setcTime(String cTime) {
		this.cTime = cTime;
	}
	public int getpStatus() {
		return pStatus;
	}
	public void setpStatus(int pStatus) {
		this.pStatus = pStatus;
	}
	public long getPcFlag() {
		return pcFlag;
	}
	public void setPcFlag(long pcFlag) {
		this.pcFlag = pcFlag;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
}
