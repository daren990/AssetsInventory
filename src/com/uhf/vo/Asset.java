package com.uhf.vo;

/**
 * �ʲ���Ϣ��
 * @author lbb
 *
 */
public class Asset {
	private int id;// �ʲ�ID
	private String name;// �ʲ�����
	private String assetType;// �ʲ����
	private String assetNo;// �ʲ����
	private String address;// ��ŵ�ַ
	private String custodian;// ����Ա
	private String status;// �ʲ�״̬
	private String labelId;// ��ǩID
	private String cTime;// �̵�Ĳ���ʱ��
	private int pStatus;// �̵��״̬��0��δ�̵㡢1�����̵�
	private long pcFlag;// ĳ���̵�ı��
	private String deptId;// ����id
	private String brand;// �ʲ�Ʒ��
	private float price;// �ʲ��۸�
	
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
