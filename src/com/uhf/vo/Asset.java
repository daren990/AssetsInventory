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
}
